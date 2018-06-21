package com.ttt.liveroom.bean.websocket;

public class GagResResponse extends WsRequest {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GagReqRequest{" +
                "data=" + data.toString() +
                '}';
    }

    public static class Data {
        private String roomId;
        private String userId;
        private String avatar;
        private String nickName;
        private String level;
        private String expiry;

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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "roomId='" + roomId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", level='" + level + '\'' +
                    ", expiry='" + expiry + '\'' +
                    '}';
        }
    }
}
