package com.yurn.satori.sdk;

import com.alibaba.fastjson2.JSONObject;
import com.yurn.satori.sdk.entity.ConnectionEntity;
import com.yurn.satori.sdk.entity.EventEntity;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
class MyWebSocketClient extends WebSocketListener {
    private final String token;
    private final ListenerContainer listenerContainer;
    private ScheduledFuture<?> heart;
    private Integer sequence;

    public MyWebSocketClient(ListenerContainer listenerContainer) {
        this.token = null;
        this.listenerContainer = listenerContainer;
    }

    public MyWebSocketClient(String token, ListenerContainer listenerContainer) {
        this.token = token;
        this.listenerContainer = listenerContainer;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        log.info("成功建立 WebSocket 连接: {}", response);
        listenerContainer.runOnOpenListener(response);
        sendIdentify(webSocket);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        ConnectionEntity connection = ConnectionEntity.parse(text);
        listenerContainer.runOnMessageListener(connection);
        switch (connection.getOp()) {
            case ConnectionEntity.EVENT -> {
                EventEntity body = (EventEntity) connection.getBody();
                sequence = body.getId();
                listenerContainer.runOnEventListener(body);
            }
            case ConnectionEntity.PONG -> {}
            case ConnectionEntity.READY -> {
                ConnectionEntity.Ready ready = (ConnectionEntity.Ready) connection.getBody();
                log.info("成功建立事件推送: {}", Arrays.toString(ready.getLogins()));
                // 心跳
                ConnectionEntity sendConnection = new ConnectionEntity();
                sendConnection.setOp(ConnectionEntity.PING);
                heart = new ScheduledThreadPoolExecutor(1, r -> new Thread(r)).scheduleAtFixedRate(
                        () -> webSocket.send(JSONObject.toJSONString(sendConnection)), 10, 10, TimeUnit.SECONDS);

                listenerContainer.runOnConnectListener(ready);
            }
            default -> log.error("Unsupported {}", connection);
        }
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("断开连接, code: {}, reason: {}", code, reason);
        if (heart != null && !heart.isCancelled() && !heart.isDone()) {
            heart.cancel(true);
        }

        listenerContainer.runOnDisconnectListener(reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        log.error("出现错误: {}, response: {}", t, response);
        listenerContainer.runOnErrorListener(t);
    }

    private void sendIdentify(WebSocket webSocket) {
        ConnectionEntity connection = new ConnectionEntity();
        connection.setOp(ConnectionEntity.IDENTIFY);
        if (token != null || sequence != null) {
            ConnectionEntity.Identify body = new ConnectionEntity.Identify();
            connection.setBody(body);
            if (token != null) {
                body.setToken(token);
            }
            if (sequence != null) {
                body.setSequence(sequence);
            }
        }
        webSocket.send(JSONObject.toJSONString(connection));
    }
}
