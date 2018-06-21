package com.ttt.liveroom.bean.websocket;


/**
 * Created by 刘景 on 2017/05/11.
 */

public class WsPublicMsgRequest extends WsRequest {

    private WsPublicMsgRequestData data;

    public WsPublicMsgRequestData getData() {
        return data;
    }

    public void setData(WsPublicMsgRequestData data) {
        this.data = data;
    }

    public static class WsPublicMsgRequestData {

        /**
         * roomId : 124
         * userId : 321
         * nickName : nickName
         * avatar : avatar
         * message : message
         */

        private String roomId;
        private String userId;
        private String nickName;
        private String avatar;
        private String message;
        private String fly;

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

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getFly() {
            return fly;
        }

        public void setFly(String fly) {
            this.fly = fly;
        }
    }

}
