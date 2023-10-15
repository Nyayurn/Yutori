package com.yurn.satori.sdk;

import com.yurn.satori.sdk.entity.ConnectionEntity;
import com.yurn.satori.sdk.entity.EventEntity;
import lombok.Data;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Yurn
 */
@Data
public class ListenerContainer {
    /**
     * 当成功连接上 WebSocket 时的委托
     */
    private final List<Consumer<Response>> onOpenDelegate = new ArrayList<>();

    /**
     * 当接受到任意消息时的委托
     */
    private final List<Consumer<ConnectionEntity>> onMessageDelegate = new ArrayList<>();

    /**
     * 当成功与 Satori 建立事件推送时的委托
     */
    private final List<Consumer<ConnectionEntity.Ready>> onConnectDelegate = new ArrayList<>();

    /**
     * 接受到 Satori 事件时的委托
     */
    private final List<Consumer<EventEntity>> onEventDelegate = new ArrayList<>();

    /**
     * 与 WebSocket 断开连接时的委托
     */
    private final List<Consumer<String>> onDisconnectDelegate = new ArrayList<>();

    /**
     * 出现错误时的委托
     */
    private final List<Consumer<Throwable>> onErrorDelegate = new ArrayList<>();

    public void addOnOpenListener(Consumer<Response> listener) {
        onOpenDelegate.add(listener);
    }

    public void addOnMessageListener(Consumer<ConnectionEntity> listener) {
        onMessageDelegate.add(listener);
    }

    public void addOnConnectListener(Consumer<ConnectionEntity.Ready> listener) {
        onConnectDelegate.add(listener);
    }

    public void addOnEventListener(Consumer<EventEntity> listener) {
        onEventDelegate.add(listener);
    }

    public void addOnDisconnectListener(Consumer<String> listener) {
        onDisconnectDelegate.add(listener);
    }

    public void addOnErrorListener(Consumer<Throwable> listener) {
        onErrorDelegate.add(listener);
    }

    public void runOnOpenListener(Response response) {
        for (Consumer<Response> delegate : onOpenDelegate) {
            delegate.accept(response);
        }
    }

    public void runOnMessageListener(ConnectionEntity entity) {
        for (Consumer<ConnectionEntity> delegate : onMessageDelegate) {
            delegate.accept(entity);
        }
    }

    public void runOnConnectListener(ConnectionEntity.Ready ready) {
        for (var delegate : onConnectDelegate) {
            delegate.accept(ready);
        }
    }

    public void runOnEventListener(EventEntity event) {
        for (var delegate : onEventDelegate) {
            delegate.accept(event);
        }
    }

    public void runOnDisconnectListener(String s) {
        for (var delegate : onDisconnectDelegate) {
            delegate.accept(s);
        }
    }

    public void runOnErrorListener(Throwable e) {
        for (var delegate : onErrorDelegate) {
            delegate.accept(e);
        }
    }
}
