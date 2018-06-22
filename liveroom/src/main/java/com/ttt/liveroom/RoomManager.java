package com.ttt.liveroom;

/**
 * Created by Iverson on 2018/4/2 下午3:11
 * 此类用于：
 */

public class RoomManager {

    private static RoomManager instance;
    private RoomInterface mRoomInstance;

    private void RoomManager() {
    }

    public static RoomManager getInstance() {
        if (instance == null) {
            synchronized (RoomManager.class) {
                if (instance == null) {
                    instance = new RoomManager();
                }
            }
        }
        return instance;
    }

    //设置三方的对象
    public void setRoomInstance(RoomInterface roomInstance) {
        mRoomInstance = roomInstance;
    }

    //便于sdk使用
    public RoomInterface getRoomInstance() {
        return mRoomInstance;
    }

}

