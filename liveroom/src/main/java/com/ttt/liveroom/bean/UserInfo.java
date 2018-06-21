package com.ttt.liveroom.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iverson on 2016/12/27 下午4:31
 * 此类用于：个人信息bean
 */

public class UserInfo implements Parcelable {
    /**
     * userName :
     * realName :
     * idCard :
     * avatar :
     * nickName :
     * mobile : 18610900467
     * description :
     * isValid : 1
     * liveTime : 0
     * balance : 0
     * level : 1
     * followers_cnt :
     * followees_cnt :
     * isAttention :
     * isLive :
     */

    private String userId;
    private String userName;
    private String realName;
    private String idCard;
    private String avatar;
    private String nickName;
    private String mobile;
    private String description;
    private String isValid;
    private String balance;
    private String birth;
    private String sex;
    private String city;
    private String province;
    private String level;
    private String followers_cnt;
    private String followees_cnt;
    private int isAttention;
    private int isLive;
    private int roomId;
    private String income ;
    private String expenditure ;
    private int isBlacklist;

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        realName = in.readString();
        idCard = in.readString();
        avatar = in.readString();
        nickName = in.readString();
        mobile = in.readString();
        description = in.readString();
        isValid = in.readString();
        balance = in.readString();
        birth = in.readString();
        sex = in.readString();
        city = in.readString();
        province = in.readString();
        level = in.readString();
        followers_cnt = in.readString();
        followees_cnt = in.readString();
        isAttention = in.readInt();
        isLive = in.readInt();
        roomId = in.readInt();
        income = in.readString();
        expenditure =in.readString();
        isBlacklist=in.readInt();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getId() {
        return userId;
    }

    public void setId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String getFollowers_cnt() {
        return followers_cnt;
    }

    public void setFollowers_cnt(String followers_cnt) {
        this.followers_cnt = followers_cnt;
    }

    public String getFollowees_cnt() {
        return followees_cnt;
    }

    public void setFollowees_cnt(String followees_cnt) {
        this.followees_cnt = followees_cnt;
    }

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public int getIsLive() {
        return isLive;
    }

    public void setIsLive(int isLive) {
        this.isLive = isLive;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(String expenditure) {
        this.expenditure = expenditure;
    }

    public int getIsBlacklist() {
        return isBlacklist;
    }

    public void setIsBlacklist(int isBlacklist) {
        this.isBlacklist = isBlacklist;
    }

    public static Creator<UserInfo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(realName);
        dest.writeString(idCard);
        dest.writeString(avatar);
        dest.writeString(nickName);
        dest.writeString(mobile);
        dest.writeString(description);
        dest.writeString(isValid);
        dest.writeString(balance);
        dest.writeString(level);
        dest.writeString(followers_cnt);
        dest.writeString(followees_cnt);
        dest.writeInt(isAttention);
        dest.writeInt(isLive);
        dest.writeInt(roomId);
        dest.writeString(income);
        dest.writeString(expenditure);
        dest.writeInt(isBlacklist);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                "roomId='" + roomId + '\'' +
                "isBlacklist='" + isBlacklist + '\'' +
                "expenditure='" + expenditure + '\'' +
                "income='" + income + '\'' +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", idCard='" + idCard + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", description='" + description + '\'' +
                ", isValid='" + isValid + '\'' +
                ", balance='" + balance + '\'' +
                ", level='" + level + '\'' +
                ", followers_cnt='" + followers_cnt + '\'' +
                ", followees_cnt='" + followees_cnt + '\'' +
                ", isAttention='" + isAttention + '\'' +
                '}';
    }

}
