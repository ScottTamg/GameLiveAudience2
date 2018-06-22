package com.ttt.liveroom.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iverson on 2016/12/23 下午11:57
 * 此类用于：
 */

public class LoginInfo {

    /**
     * token : 86d620480c7999e7c1db00b17495b5e5
     * nickname : 东方不败
     * userId : 849
     * totalBalance : 8888
     */
    private String token;
    private String nickName;
    private String userName;
    @SerializedName("userId")
    private String userId;
    private String roomId;
    private String mobile;
    private String avatar;
    private String level;
    private String followees_cnt;
    private String followers_cnt;
    private String balance;
    private String serverId;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTotalBalance() {
        return balance;
    }

    public void setTotalBalance(String balance) {
        this.balance = balance;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickName;
    }

    public void setNickname(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "token='" + token + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", mobile='" + mobile + '\'' +
                ", avatar='" + avatar + '\'' +
                ", level='" + level + '\'' +
                ", balance=" + balance +
                '}';
    }
}
