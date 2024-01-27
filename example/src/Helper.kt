package com.github.nyayurn.qbot

import com.alibaba.fastjson2.parseObject
import com.github.nyayurn.qbot.database.BlockListMapper.get
import com.github.nyayurn.qbot.database.BlockListMapper.sub
import com.github.nyayurn.qbot.database.BlockListTable.status
import com.github.nyayurn.yutori.Actions
import com.github.nyayurn.yutori.MessageEvent
import com.github.nyayurn.yutori.jsonObj
import com.github.nyayurn.yutori.message.message
import com.reine.text2image.T2IConstant
import com.reine.text2image.T2IUtil
import kotlinx.coroutines.runBlocking
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.net.URL
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Suppress("SpellCheckingInspection", "SameParameterValue")
object AiUtil {
    private const val URL = "https://spark-api.xf-yun.com/v3.1/chat"
    private const val APP_ID = "******"
    private const val API_SECRET = "******"
    private const val API_KEY = "******"

    fun send(text: String, actions: Actions, event: MessageEvent) {
        val jsonObject = jsonObj {
            putJsonObj("header") {
                put("app_id", APP_ID)
                put("uid", event.user.id)
            }
            putJsonObj("parameter") {
                putJsonObj("chat") {
                    put("domain", "generalv3")
                }
            }
            putJsonObj("payload") {
                putJsonObj("message") {
                    putJsonArr("text") {
                        addJsonObj {
                            put("role", "user")
                            put("content", text)
                        }
                    }
                }
            }
        }
        // 构建鉴权url
        val url = getAuthUrl(URL, API_KEY, API_SECRET).replace("http://", "ws://").replace("https://", "wss://")
        val client: WebSocketClient = SparkWebSocketClient(url, actions, event, jsonObject)
        client.connect()
    }

    private fun getAuthUrl(hostUrl: String, apiKey: String, apiSecret: String): String {
        val url = URL(hostUrl)
        // 时间
        val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")
        val date = format.format(Date())
        // 拼接
        val preStr = """
            host: ${url.host}
            date: $date
            GET ${url.path} HTTP/1.1
        """.trimIndent()
        // SHA256加密
        val mac = Mac.getInstance("hmacsha256")
        val spec = SecretKeySpec(apiSecret.toByteArray(StandardCharsets.UTF_8), "hmacsha256")
        mac.init(spec)

        val hexDigits = mac.doFinal(preStr.toByteArray(StandardCharsets.UTF_8))
        // Base64加密
        val sha = Base64.getEncoder().encodeToString(hexDigits)
        // 拼接
        val authorization = """
            api_key="$apiKey", algorithm="hmac-sha256", headers="host date request-line", signature="$sha"
        """.trimIndent()
        // 拼接地址
        return "https://${url.host}${url.path}?authorization=${
            Base64.getEncoder().encodeToString(authorization.toByteArray(StandardCharsets.UTF_8))
        }&date=${
            date.replace(" ", "%20")
        }&host=${url.host}"
    }

    private class SparkWebSocketClient(
        url: String, private val actions: Actions, private val event: MessageEvent, private val content: String
    ) : WebSocketClient(URI(url), Draft_6455()) {
        private val answer = StringBuilder()

        override fun onOpen(serverHandshake: ServerHandshake) = this.send(content)

        override fun onMessage(msg: String) {
            val entity = msg.parseObject<SparkResponse>()
            if (entity.header.code != 0) {
                val code = entity.header.code
                var content = message {
                    at { id = event.user.id }
                    text(" code: $code\n")
                    text(entity.header.message)
                }
                if (code == 10013 || code == 10014) {
                    sub(event.platform, event.selfId, event.user.id)
                    val query = get(event.platform, event.selfId, event.user.id)
                    query.iterator().next().use { next ->
                        val status = next[status]
                        content += if (status != null && status != 0) {
                            "\n恶意询问, 警告一次, 再警告${status}次后将永久进入黑名单"
                        } else {
                            "\n恶意询问, 多次警告无果, 已自动拉入黑名单"
                        }
                    }
                }
                runBlocking { actions.message.create(event.channel.id, content) }
                return
            }
            entity.payload?.choices?.let {
                for ((content1) in it.text) {
                    answer.append(content1)
                }
            }
            if (entity.header.status == 2) {
                val content = answer.toString()
                runBlocking {
                    actions.message.create(event.channel.id) {
                        at { id = event.user.id }
                        img {
                            src = "data:image/jpeg;base64,${
                                T2IUtil(T2IConstant()).drawImageToBase64(content).substring("base64://".length)
                            }"
                        }
                    }
                }
            }
        }

        override fun onClose(i: Int, s: String, b: Boolean) {
            // method is empty
        }

        override fun onError(e: java.lang.Exception) {
            // method is empty
        }
    }
}