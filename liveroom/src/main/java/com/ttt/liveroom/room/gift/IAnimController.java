package com.ttt.liveroom.room.gift;

import android.support.annotation.NonNull;

import com.ttt.liveroom.bean.gift.SendGiftAction;

/**
 * Created by 刘景 on 2017/06/06.
 */

public interface IAnimController {
    /**
     * 提交新的送礼动画。
     */
    void enqueue(@NonNull SendGiftAction action);

    /**
     * 停止当前正在播放的动画并移除所有的动画。
     */
    void removeAll();

    /**
     * 动画播放完成后调用该方法。
     */
    void onPlayerAvailable();
}
