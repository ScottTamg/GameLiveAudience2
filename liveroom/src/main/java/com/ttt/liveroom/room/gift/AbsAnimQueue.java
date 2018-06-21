package com.ttt.liveroom.room.gift;

import android.support.annotation.NonNull;

import com.ttt.liveroom.bean.gift.SendGiftAction;
import com.ttt.liveroom.util.L;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;


/**
 * @author Muyangmin
 * @since 1.0.0
 */
public abstract class AbsAnimQueue implements IAnimController {

    private final String LOG_TAG = getClass().getSimpleName();

    /**
     * 核心队列数据结构。
     */
    private Queue<SendGiftAction> actionQueue;
    private List<IGiftAnimPlayer> playerList;

    public AbsAnimQueue(List<IGiftAnimPlayer> list) {
        actionQueue = new LinkedList<>();
        playerList = list;
        if (playerList.size() != getMaxConcurrentNum()) {
            throw new IllegalArgumentException(String.format(Locale.US, "The size of player list " +
                    "was %d, should be %d!", playerList.size(), getMaxConcurrentNum()));
        }
        //绑定所有的动画播放器
        for (IGiftAnimPlayer player : playerList) {
            player.bindAnimController(this);
        }
        L.d(LOG_TAG, "Queue init finished with %d players.", playerList.size());
    }

    @Override
    public void enqueue(@NonNull SendGiftAction action) {
        //优先查找可加入的Player。
        IGiftAnimPlayer joinable = findJoinablePlayer(action);
        if (joinable != null) {
            joinable.joinAnim(action);
            return;
        }
        //如未找到，则查找可播放新动画的Player。
        IGiftAnimPlayer available = findAvailablePlayer();
        if (available != null) {
            available.startAnim(action);
            return;
        }
        //仍无法播放，放入队列等待。
        actionQueue.add(action);
    }

    @Override
    public synchronized void onPlayerAvailable() {
        if (actionQueue.isEmpty() || playerList.isEmpty()) {
            return;
        }
        //再加一层锁重新查找Player，如果有则加入动画。
        synchronized (this) {
            IGiftAnimPlayer player = findAvailablePlayer();
            SendGiftAction action = actionQueue.poll();
            if (player != null && action != null) {
                player.startAnim(action);
            }
        }
    }

    @Override
    public void removeAll() {
        for (IGiftAnimPlayer player : playerList) {
            player.cancelAnim();
        }
        actionQueue.clear();
    }

    /**
     * Define the max concurrent num for this player queue.
     */
    protected abstract int getMaxConcurrentNum();


    /**
     * 查找可加入连击动画的Player。
     *
     * @return 如果成功找到，则返回该Player，否则返回null。
     */
    private IGiftAnimPlayer findJoinablePlayer(SendGiftAction action) {
        for (IGiftAnimPlayer player : playerList) {
            if (player.canJoin(action)) {
                return player;
            }
        }
        return null;
    }

    /**
     * 查找可播放新动画的Player。
     *
     * @return 如果成功找到，则返回该Player，否则返回null。
     */
    private IGiftAnimPlayer findAvailablePlayer() {
        for (IGiftAnimPlayer player : playerList) {
            if (player.available()) {
                return player;
            }
        }
        return null;
    }

}
