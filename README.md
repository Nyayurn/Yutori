<div align="center">

# YurnSatoriFramework

基于 [Satori](https://satori.js.org/zh-CN/) 协议的 Java 机器人开发框架

<img src="https://img.shields.io/badge/JDK-17+-brightgreen.svg?style=flat-square" alt="jdk-version">

</div>

# 快速开始

## 基础信息

> 提示: 本文档默认您了解并熟悉 Java 基本语法

- 推荐使用 [YurnQbotFramework](https://github.com/Nyayurn/YurnQbotFramework) 框架进行 QQ 机器人的开发

## 使用流程

1. 首先创建一个空的 Maven 项目(什么?不会?可以右上角关闭本页面了)
2. 依赖引入
3. 基本配置
4. 进阶

## 依赖引入

### Maven

1. 在项目目录下新建 lib 文件夹
2. 下载 jar 包并将其丢进 lib 文件夹内
3. 配置 pom.xml

```xml

<dependencies>
    <!-- 核心框架 -->
    <dependency>
        <groupId>com.yurn</groupId>
        <artifactId>YurnSatoriFramework</artifactId>
        <version>0.0.4</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/lib/YurnSatoriFramework-0.0.4.jar</systemPath>
    </dependency>
    <!-- Http 和 WebSocket 所需依赖 -->
    <dependency>
        <artifactId>okhttp</artifactId>
        <groupId>com.squareup.okhttp3</groupId>
        <version>4.10.0</version>
    </dependency>
    <!-- JSON 序列化/反序列化 -->
    <dependency>
        <groupId>com.alibaba.fastjson2</groupId>
        <artifactId>fastjson2</artifactId>
        <version>2.0.40</version>
    </dependency>
    <!-- 日志系统(可替换为其他slf4j实现) -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.4.11</version>
    </dependency>
</dependencies>
```

## 基本配置

### 启动类

```java
public class Main {
    private final ListenerContainer listenerContainer = new ListenerContainer();
    private final PropertiesEntity properties = new PropertiesEntity("127.0.0.1:5500", "Sakura201520");

    public Main() {
        // new 一个对象以触发监听器的注册
        new TestListener(listenerContainer, properties);
    }

    private void run() {
        // 构造一个 HTTP 客户端用于进行 WebSocket 连接
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).writeTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(3, TimeUnit.SECONDS).build();
        // 构造一个 Request 请求
        Request request = new Request.Builder().get().url("ws://" + properties.getAddress() + "/v1/events").build();
        // 新建一个 WebSocket 连接
        //client.newWebSocket(request, new MyWebSocketClient());
        client.newWebSocket(request, new MyWebSocketClient(properties.getToken(), listenerContainer));
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
```

### 第一个监听器

```java
public class TestListener {
    private final PropertiesEntity properties;

    public TestListener(ListenerContainer listenerContainer, PropertiesEntity properties) {
        this.properties = properties;
        // 通过在构造器内对 listenerContainer 添加一个事件实现注册
        listenerContainer.addOnEventListener(event -> {
            // 判断事件的类型是否为 message-created(新消息创建, 即接收到消息)
            if (event.getType().equals(MessageEvents.MESSAGE_CREATED)) {
                this.onMessage(event);
            }
        });
    }

    public void onMessage(EventEntity event) {
        if ("test".equals(event.getMessage().getContent())) {
            ChannelEntity channel = event.getChannel();
            // 通过对应 API 类的方法发送消息
            MessageApi messageApi = new MessageApi(event.getPlatform(), event.getSelfId(), properties);
            messageApi.createMessage((channel.getType().equals(ChannelEntity.DIRECT) ? "private:" : "") + channel.getId(),
                    "test done!");
        }
    }
}
```

# 进阶

- 框架整体与 Satori 架构基本一致, 请参考 [Satori 文档](https://satori.js.org/zh-CN/protocol)
- 源码内有 javadoc 方便阅读, 请自行阅读源码
