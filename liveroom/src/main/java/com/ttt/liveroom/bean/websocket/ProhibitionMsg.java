package com.ttt.liveroom.bean.websocket;

/**
 * Created by 刘景 on 2016/12/23.
 */

public class ProhibitionMsg {
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

    @Override
    public String toString() {
        return "ProhibitionMsg{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
