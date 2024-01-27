# 快速开始

## 基础信息

- 本项目仅支持 WebSocket 连接而不支持 WebHook 连接
- 本项目建议使用 Kotlin 语言进行开发
- 本项目不依赖也不建议配合 Spring 进行开发

## 项目创建

1. 创建项目
2. [添加仓库](#添加仓库)
3. [引入依赖](#依赖引入)
4. [基础使用](#基础使用)

## 添加仓库

### Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

### Gradle Kotlin DSL

```kotlin
maven { url = URI("https://jitpack.io") }
```

### Gradle Groovy DSL

```groovy
maven { url 'https://jitpack.io' }
```

## 依赖引入

### Maven

```xml
<dependency>
    <groupId>com.github.Nyayurn</groupId>
    <artifactId>Yutori</artifactId>
    <version>final</version>
</dependency>
```

### Gradle Kotlin DSL

```kotlin
implementation("com.github.Nyayurn:Yutori:final")
```

### Gradle Groovy DSL

```groovy
implementation 'com.github.Nyayurn:Yutori:final'
```

## 基础使用

### Kotlin

```kotlin
fun main() {
    WebSocketEventService.of {
        listeners {
            message.created { actions, event ->
                if (event.message.content == "在吗") runBlocking {
                    actions.message.create(event.channel.id) {
                        at { id = event.user.id }
                        text { " 我在!" }
                    }
                }
            }
        }
        properties {
            token { "token" }
        }
    }.connect()
}
```

### Java

!!! warning
    该条目已过时, 请参考 Kotlin 示例使用

```java
public class Main {
    public static SimpleSatoriProperties properties = new SimpleSatoriProperties("127.0.0.1:5500", "token");
    public static void main(String[] args) {
        Satori client = Satori.client(properties);
        client.onMessageCreated((bot, event, msg) -> {
            if ("在吗".equals(msg)) {
                bot.createMessage(event.getChannel().getId(), new At(event.getUser().getId()) + "我在!");
            }
            return null;
        });
        client.connect();
    }
}
```

### C#

!!! warning
    使用 C# 可能遇到很多问题, 推荐使用 [Satori.NET](https://github.com/bsdayo/Satori.NET) 而非本项目进行开发

Coming soon...

## 其他

Yutori: 作者名称 Yurn 与 Satori 协议名称结合而来

## 下一步
[进阶](Advanced.md)