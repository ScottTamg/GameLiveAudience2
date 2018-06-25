package com.ttt.liveroom;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ttt.liveroom.room.utils.TTTSDK;
import com.ttt.liveroom.util.SPUtils;

/**
 * Created by Iverson on 2018/4/2 下午3:37
 * 此类用于：
 */
public class RoomSDK {

    private static RoomSDK instance;
    private static Context mContext;

    private void RoomSDK() {
    }


    //初始化sdk数据
    public static void init(Context context) {
        if (instance == null) {
            synchronized (RoomSDK.class) {
                if (instance == null) {
                    instance = new RoomSDK();
                }
            }
        }
        mContext = context;
        SPUtils.init(mContext, "gamelive");
        Fresco.initialize(mContext);
        //天象APPID
        TTTSDK.init(mContext, "555e189bf1278119c78f6b1753bfe4b5");
    }

    public static Context getContext() {
        return mContext;
    }


    //释放一些数据
    public static void release() {
        if (mContext != null) {
            mContext = null;
        }
    }
}
