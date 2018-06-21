package com.ttt.liveroom.room.gift;

import android.support.annotation.NonNull;

import com.ttt.liveroom.bean.gift.SendGiftAction;

/**
 * @author Muyangmin
 * @since 1.0.0
 */
public interface IGiftAnimPlayer {

    /**
     * 绑定控制器，主要是为了动画播放完成后可以调用Controller的方法。
     */
    void bindAnimController(IAnimController controller);

    /**
     * 是否可加入动画中完成连发动画。
     */
    boolean canJoin(@NonNull SendGiftAction action);

    /**
     * 是否可以开始播放新的动画。
     *
     * @see #startAnim(SendGiftAction)
     */
    boolean available();

    /**
     * 开始播放动画效果。
     * <p>注意：动画播放完成后必须调用 {@link IAnimController#onPlayerAvailable()}方法！</p>
     *
     * @see #available()
     * @see #bindAnimController(IAnimController)
     */
    void startAnim(@NonNull SendGiftAction action);

    /**
     * @see #canJoin(SendGiftAction)
     */
    void joinAnim(@NonNull SendGiftAction action);

    /**
     * 停止当前正在播放的动画效果，通常在退出页面时由Controller调用。
     */
    void cancelAnim();
}
