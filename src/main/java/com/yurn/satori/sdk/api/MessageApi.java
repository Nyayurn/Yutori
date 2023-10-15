package com.yurn.satori.sdk.api;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yurn.satori.sdk.entity.MessageEntity;
import com.yurn.satori.sdk.entity.PageResponseEntity;
import com.yurn.satori.sdk.entity.PropertiesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 消息 API
 *
 * @author Yurn
 */
@Data
@AllArgsConstructor
public class MessageApi {
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

    public MessageApi(String platform, String selfId, PropertiesEntity properties) {
        this.platform = platform;
        this.selfId = selfId;
        this.sendMessage = new SendMessage(platform, selfId, properties);
    }


    /**
     * 发送消息
     * 发送消息, 返回一个 Message 对象构成的数组
     *
     * @param channelId 频道 ID
     * @param content   消息内容
     * @return 输出
     */
    public List<MessageEntity> createMessage(String channelId, String content) {
        JSONObject map = new JSONObject();
        map.put("channel_id", channelId);
        map.put("content", content);
        String response = sendMessage.sendGenericMessage("message", "create", map.toString());
        return JSONArray.parse(response).toList(MessageEntity.class);
    }

    /**
     * 获取消息
     * 获取特定消息, 返回一个 Message 对象
     *
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @return 输出
     */
    public MessageEntity getMessage(String channelId, String messageId) {
        JSONObject map = new JSONObject();
        map.put("channel_id", channelId);
        map.put("message_id", messageId);
        String response = sendMessage.sendGenericMessage("message", "get", map.toString());
        return JSONObject.parseObject(response, MessageEntity.class);
    }

    /**
     * 撤回消息
     * 撤回特定消息
     *
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     */
    public void deleteMessage(String channelId, String messageId) {
        JSONObject map = new JSONObject();
        map.put("channel_id", channelId);
        map.put("message_id", messageId);
        sendMessage.sendGenericMessage("message", "delete", map.toString());
    }

    /**
     * 编辑消息
     * 编辑特定消息
     *
     * @param channelId 频道 ID
     * @param messageId 消息 ID
     * @param content   消息内容
     */
    public void updateMessage(String channelId, String messageId, String content) {
        JSONObject map = new JSONObject();
        map.put("channel_id", channelId);
        map.put("message_id", messageId);
        map.put("content", content);
        sendMessage.sendGenericMessage("message", "update", map.toString());
    }

    /**
     * 获取消息列表
     * 获取频道消息列表, 返回一个 Message 的 分页列表
     *
     * @param channelId 频道 ID
     * @param next      分页令牌
     * @return 输出
     */
    public List<PageResponseEntity<MessageEntity>> listMessage(String channelId, String next) {
        JSONObject map = new JSONObject();
        map.put("channel_id", channelId);
        map.put("next", next);
        String response = sendMessage.sendGenericMessage("message", "list", map.toString());
        //noinspection unchecked
        return JSONArray.parse(response).stream().map(o -> ((PageResponseEntity<MessageEntity>) o)).toList();
    }
}
