package com.ttt.liveroom.bean.websocket;

import java.util.List;

/**
 * Created by 刘景 on 2017/05/11.
 */

public class SystemWelcome implements RoomPublicMsg {

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

    //Empty
    private String messageType;
    private String code;
    private String message;
    private SystemWelcomeData data;

    public SystemWelcomeData getData() {
        return data;
    }

    public void setData(SystemWelcomeData data) {
        this.data = data;
    }

    public static class SystemWelcomeData {


        /**
         * roomId : 200004
         * userId : 200004
         * avatar : 1
         * nickName : 昵称
         * level : 1
         * count : 14
         */

        private String roomId;
        private String userId;
        private String avatar;
        private String nickName;
        private int level;
        private int count;
        private String income;

        private List<SystemWelcomeDataList> userList;

        public List<SystemWelcomeDataList> getUserList() {
            return userList;
        }

        public void setUserList(List<SystemWelcomeDataList> userList) {
            this.userList = userList;
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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        @Override
        public String toString() {
            return "SystemWelcomeData{" +
                    "roomId='" + roomId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", income='" + income + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", level=" + level +
                    ", count=" + count +
                    ", userList=" + userList +
                    '}';
        }

        public static class SystemWelcomeDataList {
            private String userId;
            private String nickName;
            private String avatar;
            private String level;
            private String role;

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

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            @Override
            public String toString() {
                return "SystemWelcomeDataList{" +
                        "userId='" + userId + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", avatar='" + avatar + '\'' +
                        ", level='" + level + '\'' +
                        ", role='" + role + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "SystemWelcome{" +
                "messageType='" + messageType + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
