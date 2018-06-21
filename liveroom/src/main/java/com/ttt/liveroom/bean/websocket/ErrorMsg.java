package com.ttt.liveroom.bean.websocket;

/**
 * @author Muyangmin
 * @since 1.0.0
 */
public class ErrorMsg {

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
        return "ErrorMsg{" +
                "type='" + type + '\'' +
                '}';
    }
}
