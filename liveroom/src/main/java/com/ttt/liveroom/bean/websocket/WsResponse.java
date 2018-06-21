package com.ttt.liveroom.bean.websocket;

/**
 * Created by mrliu on 2018/1/9.
 * 此类用于:websocket响应基类
 */

public class WsResponse {

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //Empty
    private String messageType;
    private int code;
    private String message;
}
