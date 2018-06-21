package com.ttt.liveroom.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ttt.liveroom.bean.LoginInfo;
import com.ttt.liveroom.bean.PayChannel;
import com.ttt.liveroom.bean.PublishRoomIdBean;
import com.ttt.liveroom.bean.UserInfo;

/**
 * @author Iverson
 * Created by Iverson on 2016/12/26 下午8:33
 * 此类用于：用于管理SP工具类
 */
public class SPHelper {
    private static final String IS_FIRST_OPEN = "isFirstOpen";
    private static final String IS_FIRST_ENTER_SPACE = "is_first_enter_space";
    private static final String KEY_LOGIN_INFO = "loginInfo";
    private static final String KEY_USER_INFO = "userinfo";
    private static final String PHONE_NUM = "phoneNum";
    private static final String KEY_PAY_CHANNEL = "payChannel";
    private static final String KEY_SERVER_ADDRESS = "server_address";
    private static final String KEY_UNIONID = "unionID";
    private static final String KEY_PUBLISH_ROOM_ID = "publish_room_id";
    private static final String KEY_RONG_TOKEN = "rong_token";

    public static void setIsFirst() {
        SPUtils.save(IS_FIRST_OPEN, false);
    }

    public static boolean getIsFirst() {
        return (boolean) SPUtils.get(IS_FIRST_OPEN, true);
    }

    public static void setIsFirstEnterSpace() {
        SPUtils.save(IS_FIRST_ENTER_SPACE, false);
    }

    public static boolean getIsFirstEnterSpace() {
        return (boolean) SPUtils.get(IS_FIRST_ENTER_SPACE, true);
    }

    /**
     * 保存用户的登录数据。
     */
    public static void setLoginInfo(@NonNull LoginInfo info) {
        String infoToSave = new Gson().toJson(info);
        SPUtils.save(KEY_LOGIN_INFO, infoToSave);
    }

    /**
     * 保存用户的数据。
     */
    public static void setUserInfo(@NonNull UserInfo info) {
        String infoToSave = new Gson().toJson(info);
        SPUtils.save(KEY_USER_INFO, infoToSave);
    }

    /**
     * 清除用户数据。
     */
    public static void removeUserInfo() {
        SPUtils.remove(KEY_USER_INFO);
    }

    /**
     * 清除登录数据。
     */
    public static void removeLoginInfo() {
        SPUtils.remove(KEY_LOGIN_INFO);
    }

    public static void removeUnionId() {
        SPUtils.remove(KEY_UNIONID);
    }

    /**
     * 查询存储的用户登录数据，如果不存在则返回null。
     */
    @Nullable
    public static LoginInfo getLoginInfo() {
        String savedInfo = (String) SPUtils.get(KEY_LOGIN_INFO, "");
        if (TextUtils.isEmpty(savedInfo)) {
            return null;
        }
        return new Gson().fromJson(savedInfo, LoginInfo.class);
    }

    /**
     * 查询存储的用户数据，如果不存在则返回null。
     */
    @Nullable
    public static UserInfo getUserInfo() {
        String savedInfo = (String) SPUtils.get(KEY_USER_INFO, "");
        if (TextUtils.isEmpty(savedInfo)) {
            return null;
        }
        return new Gson().fromJson(savedInfo, UserInfo.class);
    }

    /**
     * 保存用户的登录的账号
     */
    public static void setPhoneNum(String num) {
        SPUtils.save(PHONE_NUM, num);
    }

    /**
     * 用户的登录账号
     */

    public static String getPhoneNum() {
        return (String) SPUtils.get(PHONE_NUM, "");
    }

    /**
     * 保存unionid
     *
     * @param unionId
     * @return
     */
    public static void saveUnionId(String unionId) {
        SPUtils.save(KEY_UNIONID, unionId);
    }

    /**
     * 获取unionId
     *
     * @return
     */

    public static String getUnionId() {
        return (String) SPUtils.get(KEY_UNIONID, "");
    }

    @PayChannel
    public static int getPreferredChannel(@PayChannel int defValue) {
        return (int) SPUtils.get(KEY_PAY_CHANNEL, defValue);
    }

    public static void savePreferredPayChannel(@PayChannel int channel) {
        SPUtils.save(KEY_PAY_CHANNEL, channel);
    }


    /**
     * 设置服务器地址
     *
     * @param serverAddress
     */
    public static void setKeyServerAddress(String serverAddress) {
        SPUtils.save(KEY_SERVER_ADDRESS, serverAddress);
    }

    /**
     * 获取服务器地址
     *
     * @return
     */
    public static String getKeyServerAddress() {
        return (String) SPUtils.get(KEY_SERVER_ADDRESS, "101.37.75.52");
    }

    /**
     * 保存用户开播的用户信息
     */

    public static void savePublishId(@NonNull PublishRoomIdBean bean) {
        String publishRoomId = new Gson().toJson(bean);
        SPUtils.save(KEY_PUBLISH_ROOM_ID, publishRoomId);
    }

    /**
     * 获取用户开播的用户信息
     */
    @Nullable
    public static PublishRoomIdBean getPubLishRoomId() {
        String publishRoomId = (String) SPUtils.get(KEY_PUBLISH_ROOM_ID, "");
        if (TextUtils.isEmpty(publishRoomId)) {
            return null;
        }
        return new Gson().fromJson(publishRoomId, PublishRoomIdBean.class);
    }

    /**
     * 保存融云token
     *
     * @param token
     */
    public static void saveRongToken(String token) {
        SPUtils.save(KEY_RONG_TOKEN, token);
    }

    /**
     * 获取融云token
     *
     * @return
     */
    public static String getRongToken() {
        return (String) SPUtils.get(KEY_RONG_TOKEN, "");
    }
}
