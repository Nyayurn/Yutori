package com.github.nyayurn.qbot

import com.github.nyayurn.yutori.Actions
import com.github.nyayurn.yutori.MessageEvent
import com.github.nyayurn.yutori.MessageUtil.decode

interface Command {
    fun test(actions: Actions, event: MessageEvent, msg: String): Boolean
    suspend fun command(actions: Actions, event: MessageEvent, msg: String)
}

object AiCommand : Command {
    private val regex = Regex("^[Aa]i( [\\w\\W]*)?$")
    override fun test(actions: Actions, event: MessageEvent, msg: String) = regex.matches(msg)

    override suspend fun command(actions: Actions, event: MessageEvent, msg: String) {
        regex.matchEntire(msg)?.groupValues?.get(1)?.let {
            run(actions, event, it)
        } ?: actions.message.create(event.channel.id) {
            at { id = event.user.id }
            text { "缺少参数" }
        }
    }

    fun run(actions: Actions, event: MessageEvent, msg: String) {
        AiUtil.send(msg.trim(), actions, event)
    }
}

object HelpCommand : Command {
    private val regex = Regex("^[Hh]elp( [\\w\\W]*)?$")
    override fun test(actions: Actions, event: MessageEvent, msg: String) = regex.matches(msg)

    override suspend fun command(actions: Actions, event: MessageEvent, msg: String) {
        actions.message.create(event.channel.id) {
            at { id = event.user.id }
            text {
                """
                     反馈问题请找Bot姐姐:
                        799712878(QQ)
                        莉樱#7524(Kook)
                        nyayurn(Discord)
                        https://t.me/Nyayurn(Telegram)
                    命令前缀:
                        /(左斜线, 同MC的指令前缀)
                        !(英文感叹号)
                    命令列表:
                        ai
                        help
                    其他内容:
                        如果发送的消息是符合OpenGraph的网址的链接(如github), 会自动发送预览图
                """.trimIndent()
            }
        }
    }
}

object EchoCommand : Command {
    private val regex = Regex("^[Ee]cho ([\\w\\W]*)\$")
    override fun test(actions: Actions, event: MessageEvent, msg: String) = regex.matches(msg)

    override suspend fun command(actions: Actions, event: MessageEvent, msg: String) {
        val qq =
            (event.platform == "chronocat" || event.platform == "red") && (event.user.id == "799712878" || event.user.id == "3175473426")
        val kook = event.platform == "kook" && (event.user.id == "1758544026")
        val discord = event.platform == "discord" && (event.user.id == "626549826028437515")
        val telegram = event.platform == "telegram" && (event.user.id == "6388516906")
        if (qq || kook || discord || telegram) {
            actions.message.create(event.channel.id, msg.decode().substring(5))
            return
        }
        actions.message.create(event.channel.id) {
            at { id = event.user.id }
            text { " 你不是Bot姐姐或Bot姐姐的男盆友, 别乱用指令!" }
        }
    }
}