package com.yurn.satori.sdk.api;

import com.yurn.satori.sdk.entity.PropertiesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.*;

import java.io.IOException;
import java.util.Optional;

/**
 * 一个比较底层的 API
 *
 * @author Yurn
 */
@Data
@AllArgsConstructor
public class SendMessage {
    /**
     * HTTP Client
     */
    private OkHttpClient client = new OkHttpClient();

    /**
     * 平台名称
     */
    private String platform;

    /**
     * 机器人 ID
     */
    private String selfId;

    /**
     * Satori API 版本
     */
    private String version = "v1";

    /**
     * Satori 对接相关设置
     */
    private PropertiesEntity properties;

    public SendMessage(String platform, String selfId, PropertiesEntity properties) {
        this.platform = platform;
        this.selfId = selfId;
        this.properties = properties;
    }

    public String sendGenericMessage(String resource, String method, String body) {
        RequestBody requestBody = RequestBody.create(Optional.ofNullable(body).orElse(""),
                MediaType.parse("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .url(String.format("http://%s/%s/%s.%s", properties.getAddress(), version, resource, method))
                .headers(makeHttpHeaders())
                .post(requestBody)
                .build();
        return send(request);
    }

    public String sendInternalMessage(String method, String body) {
        RequestBody requestBody = RequestBody.create(Optional.ofNullable(body).orElse(""),
                MediaType.parse("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .url(String.format("http://%s/%s/internal/%s", properties.getAddress(), version, method))
                .headers(makeHttpHeaders())
                .post(requestBody)
                .build();
        return send(request);
    }

    private Headers makeHttpHeaders() {
        Headers.Builder builder = new Headers.Builder();
        builder.add("Content-Type", "application/json");
        if (properties.getToken() != null) {
            builder.add("Authorization", "Bearer " + properties.getToken());
        }
        if (platform != null) {
            builder.add("X-Platform", platform);
        }
        if (selfId != null) {
            builder.add("X-Self-ID", selfId);
        }
        return builder.build();
    }

    private String send(Request request) {
        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            if (response.body() != null) {
                return response.body().string();
            }
            return response.message();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
