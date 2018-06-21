package com.ttt.liveroom.widget;

import com.ttt.liveroom.base.BaseUiInterface;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.bean.room.RoomAdminInfo;

import java.util.List;

/**
 * Created by huanzhang on 2016/4/16.
 */
public interface IUserInfoDialog extends BaseUiInterface {
    void showUserInfo(UserInfo info);

    //    得到播放人数
    void getAdminLists(List<RoomAdminInfo> adminList);

    //    如果该房间没有管理员
    void adminnullgoinit();

    //    关注
    void getStartCode(int code);

    //    解除关注
    void getRemoveStartCode(int code);

}
