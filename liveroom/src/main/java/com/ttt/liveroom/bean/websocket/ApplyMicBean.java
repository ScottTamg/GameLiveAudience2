package com.ttt.liveroom.bean.websocket;

/**
 * Created by Iverson on 2016/12/8 下午3:46.
 * 版权所有：国士无双
 */

public class ApplyMicBean extends WsRequest {

    private ApplyMicBeanData data;

    public ApplyMicBeanData getData() {
        return data;
    }

    public void setData(ApplyMicBeanData data) {
        this.data = data;
    }

    public static class ApplyMicBeanData {
        //房间号
        private String roomId;
        //主播id
        private String adminUserId;
        //用户ID
        private String userId;
        //用户昵称
        private String nickName;
        //用户头像
        private String avatar;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getAdminUserId() {
            return adminUserId;
        }

        public void setAdminUserId(String adminUserId) {
            this.adminUserId = adminUserId;
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

    }


}
