package com.ttt.liveroom.bean.websocket;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28 0028.
 */
public class WsLoginOutMsg extends WsResponse {

    private WsLoginOutMsgData data;

    public WsLoginOutMsgData getData() {
        return data;
    }

    public void setData(WsLoginOutMsgData data) {
        this.data = data;
    }

    public static class WsLoginOutMsgData {


        /**
         * roomId : 200004
         * isMaster : 1
         * userId : 200004
         * avatar :
         * nickName : 昵称
         * level : 1
         * count : 17
         */

        private int roomId;
        private int isMaster;
        private int userId;
        private String avatar;
        private String nickName;
        private String level;
        private int count;

        private List<WsLoginOutMsgList> userList;

        @Override
        public String toString() {
            return "WsLoginOutMsgData{" +
                    "roomId=" + roomId +
                    ", isMaster=" + isMaster +
                    ", userId=" + userId +
                    ", avatar='" + avatar + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", level='" + level + '\'' +
                    ", count=" + count +
                    ", userList=" + userList +
                    '}';
        }

        public List<WsLoginOutMsgList> getUserList() {
            return userList;
        }

        public void setUserList(List<WsLoginOutMsgList> userList) {
            this.userList = userList;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public int getIsMaster() {
            return isMaster;
        }

        public void setIsMaster(int isMaster) {
            this.isMaster = isMaster;
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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public static class WsLoginOutMsgList {
            private String userId;
            private String nickName;
            private String avatar;
            private int level;
            private int fd;
            private int role;

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

            public int getFd() {
                return fd;
            }

            public void setFd(int fd) {
                this.fd = fd;
            }

            public int getRole() {
                return role;
            }

            public void setRole(int role) {
                this.role = role;
            }

            @Override
            public String toString() {
                return "WsLoginOutMsgList{" +
                        "userId='" + userId + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", avatar='" + avatar + '\'' +
                        ", level=" + level +
                        ", fd=" + fd +
                        ", role=" + role +
                        '}';
            }
        }
    }

}
