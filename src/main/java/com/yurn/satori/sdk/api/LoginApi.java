package com.yurn.satori.sdk.api;

import com.alibaba.fastjson2.JSONObject;
import com.yurn.satori.sdk.entity.LoginEntity;
import com.yurn.satori.sdk.entity.PropertiesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录信息 API
 *
 * @author Yurn
 */
@Data
@AllArgsConstructor
public class LoginApi {
    /**
     * SendMessage 实例类
     */
    private SendMessage sendMessage;

    public LoginApi(PropertiesEntity properties) {
        this.sendMessage = new SendMessage(null, null, properties);
    }

    /**
     * 获取登录信息
     * 获取登录信息, 返回一个 Login 对象
     *
     * @return 输出
     */
    public LoginEntity getLogin() {
        String response = sendMessage.sendGenericMessage("login", "get", null);
        return JSONObject.parseObject(response, LoginEntity.class);
    }
}
