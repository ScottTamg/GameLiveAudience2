package com.ttt.liveroom.websocket;

/**
 * Created by 刘景 on 2017/05/11.
 */

public interface WsListener<Data> {
    /**
     * Handle the data, often display it.
     * <p>This method would be called on main thread.</p>
     */
    void handleData(Data data);
}
