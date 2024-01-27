package com.github.nyayurn.qbot

import com.github.nyayurn.yutori.*
import com.github.nyayurn.yutori.message.element.At
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import java.io.IOException
import java.util.regex.Pattern

object CommandListener : Listener<MessageEvent> {
    private val commands: Array<Command> = arrayOf(AiCommand, HelpCommand, EchoCommand)
    private val logger = Slf4jLoggerFactory.getLogger {}
    override fun invoke(actions: Actions, event: MessageEvent) {
        if (!mailBlockFilter(event)) return
        if (!blockListFilter(event)) return
        var msg = event.message.content
        if (msg.startsWith("/") or msg.startsWith("!")) {
            msg = msg.substring(1)
            if (msg.isNotEmpty()) runBlocking {
                commands.find { it.test(actions, event, msg) }?.let {
                    logger.info("User ${event.user.id} 触发命令: ${it::class.simpleName}(${event.message.content})")
                    it.command(actions, event, msg)
                } ?: actions.message.create(event.channel.id) {
                    at { id = event.user.id }
                    text { " 未知命令: ${msg.split(" ")[0]}" }
                }
            }
        }
    }
}

object OpenGraphListener : Listener<MessageEvent> {
    private val pattern = Pattern.compile("(http(s)?://[\\w./-]+)")
    private val logger = Slf4jLoggerFactory.getLogger {}
    override fun invoke(actions: Actions, event: MessageEvent) {
        if (!mailBlockFilter(event)) return
        if (!blockListFilter(event)) return
        val msg = MessageUtil.extractTextChain(event.message.content).joinToString { it.toString() }
        val matcher = pattern.matcher(msg)
        if (matcher.find()) runBlocking {
            val url = matcher.group(0)
            val request = HttpClient(CIO) { engine { https { trustManager = trustAllCerts } } }.use { it.get(url) }
            try {
                val document = Jsoup.parse(request.body<String>())
                val element = document.head().getElementsByAttributeValue("property", "og:image").first()
                if (element != null) {
                    actions.message.create(event.channel.id) {
                        at { id = event.user.id }
                        text { " OpenGraph:" }
                        img { src = element.attr("content") }
                    }
                }
            } catch (e: IOException) {
                if (request.status.value != 200) {
                    actions.message.create(event.channel.id) {
                        at { id = event.user.id }
                        text { " OpenGraph: 获取失败: ${request.status}" }
                    }
                }
                logger.warn(e.localizedMessage)
            }
        }
    }
}

val AtListener = Listener<MessageEvent> { actions, event ->
    if (!mailBlockFilter(event)) return@Listener
    if (!blockListFilter(event)) return@Listener
    val msg = MessageUtil.extractTextChain(event.message.content).joinToString { it.toString() }
    // 忽略 Q 群管家
    if ((event.platform == "chronocat" || event.platform == "red") && event.user.id == "2854196310") return@Listener
    val atBot =
        MessageUtil.parseElementChain(event.message.content).getOrNull(0).let { it is At && it.id == event.selfId }
    if (atBot && msg.isNotEmpty()) {
        Slf4jLoggerFactory.getLogger {}.info("User ${event.user.id} 触发命令: $AiCommand")
        AiCommand.run(actions, event, msg)
    }
}

val MailListener = Listener<MessageEvent> { _, event ->
    if (!mailOnlyFilter(event)) return@Listener
    val actions = Actions.of("chronocat", "1903909576", chronoProperties)
    runBlocking {
        actions.message.create("private:799712878") {
            text { "${event.user.name}: ${event.message.content}" }
        }
    }
}

object LoginListener {
    private val logger = Slf4jLoggerFactory.getLogger {}
    val onAdded = Listener<LoginEvent> { _, event ->
        logger.info(
            "Login added: platform: ${event.login.platform}, selfId: ${event.login.selfId}, status: ${event.login.status}"
        )
    }

    val onRemoved = Listener<LoginEvent> { _, event ->
        logger.info(
            "Login removed: platform: ${event.login.platform}, selfId: ${event.login.selfId}, status: ${event.login.status}"
        )
    }

    val onUpdated = Listener<LoginEvent> { _, event ->
        logger.info(
            "Login updated: platform: ${event.login.platform}, selfId: ${event.login.selfId}, status: ${event.login.status}"
        )
    }
}

val UserListener = Listener<UserEvent> { _, event ->
    Slf4jLoggerFactory.getLogger {}.info("Friend request: ${event.user}")
}