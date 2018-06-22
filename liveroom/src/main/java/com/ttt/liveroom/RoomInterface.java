package com.ttt.liveroom;

import com.ttt.liveroom.bean.LoginInfo;

/**
 * Created by Iverson on 2018/4/2 下午2:32
 * 此类用于：用于对接第三方的顶层接口
 */

public interface RoomInterface {

    /**
     * 需要调用方的提供观众的信息
     *
     * @return
     */
    LoginInfo getRoomInfo();

    /**
     * @param num        送的礼物数量
     * @param giftName   送的礼物名称
     * @param price      送的礼物的单价
     * @param totalMoney 观众剩余的金币
     * @param serverId   观众的服务器id
     * @param sendUserId 观众的userid
     * @param AnchorId     主播的id
     */
    void sendGift(int num, String giftName, String price, String totalMoney,
                  String serverId, String sendUserId, String AnchorId);

    /**
     * 直播界面切换成小屏的时候调用
     */
    void changeSmall();

}
