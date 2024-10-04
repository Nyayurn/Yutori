@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package cn.yurn.yutori

import cn.yurn.yutori.message.element.MessageElement
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

@Serializable
abstract class SigningRequest

interface SatoriPlatformNotNullRequest
interface SatoriUserIdNotNullRequest
interface ChannelIdNotNullRequest
interface GuildIdNotNullRequest
interface NextNullableRequest
interface ChannelDataNotNullRequest
interface DurationNotNullRequest
interface UserIdNotNullRequest
interface GuildIdNullableRequest
interface MessageIdNotNullRequest
interface ApproveNotNullRequest
interface CommentNotNullRequest
interface PermanentNullableRequest
interface CommentNullableRequest
interface RoleIdNotNullRequest
interface RoleNotNullRequest
interface ContentNotNullRequest
interface DirectionNullableRequest
interface LimitNullableRequest
interface OrderNullableRequest
interface EmojiNotNullRequest
interface UserIdNullableRequest
interface EmojiNullableRequest

val <T> Request<T>.satoriPlatform: String where T : SigningRequest, T : SatoriPlatformNotNullRequest
    get() = properties["satoriPlatform"] as String

val <T> Request<T>.satoriUserId: String where T : SigningRequest, T : SatoriUserIdNotNullRequest
    get() = properties["satoriUserId"] as String

val <T> Request<T>.channelId: String where T : SigningRequest, T : ChannelIdNotNullRequest
    get() = properties["channelId"] as String

val <T> Request<T>.guildId: String where T : SigningRequest, T : GuildIdNotNullRequest
    get() = properties["guildId"] as String

val <T> Request<T>.next: String? where T : SigningRequest, T : NextNullableRequest
    get() = properties["next"] as String?

val <T> Request<T>.data: Channel where T : SigningRequest, T : ChannelDataNotNullRequest
    get() = properties["data"] as Channel

val <T> Request<T>.duration: Number where T : SigningRequest, T : DurationNotNullRequest
    get() = properties["duration"] as Number

val <T> Request<T>.userId: String where T : SigningRequest, T : UserIdNotNullRequest
    get() = properties["userId"] as String

val <T> Request<T>.guildId: String? where T : SigningRequest, T : GuildIdNullableRequest
    @JvmName("nullableGuildId") get() = properties["guildId"] as String?

val <T> Request<T>.messageId: String where T : SigningRequest, T : MessageIdNotNullRequest
    get() = properties["messageId"] as String

val <T> Request<T>.approve: Boolean where T : SigningRequest, T : ApproveNotNullRequest
    get() = properties["approve"] as Boolean

val <T> Request<T>.comment: String where T : SigningRequest, T : CommentNotNullRequest
    get() = properties["comment"] as String

val <T> Request<T>.permanent: Boolean? where T : SigningRequest, T : PermanentNullableRequest
    get() = properties["permanent"] as Boolean?

val <T> Request<T>.comment: String? where T : SigningRequest, T : CommentNullableRequest
    @JvmName("nullableComment") get() = properties["comment"] as String?

val <T> Request<T>.roleId: String where T : SigningRequest, T : RoleIdNotNullRequest
    get() = properties["roleId"] as String

val <T> Request<T>.role: GuildRole where T : SigningRequest, T : RoleNotNullRequest
    get() = properties["role"] as GuildRole

val <T> Request<T>.content: List<MessageElement> where T : SigningRequest, T : ContentNotNullRequest
    get() = properties["content"] as List<MessageElement>

val <T> Request<T>.direction: BidiPagingList.Direction? where T : SigningRequest, T : DirectionNullableRequest
    get() = properties["direction"] as BidiPagingList.Direction?

val <T> Request<T>.limit: Number? where T : SigningRequest, T : LimitNullableRequest
    get() = properties["limit"] as Number?

val <T> Request<T>.order: BidiPagingList.Order? where T : SigningRequest, T : OrderNullableRequest
    get() = properties["order"] as BidiPagingList.Order?

val <T> Request<T>.emoji: String where T : SigningRequest, T : EmojiNotNullRequest
    get() = properties["emoji"] as String

val <T> Request<T>.userId: String? where T : SigningRequest, T : UserIdNullableRequest
    @JvmName("nullableUserId") get() = properties["userId"] as String?

val <T> Request<T>.emoji: String? where T : SigningRequest, T : EmojiNullableRequest
    @JvmName("nullableEmoji") get() = properties["emoji"] as String?

object ChannelRequests {
    const val GET = "/channel.get"
    const val LIST = "/channel.list"
    const val CREATE = "/channel.create"
    const val UPDATE = "/channel.update"
    const val DELETE = "/channel.delete"
    const val MUTE = "/channel.mute"
    val Types = setOf(GET, LIST, CREATE, UPDATE, DELETE, MUTE)
}

object UserChannelRequests {
    const val CREATE = "/user.channel.create"
    val Types = setOf(CREATE)
}

object GuildRequests {
    const val GET = "/guild.get"
    const val LIST = "/guild.list"
    const val APPROVE = "/guild.approve"
    val Types = setOf(GET, LIST, APPROVE)
}

object GuildMemberRequests {
    const val GET = "/guild.member.get"
    const val LIST = "/guild.member.list"
    const val KICK = "/guild.member.kick"
    const val MUTE = "/guild.member.mute"
    const val APPROVE = "/guild.member.approve"
    val Types = setOf(GET, LIST, KICK, MUTE, APPROVE)
}

object GuildMemberRoleRequests {
    const val SET = "/guild.member.role.set"
    const val UNSET = "/guild.member.role.unset"
    val Types = setOf(SET, UNSET)
}

object GuildRoleRequests {
    const val LIST = "/guild.role.list"
    const val CREATE = "/guild.role.create"
    const val UPDATE = "/guild.role.update"
    const val DELETE = "/guild.role.delete"
    val Types = setOf(LIST, CREATE, UPDATE, DELETE)
}

object LoginRequests {
    const val GET = "/login.get"
    val Types = setOf(GET)
}

object MessageRequests {
    const val CREATE = "/message.create"
    const val GET = "/message.get"
    const val DELETE = "/message.delete"
    const val UPDATE = "/message.update"
    const val LIST = "/message.list"
    val Types = setOf(CREATE, GET, DELETE, UPDATE, LIST)
}

object ReactionRequests {
    const val CREATE = "/reaction.create"
    const val DELETE = "/reaction.delete"
    const val CLEAR = "/reaction.clear"
    const val LIST = "/reaction.list"
    val Types = setOf(CREATE, DELETE, CLEAR, LIST)
}

object UserRequests {
    const val GET = "/user.get"
    val Types = setOf(GET)
}

object FriendRequests {
    const val LIST = "/friend.list"
    const val APPROVE = "/friend.approve"
    val Types = setOf(LIST, APPROVE)
}

object ChannelGetRequest : SigningRequest(), ChannelIdNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object ChannelListRequest : SigningRequest(), GuildIdNotNullRequest, NextNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object ChannelCreateRequest : SigningRequest(), GuildIdNotNullRequest, ChannelDataNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object ChannelUpdateRequest : SigningRequest(), ChannelIdNotNullRequest, ChannelDataNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object ChannelDeleteRequest : SigningRequest(), ChannelIdNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object ChannelMuteRequest : SigningRequest(), ChannelIdNotNullRequest, DurationNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object UserChannelCreateRequest : SigningRequest(), UserIdNotNullRequest, GuildIdNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildGetRequest : SigningRequest(), GuildIdNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildListRequest : SigningRequest(), NextNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildApproveRequest : SigningRequest(), MessageIdNotNullRequest, ApproveNotNullRequest, CommentNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildMemberGetRequest : SigningRequest(), GuildIdNotNullRequest, UserIdNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildMemberListRequest : SigningRequest(), GuildIdNotNullRequest, NextNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildMemberKickRequest : SigningRequest(), GuildIdNotNullRequest, UserIdNotNullRequest, PermanentNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildMemberMuteRequest : SigningRequest(), GuildIdNotNullRequest, UserIdNotNullRequest, DurationNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildMemberApproveRequest : SigningRequest(), MessageIdNotNullRequest, ApproveNotNullRequest, CommentNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildMemberRoleSetRequest : SigningRequest(), GuildIdNotNullRequest, UserIdNotNullRequest, RoleIdNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildMemberRoleUnsetRequest : SigningRequest(), GuildIdNotNullRequest, UserIdNotNullRequest, RoleIdNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildRoleListRequest : SigningRequest(), GuildIdNotNullRequest, NextNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildRoleCreateRequest : SigningRequest(), GuildIdNotNullRequest, RoleNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildRoleUpdateRequest : SigningRequest(), GuildIdNotNullRequest, RoleIdNotNullRequest, RoleNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object GuildRoleDeleteRequest : SigningRequest(), GuildIdNotNullRequest, RoleIdNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object LoginGetRequest : SigningRequest(), SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object MessageCreateRequest : SigningRequest(), ChannelIdNotNullRequest, ContentNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object MessageGetRequest : SigningRequest(), ChannelIdNotNullRequest, MessageIdNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object MessageDeleteRequest : SigningRequest(), ChannelIdNotNullRequest, MessageIdNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object MessageUpdateRequest : SigningRequest(), ChannelIdNotNullRequest, MessageIdNotNullRequest, ContentNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object MessageListRequest : SigningRequest(), ChannelIdNotNullRequest, NextNullableRequest, DirectionNullableRequest, LimitNullableRequest, OrderNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object ReactionCreateRequest : SigningRequest(), ChannelIdNotNullRequest, MessageIdNotNullRequest, EmojiNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object ReactionDeleteRequest : SigningRequest(), ChannelIdNotNullRequest, MessageIdNotNullRequest, EmojiNotNullRequest, UserIdNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object ReactionClearRequest : SigningRequest(), ChannelIdNotNullRequest, MessageIdNotNullRequest, EmojiNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object ReactionListRequest : SigningRequest(), ChannelIdNotNullRequest, MessageIdNotNullRequest, EmojiNotNullRequest, NextNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object UserGetRequest : SigningRequest(), UserIdNotNullRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object FriendListRequest : SigningRequest(), NextNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest
object FriendApproveRequest : SigningRequest(), MessageIdNotNullRequest, ApproveNotNullRequest, CommentNullableRequest, SatoriPlatformNotNullRequest, SatoriUserIdNotNullRequest