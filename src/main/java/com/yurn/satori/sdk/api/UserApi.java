package com.yurn.satori.sdk.api;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yurn.satori.sdk.entity.PageResponseEntity;
import com.yurn.satori.sdk.entity.PropertiesEntity;
import com.yurn.satori.sdk.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 用户 API
 *
 * @author Yurn
 */
@Data
@AllArgsConstructor
public class UserApi {
    /**
     * 平台名称
     */
    private String platform;

    /**
     * 机器人 ID
     */
    private String selfId;

    /**
     * SendMessage 实例类
     */
    private SendMessage sendMessage;

    public UserApi(String platform, String selfId, PropertiesEntity properties) {
        this.platform = platform;
        this.selfId = selfId;
        this.sendMessage = new SendMessage(platform, selfId, properties);
    }

    /**
     * 获取用户信息
     * 获取用户信息, 返回一个 User 对象
     *
     * @param userId 用户 ID
     * @return 输出
     */
    public UserEntity getUser(String userId) {
        JSONObject map = new JSONObject();
        map.put("user_id", userId);
        String response = sendMessage.sendGenericMessage("user", "get", map.toString());
        return JSONObject.parseObject(response, UserEntity.class);
    }

    /**
     * 获取好友列表
     * 获取好友列表。返回一个 User 的 分页列表
     *
     * @return 输出
     */
    public List<PageResponseEntity<UserEntity>> listFriend() {
        String response = sendMessage.sendGenericMessage("friend", "list", null);
        //noinspection unchecked
        return JSONArray.parse(response).stream().map(o -> (PageResponseEntity<UserEntity>) o).toList();
    }

    /**
     * 获取好友列表
     * 获取好友列表。返回一个 User 的 分页列表
     *
     * @param next 分页令牌
     * @return 输出
     */
    public List<PageResponseEntity<UserEntity>> listFriend(String next) {
        JSONObject map = new JSONObject();
        map.put("next", next);
        String response = sendMessage.sendGenericMessage("friend", "list", map.toString());
        //noinspection unchecked
        return JSONArray.parse(response).stream().map(o -> (PageResponseEntity<UserEntity>) o).toList();
    }

    /**
     * 处理好友申请
     *
     * @param messageId 消息 ID
     * @param approve   是否通过请求
     */
    public void approveFriend(String messageId, boolean approve) {
        JSONObject map = new JSONObject();
        map.put("message_id", messageId);
        map.put("approve", approve);
        sendMessage.sendGenericMessage("friend", "approve", map.toString());
    }

    /**
     * 处理好友申请
     *
     * @param messageId 消息 ID
     * @param approve   是否通过请求
     * @param comment   备注信息
     */
    public void approveFriend(String messageId, boolean approve, String comment) {
        JSONObject map = new JSONObject();
        map.put("message_id", messageId);
        map.put("approve", approve);
        map.put("comment", comment);
        sendMessage.sendGenericMessage("friend", "approve", map.toString());
    }
}
