package com.ttt.liveroom.bean.websocket;

import com.google.gson.annotations.SerializedName;

/**
 * 观众信息。
 *
 * @author Muyangmin
 * @since 1.0.0
 */
public class AudienceInfo {

    /**
     * client_name : 蝶恋
     * user_id : 829
     * room_id : 1350268125
     * ucuid : 759
     * client_id : 8b8113be08ff0033fa2b
     * vip : 0
     * levelid : 11
     * time : 0
     * msged : false
     */

    @SerializedName("client_name")
    private String clientName;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("room_id")
    private String roomId;
    private String ucuid;
    @SerializedName("client_id")
    private String clientId;
    private int vip;
    @SerializedName("levelid")
    private String levelId;
    private String avatar;
    private int time;
    private boolean msged;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUcuid() {
        return ucuid;
    }

    public void setUcuid(String ucuid) {
        this.ucuid = ucuid;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isMsged() {
        return msged;
    }

    public void setMsged(boolean msged) {
        this.msged = msged;
    }
}
