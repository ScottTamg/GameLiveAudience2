package com.ttt.liveroom.bean.room;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/9 0009.
 */
public class RoomAdminInfo implements Parcelable {
    private String id;
    private String sex;
    private String intro;
    private String nickname;
    private String city;
    private String snap;
    //    房间号
    private String curroomnum;
    //    vip等级
    private String vip;
    //    秀逗余额
    private String anchorBalance;
    //    等级
    private String emceelevel;
    //    头像
    private String avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSnap() {
        return snap;
    }

    public void setSnap(String snap) {
        this.snap = snap;
    }

    public String getCurroomnum() {
        return curroomnum;
    }

    public void setCurroomnum(String curroomnum) {
        this.curroomnum = curroomnum;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getAnchorBalance() {
        return anchorBalance;
    }

    public void setAnchorBalance(String anchorBalance) {
        this.anchorBalance = anchorBalance;
    }

    public String getEmceelevel() {
        return emceelevel;
    }

    public void setEmceelevel(String emceelevel) {
        this.emceelevel = emceelevel;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nickname);
        dest.writeString(this.curroomnum);
        dest.writeString(this.snap);
        dest.writeString(this.city);
        dest.writeString(this.avatar);
        dest.writeString(this.sex);
        dest.writeString(this.emceelevel);
    }

    public RoomAdminInfo() {

    }

    protected RoomAdminInfo(Parcel in) {
        this.id = in.readString();
        this.nickname = in.readString();
        this.curroomnum = in.readString();
        this.snap = in.readString();
        this.city = in.readString();
        this.avatar = in.readString();
        this.sex = in.readString();
        this.emceelevel = in.readString();
    }

    public static final Creator<RoomAdminInfo> CREATOR = new Creator<RoomAdminInfo>() {
        @Override
        public RoomAdminInfo createFromParcel(Parcel source) {
            return new RoomAdminInfo(source);
        }

        @Override
        public RoomAdminInfo[] newArray(int size) {
            return new RoomAdminInfo[size];
        }
    };
}
