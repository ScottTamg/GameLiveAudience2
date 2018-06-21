package com.ttt.liveroom.bean;

import android.graphics.Bitmap;

/**
 * Created by 刘景 on 2017/06/09.
 */

public class Danmu {
    public long id;
    public int userId;
    public String type;
    public Bitmap avatarUrl;
    public String content;

    public Danmu(long id, int userId, String type, String content) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.content = content;
    }

    public void setAvatarUrl(Bitmap avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Danmu() {
    }
}
