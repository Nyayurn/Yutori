package cn.yurn.yutori.module.yhchat.adapter

import cn.yurn.yutori.Actions
import cn.yurn.yutori.RootActions
import cn.yurn.yutori.module.yhchat.Content
import cn.yurn.yutori.module.yhchat.MessageInfo
import cn.yurn.yutori.module.yhchat.Messages
import cn.yurn.yutori.module.yhchat.YhChatProperties
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.use
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put

val RootActions.yhchat: YhChatActions
    get() = containers["yhchat"] as YhChatActions

class YhChatActions(private val properties: YhChatProperties) : Actions() {
    suspend fun send(
        recvId: String,
        recvType: String,
        contentType: String,
        content: Content
    ): MessageInfo = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }.use { client ->
        client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = "chat-go.jwzhd.com"
                parameter("token", properties.token)
                appendPathSegments("open-apis", "v1", "bot", "send")
            }
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("recvId", recvId)
                put("recvType", recvType)
                put("contentType", contentType)
                put("content", Json.encodeToJsonElement(content))
            })
        }.body()
    }

    suspend fun batch_send(
        recvIds: List<String>,
        recvType: String,
        contentType: String,
        content: Content
    ): List<MessageInfo> = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }.use { client ->
        client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = "chat-go.jwzhd.com"
                parameter("token", properties.token)
                appendPathSegments("open-apis", "v1", "bot", "batch_send")
            }
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("recvIds", Json.encodeToJsonElement(recvIds))
                put("recvType", recvType)
                put("contentType", contentType)
                put("content", Json.encodeToJsonElement(content))
            })
        }.body()
    }

    suspend fun edit(
        msgId: String,
        recvId: String,
        recvType: String,
        contentType: String,
        content: Content
    ): Int = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }.use { client ->
        client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = "chat-go.jwzhd.com"
                parameter("token", properties.token)
                appendPathSegments("open-apis", "v1", "bot", "edit")
            }
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("msgId", msgId)
                put("recvId", recvId)
                put("recvType", recvType)
                put("contentType", contentType)
                put("content", Json.encodeToJsonElement(content))
            })
        }.body()
    }

    suspend fun recall(msgId: String, chatId: String, chatType: String) {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }.use { client ->
            client.post {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "chat-go.jwzhd.com"
                    parameter("token", properties.token)
                    appendPathSegments("open-apis", "v1", "bot", "recall")
                }
                contentType(ContentType.Application.Json)
                setBody(buildJsonObject {
                    put("msgId", msgId)
                    put("chatId", chatId)
                    put("chatType", chatType)
                })
            }
        }
    }

    suspend fun messages(
        chat_id: String,
        chat_type: String,
        message_id: String? = null,
        before: Int? = null,
        after: Int? = null
    ): Messages = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }.use { client ->
        client.post {
            url {
                protocol = URLProtocol.HTTPS
                host = "chat-go.jwzhd.com"
                parameter("token", properties.token)
                appendPathSegments("open-apis", "v1", "bot", "messages")
            }
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("chat-id", chat_id)
                put("chat-type", chat_type)
                put("message-id", message_id)
                put("before", before)
                put("after", after)
            })
        }.body()
    }
}