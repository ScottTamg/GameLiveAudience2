package com.ttt.liveroom.bean.websocket;


/**
 * Created by 刘景 on 2017/05/11.
 */

public class WsLoginRequest extends WsRequest {


    private WsLoginRequestData data;

    public WsLoginRequestData getData() {
        return data;
    }

    public void setData(WsLoginRequestData data) {
        this.data = data;
    }

    public static class WsLoginRequestData {
        /**
         * roomId : 124
         * userId : 321
         * nickName : nickName
         * avatar : avatar
         * message : message
         * role : 0
         * level : 5
         * masterUserId : 111
         * masterNickName : masterNickName
         * masterAvatar : masterAvatar
         * masterLevel : 9
         */

        private String roomId;
        private String userId;
        private String nickName;
        private String avatar;
        private String message;
        private int role;
        private int level;
        private int masterUserId;
        private String masterNickName;
        private String masterAvatar;
        private int masterLevel;
        private String balance;
        private String income;

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

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getMasterUserId() {
            return masterUserId;
        }

        public void setMasterUserId(int masterUserId) {
            this.masterUserId = masterUserId;
        }

        public String getMasterNickName() {
            return masterNickName;
        }

        public void setMasterNickName(String masterNickName) {
            this.masterNickName = masterNickName;
        }

        public String getMasterAvatar() {
            return masterAvatar;
        }

        public void setMasterAvatar(String masterAvatar) {
            this.masterAvatar = masterAvatar;
        }

        public int getMasterLevel() {
            return masterLevel;
        }

        public void setMasterLevel(int masterLevel) {
            this.masterLevel = masterLevel;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }
    }

}
