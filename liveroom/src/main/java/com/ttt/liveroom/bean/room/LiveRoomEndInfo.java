package com.ttt.liveroom.bean.room;


/**
 * 直播结束后看到的数据，目前包含观看人数和秀币总收入。
 *
 * @author Muyangmin
 * @since 1.0.0
 */
public class LiveRoomEndInfo {


    private String avatar;
    private String nickName;
    private int isAttention;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickName;
    }

    public void setNickname(String nickName) {
        this.nickName = nickName;
    }

    public int getIsattention() {
        return isAttention;
    }

    public void setIsattention(int isattention) {
        this.isAttention = isAttention;
    }

    @Override
    public String toString() {
        return "LiveRoomEndInfo{" +

                "avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                ", isAttention='" + isAttention + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
