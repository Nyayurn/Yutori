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

package com.github.nyayurn.yutori

fun interface Listener<T : Event> {
    operator fun invoke(actions: Actions, event: T)
}

fun interface ListenersContainer {
    fun runEvent(event: Event, properties: SatoriProperties)
}

class FrameworkContainer private constructor() : ListenersContainer {
    @JvmField val any = mutableListOf<Listener<Event>>()
    @JvmField val guild = GuildContainer()
    @JvmField val interaction = InteractionContainer()
    @JvmField val login = LoginContainer()
    @JvmField val message = MessageContainer()
    @JvmField val reaction = ReactionContainer()
    @JvmField val friend = FriendContainer()
    private val logger = GlobalLoggerFactory.getLogger {}

    fun any(listener: Listener<Event>) {
        any += listener
    }

    private fun parseEvent(event: Event) = try {
        val type = event.type
        when {
            type.startsWith("guild-member-") -> GuildMemberEvent.parse(event)
            type.startsWith("guild-role-") -> GuildRoleEvent.parse(event)
            type.startsWith("guild-") -> GuildEvent.parse(event)
            type == InteractionEvents.BUTTON -> InteractionButtonEvent.parse(event)
            type == InteractionEvents.COMMAND -> InteractionCommandEvent.parse(event)
            type.startsWith("login-") -> LoginEvent.parse(event)
            type.startsWith("message-") -> MessageEvent.parse(event)
            type.startsWith("reaction-") -> ReactionEvent.parse(event)
            type.startsWith("friend-") -> UserEvent.parse(event)
            else -> event
        }
    } catch (e: Throwable) {
        throw EventParsingException(e)
    }

    companion object {
        @JvmStatic
        fun of() = FrameworkContainer()

        @JvmSynthetic
        inline fun of(apply: FrameworkContainer.() -> Unit = {}) = of().apply(apply)
    }

    override fun runEvent(event: Event, properties: SatoriProperties) {
        try {
            val actions = Actions.of(event, properties)
            val newEvent = parseEvent(event)
            for (listener in this.any) listener(actions, newEvent)
            when (newEvent) {
                is GuildEvent -> guild.runEvent(actions, newEvent)
                is GuildMemberEvent -> guild.member.runEvent(actions, newEvent)
                is GuildRoleEvent -> guild.role.runEvent(actions, newEvent)
                is InteractionButtonEvent, is InteractionCommandEvent -> interaction.runEvent(actions, newEvent)
                is LoginEvent -> login.runEvent(actions, newEvent)
                is MessageEvent -> message.runEvent(actions, newEvent)
                is ReactionEvent -> reaction.runEvent(actions, newEvent)
                is UserEvent -> friend.runEvent(actions, newEvent)
            }
        } catch (e: EventParsingException) {
            logger.error("$e, event: $event")
        }
    }

    class GuildContainer {
        @JvmField val added = mutableListOf<Listener<GuildEvent>>()
        @JvmField val updated = mutableListOf<Listener<GuildEvent>>()
        @JvmField val removed = mutableListOf<Listener<GuildEvent>>()
        @JvmField val request = mutableListOf<Listener<GuildEvent>>()
        @JvmField val member = MemberContainer()
        @JvmField val role = RoleContainer()
        private val logger = GlobalLoggerFactory.getLogger {}

        fun added(listener: Listener<GuildEvent>) {
            added += listener
        }

        fun updated(listener: Listener<GuildEvent>) {
            updated += listener
        }

        fun removed(listener: Listener<GuildEvent>) {
            removed += listener
        }

        fun request(listener: Listener<GuildEvent>) {
            request += listener
        }

        fun runEvent(actions: Actions, event: GuildEvent) = when (event.type) {
            GuildEvents.ADDED -> added.forEach { it(actions, event) }
            GuildEvents.UPDATED -> updated.forEach { it(actions, event) }
            GuildEvents.REMOVED -> removed.forEach { it(actions, event) }
            GuildEvents.REQUEST -> request.forEach { it(actions, event) }
            else -> logger.warn("Unsupported event: $event")
        }

        class MemberContainer {
            @JvmField val added = mutableListOf<Listener<GuildMemberEvent>>()
            @JvmField val updated = mutableListOf<Listener<GuildMemberEvent>>()
            @JvmField val removed = mutableListOf<Listener<GuildMemberEvent>>()
            @JvmField val request = mutableListOf<Listener<GuildMemberEvent>>()
            private val logger = GlobalLoggerFactory.getLogger {}

            fun added(listener: Listener<GuildMemberEvent>) {
                added += listener
            }

            fun updated(listener: Listener<GuildMemberEvent>) {
                updated += listener
            }

            fun removed(listener: Listener<GuildMemberEvent>) {
                removed += listener
            }

            fun request(listener: Listener<GuildMemberEvent>) {
                request += listener
            }

            fun runEvent(actions: Actions, event: GuildMemberEvent) = when (event.type) {
                GuildMemberEvents.ADDED -> added.forEach { it(actions, event) }
                GuildMemberEvents.UPDATED -> updated.forEach { it(actions, event) }
                GuildMemberEvents.REMOVED -> removed.forEach { it(actions, event) }
                GuildMemberEvents.REQUEST -> request.forEach { it(actions, event) }
                else -> logger.warn("Unsupported event: $event")
            }
        }

        class RoleContainer {
            @JvmField val created = mutableListOf<Listener<GuildRoleEvent>>()
            @JvmField val updated = mutableListOf<Listener<GuildRoleEvent>>()
            @JvmField val deleted = mutableListOf<Listener<GuildRoleEvent>>()
            private val logger = GlobalLoggerFactory.getLogger {}

            fun created(listener: Listener<GuildRoleEvent>) {
                created += listener
            }

            fun updated(listener: Listener<GuildRoleEvent>) {
                updated += listener
            }

            fun deleted(listener: Listener<GuildRoleEvent>) {
                deleted += listener
            }

            fun runEvent(actions: Actions, event: GuildRoleEvent) = when (event.type) {
                GuildRoleEvents.CREATED -> created.forEach { it(actions, event) }
                GuildRoleEvents.UPDATED -> updated.forEach { it(actions, event) }
                GuildRoleEvents.DELETED -> deleted.forEach { it(actions, event) }
                else -> logger.warn("Unsupported event: $event")
            }
        }
    }

    class InteractionContainer {
        @JvmField val button = mutableListOf<Listener<InteractionButtonEvent>>()
        @JvmField val command = mutableListOf<Listener<InteractionCommandEvent>>()
        private val logger = GlobalLoggerFactory.getLogger {}

        fun button(listener: Listener<InteractionButtonEvent>) {
            button += listener
        }

        fun command(listener: Listener<InteractionCommandEvent>) {
            command += listener
        }

        fun runEvent(actions: Actions, event: Event) = when (event) {
            is InteractionButtonEvent -> button.forEach { it(actions, event) }
            is InteractionCommandEvent -> command.forEach { it(actions, event) }
            else -> logger.warn("Unsupported event: $event")
        }
    }

    class LoginContainer {
        @JvmField val added = mutableListOf<Listener<LoginEvent>>()
        @JvmField val removed = mutableListOf<Listener<LoginEvent>>()
        @JvmField val updated = mutableListOf<Listener<LoginEvent>>()
        private val logger = GlobalLoggerFactory.getLogger {}

        fun added(listener: Listener<LoginEvent>) {
            added += listener
        }

        fun removed(listener: Listener<LoginEvent>) {
            removed += listener
        }

        fun updated(listener: Listener<LoginEvent>) {
            updated += listener
        }

        fun runEvent(actions: Actions, event: LoginEvent) = when (event.type) {
            LoginEvents.ADDED -> added.forEach { it(actions, event) }
            LoginEvents.REMOVED -> removed.forEach { it(actions, event) }
            LoginEvents.UPDATED -> updated.forEach { it(actions, event) }
            else -> logger.warn("Unsupported event: $event")
        }
    }

    class MessageContainer {
        @JvmField val created = mutableListOf<Listener<MessageEvent>>()
        @JvmField val updated = mutableListOf<Listener<MessageEvent>>()
        @JvmField val deleted = mutableListOf<Listener<MessageEvent>>()
        private val logger = GlobalLoggerFactory.getLogger {}

        fun created(listener: Listener<MessageEvent>) {
            created += listener
        }

        fun updated(listener: Listener<MessageEvent>) {
            updated += listener
        }

        fun deleted(listener: Listener<MessageEvent>) {
            deleted += listener
        }

        fun runEvent(actions: Actions, event: MessageEvent) = when (event.type) {
            MessageEvents.CREATED -> created.forEach { it(actions, event) }
            MessageEvents.UPDATED -> updated.forEach { it(actions, event) }
            MessageEvents.DELETED -> deleted.forEach { it(actions, event) }
            else -> logger.warn("Unsupported event: $event")
        }
    }

    class ReactionContainer {
        @JvmField val added = mutableListOf<Listener<ReactionEvent>>()
        @JvmField val removed = mutableListOf<Listener<ReactionEvent>>()
        private val logger = GlobalLoggerFactory.getLogger {}

        fun added(listener: Listener<ReactionEvent>) {
            added += listener
        }

        fun removed(listener: Listener<ReactionEvent>) {
            removed += listener
        }

        fun runEvent(actions: Actions, event: ReactionEvent) = when (event.type) {
            ReactionEvents.ADDED -> added.forEach { it(actions, event) }
            ReactionEvents.REMOVED -> removed.forEach { it(actions, event) }
            else -> logger.warn("Unsupported event: $event")
        }
    }

    class FriendContainer {
        @JvmField val request = mutableListOf<Listener<UserEvent>>()
        private val logger = GlobalLoggerFactory.getLogger {}

        fun request(listener: Listener<UserEvent>) {
            request += listener
        }

        fun runEvent(actions: Actions, event: UserEvent) = when (event.type) {
            UserEvents.FRIEND_REQUEST -> request.forEach { it(actions, event) }
            else -> logger.warn("Unsupported event: $event")
        }
    }
}