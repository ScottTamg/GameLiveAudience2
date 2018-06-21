package com.ttt.liveroom.bean.websocket;

/**
 * @author Muyangmin
 * @since 1.0.0
 */
public class UserPublicMsg implements RoomPublicMsg {

    private String messageType;
    private String code;
    private String message;
    private UserPublicMsgData data;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserPublicMsgData getData() {
        return data;
    }

    public void setData(UserPublicMsgData data) {
        this.data = data;
    }

    public static class UserPublicMsgData {

        /**
         * roomId : 200004
         * userId : 200004
         * nickName : 超级巨星
         * avatar :
         */

        private String roomId;
        private String userId;
        private String nickName;
        private String avatar;
        private int fly;

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

        public int getFly() {
            return fly;
        }

        public void setFly(int fly) {
            this.fly = fly;
        }
    }

}
