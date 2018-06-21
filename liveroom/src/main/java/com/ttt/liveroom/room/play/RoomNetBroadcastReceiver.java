package com.ttt.liveroom.room.play;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.ttt.liveroom.room.RoomActivity;
import com.ttt.liveroom.room.RoomFragment;
import com.ttt.liveroom.util.Networks;


/**
 * @author liujing
 * Created by ybs on 2017/4/1.
 */
public class RoomNetBroadcastReceiver extends BroadcastReceiver {

    public NetEvevt evevt = RoomFragment.evevt;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = Networks.getNetWorkState(context);
            // 接口回调传过去状态的类型
            if(evevt!=null){
                evevt.onNetChange(context,netWorkState);
            }
        }
    }

    // 自定义接口
    public interface NetEvevt {
        public void onNetChange(Context context,int netMobile);
    }
}