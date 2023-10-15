package com.yurn.satori.sdk.api;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yurn.satori.sdk.entity.GuildEntity;
import com.yurn.satori.sdk.entity.PageResponseEntity;
import com.yurn.satori.sdk.entity.PropertiesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 群组 API
 *
 * @author Yurn
 */
@Data
@AllArgsConstructor
public class GuildApi {
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

    public GuildApi(String platform, String selfId, PropertiesEntity properties) {
        this.platform = platform;
        this.selfId = selfId;
        this.sendMessage = new SendMessage(platform, selfId, properties);
    }

    /**
     * 获取群组
     * 根据 ID 获取, 返回一个 Guild 对象
     *
     * @param guildId 群组 ID
     * @return 输出
     */
    public GuildEntity getGuild(String guildId) {
        JSONObject map = new JSONObject();
        map.put("guild_id", guildId);
        String response = sendMessage.sendGenericMessage("guild", "get", map.toString());
        return JSONObject.parseObject(response, GuildEntity.class);
    }

    /**
     * 获取群组列表
     * 获取当前用户加入的全部群组, 返回一个 Guild 的 分页列表。
     *
     * @param next 分页令牌
     * @return 输出
     */
    public List<PageResponseEntity<GuildEntity>> listGuild(String next) {
        JSONObject map = new JSONObject();
        map.put("next", next);
        String response = sendMessage.sendGenericMessage("guild", "list", map.toString());
        //noinspection unchecked
        return JSONArray.parse(response).stream().map(o -> (PageResponseEntity<GuildEntity>) o).toList();
    }

    /**
     * 处理群组邀请
     * 处理来自群组的邀请
     *
     * @param messageId 请求 ID
     * @param approve   是否通过请求
     * @param comment   备注信息
     */
    public void approveGuild(String messageId, boolean approve, String comment) {
        JSONObject map = new JSONObject();
        map.put("message_id", messageId);
        map.put("approve", approve);
        map.put("comment", comment);
        sendMessage.sendGenericMessage("guild", "approve", map.toString());
    }
}
