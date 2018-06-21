package com.ttt.liveroom.base;

import android.support.annotation.NonNull;

import com.ttt.liveroom.bean.LoginInfo;
import com.ttt.liveroom.bean.PayChannel;
import com.ttt.liveroom.bean.PublishRoomIdBean;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.util.SPHelper;

/**
 * Created by 刘景 on 2017/05/11.
 */

public class DataManager {
    private static DataManager instance;
    private LoginInfo mLoginInfo;
    private UserInfo mUserInfo;
    private PublishRoomIdBean mPublishRoomIdBean;

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (SPHelper.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    public void saveUserinfo(@NonNull UserInfo userInfo) {
        mUserInfo = userInfo;
        SPHelper.setUserInfo(mUserInfo);
    }

    public void saveLoginInfo(@NonNull LoginInfo loginInfo) {
        //Update cached object!
        mLoginInfo = loginInfo;
        SPHelper.setLoginInfo(loginInfo);
//        CrashReport.setUserId(loginInfo.getUserId());
    }

    public void setPhoneNum(String num) {
        SPHelper.setPhoneNum(num);
    }

    public String getPhoneNum() {
        return SPHelper.getPhoneNum();
    }

    //保存微信登录unionId
    public void removeUserInfo() {
        mUserInfo = null;
        SPHelper.removeUserInfo();
    }

    public UserInfo getmUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = SPHelper.getUserInfo();
        }
        return mUserInfo;
    }

    public void clearLoginInfo() {
        mLoginInfo = null;
        SPHelper.removeLoginInfo();
    }

    public LoginInfo getLoginInfo() {
        if (mLoginInfo == null) {
            mLoginInfo = SPHelper.getLoginInfo();
        }
        return mLoginInfo;
    }

    //保存支付选中状态
    @PayChannel
    public int getPreferredPayChannel(int defValue) {
        return SPHelper.getPreferredChannel(defValue);
    }

    public void savePreferredPayChannel(@PayChannel int channel) {
        SPHelper.savePreferredPayChannel(channel);
    }

    public void savePublishId(@NonNull PublishRoomIdBean bean) {
        mPublishRoomIdBean = bean;
        SPHelper.savePublishId(mPublishRoomIdBean);
    }

    public PublishRoomIdBean getPubLishRoomId() {
        if (mPublishRoomIdBean == null) {
            mPublishRoomIdBean = SPHelper.getPubLishRoomId();
        }
        return mPublishRoomIdBean;
    }

}
