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

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.nyayurn.yutori.message.MessageDSLBuilder
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * 封装所有 Action, 应通过本类对 Satori Server 发送事件
 * @property channel 频道 API
 * @property guild 群组 API
 * @property login 登录信息 API
 * @property message 消息 API
 * @property reaction 表态 API
 * @property user 用户 API
 * @property friend 好友 API
 * @property properties 配置信息, 供使用者获取
 */
class Actions private constructor(
    @JvmField val channel: ChannelAction,
    @JvmField val guild: GuildAction,
    @JvmField val login: LoginAction,
    @JvmField val message: MessageAction,
    @JvmField val reaction: ReactionAction,
    @JvmField val user: UserAction,
    @JvmField val friend: FriendAction,
    val properties: SatoriProperties
) {
    companion object {
        /**
         * 工厂方法
         * @param platform 平台
         * @param selfId 自己 ID
         * @param properties 配置
         * @return Bot 实例
         */
        @JvmStatic
        fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) = Actions(
            ChannelAction.of(platform, selfId, properties, logger),
            GuildAction.of(platform, selfId, properties, logger),
            LoginAction.of(platform, selfId, properties, logger),
            MessageAction.of(platform, selfId, properties, logger),
            ReactionAction.of(platform, selfId, properties, logger),
            UserAction.of(platform, selfId, properties, logger),
            FriendAction.of(platform, selfId, properties, logger),
            properties
        )

        /**
         * 工厂方法
         * @param event 事件
         * @param properties 配置
         * @return Bot 实例
         */
        @JvmStatic
        fun of(event: Event, properties: SatoriProperties, logger: Logger) =
            of(event.platform, event.selfId, properties, logger)
    }
}

class ChannelAction private constructor(private val satoriAction: SatoriAction) {
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    /**
     * 获取群组频道
     * @param channelId 频道 ID
     */
    suspend fun get(channelId: String): Channel {
        return mapper.readValue(satoriAction.send("get") {
            put("channel_id", channelId)
        })
    }

    /**
     * 获取群组频道列表
     * @param guildId 群组 ID
     * @param next 分页令牌
     */
    @JvmOverloads
    suspend fun list(guildId: String, next: String? = null): List<PaginatedData<Channel>> {
        return mapper.readValue(satoriAction.send("list") {
            put("guild_id", guildId)
            put("next", next)
        })
    }

    /**
     * 创建群组频道
     * @param guildId 群组 ID
     * @param data 频道数据
     */
    suspend fun create(guildId: String, data: Channel): Channel {
        return mapper.readValue(satoriAction.send("create") {
            put("guild_id", guildId)
            put("data", data)
        })
    }

    /**
     * 修改群组频道
     * @param channelId 频道 ID
     * @param data 频道数据
     */
    suspend fun update(channelId: String, data: Channel) {
        satoriAction.send("update") {
            put("channel_id", channelId)
            put("data", data)
        }
    }

    /**
     * 删除群组频道
     * @param channelId 频道 ID
     */
    suspend fun delete(channelId: String) {
        satoriAction.send("delete") {
            put("channel_id", channelId)
        }
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) =
            ChannelAction(SatoriAction(platform, selfId, properties, "channel", logger))
    }
}

class GuildAction private constructor(
    @JvmField val member: MemberAction,
    @JvmField val role: RoleAction,
    private val satoriAction: SatoriAction
) {
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    /**
     * 获取群组
     * @param guildId 群组 ID
     */
    suspend fun get(guildId: String): Guild {
        return mapper.readValue(satoriAction.send("get") {
            put("guild_id", guildId)
        })
    }

    /**
     * 获取群组列表
     * @param next 分页令牌
     */
    @JvmOverloads
    suspend fun list(next: String? = null): List<PaginatedData<Guild>> {
        return mapper.readValue(satoriAction.send("list") {
            put("next", next)
        })
    }

    /**
     * 处理群组邀请
     * @param messageId 请求 ID
     * @param approve 是否通过请求
     * @param comment 备注信息
     */
    suspend fun approve(messageId: String, approve: Boolean, comment: String) {
        satoriAction.send("approve") {
            put("message_id", messageId)
            put("approve", approve)
            put("comment", comment)
        }
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) = GuildAction(
            MemberAction.of(platform, selfId, properties, logger),
            RoleAction.of(platform, selfId, properties, logger),
            SatoriAction(platform, selfId, properties, "guild", logger)
        )
    }

    class MemberAction private constructor(
        @JvmField val role: RoleAction,
        private val satoriAction: SatoriAction
    ) {
        private val mapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        /**
         * 获取群组成员
         * @param guildId 群组 ID
         * @param userId 用户 ID
         */
        suspend fun get(guildId: String, userId: String): GuildMember {
            return mapper.readValue(satoriAction.send("get") {
                put("guild_id", guildId)
                put("user_id", userId)
            })
        }

        /**
         * 获取群组成员列表
         * @param guildId 群组 ID
         * @param next 分页令牌
         */
        @JvmOverloads
        suspend fun list(guildId: String, next: String? = null): List<PaginatedData<GuildMember>> {
            return mapper.readValue(satoriAction.send("list") {
                put("guild_id", guildId)
                put("next", next)
            })
        }

        /**
         * 踢出群组成员
         * @param guildId 群组 ID
         * @param userId 用户 ID
         * @param permanent 是否永久踢出 (无法再次加入群组)
         */
        @JvmOverloads
        suspend fun kick(guildId: String, userId: String, permanent: Boolean? = null) {
            satoriAction.send("kick") {
                put("guild_id", guildId)
                put("user_id", userId)
                put("permanent", permanent)
            }
        }

        /**
         * 通过群组成员申请
         * @param messageId 请求 ID
         * @param approve 是否通过请求
         * @param comment 备注信息
         */
        @JvmOverloads
        suspend fun approve(messageId: String, approve: Boolean, comment: String? = null) {
            satoriAction.send("approve") {
                put("message_id", messageId)
                put("approve", approve)
                put("comment", comment)
            }
        }

        companion object {
            fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) = MemberAction(
                RoleAction.of(platform, selfId, properties, logger),
                SatoriAction(platform, selfId, properties, "guild.member", logger)
            )
        }

        class RoleAction private constructor(private val satoriAction: SatoriAction) {
            /**
             * 设置群组成员角色
             * @param guildId 群组 ID
             * @param userId 用户 ID
             * @param roleId 角色 ID
             */
            suspend fun set(guildId: String, userId: String, roleId: String) {
                satoriAction.send("set") {
                    put("guild_id", guildId)
                    put("user_id", userId)
                    put("role_id", roleId)
                }
            }

            /**
             * 取消群组成员角色
             * @param guildId 群组 ID
             * @param userId 用户 ID
             * @param roleId 角色 ID
             */
            suspend fun unset(guildId: String, userId: String, roleId: String) {
                satoriAction.send("unset") {
                    put("guild_id", guildId)
                    put("user_id", userId)
                    put("role_id", roleId)
                }
            }

            companion object {
                fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) =
                    RoleAction(SatoriAction(platform, selfId, properties, "guild.member.role", logger))
            }
        }
    }

    class RoleAction private constructor(private val satoriAction: SatoriAction) {
        private val mapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        /**
         * 获取群组角色列表
         * @param guildId 群组 ID
         * @param next 分页令牌
         */
        @JvmOverloads
        suspend fun list(guildId: String, next: String? = null): List<PaginatedData<GuildRole>> {
            return mapper.readValue(satoriAction.send("list") {
                put("guild_id", guildId)
                put("next", next)
            })
        }

        /**
         * 创建群组角色
         * @param guildId 群组 ID
         * @param role 角色数据
         */
        suspend fun create(guildId: String, role: GuildRole): GuildRole {
            return mapper.readValue(satoriAction.send("create") {
                put("guild_id", guildId)
                put("role", role)
            })
        }

        /**
         * 修改群组角色
         * @param guildId 群组 ID
         * @param roleId 角色 ID
         * @param role 角色数据
         */
        suspend fun update(guildId: String, roleId: String, role: GuildRole) {
            satoriAction.send("update") {
                put("guild_id", guildId)
                put("role_id", roleId)
                put("role", role)
            }
        }

        /**
         * 删除群组角色
         * @param guildId 群组 ID
         * @param roleId 角色 ID
         */
        suspend fun delete(guildId: String, roleId: String) {
            satoriAction.send("delete") {
                put("guild_id", guildId)
                put("role_id", roleId)
            }
        }

        companion object {
            fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) =
                RoleAction(SatoriAction(platform, selfId, properties, "guild.role", logger))
        }
    }
}

class LoginAction private constructor(private val satoriAction: SatoriAction) {
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    /**
     * 获取登录信息
     */
    suspend fun get(): Login = mapper.readValue(satoriAction.send("get"))

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) =
            LoginAction(SatoriAction(platform, selfId, properties, "login", logger))
    }
}

class MessageAction private constructor(private val satoriAction: SatoriAction) {
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    /**
     * 发送消息
     * @param channelId 频道 ID
     * @param content 消息内容
     */
    suspend fun create(channelId: String, content: String): List<Message> {
        return mapper.readValue(satoriAction.send("create") {
            put("channel_id", channelId)
            put("content", content.replace("\n", "\\n").replace("\"", "\\\""))
        })
    }

    /**
     * 使用 DSL 发送消息
     * @param channelId 频道 ID
     * @param dsl 消息内容 DSL
     */
    suspend inline fun create(channelId: String, dsl: MessageDSLBuilder.() -> Unit) =
        create(channelId, MessageDSLBuilder().apply(dsl).build())

    /**
     * 获取消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     */
    suspend fun get(channelId: String, messageId: String): Message {
        return mapper.readValue(satoriAction.send("get") {
            put("channel_id", channelId)
            put("message_id", messageId)
        })
    }

    /**
     * 撤回消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     */
    suspend fun delete(channelId: String, messageId: String) {
        satoriAction.send("delete") {
            put("channel_id", channelId)
            put("message_id", messageId)
        }
    }

    /**
     * 编辑消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param content 消息内容
     */
    suspend fun update(channelId: String, messageId: String, content: String) {
        satoriAction.send("update") {
            put("channel_id", channelId)
            put("message_id", messageId)
            put("content", content.replace("\n", "\\n").replace("\"", "\\\""))
        }
    }

    /**
     * 使用 DSL 编辑消息
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param dsl 消息内容 DSL
     */
    suspend inline fun update(channelId: String, messageId: String, dsl: MessageDSLBuilder.() -> Unit) =
        update(channelId, messageId, MessageDSLBuilder().apply(dsl).build())

    /**
     * 获取消息列表
     * @param channelId 频道 ID
     * @param next 分页令牌
     */
    @JvmOverloads
    suspend fun list(channelId: String, next: String? = null): List<PaginatedData<Message>> {
        return mapper.readValue(satoriAction.send("list") {
            put("channel_id", channelId)
            put("next", next)
        })
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) =
            MessageAction(SatoriAction(platform, selfId, properties, "message", logger))
    }
}

class ReactionAction private constructor(private val satoriAction: SatoriAction) {
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    /**
     * 添加表态
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param emoji 表态名称
     */
    suspend fun create(channelId: String, messageId: String, emoji: String) {
        satoriAction.send("create") {
            put("channel_id", channelId)
            put("message_id", messageId)
            put("emoji", emoji)
        }
    }

    /**
     * 删除表态
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param emoji 表态名称
     * @param userId 用户 ID
     */
    @JvmOverloads
    suspend fun delete(channelId: String, messageId: String, emoji: String, userId: String? = null) {
        satoriAction.send("delete") {
            put("channel_id", channelId)
            put("message_id", messageId)
            put("emoji", emoji)
            put("user_id", userId)
        }
    }

    /**
     * 清除表态
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param emoji 表态名称
     */
    @JvmOverloads
    suspend fun clear(channelId: String, messageId: String, emoji: String? = null) {
        satoriAction.send("clear") {
            put("channel_id", channelId)
            put("message_id", messageId)
            put("emoji", emoji)
        }
    }

    /**
     * 获取表态列表
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param emoji 表态名称
     * @param next 分页令牌
     */
    @JvmOverloads
    suspend fun list(
        channelId: String,
        messageId: String,
        emoji: String,
        next: String? = null
    ): List<PaginatedData<User>> {
        return mapper.readValue(satoriAction.send("list") {
            put("channel_id", channelId)
            put("message_id", messageId)
            put("emoji", emoji)
            put("next", next)
        })
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) =
            ReactionAction(SatoriAction(platform, selfId, properties, "reaction", logger))
    }
}

class UserAction private constructor(
    @JvmField val channel: ChannelAction,
    private val satoriAction: SatoriAction
) {
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    /**
     * 获取用户信息
     * @param userId 用户 ID
     */
    suspend fun get(userId: String): User {
        return mapper.readValue(satoriAction.send("get") {
            put("user_id", userId)
        })
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) = UserAction(
            ChannelAction.of(platform, selfId, properties, logger),
            SatoriAction(platform, selfId, properties, "user", logger)
        )
    }

    class ChannelAction private constructor(private val satoriAction: SatoriAction) {
        private val mapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        /**
         * 创建私聊频道
         * @param userId 用户 ID
         * @param guildId 群组 ID
         */
        @JvmOverloads
        suspend fun create(userId: String, guildId: String? = null): Channel {
            return mapper.readValue(satoriAction.send("create") {
                put("user_id", userId)
                put("guild_id", guildId)
            })
        }

        companion object {
            fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) =
                ChannelAction(SatoriAction(platform, selfId, properties, "user.channel", logger))
        }
    }
}

class FriendAction private constructor(private val satoriAction: SatoriAction) {
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    /**
     * 获取好友列表
     * @param next 分页令牌
     */
    @JvmOverloads
    suspend fun list(next: String? = null): List<PaginatedData<User>> {
        return mapper.readValue(satoriAction.send("list") {
            put("next", next)
        })
    }

    /**
     * 处理好友申请
     * @param messageId 请求 ID
     * @param approve 是否通过请求
     * @param comment 备注信息
     */
    @JvmOverloads
    suspend fun approve(messageId: String, approve: Boolean, comment: String? = null) {
        satoriAction.send("approve") {
            put("message_id", messageId)
            put("approve", approve)
            put("comment", comment)
        }
    }

    companion object {
        fun of(platform: String, selfId: String, properties: SatoriProperties, logger: Logger) =
            FriendAction(SatoriAction(platform, selfId, properties, "friend", logger))
    }
}

/**
 * Satori Action 实现
 * @property platform 平台
 * @property selfId 自身的 ID
 * @property properties 配置
 * @property resource 资源路径
 * @property logger 日志接口
 */
class SatoriAction @JvmOverloads constructor(
    private val platform: String? = null,
    private val selfId: String? = null,
    private val properties: SatoriProperties,
    private val resource: String,
    private val logger: Logger
) {
    suspend fun send(method: String, body: String? = null): String {
        HttpClient(CIO).use { client ->
            val response = client.post {
                url {
                    host = properties.host
                    port = properties.port
                    appendPathSegments(properties.path, properties.version, "$resource.$method")
                }
                contentType(ContentType.Application.Json)
                headers {
                    properties.token?.let { append(HttpHeaders.Authorization, "Bearer $it") }
                    platform?.let { append("X-Platform", it) }
                    selfId?.let { append("X-Self-ID", selfId) }
                }
                body?.let { setBody(it) }
                logger.debug(
                    """
                    Satori Action: url: ${this.url},
                        headers: ${this.headers.build()},
                        body: ${this.body}
                    """.trimIndent(), this::class.java
                )
            }
            logger.debug("Satori Action Response: $response", this::class.java)
            return response.body()
        }
    }

    @JvmSynthetic
    suspend inline fun send(method: String, dsl: JsonObjectDSLBuilder.() -> Unit) = send(method, jsonObj(dsl))
}