package com.ttt.liveroom.bean.websocket;

/**
 * Created by mrliu on 2018/2/2.
 * 此类用于:
 */

public class DisConnectLmRequest extends WsRequest {

    private DisConnectLmRequestData data;

    public DisConnectLmRequestData getData() {
        return data;
    }

    public void setData(DisConnectLmRequestData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DisConnectLmRequest{" +
                "data=" + data +
                '}';
    }

    public static class DisConnectLmRequestData {
        private String roomId;
        private String adminUserId;
        private String userId;

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

        @Override
        public String toString() {
            return "DisConnectLmRequestData{" +
                    "roomId='" + roomId + '\'' +
                    ", adminUserId='" + adminUserId + '\'' +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }
}
