package com.ttt.liveroom.bean.websocket;

/**
 * Created by mrliu on 2018/1/12.
 * 此类用于:
 */

public class ApplyMicRsqBean extends WsRequest {

    private ApplyMicRsqBeanData data;

    public ApplyMicRsqBeanData getData() {
        return data;
    }

    public void setData(ApplyMicRsqBeanData data) {
        this.data = data;
    }

    public static class ApplyMicRsqBeanData {
        private String roomId;
        private String userId;
        private String type;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

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
