package com.ttt.liveroom.bean.websocket;

/**
 * Created by mrliu on 2018/1/11.
 * 此类用于:
 */

public class WsPongMsg extends WsResponse {

    private WsPongMsgData data;

    public WsPongMsgData getData() {
        return data;
    }

    public void setData(WsPongMsgData data) {
        this.data = data;
    }

    public static class WsPongMsgData {
        private String userId;
        private String isMaster;
        private String roomId;


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

        public String getIsMaster() {
            return isMaster;
        }

        public void setIsMaster(String isMaster) {
            this.isMaster = isMaster;
        }
    }
}
