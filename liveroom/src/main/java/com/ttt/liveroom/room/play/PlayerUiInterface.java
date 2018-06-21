package com.ttt.liveroom.room.play;


import com.ttt.liveroom.base.BaseUiInterface;
import com.ttt.liveroom.bean.GetFriendBean;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.bean.gift.Gift;
import com.ttt.liveroom.bean.room.ComplainOptionBean;

import java.util.List;


/**
 * Created by 刘景 on 2017/06/11.
 */

public interface PlayerUiInterface extends BaseUiInterface {

    void showGiftList(List<Gift> giftList);

    void showUserInfo(UserInfo userInfo, boolean isPopup);

    void showFriendList(List<GetFriendBean> friendBeanList);

    void startHostResult();

    void getComOptionSuccess(String reportId, List<ComplainOptionBean.ListBean> list);

    void getHitCode(String msg);
}
