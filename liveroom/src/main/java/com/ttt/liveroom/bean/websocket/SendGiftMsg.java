package com.ttt.liveroom.bean.websocket;


/**
 * @author Muyangmin
 * @since 1.0.0
 */
public class SendGiftMsg implements RoomPublicMsg {


    /**
     * messageType : gift_notify_res
     * code : 0
     * message :
     * data : {"roomId":"100008","userId":"200004","nickName":"超级巨星","avatar":"http://3tdoc.oss-cn-beijing.aliyuncs.com/wechat/avatar/8.jpg","level":"1","userIdTo":"100008","giftId":"100001","num":1}
     */

    private String messageType;
    private int code;
    private String message;
    private DataBean data;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * roomId : 100008
         * userId : 200004
         * nickName : 超级巨星
         * avatar : http://3tdoc.oss-cn-beijing.aliyuncs.com/wechat/avatar/8.jpg
         * level : 1
         * userIdTo : 100008
         * giftId : 100001
         * num : 1
         */

        private String roomId;
        private String userId;
        private String nickName;
        private String giftId;
        private String giftName;
        private String giftImg;
        private int num;
        private String userIdTo;
        private String avatar;
        private String level;
        private String income ;

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

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getUserIdTo() {
            return userIdTo;
        }

        public void setUserIdTo(String userIdTo) {
            this.userIdTo = userIdTo;
        }

        public String getGiftId() {
            return giftId;
        }

        public void setGiftId(String giftId) {
            this.giftId = giftId;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
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

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "roomId='" + roomId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", level='" + level + '\'' +
                    ", userIdTo='" + userIdTo + '\'' +
                    ", giftId='" + giftId + '\'' +
                    ", num=" + num +
                    ", giftName='" + giftName + '\'' +
                    ", giftImg='" + giftImg + '\'' +
                    ", income=" + income +
                    '}';
        }
    }
}