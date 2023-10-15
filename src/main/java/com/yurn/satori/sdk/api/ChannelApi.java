package com.yurn.satori.sdk.api;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yurn.satori.sdk.entity.ChannelEntity;
import com.yurn.satori.sdk.entity.PageResponseEntity;
import com.yurn.satori.sdk.entity.PropertiesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 频道的 API
 *
 * @author Yurn
 */
@Data
@AllArgsConstructor
public class ChannelApi {
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

    public ChannelApi(String platform, String selfId, PropertiesEntity properties) {
        this.platform = platform;
        this.selfId = selfId;
        this.sendMessage = new SendMessage(platform, selfId, properties);
    }

    /**
     * 获取群组频道
     * 根据 ID 获取频道。返回一个 Channel 对象
     *
     * @param channelId 频道id
     * @return 输出
     */
    public ChannelEntity getChannel(String channelId) {
        JSONObject map = new JSONObject();
        map.put("channel_id", channelId);
        String response = sendMessage.sendGenericMessage("channel", "get", map.toString());
        return JSONObject.parseObject(response, ChannelEntity.class);
    }

    /**
     * 获取群组频道列表
     * 获取群组中的全部频道, 返回一个 Channel 的 分页列表
     *
     * @param guildId 群组 ID
     * @param next    分页令牌
     * @return 输出
     */
    public List<PageResponseEntity<ChannelEntity>> listChannel(String guildId, String next) {
        JSONObject map = new JSONObject();
        map.put("guild_id", guildId);
        map.put("next", next);
        String response = sendMessage.sendGenericMessage("channel", "list", map.toString());
        //noinspection unchecked
        return JSONArray.parse(response).stream().map(o -> (PageResponseEntity<ChannelEntity>) o).toList();
    }

    /**
     * 创建群组频道
     *
     * @param guildId 群组 ID
     * @param data    频道数据
     * @return 输出
     */
    public ChannelEntity createChannel(String guildId, ChannelEntity data) {
        JSONObject map = new JSONObject();
        map.put("guild_id", guildId);
        map.put("data", data);
        String response = sendMessage.sendGenericMessage("channel", "create", map.toString());
        return JSONObject.parseObject(response, ChannelEntity.class);
    }

    /**
     * 修改群组频道
     *
     * @param channelId 频道 ID
     * @param data      频道数据
     * @return 输出
     */
    public ChannelEntity updateChannel(String channelId, ChannelEntity data) {
        JSONObject map = new JSONObject();
        map.put("channel_id", channelId);
        map.put("data", data);
        String response = sendMessage.sendGenericMessage("channel", "update", map.toString());
        return JSONObject.parseObject(response, ChannelEntity.class);
    }

    /**
     * 删除群组频道
     *
     * @param channelId 频道 ID
     */
    public void deleteChannel(String channelId) {
        JSONObject map = new JSONObject();
        map.put("channel_id", channelId);
        sendMessage.sendGenericMessage("channel", "delete", map.toString());
    }

    /**
     * 创建私聊频道
     *
     * @param userId 用户 ID
     * @return 输出
     */
    public ChannelEntity createUserChannel(String userId) {
        JSONObject map = new JSONObject();
        map.put("user_id", userId);
        String response = sendMessage.sendGenericMessage("user.channel", "create", map.toString());
        return JSONObject.parseObject(response, ChannelEntity.class);
    }
}
