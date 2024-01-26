# 频道 (Channel)

## 获取群组频道

> actions.channel.get()

**参数**

|    字段     |   类型   |  描述   |
|:---------:|:------:|:-----:|
| channelId | String | 频道 ID |

**返回值**

|   类型    |  描述  |
|:-------:|:----:|
| Channel | 频道信息 |

## 获取群组频道列表

> actions.channel.list()

**参数**

|   字段    |   类型    |  描述   |
|:-------:|:-------:|:-----:|
| guildId | String  | 群组 ID |
|  next   | String? | 分页令牌  |

**返回值**

|             类型              |    描述    |
|:---------------------------:|:--------:|
| List<PageResponse<Channel>> | 群组中的全部频道 |

## 创建群组频道

> actions.channel.create()

**参数**

|   字段    |   类型    |  描述   |
|:-------:|:-------:|:-----:|
| guildId | String  | 群组 ID |
|  data   | Channel | 频道数据  |

**返回值**

|   类型    |  描述  |
|:-------:|:----:|
| Channel | 频道信息 |

## 修改群组频道

> actions.channel.update()

**参数**

|   字段    |   类型    |  描述   |
|:-------:|:-------:|:-----:|
| guildId | String  | 群组 ID |
|  data   | Channel | 频道数据  |

**返回值**

|  类型  | 描述 |
|:----:|:--:|
| Unit | 空  |

## 删除群组频道

> actions.channel.delete()

**参数**

|    字段     |   类型   |  描述   |
|:---------:|:------:|:-----:|
| channelId | String | 频道 ID |

**返回值**

|  类型  | 描述 |
|:----:|:--:|
| Unit | 空  |

## 创建私聊频道

> actions.user.channel.create()

**参数**

|   字段    |   类型   |  描述   |
|:-------:|:------:|:-----:|
| userId  | String | 用户 ID |
| guildId | String | 群组 ID |

**返回值**

|   类型    |  描述  |
|:-------:|:----:|
| Channel | 频道信息 |

# 群组 (Guild)

## 获取群组

> actions.guild.get()

## 获取群组列表

> actions.guild.list()

## 处理群组邀请

> actions.guild.approve()

# 群组成员 (GuildMember)

## 获取群组成员

> actions.guild.member.get()

## 获取群组成员列表

> actions.guild.member.list()

## 踢出群组成员

> actions.guild.member.kick()

## 通过群组成员申请

> actions.guild.member.approve()

# 群组角色 (GuildRole)

## 设置群组成员角色

> actions.guild.role.set()

## 取消群组成员角色

> actions.guild.role.unset()

## 获取群组角色列表

> actions.guild.role.list()

## 创建群组角色

> actions.guild.role.create()

## 修改群组角色

> actions.guild.role.update()

## 删除群组角色

> actions.guild.role.delete()

# 登录信息 (Login)

## 获取登录信息

> actions.login.get()

# 消息 (Message)

## 发送消息

> actions.message.create()

## 获取消息

> actions.message.get()

## 撤回消息

> actions.message.delete()

## 编辑消息

> actions.message.update()

## 获取消息列表

> actions.message.list()

# 表态 (Reaction)

## 添加表态

> actions.reaction.create()

## 删除表态

> actions.reaction.delete()

## 清除表态

> actions.reaction.clear()

## 获取表态列表

> actions.reaction.list()

# 用户 (User)

## 获取用户信息

> actions.user.get()

## 获取好友列表

> actions.friend.get()

## 处理好友申请

> actions.friend.approve()