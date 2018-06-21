package com.ttt.liveroom.bean.websocket;

/**
 * Created by mrliu on 2018/1/12.
 * 此类用于:
 */

public class RsqMicBean extends WsRequest {

    private RsqMicBeanData data;

    public RsqMicBeanData getData() {
        return data;
    }

    public void setData(RsqMicBeanData data) {
        this.data = data;
    }

    public static class RsqMicBeanData {
        private String userId;
        private String type;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
