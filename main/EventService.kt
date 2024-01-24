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

@file:Suppress("unused")

package io.github.nyayurn.yutori

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*

interface SatoriEventService : AutoCloseable {
    fun connect(scope: CoroutineScope?)
}

/**
 * Satori 事件服务的 WebSocket 实现
 * @param satori Satori 实例
 * @param name 用于区分不同 Satori 事件服务的名称
 * @param logger 日志类实现
 */
class
WebSocketEventService @JvmOverloads constructor(
    private val satori: Satori,
    private val name: String = "Satori",
    private val logger: Logger = Slf4jLogger()
) : SatoriEventService {
    private var sequence: Number? = null
    private lateinit var baseScope: CoroutineScope
    private var isConnected = false
    private val client = HttpClient {
        install(WebSockets)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun connect(scope: CoroutineScope?) {
        (scope ?: GlobalScope).launch {
            baseScope = this
            run()
        }
    }

    override fun close() {
        isConnected = false
        client.close()
        baseScope.cancel()
    }

    private suspend fun run() {
        try {
            client.webSocket(
                HttpMethod.Get,
                satori.properties.host,
                satori.properties.port,
                "${satori.properties.path}/${satori.properties.version}/events"
            ) {
                logger.info("[$name]: 成功建立 WebSocket 连接", this::class.java)
                isConnected = true
                launch { sendIdentity(this@webSocket) }
                for (frame in incoming) try {
                    frame as? Frame.Text ?: continue
                    val signaling = Signaling.parse(frame.readText())
                    onEvent(signaling)
                } catch (e: Exception) {
                    logger.warn(
                        "[$name]: 处理事件时出错(${(frame as Frame.Text).readText()}): ${e.localizedMessage}",
                        this::class.java
                    )
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            logger.warn("[$name]: WebSocket 连接断开: ${e.localizedMessage}", this::class.java)
            e.printStackTrace()
            isConnected = false
            baseScope.launch {
                logger.info("[$name]: 将在5秒后尝试重新连接", this::class.java)
                delay(5000)
                logger.info("[$name]: 尝试重新连接", this::class.java)
                run()
            }
        }
    }

    private suspend fun sendIdentity(session: DefaultClientWebSocketSession) {
        val token = satori.properties.token
        val content = jsonObj {
            put("op", Signaling.IDENTIFY)
            if (token != null || sequence != null) putJsonObj("body") {
                put("token", token)
                put("sequence", sequence)
            }
        }
        logger.info("[$name]: 发送身份验证: $content", this::class.java)
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
                }", this::class.java)
                // 心跳
                launch {
                    val content = jsonObj { put("op", Signaling.PING) }
                    while (isConnected) {
                        delay(10000)
                        send(content)
                    }
                }
            }

            Signaling.EVENT -> launch { sendEvent(signaling) }
            Signaling.PONG -> logger.debug("[$name]: 收到 PONG", this::class.java)
            else -> logger.error("Unsupported $signaling", this::class.java)
        }
    }

    private fun sendEvent(signaling: Signaling) {
        val body = signaling.body as Event
        logger.info(
            "[$name]: 接收事件: platform: ${body.platform}, selfId: ${body.selfId}, type: ${body.type}",
            this::class.java
        )
        logger.debug("[$name]: 事件详细信息: $body", this::class.java)
        sequence = body.id
        satori.runEvent(body)
    }
}