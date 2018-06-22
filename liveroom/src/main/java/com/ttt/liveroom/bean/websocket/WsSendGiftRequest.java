package com.ttt.liveroom.bean.websocket;

/**
 * Created by mrliu on 2018/1/12.
 * 此类用于:
 */

public class WsSendGiftRequest extends WsRequest {


    private WsSendGiftRequestData data;

    public WsSendGiftRequestData getData() {
        return data;
    }

    public void setData(WsSendGiftRequestData data) {
        this.data = data;
    }

    public static class WsSendGiftRequestData {
        /**
         * roomId : 100001
         * userId : 100001
         * giftId : 100001
         * price : 1
         * num : 2
         * nickName : nickName
         * avatar : avatar
         * level : 1
         * message : message
         */

        private String roomId;
        private String userId;
        private String userIdTo;
        private String giftId;
        private String price;
        private int num;
        private String nickName;
        private String avatar;
        private String level;
        private String message;
        private String giftName;
        private String giftImg;
        private String giftGif;
        private String giftLevel;
        private String isFire;

        public String getUserIdTo() {
            return userIdTo;
        }

        public void setUserIdTo(String userIdTo) {
            this.userIdTo = userIdTo;
        }

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

        public String getGiftId() {
            return giftId;
        }

        public void setGiftId(String giftId) {
            this.giftId = giftId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
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

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public String getGiftImg() {
            return giftImg;
        }

        public void setGiftImg(String giftImg) {
            this.giftImg = giftImg;
        }

        public String getGiftGif() {
            return giftGif;
        }

        public void setGiftGif(String giftGif) {
            this.giftGif = giftGif;
        }

        public String getGiftLevel() {
            return giftLevel;
        }

        public void setGiftLevel(String giftLevel) {
            this.giftLevel = giftLevel;
        }

        public String getIsFire() {
            return isFire;
        }

        public void setIsFire(String isFire) {
            this.isFire = isFire;
        }
    }


}
