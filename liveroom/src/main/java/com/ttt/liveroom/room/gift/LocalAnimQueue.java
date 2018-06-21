package com.ttt.liveroom.room.gift;

import java.util.List;

/**
 * @author Muyangmin
 * @since 1.0.0
 */
public class LocalAnimQueue extends AbsAnimQueue {

    public LocalAnimQueue(List<IGiftAnimPlayer> list) {
        super(list);
    }

    @Override
    protected int getMaxConcurrentNum() {
        return 2;
    }
}
