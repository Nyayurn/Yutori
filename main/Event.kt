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

package com.github.nyayurn.yutori

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 群组事件列表, 参考 https://satori.chat/zh-CN/resources/guild.html#%E4%BA%8B%E4%BB%B6
 */
object GuildEvents {
    const val ADDED = "guild-added"
    const val UPDATED = "guild-updated"
    const val REMOVED = "guild-removed"
    const val REQUEST = "guild-request"
}

/**
 * 群组事件实体类
 */
class GuildEvent @JvmOverloads constructor(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    override val guild: Guild,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null
) : Event(id, type, platform, selfId, timestamp, argv, button, channel, guild, login, member, message, operator, role, user) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = GuildEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild!!,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user
        )
    }
}

/**
 * 群组成员事件列表, 参考 https://satori.chat/zh-CN/resources/member.html#%E4%BA%8B%E4%BB%B6
 */
object GuildMemberEvents {
    const val ADDED = "guild-member-added"
    const val UPDATED = "guild-member-updated"
    const val REMOVED = "guild-member-removed"
    const val REQUEST = "guild-member-request"
}

/**
 * 群组成员事件实体类
 */
class GuildMemberEvent @JvmOverloads constructor(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    override val guild: Guild,
    login: Login? = null,
    override val member: GuildMember,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    override val user: User
) : Event(id, type, platform, selfId, timestamp, argv, button, channel, guild, login, member, message, operator, role, user) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = GuildMemberEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild!!,
            event.login,
            event.member!!,
            event.message,
            event.operator,
            event.role,
            event.user!!
        )
    }
}

/**
 * 群组角色事件列表, 参考 https://satori.chat/zh-CN/resources/role.html#%E4%BA%8B%E4%BB%B6
 */
object GuildRoleEvents {
    const val CREATED = "guild-role-created"
    const val UPDATED = "guild-role-updated"
    const val DELETED = "guild-role-deleted"
}

/**
 * 群组角色事件实体类
 */
class GuildRoleEvent @JvmOverloads constructor(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    override val guild: Guild,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    override val role: GuildRole,
    user: User? = null
) : Event(id, type, platform, selfId, timestamp, argv, button, channel, guild, login, member, message, operator, role, user) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = GuildRoleEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild!!,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role!!,
            event.user
        )
    }
}

/**
 * 交互事件列表, 参考 https://satori.chat/zh-CN/resources/interaction.html#%E4%BA%8B%E4%BB%B6
 */
object InteractionEvents {
    const val BUTTON = "interaction/button"
    const val COMMAND = "interaction/command"
}

/**
 * 交互事件 interaction/button 实体类
 */
class InteractionButtonEvent @JvmOverloads constructor(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    override val button: Interaction.Button,
    channel: Channel? = null,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null
) : Event(id, type, platform, selfId, timestamp, argv, button, channel, guild, login, member, message, operator, role, user) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = InteractionButtonEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button!!,
            event.channel,
            event.guild,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user
        )
    }
}

/**
 * 交互事件 interaction/command 实体类
 */
class InteractionCommandEvent @JvmOverloads constructor(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null
) : Event(id, type, platform, selfId, timestamp, argv, button, channel, guild, login, member, message, operator, role, user) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = InteractionCommandEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user
        )
    }
}

/**
 * 登录事件列表, 参考 https://satori.chat/zh-CN/resources/login.html#%E4%BA%8B%E4%BB%B6
 */
object LoginEvents {
    const val ADDED = "login-added"
    const val REMOVED = "login-removed"
    const val UPDATED = "login-updated"
}

/**
 * 登录事件实体类
 */
class LoginEvent @JvmOverloads constructor(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    guild: Guild? = null,
    override val login: Login,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null
) : Event(id, type, platform, selfId, timestamp, argv, button, channel, guild, login, member, message, operator, role, user) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = LoginEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild,
            event.login!!,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user
        )
    }
}

/**
 * 消息事件列表, 参考 https://satori.chat/zh-CN/resources/message.html#%E4%BA%8B%E4%BB%B6
 */
object MessageEvents {
    const val CREATED = "message-created"
    const val UPDATED = "message-updated"
    const val DELETED = "message-deleted"
}

/**
 * 消息事件实体类
 */
class MessageEvent @JvmOverloads constructor(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    override val channel: Channel,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    override val message: Message,
    operator: User? = null,
    role: GuildRole? = null,
    override val user: User
) : Event(id, type, platform, selfId, timestamp, argv, button, channel, guild, login, member, message, operator, role, user) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = MessageEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel!!,
            event.guild,
            event.login,
            event.member,
            event.message!!,
            event.operator,
            event.role,
            event.user!!
        )
    }
}

/**
 * 表态事件列表, 参考 https://satori.chat/zh-CN/resources/reaction.html#%E4%BA%8B%E4%BB%B6
 */
object ReactionEvents {
    const val ADDED = "reaction-added"
    const val REMOVED = "reaction-removed"
}

/**
 * 表态事件实体类
 */
class ReactionEvent @JvmOverloads constructor(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    user: User? = null
) : Event(id, type, platform, selfId, timestamp, argv, button, channel, guild, login, member, message, operator, role, user) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = ReactionEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user
        )
    }
}

/**
 * 用户事件列表, 参考 https://satori.chat/zh-CN/resources/user.html#%E4%BA%8B%E4%BB%B6
 */
object UserEvents {
    const val FRIEND_REQUEST = "friend-request"
}

/**
 * 用户事件实体类
 */
class UserEvent @JvmOverloads constructor(
    id: Number,
    type: String,
    platform: String,
    @JsonProperty("self_id") selfId: String,
    timestamp: Number,
    argv: Interaction.Argv? = null,
    button: Interaction.Button? = null,
    channel: Channel? = null,
    guild: Guild? = null,
    login: Login? = null,
    member: GuildMember? = null,
    message: Message? = null,
    operator: User? = null,
    role: GuildRole? = null,
    override val user: User
) : Event(id, type, platform, selfId, timestamp, argv, button, channel, guild, login, member, message, operator, role, user) {
    companion object {
        @JvmStatic
        fun parse(event: Event) = UserEvent(
            event.id,
            event.type,
            event.platform,
            event.selfId,
            event.timestamp,
            event.argv,
            event.button,
            event.channel,
            event.guild,
            event.login,
            event.member,
            event.message,
            event.operator,
            event.role,
            event.user!!
        )
    }
}