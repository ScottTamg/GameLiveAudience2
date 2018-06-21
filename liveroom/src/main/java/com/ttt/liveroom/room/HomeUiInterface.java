package com.ttt.liveroom.room;


import com.ttt.liveroom.base.BaseUiInterface;
import com.ttt.liveroom.bean.WebSocketInfoBean;

/**
 * Created by Iverson on 2017/7/18 上午10:15
 * 此类用于：
 */

public interface HomeUiInterface extends BaseUiInterface {
    //获取websocket成功
    void getWebSocketSuccess(WebSocketInfoBean bean);

    //获取websocket失败
    void onDataFailure(String msg);
}
