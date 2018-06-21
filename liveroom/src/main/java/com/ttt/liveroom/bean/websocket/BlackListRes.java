package com.ttt.liveroom.bean.websocket;

public class BlackListRes extends WsResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String roomId;
        private String userId;
        private String blacklistUserId;

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

        public String getBlacklistUserId() {
            return blacklistUserId;
        }

        public void setBlacklistUserId(String blacklistUserId) {
            this.blacklistUserId = blacklistUserId;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "roomId='" + roomId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", blacklistUserId='" + blacklistUserId + '\'' +
                    '}';
        }
    }
}
