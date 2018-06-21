package com.ttt.liveroom.bean.websocket;


import java.util.List;

/**
 * 登录到IM房间返回的信息。
 *
 * @author Muyangmin
 * @since 1.0.0
 */
public class WsLoginMsg extends WsResponse {

    private WsLoginMsgData data;

    public WsLoginMsgData getData() {
        return data;
    }

    public void setData(WsLoginMsgData data) {
        this.data = data;
    }

    public static class WsLoginMsgData {


        /**
         * roomId : 200004
         * userId : 200004
         * avatar :
         * nickName : 主播昵称
         * level : 1
         * income : 0
         * count : 11
         * banlance 可用余额
         * userList : [{"userId":"200004","nickName":"昵称","avatar":"1","level":1}]
         */

        private String roomId;
        private int userId;
        private String avatar;
        private String nickName;
        private int level;
        private String balance;
        private int count;
        private String income;
        private List<UserListBean> userList;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
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

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public List<UserListBean> getUserList() {
            return userList;
        }

        public void setUserList(List<UserListBean> userList) {
            this.userList = userList;
        }

        public static class UserListBean {
            /**
             * userId : 200004
             * nickName : 昵称
             * avatar : 1
             * level : 1
             */

            private String userId;
            private String nickName;
            private String avatar;
            private int level;

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

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }
        }
    }

}
