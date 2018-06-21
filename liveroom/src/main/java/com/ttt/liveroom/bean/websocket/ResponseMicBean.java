package com.ttt.liveroom.bean.websocket;

/**
 * Created by mrliu on 2018/1/12.
 * 此类用于:响应连麦
 */

public class ResponseMicBean {
    private String messageType;


    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * data : {"userList":[{"avatar":"http://img5.imgtn.bdimg.com/it/u=3208258265,2930130286&fm=27&gp=0.jpg","nickName":"魁拔","introduction":"连个麦可好","userId":"2"}]}
     */
    private DataBean data;

    @Override
    public String toString() {
        return "ResponseMicBean{" +
                "data=" + data +
                '}';
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * avatar : http://img5.imgtn.bdimg.com/it/u=3208258265,2930130286&fm=27&gp=0.jpg
         * nickName : 刘景
         * introduction : 连个麦可好
         * userId : 2
         */
        private String roomId;
        private int count;
        private String userId;
        private String nickName;
        private String avatar;
        private String type;

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

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "roomId='" + roomId + '\'' +
                    ", count=" + count +
                    ", userId='" + userId + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
