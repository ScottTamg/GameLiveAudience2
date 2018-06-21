package com.ttt.liveroom.bean.websocket;

/**
 * Created by 刘景 on 2017/05/11.
 */

public class WsLogoutRequest extends WsRequest {

    private WsLogoutRequestData data;

    public WsLogoutRequestData getData() {
        return data;
    }

    public void setData(WsLogoutRequestData data) {
        this.data = data;
    }

    public static class WsLogoutRequestData {


        /**
         * roomId : 124
         * userId : 321
         * avatar : avatar
         * nickName : nickName
         * level : level
         * isMaster : 0
         * message :
         */

        private int roomId;
        private int userId;
        private String avatar;
        private String nickName;
        private String level;
        private int isMaster;
        private String message;

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
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

        public int getIsMaster() {
            return isMaster;
        }

        public void setIsMaster(int isMaster) {
            this.isMaster = isMaster;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
