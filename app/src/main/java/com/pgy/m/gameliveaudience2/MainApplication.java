package com.pgy.m.gameliveaudience2;

import android.app.Application;

import com.ttt.liveroom.RoomSDK;

public class MainApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        RoomSDK.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RoomSDK.release();
    }
}
