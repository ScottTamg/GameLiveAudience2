package com.ttt.liveroom.room.play;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ttt.liveroom.base.BaseObserver;
import com.ttt.liveroom.base.BasePresenter;
import com.ttt.liveroom.base.DataManager;
import com.ttt.liveroom.bean.BaseResponse;
import com.ttt.liveroom.bean.GetFriendBean;
import com.ttt.liveroom.bean.LoginInfo;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.bean.gift.Gift;
import com.ttt.liveroom.bean.room.ComplainOptionBean;
import com.ttt.liveroom.net.NetManager;
import com.ttt.liveroom.room.RoomApi;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 刘景 on 2017/06/11.
 */

public class PlayPresenter extends BasePresenter<PlayerUiInterface> {

    protected PlayPresenter(PlayerUiInterface uiInterface) {
        super(uiInterface);
    }

    /**
     * 加载礼物列表
     */
    public void loadGiftList(@NonNull String userId) {
        Subscription subscription = NetManager.getInstance().create(RoomApi.class).getAvailableGifts(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<List<Gift>>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<List<Gift>> response) {
                        if (response.getData() != null && response.getData().size() > 0) {
                            getUiInterface().showGiftList(response.getData());
                        } else {
                            getUiInterface().showGiftList(new ArrayList<Gift>());
                        }

                    }
                });
        addSubscription(subscription);
    }

    /**
     * 发送礼物
     *
     * @param token
     * @param toUserId
     * @param giftId
     * @param count
     */
    public void sendGift(String token, String toUserId, String giftId, String count) {
        Subscription subscription = NetManager.getInstance().create(RoomApi.class).sendGift(token, toUserId, giftId, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        //Empty

                    }
                });
        addSubscription(subscription);
    }

    /**
     * 关注
     *
     * @param uid
     * @param roomid
     */
    public void starUser(String uid, String roomid) {
        Subscription subscription = NetManager.getInstance().create(RoomApi.class).starUser(uid, roomid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        if ("0".equals(response.getCode())) {
                            getUiInterface().startHostResult();
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 取消关注
     *
     * @param uid
     * @param roomid
     */
    public void unStarUser(String uid, String roomid) {
        Subscription subscription = NetManager.getInstance().create(RoomApi.class).unStarUser(uid, roomid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        if ("0".equals(response.getCode())) {
                            getUiInterface().startHostResult();
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 获取一下用户信息 == 就是获取那个是否直播
     *
     * @param userId
     */
    public void loadUserInfo(String userId, final boolean isPopup) {
        Subscription subscription = null;
        LoginInfo loginInfo = DataManager.getInstance().getLoginInfo();
        String caller_id = null;
        if (null != loginInfo) {
            caller_id = loginInfo.getUserId();
        }
        subscription = NetManager.getInstance().create(RoomApi.class).getUserInfo(userId, caller_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<UserInfo>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> response) {
                        getUiInterface().showUserInfo(response.getData(), isPopup);
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 小伙伴
     */
    public void loadFriendList() {
        Subscription subscription = NetManager.getInstance().create(RoomApi.class).getFriendList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<List<GetFriendBean>>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<List<GetFriendBean>> response) {
                        getUiInterface().showFriendList(response.getData());
                    }
                });
        addSubscription(subscription);
    }


    /**
     * 观看直播
     *
     * @param token
     * @param uid
     * @param roomid
     */
    public void watchLive(String token, String uid, String roomid) {
        Subscription subscription = NetManager.getInstance().create(RoomApi.class).watchLive(token, uid, roomid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        Log.e("PlayPresenter", response.toString());
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 结束观看直播
     *
     * @param token
     * @param uid
     */
    public void stopWatchLive(String token, String uid) {
        Subscription subscription = NetManager.getInstance().create(RoomApi.class).stopWatchLive(token, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {

                    }
                });
        addSubscription(subscription);
    }

    /**
     * 获取举报选项
     */
    public void getComplainOptions(final String reportId) {
        Subscription subscription = NetManager.getInstance().create(RoomApi.class).getComplainOption("1", "10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<ComplainOptionBean>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<ComplainOptionBean> response) {
                        if (response.getData() != null && response.getData().getList() != null) {
                            getUiInterface().getComOptionSuccess(reportId, response.getData().getList());
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 举报
     *
     * @param userid
     * @param reportedUserId
     * @param roomId
     * @param content
     */
    public void complain(String userid, String reportedUserId, String roomId, String content) {
        Subscription subscription = NetManager.getInstance().create(RoomApi.class).complain(userid, reportedUserId, roomId, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        getUiInterface().getHitCode(response.getMsg());
                    }
                });
        addSubscription(subscription);
    }
}
