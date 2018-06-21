package com.ttt.liveroom.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mrliu on 2018/1/8.
 * 此类用于:获取websocket信息
 */

public class WebSocketInfoBean {
    /**
     * roomServer : {"host":"dev.api.demo.3ttech.cn","port":"9505"}
     * roomServer-wss : {"host":"dev.api.demo.3ttech.cn","port":"9506"}
     */
    private RoomServerBean roomServer;
    @SerializedName("roomServer-wss")
    private RoomServerwssBean roomServerwss;

    public RoomServerBean getRoomServer() {
        return roomServer;
    }

    public void setRoomServer(RoomServerBean roomServer) {
        this.roomServer = roomServer;
    }

    public RoomServerwssBean getRoomServerwss() {
        return roomServerwss;
    }

    public void setRoomServerwss(RoomServerwssBean roomServerwss) {
        this.roomServerwss = roomServerwss;
    }

    public static class RoomServerBean {
        /**
         * host : dev.api.demo.3ttech.cn
         * port : 9505
         */

        private String host;
        private String port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "RoomServerBean{" +
                    "host='" + host + '\'' +
                    ", port='" + port + '\'' +
                    '}';
        }
    }

    public static class RoomServerwssBean {
        /**
         * host : dev.api.demo.3ttech.cn
         * port : 9506
         */

        private String host;
        private String port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "RoomServerwssBean{" +
                    "host='" + host + '\'' +
                    ", port='" + port + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WebSocketInfoBean{" +
                "roomServer=" + roomServer +
                ", roomServerwss=" + roomServerwss +
                '}';
    }
}
