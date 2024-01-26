/*
Copyright (c) 2023 Yurn
Yutori is licensed under Mulan PSL v2.
You can use this software according to the terms and conditions of the Mulan PSL v2.
You may obtain a copy of Mulan PSL v2 at:
         http://license.coscl.org.cn/MulanPSL2
THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
See the Mulan PSL v2 for more details.
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.nyayurn.yutori

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.*

/**
 * Satori 事件服务接口, 用于与 Satori Server 进行通信
 */
interface SatoriEventService : AutoCloseable {
    /**
     * 与 Satori Server 建立连接
     */
    fun connect(): SatoriEventService
}

/**
 * 协程的 Satori 事件服务抽象类
 * @param scope 协程作用域
 */
abstract class CoroutineSatoriEventService(private val scope: CoroutineScope) : AutoCloseable, SatoriEventService {
    protected lateinit var baseScope: CoroutineScope
    override fun connect(): SatoriEventService {
        scope.launch {
            baseScope = this
            suspendConnect()
        }
        return this
    }

    /**
     * 在协程内与 Satori Server 建立连接
     */
    protected abstract suspend fun suspendConnect()
}

/**
 * Satori 事件服务的 WebSocket 实现
 * @param container 监听器容器
 * @param properties Satori Server 配置
 * @param name 用于区分不同 Satori 事件服务的名称
 * @param scope 协程作用域
 */
@OptIn(DelicateCoroutinesApi::class)
class WebSocketEventService @JvmOverloads constructor(
    val container: ListenersContainer,
    val properties: SatoriProperties,
    val name: String = "Satori",
    scope: CoroutineScope = GlobalScope
) : CoroutineSatoriEventService(scope) {
    private var sequence: Number? = null
    private var isConnected = false
    private val client = HttpClient {
        install(WebSockets)
    }
    private val logger = GlobalLoggerFactory.getLogger {}

    override fun close() {
        isConnected = false
        client.close()
        baseScope.cancel()
    }

    override suspend fun suspendConnect() {
        try {
            client.webSocket(
                HttpMethod.Get,
                properties.host,
                properties.port,
                "${properties.path}/${properties.version}/events"
            ) {
                logger.info("[$name]: 成功建立 WebSocket 连接")
                isConnected = true
                launch { sendIdentity(this@webSocket) }
                for (frame in incoming) try {
                    frame as? Frame.Text ?: continue
                    val signaling = Signaling.parse(frame.readText())
                    onEvent(signaling)
                } catch (e: Exception) {
                    logger.warn(
                        "[$name]: 处理事件时出错(${(frame as Frame.Text).readText()}): ${e.localizedMessage}"
                    )
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            logger.warn("[$name]: WebSocket 连接断开: ${e.localizedMessage}")
            e.printStackTrace()
            isConnected = false
            // 重连
            baseScope.launch {
                logger.info("[$name]: 将在5秒后尝试重新连接")
                delay(5000)
                logger.info("[$name]: 尝试重新连接")
                suspendConnect()
            }
        }
    }

    private suspend fun sendIdentity(session: DefaultClientWebSocketSession) {
        val token = properties.token
        val content = jsonObj {
            put("op", Signaling.IDENTIFY)
            if (token != null || sequence != null) putJsonObj("body") {
                put("token", token)
                put("sequence", sequence)
            }
        }
        logger.info("[$name]: 发送身份验证: $content")
        session.send(content)
    }

    private fun DefaultClientWebSocketSession.onEvent(signaling: Signaling) {
        when (signaling.op) {
            Signaling.READY -> {
                val ready = signaling.body as Ready
                logger.info("[$name]: 成功建立事件推送(${ready.logins.size}): \n${
                    ready.logins.joinToString(
                        "\n"
                    ) { "{platform: ${it.platform}, selfId: ${it.selfId}}" }
                }")
                // 心跳
                launch {
                    val content = jsonObj { put("op", Signaling.PING) }
                    while (isConnected) {
                        delay(10000)
                        send(content)
                    }
                }
            }

            Signaling.EVENT -> launch { sendEvent(signaling.body as Event) }
            Signaling.PONG -> logger.debug("[$name]: 收到 PONG")
            else -> logger.error("Unsupported $signaling")
        }
    }

    private fun sendEvent(event: Event) {
        logger.info(
            "[$name]: 接收事件: platform: ${event.platform}, selfId: ${event.selfId}, type: ${event.type}"
        )
        logger.debug("[$name]: 事件详细信息: $event")
        sequence = event.id
        container.runEvent(event, properties)
    }

    companion object {
        @JvmStatic
        @JvmOverloads
        fun of(
            container: ListenersContainer,
            properties: SatoriProperties = SimpleSatoriProperties(),
            name: String = "Satori",
            scope: CoroutineScope = GlobalScope
        ) = WebSocketEventService(container, properties, name, scope)

        @JvmSynthetic
        inline fun of(name: String = "Satori", scope: CoroutineScope = GlobalScope, dsl: Builder.() -> Unit) =
            Builder(name, scope).apply(dsl).build()
    }

    class Builder(private val name: String, private val scope: CoroutineScope) {
        var container: ListenersContainer = FrameworkContainer.of()
        var properties: SatoriProperties = SimpleSatoriProperties()

        fun listeners(lambda: FrameworkContainer.() -> Unit) {
            container = FrameworkContainer.of(lambda)
        }

        fun properties(lambda: PropertiesBuilder.() -> Unit) {
            properties = PropertiesBuilder().apply(lambda).build()
        }

        fun build() = WebSocketEventService(container, properties, name, scope)

        class PropertiesBuilder {
            var host: String = "127.0.0.1"
            var port: Int = 5500
            var path: String = ""
            var token: String? = null
            var version: String = "v1"

            fun host(lambda: () -> String) {
                host = lambda()
            }

            fun port(lambda: () -> Int) {
                port = lambda()
            }

            fun path(lambda: () -> String) {
                path = lambda()
            }

            fun token(lambda: () -> String?) {
                token = lambda()
            }

            fun version(lambda: () -> String) {
                version = lambda()
            }

            fun build() = SimpleSatoriProperties(host, port, path, token, version)
        }
    }
}

/**
 * Satori 事件服务的 WebHook 实现
 * @param container 监听器容器
 * @param properties Satori WebHook 配置
 * @param name 用于区分不同 Satori 事件服务的名称
 * @param scope 协程作用域
 */
@OptIn(DelicateCoroutinesApi::class)
class WebHookEventService @JvmOverloads constructor(
    val container: ListenersContainer,
    val properties: SatoriWebHookProperties,
    val name: String = "Satori",
    scope: CoroutineScope = GlobalScope
) : CoroutineSatoriEventService(scope) {
    private var client: ApplicationEngine? = null
    private val logger = GlobalLoggerFactory.getLogger {}

    override fun close() {
        client?.stop()
        baseScope.cancel()
    }

    override suspend fun suspendConnect() {
        client = embeddedServer(CIO, properties.serverPort, properties.serverHost) {
            routing {
                post("/") {
                    val authorization = call.request.headers["Authorization"]
                    if (authorization != properties.token) {
                        call.response.status(HttpStatusCode.Unauthorized)
                        return@post
                    }
                    val body = call.receiveText()
                    try {
                        val mapper =
                            jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        val event = mapper.readValue<Event>(body)
                        launch { sendEvent(event) }
                        call.response.status(HttpStatusCode.OK)
                    } catch (e: Exception) {
                        logger.warn(
                            "[$name]: 处理事件时出错(${body}): ${e.localizedMessage}"
                        )
                        e.printStackTrace()
                        call.response.status(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }.start()
        logger.info("[$name]: 成功启动 HTTP 服务器")
        AdminAction.of(properties).webhook.create(
            "http://${properties.serverHost}:${properties.serverPort}",
            properties.token
        )
    }

    private fun sendEvent(event: Event) {
        logger.info(
            "[$name]: 接收事件: platform: ${event.platform}, selfId: ${event.selfId}, type: ${event.type}"
        )
        logger.debug("[$name]: 事件详细信息: $event")
        container.runEvent(event, properties)
    }

    companion object {
        @JvmStatic
        @JvmOverloads
        fun of(
            container: ListenersContainer,
            properties: SatoriWebHookProperties = SimpleSatoriWebHookProperties(),
            name: String = "Satori",
            scope: CoroutineScope = GlobalScope
        ) = WebHookEventService(container, properties, name, scope)

        @JvmSynthetic
        fun of(name: String = "Satori", scope: CoroutineScope = GlobalScope, dsl: Builder.() -> Unit) =
            Builder(name, scope).apply(dsl).build()
    }

    class Builder(private val name: String, private val scope: CoroutineScope) {
        var container: ListenersContainer = FrameworkContainer.of()
        var properties: SatoriWebHookProperties = SimpleSatoriWebHookProperties()

        fun listeners(lambda: FrameworkContainer.() -> Unit) {
            container = FrameworkContainer.of(lambda)
        }

        fun properties(lambda: PropertiesBuilder.() -> Unit) {
            properties = PropertiesBuilder().apply(lambda).build()
        }

        fun properties(lambda: () -> SatoriWebHookProperties) {
            this.properties = lambda()
        }

        fun build() = WebHookEventService(container, properties, name, scope)

        class PropertiesBuilder {
            var server: Server = Server()
            var host: String = "127.0.0.1"
            var port: Int = 5500
            var path: String = ""
            var token: String? = null
            var version: String = "v1"

            fun server(lambda: Server.() -> Unit) {
                server = Server().apply(lambda)
            }

            fun host(lambda: () -> String) {
                host = lambda()
            }

            fun port(lambda: () -> Int) {
                port = lambda()
            }

            fun path(lambda: () -> String) {
                path = lambda()
            }

            fun token(lambda: () -> String?) {
                token = lambda()
            }

            fun version(lambda: () -> String) {
                version = lambda()
            }

            fun build() = SimpleSatoriWebHookProperties(server.host, server.port, host, port, path, token, version)

            class Server {
                var host: String = "0.0.0.0"
                var port: Int = 8080

                fun host(lambda: () -> String) {
                    host = lambda()
                }

                fun port(lambda: () -> Int) {
                    port = lambda()
                }
            }
        }
    }
}