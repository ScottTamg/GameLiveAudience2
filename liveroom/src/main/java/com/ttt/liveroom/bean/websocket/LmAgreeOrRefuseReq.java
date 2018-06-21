package com.ttt.liveroom.bean.websocket;

public class LmAgreeOrRefuseReq extends WsRequest {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String roomId;
        private String adminUserId;
        private String userId;
        private String type;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getAdminUserId() {
            return adminUserId;
        }

        public void setAdminUserId(String adminUserId) {
            this.adminUserId = adminUserId;
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

        @Override
        public String toString() {
            return "Data{" +
                    "roomId='" + roomId + '\'' +
                    ", adminUserId='" + adminUserId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
