package com.ttt.liveroom.bean.gift;

/**
 * 送礼行为类，使用组合，标识谁送了什么礼物，这个实体决定最终送礼动画和逻辑。
 * Created by 刘景 on 2017/06/06.
 */

public class SendGiftAction {
    private String fromUid;
    private String nickname;
    private String avatar;
    private String giftName;
    private String giftIcon;
    private int combo;

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftIcon() {
        return giftIcon;
    }

    public void setGiftIcon(String giftIcon) {
        this.giftIcon = giftIcon;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    @Override
    public String toString() {
        return "SendGiftAction{" +
                "fromUid='" + fromUid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", giftName='" + giftName + '\'' +
                ", giftIcon='" + giftIcon + '\'' +
                ", combo=" + combo +
                '}';
    }
}
