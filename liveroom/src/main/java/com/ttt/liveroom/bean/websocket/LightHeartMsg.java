package com.ttt.liveroom.bean.websocket;

import com.google.gson.annotations.SerializedName;

/**
 * 点亮❤的消息。
 *
 * @author Muyangmin
 * @since 1.0.0
 */
public class LightHeartMsg implements RoomPublicMsg {

    /**
     * type : LightHeart
     * color : -14575885
     * client_id : 8b8113be08ff00055f65
     * user_id : 830
     * client_name : 婧婧的旋转
     * levelid : 11
     * vip : 0
     * time : 23:19
     */

    private String type;
    private int color;
    @SerializedName("client_id")
    private String fromClientId;
    @SerializedName("user_id")
    private String fromUserId;
    @SerializedName("client_name")
    private String fromClientName;
    @SerializedName("levelid")
    private int level;
    private int vip;
    private String time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getFromClientId() {
        return fromClientId;
    }

    public void setFromClientId(String fromClientId) {
        this.fromClientId = fromClientId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromClientName() {
        return fromClientName;
    }

    public void setFromClientName(String fromClientName) {
        this.fromClientName = fromClientName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
