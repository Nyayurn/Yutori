# 任意事件 (Event)

> container.any()

# 频道 (Channel)

## 加入群组

> container.guild.added()

## 群组被修改

> container.guild.updated()

## 退出群组

> container.guild.removed()

## 接收到新的入群邀请

> container.guild.request()

# 群组成员 (GuildMember)

## 群组成员增加

> container.guild.member.added()

## 群组成员信息更新

> container.guild.member.updated()

## 群组成员移除

> container.guild.member.removed()

## 接收到新的加群请求

> container.guild.member.request()

# 群组角色 (GuildRole)

## 群组角色被创建

> container.guild.role.created()

## 群组角色被修改

> container.guild.role.updated()

## 群组角色被删除

> container.guild.role.deleted()

# 交互 (Interaction)

## 类型为 action 的按钮被点击

> container.interaction.button()

## 调用斜线指令

> container.interaction.command()

# 登录信息 (Login)

## 登录被创建

> container.login.added()

## 登录被删除

> container.login.removed()

## 登录信息更新

> container.login.updated()

# 消息 (Message)

## 消息被创建

> container.message.created()

## 消息被编辑

> container.message.updated()

## 消息被删除

> container.message.deleted()

# 表态 (Reaction)

## 表态被添加

> container.reaction.added()

## 表态被移除

> container.reaction.removed()

# 用户 (User)

## 接收到新的好友申请

> container.friend.request()