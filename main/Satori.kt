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

package io.github.nyayurn.yutori

import kotlinx.coroutines.CoroutineScope

fun interface Listener<T : Event> {
    operator fun invoke(actions: Actions, event: T)
}

class Satori private constructor(
    val properties: SatoriProperties,
    private val logger: Logger
) {
    val onEvent = mutableListOf<ListenerContext<Event>>()
    val onGuild = mutableListOf<ListenerContext<GuildEvent>>()
    val onMember = mutableListOf<ListenerContext<GuildMemberEvent>>()
    val onRole = mutableListOf<ListenerContext<GuildRoleEvent>>()
    val onButton = mutableListOf<ListenerContext<InteractionButtonEvent>>()
    val onCommand = mutableListOf<ListenerContext<InteractionCommandEvent>>()
    val onLogin = mutableListOf<ListenerContext<LoginEvent>>()
    val onMessage = mutableListOf<ListenerContext<MessageEvent>>()
    val onReaction = mutableListOf<ListenerContext<ReactionEvent>>()
    val onUser = mutableListOf<ListenerContext<UserEvent>>()

    fun onEvent(listener: Listener<Event>) = ListenerContext(listener).apply { onEvent += this }

    fun onGuildAdded(listener: Listener<GuildEvent>) = ListenerContext(listener).apply {
        onGuild += this
        withFilter(eventTypeFilter(GuildEvents.ADDED))
    }

    fun onGuildUpdated(listener: Listener<GuildEvent>) = ListenerContext(listener).apply {
        onGuild += this
        withFilter(eventTypeFilter(GuildEvents.UPDATED))
    }

    fun onGuildRemoved(listener: Listener<GuildEvent>) = ListenerContext(listener).apply {
        onGuild += this
        withFilter(eventTypeFilter(GuildEvents.REMOVED))
    }

    fun onGuildRequest(listener: Listener<GuildEvent>) = ListenerContext(listener).apply {
        onGuild += this
        withFilter(eventTypeFilter(GuildEvents.REQUEST))
    }

    fun onGuildMemberAdded(listener: Listener<GuildMemberEvent>) = ListenerContext(listener).apply {
        onMember += this
        withFilter(eventTypeFilter(GuildMemberEvents.ADDED))
    }

    fun onGuildMemberUpdated(listener: Listener<GuildMemberEvent>) = ListenerContext(listener).apply {
        onMember += this
        withFilter(eventTypeFilter(GuildMemberEvents.UPDATED))
    }

    fun onGuildMemberRemoved(listener: Listener<GuildMemberEvent>) = ListenerContext(listener).apply {
        onMember += this
        withFilter(eventTypeFilter(GuildMemberEvents.REMOVED))
    }

    fun onGuildMemberRequest(listener: Listener<GuildMemberEvent>) = ListenerContext(listener).apply {
        onMember += this
        withFilter(eventTypeFilter(GuildMemberEvents.REQUEST))
    }

    fun onGuildRoleCreated(listener: Listener<GuildRoleEvent>) = ListenerContext(listener).apply {
        onRole += this
        withFilter(eventTypeFilter(GuildRoleEvents.CREATED))
    }

    fun onGuildRoleUpdated(listener: Listener<GuildRoleEvent>) = ListenerContext(listener).apply {
        onRole += this
        withFilter(eventTypeFilter(GuildRoleEvents.UPDATED))
    }

    fun onGuildRoleDeleted(listener: Listener<GuildRoleEvent>) = ListenerContext(listener).apply {
        onRole += this
        withFilter(eventTypeFilter(GuildRoleEvents.DELETED))
    }

    fun onInteractionButton(listener: Listener<InteractionButtonEvent>) = ListenerContext(listener).apply {
        onButton += this
        withFilter(eventTypeFilter(InteractionEvents.BUTTON))
    }

    fun onInteractionCommand(listener: Listener<InteractionCommandEvent>) = ListenerContext(listener).apply {
        onCommand += this
        withFilter(eventTypeFilter(InteractionEvents.COMMAND))
    }

    fun onLoginAdded(listener: Listener<LoginEvent>) = ListenerContext(listener).apply {
        onLogin += this
        withFilter(eventTypeFilter(LoginEvents.ADDED))
    }

    fun onLoginRemoved(listener: Listener<LoginEvent>) = ListenerContext(listener).apply {
        onLogin += this
        withFilter(eventTypeFilter(LoginEvents.REMOVED))
    }

    fun onLoginUpdated(listener: Listener<LoginEvent>) = ListenerContext(listener).apply {
        onLogin += this
        withFilter(eventTypeFilter(LoginEvents.UPDATED))
    }

    fun onMessageCreated(listener: Listener<MessageEvent>) = ListenerContext(listener).apply {
        onMessage += this
        withFilter(eventTypeFilter(MessageEvents.CREATED))
    }

    fun onMessageUpdated(listener: Listener<MessageEvent>) = ListenerContext(listener).apply {
        onMessage += this
        withFilter(eventTypeFilter(MessageEvents.UPDATED))
    }

    fun onMessageDeleted(listener: Listener<MessageEvent>) = ListenerContext(listener).apply {
        onMessage += this
        withFilter(eventTypeFilter(MessageEvents.DELETED))
    }

    fun onReactionAdded(listener: Listener<ReactionEvent>) = ListenerContext(listener).apply {
        onReaction += this
        withFilter(eventTypeFilter(ReactionEvents.ADDED))
    }

    fun onReactionRemoved(listener: Listener<ReactionEvent>) = ListenerContext(listener).apply {
        onReaction += this
        withFilter(eventTypeFilter(ReactionEvents.REMOVED))
    }

    fun onFriendRequest(listener: Listener<UserEvent>) = ListenerContext(listener).apply {
        onUser += this
        withFilter(eventTypeFilter(UserEvents.FRIEND_REQUEST))
    }

    /**
     * 与 Satori Server 建立 Websocket 连接
     * @param eventService Satori 事件服务实现类
     * @param scope 协程作用域
     */
    @JvmOverloads
    fun connect(
        eventService: SatoriEventService,
        scope: CoroutineScope? = null
    ) = eventService.connect(scope)

    private fun parseEvent(event: Event) = try {
        when (event.type) {
            GuildEvents.ADDED, GuildEvents.UPDATED, GuildEvents.REMOVED, GuildEvents.REQUEST -> GuildEvent.parse(event)
            GuildMemberEvents.ADDED, GuildMemberEvents.UPDATED, GuildMemberEvents.REMOVED, GuildMemberEvents.REQUEST ->
                GuildMemberEvent.parse(event)

            GuildRoleEvents.CREATED, GuildRoleEvents.UPDATED, GuildRoleEvents.DELETED -> GuildRoleEvent.parse(event)
            InteractionEvents.BUTTON -> InteractionButtonEvent.parse(event)
            InteractionEvents.COMMAND -> InteractionCommandEvent.parse(event)
            LoginEvents.ADDED, LoginEvents.REMOVED, LoginEvents.UPDATED -> LoginEvent.parse(event)
            MessageEvents.CREATED, MessageEvents.UPDATED, MessageEvents.DELETED -> MessageEvent.parse(event)
            ReactionEvents.ADDED, ReactionEvents.REMOVED -> ReactionEvent.parse(event)
            UserEvents.FRIEND_REQUEST -> UserEvent.parse(event)
            else -> event
        }
    } catch (e: Throwable) {
        throw EventParsingException(e)
    }

    fun runEvent(event: Event) {
        try {
            val actions = Actions.of(event, properties, logger)
            val newEvent = parseEvent(event)
            runEvent(onEvent, actions, newEvent)
            when (newEvent) {
                is GuildEvent -> runEvent(onGuild, actions, newEvent)
                is GuildMemberEvent -> runEvent(onMember, actions, newEvent)
                is GuildRoleEvent -> runEvent(onRole, actions, newEvent)
                is InteractionButtonEvent -> runEvent(onButton, actions, newEvent)
                is InteractionCommandEvent -> runEvent(onCommand, actions, newEvent)
                is LoginEvent -> runEvent(onLogin, actions, newEvent)
                is MessageEvent -> runEvent(onMessage, actions, newEvent)
                is ReactionEvent -> runEvent(onReaction, actions, newEvent)
                is UserEvent -> runEvent(onUser, actions, newEvent)
            }
        } catch (e: EventParsingException) {
            logger.error("$e, event: $event", this::class.java)
        }
    }

    private fun <T : Event> runEvent(list: List<ListenerContext<T>>, actions: Actions, event: T) {
        for (context in list) context.run(actions, event)
    }

    companion object {
        @JvmStatic
        @JvmOverloads
        fun of(properties: SatoriProperties, logger: Logger = Slf4jLogger()) = Satori(properties, logger)

        @JvmStatic
        @JvmOverloads
        fun of(
            host: String = "127.0.0.1",
            port: Int = 5500,
            path: String = "",
            token: String? = null,
            version: String = "v1",
            logger: Logger = Slf4jLogger()
        ) = Satori(SimpleSatoriProperties(host, port, path, token, version), logger)

        @JvmSynthetic
        @JvmOverloads
        inline fun of(properties: SatoriProperties, logger: Logger = Slf4jLogger(), apply: Satori.() -> Unit) =
            of(properties, logger).apply { apply() }

        @JvmSynthetic
        @JvmOverloads
        inline fun of(
            host: String = "127.0.0.1",
            port: Int = 5500,
            path: String = "",
            token: String? = null,
            version: String = "v1",
            logger: Logger = Slf4jLogger(),
            apply: Satori.() -> Unit
        ) = of(host, port, path, token, version, logger).apply { apply() }
    }
}

class ListenerContext<T : Event>(private val listener: Listener<T>) {
    private val filters = mutableListOf<(Actions, Event) -> Boolean>()

    fun withFilter(filter: (Actions, Event) -> Boolean) = this.apply { filters += filter }
    fun run(actions: Actions, event: T) {
        for (filter in filters) if (!filter(actions, event)) return
        listener(actions, event)
    }
}