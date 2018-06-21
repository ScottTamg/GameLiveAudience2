package com.ttt.liveroom.bean.websocket;

/**
 * Created by Iverson on 2017/3/22 下午3:12
 * 此类用于：
 */

public class StopLiveMsg implements RoomPublicMsg {

    private String type;
    private String user_id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
