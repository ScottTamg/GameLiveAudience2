package com.ttt.liveroom.bean.websocket;

/**
 * Created by Administrator on 2016/6/18 0018.
 */
public class SystemMsg implements RoomPublicMsg {
    private String type;
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
