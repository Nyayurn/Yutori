package com.yurn.satori.sdk.api;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.yurn.satori.sdk.entity.GuildRoleEntity;
import com.yurn.satori.sdk.entity.PageResponseEntity;
import com.yurn.satori.sdk.entity.PropertiesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 群组角色 API
 *
 * @author Yurn
 */
@Data
@AllArgsConstructor
public class GuildRoleApi {
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

    public GuildRoleApi(String platform, String selfId, PropertiesEntity properties) {
        this.platform = platform;
        this.selfId = selfId;
        this.sendMessage = new SendMessage(platform, selfId, properties);
    }


    /**
     * 设置群组成员角色
     * 设置群组内用户的角色
     *
     * @param guildId 群组 ID
     * @param userId  用户 ID
     * @param roleId  角色 ID
     */
    public void setGuildRole(String guildId, String userId, String roleId) {
        JSONObject map = new JSONObject();
        map.put("guild_id", guildId);
        map.put("user_id", userId);
        map.put("role_id", roleId);
        sendMessage.sendGenericMessage("guild.member.role", "set", map.toString());
    }

    /**
     * 取消群组成员角色
     * 取消群组内用户的角色
     *
     * @param guildId 群组 ID
     * @param userId  用户 ID
     * @param roleId  角色 ID
     */
    public void unsetGuildRole(String guildId, String userId, String roleId) {
        JSONObject map = new JSONObject();
        map.put("guild_id", guildId);
        map.put("user_id", userId);
        map.put("role_id", roleId);
        sendMessage.sendGenericMessage("guild.member.role", "unset", map.toString());
    }

    /**
     * 获取群组角色列表
     * 获取群组角色列表, 返回一个 GuildRole 的 分页列表
     *
     * @param guildId 群组 ID
     * @param next    分页令牌
     * @return 输出
     */
    public List<PageResponseEntity<GuildRoleEntity>> listGuildRole(String guildId, String next) {
        JSONObject map = new JSONObject();
        map.put("guild_id", guildId);
        map.put("next", next);
        String response = sendMessage.sendGenericMessage("guild.role", "list", map.toString());
        //noinspection unchecked
        return JSONArray.parse(response).stream().map(o -> ((PageResponseEntity<GuildRoleEntity>) o)).toList();
    }

    /**
     * 创建群组角色
     * 创建群组角色, 返回一个 GuildRole 对象
     *
     * @param guildId 群组 ID
     * @param role    角色数据
     * @return 输出
     */
    public GuildRoleEntity createGuildRole(String guildId, GuildRoleEntity role) {
        JSONObject map = new JSONObject();
        map.put("guild_id", guildId);
        map.put("role", role);
        String response = sendMessage.sendGenericMessage("guild.role", "create", map.toString());
        return JSONObject.parseObject(response, GuildRoleEntity.class);
    }

    /**
     * 修改群组角色
     *
     * @param guildId 群组 ID
     * @param roleId  角色 ID
     * @param role    角色数据
     */
    public void updateGuildRole(String guildId, String roleId, GuildRoleEntity role) {
        JSONObject map = new JSONObject();
        map.put("guild_id", guildId);
        map.put("role_id", roleId);
        map.put("role", role);
        sendMessage.sendGenericMessage("guild.role", "update", map.toString());
    }

    /**
     * 删除群组角色
     *
     * @param guildId 群组 ID
     * @param roleId  角色 ID
     */
    public void deleteGuildRole(String guildId, String roleId) {
        JSONObject map = new JSONObject();
        map.put("guild_id", guildId);
        map.put("role_id", roleId);
        sendMessage.sendGenericMessage("guild.role", "delete", map.toString());
    }
}
