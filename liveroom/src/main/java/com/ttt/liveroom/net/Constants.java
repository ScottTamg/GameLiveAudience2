package com.ttt.liveroom.net;

/**
 * Created by Iverson on 2016/12/23 下午5:44
 * 此类用于  放置一些常量的数据和网址
 */

public class Constants {

    public static final int VIEW_THROTTLE_TIME = 3;
    public static final int VIEW_THROTTLE_TIME_SHORT = 5;

    /**
     * 基地址
     */
//    public static final String MAIN_HOST_FOR_PING = "dev.api.customize.3ttech.cn/";//2.0版本
    public static final String MAIN_HOST_FOR_PING = "118.25.93.124/";
    //服务器root地址
    public static String MAIN_HOST_URL = "http://" + MAIN_HOST_FOR_PING;

    /**
     * 直播间心心防抖动时间。
     * 1s <= 50次点击。
     */
    public static final int LIVE_ROOM_HEART_THROTTLE = 200;

    /**
     * Web Socket 服务器地址。
     */
    public static String SOCKET_URL = "ws://118.25.93.124:9550";
    public static final String WEBSOCKET_ROLE_HOST = "1";
    public static final String WEBSOCKET_ROLE_AUDIENCE = "0";

    /**
     * 微信开放平台appId
     */
    public static final String WX_APPID = "wx66280587679b13f2";
    public static final String PAY_RESULT_STATUS_SUCCESS = "200";
    public static final String PAY_RESULT_STATUS_FAIL = "500";
    public static final String PAY_TYPE_WEIXIN = "1";

    public static final String KEYROOMID = "key_room_id";

    public static final String IM_LOG_TAG = "IM_LOG_TAG";

    public static String IS_LIVE;


}
