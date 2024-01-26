# 注册监听器

- 通过 FrameworkContainer 类的方法注册对应[事件](Event.md)的监听器
```kotlin
val container = FrameworkContainer.of {
    message.created(TestListener)
    message.created += TestListener
    message.created { actions, event ->
        // ...
    }
    message.created += Listener { actions, event ->
        // ...
    }
}
```

# Actions

- 封装了所有 [Action](Action.md)
- 负责与 Satori Server 交互

# 多帐号

Yutori 支持多帐号, 只需多实例几个 EventService 并分别连接即可
```kotlin
val chronoClient = WebSocketEventService.of("chronocat") {
    this.container = container
    this.properties = chronoProperties
}.connect()
val koishiClient = WebSocketEventService.of("koishi") {
    this.container = container
    this.properties = koishiProperties
}.connect()
```

# 消息构建方式

!!! warning
本条目可能已经过时

## 链式构建(推荐 Java 使用)

```java
public class Main {
    public static void main(String[] args) {
        MessageChainBuilder.of()
            .at(event.getUser().getId())
            .text(" 菜单:\n")
            .text("红烧肉 红烧排骨 可乐鸡翅 糖醋排骨 水煮鱼 红烧鱼\n")
            .text("凉拌黑木耳 鱼香肉丝 水煮肉片 意大利面 麻辣小龙虾 凉拌木耳\n")
            .text("茶叶蛋 龙井虾仁 口水鸡 回锅肉 红烧猪蹄 皮蛋瘦肉\n")
            .text("粥酸菜鱼 咖喱牛肉 西红柿炒鸡蛋 辣椒酱 麻辣烫 辣白菜\n")
            .text("牛肉酱 红烧茄子 蛋炒饭 佛跳墙 四物汤 固元膏\n")
            .text("龟苓膏 银耳莲子 羹酸梅 汤腊肉")
            .build();
    }
}
```

## DSL (仅 Kotlin)

DSL 提供多种语法, 根据自己喜好选择即可

```kotlin
fun main() {
    message {
        at { id = event.user.id }
        at { id { event.user.id } }
        at(event.user.id)
        at(id = event.user.id)
        text(" 菜单:\n")
        text { "红烧肉 红烧排骨 可乐鸡翅 糖醋排骨 水煮鱼 红烧鱼\n" }
    }
}
```

actions.message.create 等方法提供更方便的 DSL 使用

```kotlin
fun listen(actions: Actions, event: MessageEvent) {
    actions.message.create(event.channel.id) {
        at { id = event.user.id }
        text { " 菜单:\n" }
        text { "红烧肉 红烧排骨 可乐鸡翅 糖醋排骨 水煮鱼 红烧鱼\n" }
        text { "凉拌黑木耳 鱼香肉丝 水煮肉片 意大利面 麻辣小龙虾 凉拌木耳\n" }
        text { "茶叶蛋 龙井虾仁 口水鸡 回锅肉 红烧猪蹄 皮蛋瘦肉\n" }
        text { "粥酸菜鱼 咖喱牛肉 西红柿炒鸡蛋 辣椒酱 麻辣烫 辣白菜\n" }
        text { "牛肉酱 红烧茄子 蛋炒饭 佛跳墙 四物汤 固元膏\n" }
        text { "龟苓膏 银耳莲子 羹酸梅 汤腊肉" }
    }
}
```

# 主动发送消息

## Kotlin

```kotlin
fun main() {
    val actions = Actions.of("platform", "selfId", properties)
    actions.message.create("channelId", "Hello, world!")
}
```

## Java

```java
public class Main {
    public static void main(String[] args) {
        Actions actions = Actions.of("platform", "selfId", properties);
        actions.message.create("channelId", "Hello, world!");
    }
}
```

# 消息链

```kotlin
fun listen(actions: Actions, event: MessageEvent) {
    val msgChain = MessageUtil.parseElementChain(event.message.content)
    msgChain.forEach(::println)
}
```

# WebHook

!!! warning
    本条目内容未经过测试, 可能无法正常使用

- 和 WebSocket 使用大致相同

# 内部接口

- 请自行使用 HTTP 库发送 HTTP 请求