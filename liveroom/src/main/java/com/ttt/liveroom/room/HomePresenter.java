package com.ttt.liveroom.room;

import android.support.annotation.NonNull;

import com.ttt.liveroom.base.BaseObserver;
import com.ttt.liveroom.base.BasePresenter;
import com.ttt.liveroom.bean.BaseResponse;
import com.ttt.liveroom.bean.WebSocketInfoBean;
import com.ttt.liveroom.net.NetManager;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Iverson on 2017/7/18 上午10:17
 * 此类用于：
 */

public class HomePresenter extends BasePresenter<HomeUiInterface> {
    public HomePresenter(HomeUiInterface uiInterface) {
        super(uiInterface);
    }

    //获取websocket信息
    public void getWebSocket(@NonNull String roomId){
        Subscription subscription = NetManager.getInstance().create(PublishApi.class).getWebSocket(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<WebSocketInfoBean>>(getUiInterface()) {
                    @Override
                    public void onSuccess(BaseResponse<WebSocketInfoBean> response) {
                        if(response.getData()!=null){
                            getUiInterface().getWebSocketSuccess(response.getData());
                        }
                    }

                    @Override
                    protected void onDataFailure(BaseResponse<WebSocketInfoBean> response) {
                        getUiInterface().onDataFailure(response.getMsg());
                    }
                });
        addSubscription(subscription);
    }
}
