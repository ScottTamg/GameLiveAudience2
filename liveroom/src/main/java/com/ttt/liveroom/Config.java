package com.ttt.liveroom;

import com.ttt.liveroom.bean.room.NewestAuthorBean;

/**
 * Created by Iverson on 2018/3/23 上午11:13
 * 此类用于：
 */

public class Config {

    public static int Local_Role = 0;  // 0：代表观众  1：代表主播

    public final static int ROLE_HOST = 1;
    public final static int ROLE_AUDIENCE = 0;
    public static String ROOM_ID = "";
    public static String ROOM_HOST_ID = "";
    public static String ROOM_HOST_AVATAR = "";
    public static String ROOM_HOST_NICKNAME = "";
    public static final String WEBSOCKET_ROLE_HOST = "1";
    public static final String WEBSOCKET_ROLE_AUDIENCE = "0";
    public static int SCREEND_HEIGHT = 0;
    public static String LIVE_PULL_URL = "";
    public static String LIVE_WS_URL = "";
    public static final int VIEW_THROTTLE_TIME = 3;

    public static NewestAuthorBean.ListBean LIVE_DATA;
}
