package com.ttt.liveroom.room.utils;

import java.util.List;

/**
 * Created by Iverson on 2017/11/15 下午2:54
 * 此类用于：用于直播的辅助类
 */

public interface RoomLiveInterface {

    //进入房间成功
    void enterRoomSuccess();

    //进入房间失败
    void enterRoomFailue(int error);

    //直播中断线
    void onDisconnected(int errorCode);

    //直播成员退出
    void onMemberExit(long userId);

    //直播成员进入
    void onMemberEnter(long userId, EnterUserInfo userInfo);

    //直播主播进入
    void onHostEnter(long userId, EnterUserInfo userInfo);

    void onUpdateLiveView(List<EnterUserInfo> userInfos);

}
