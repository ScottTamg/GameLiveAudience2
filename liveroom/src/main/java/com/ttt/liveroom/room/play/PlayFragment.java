package com.ttt.liveroom.room.play;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.ttt.liveroom.R;
import com.ttt.liveroom.base.DataManager;
import com.ttt.liveroom.bean.GetFriendBean;
import com.ttt.liveroom.bean.HotAnchorSummary;
import com.ttt.liveroom.bean.LoginInfo;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.bean.gift.Gift;
import com.ttt.liveroom.bean.gift.GiftSendNumBean;
import com.ttt.liveroom.bean.room.ComplainOptionBean;
import com.ttt.liveroom.bean.websocket.LmAgreeOrRefuseRes;
import com.ttt.liveroom.bean.websocket.ResponseMicBean;
import com.ttt.liveroom.net.Constants;
import com.ttt.liveroom.net.NetManager;
import com.ttt.liveroom.room.ApplyLMDialog;
import com.ttt.liveroom.room.MessageDialog;
import com.ttt.liveroom.room.RoomActivity;
import com.ttt.liveroom.room.RoomFragment;
import com.ttt.liveroom.room.ijkplayer.PlayerManager;
import com.ttt.liveroom.room.utils.EnterUserInfo;
import com.ttt.liveroom.util.MrlCountDownTimer;
import com.ttt.liveroom.util.Networks;
import com.ttt.liveroom.websocket.SocketConstants;
import com.ttt.liveroom.websocket.WsObjectPool;
import com.ttt.liveroom.widget.LiveView;
import com.ttt.liveroom.widget.getfriendview.GetFriendLayout;
import com.ttt.liveroom.widget.giftview.GiftClickListener;
import com.ttt.liveroom.widget.giftview.GiftLayoutView;
import com.wushuangtech.api.EnterConfApi;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import ijkplayer.IjkVideoView;
import retrofit2.Response;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author liujing
 * Created by 刘景 on 2017/06/11.
 */
public class PlayFragment extends RoomFragment implements PlayerUiInterface,
        PlayerManager.PlayerStateListener {

    private final String LOG_TAG = PlayFragment.class.getSimpleName();
    private static final String ARG_ANCHOR_SUMMARY = "anchor";
    private static final String ARG_ANCHOR_ISBACK = "isback";

    RelativeLayout mVideoViewRoot;
    ImageView loadingView;
    RelativeLayout surfaceFrame;
    IjkVideoView mVideoView;
    private PlayerManager playerManager;

    private String playbackUrl = "";

    /**
     * 自定义的小伙伴view
     */
    private GetFriendLayout getFriendLayout;

    /**
     * 触摸移动
     */
    private View moveInculde;
    /**
     * 计算便宜量
     */
    private float touchMoveX = -1;
    private float touchMoveY = -1;
    /**
     * 记录
     */
    private boolean isHideMove;
    private LinearLayout mRlApplayConnectMic;
    private ImageView mRoomImgbtnLm;
    private TextView mTvMicText;
    /**
     * 房间主人控件
     */
    private View mRoomOwner;
    public static PlayPresenter presenter;

    private HotAnchorSummary.ListBean mSummary;

    //礼物弹出窗
    private View mChargeLay, mGiftLay;

    private TextView mChargeTv, mchargeinforBtn;
    private Button mGiftSentBtn;
    private TextView mGiftContinue;
    private int giftComboCount = 1;
    private GiftLayoutView mGiftView;
    private LoginInfo loginInfo;
    private boolean isCanApplayLm = true;
    private ImageButton mScreenLandOrPortr;
    private boolean isLand = false;

    private Timer mHearBeatTimer;
    private HeartBeatTask mHeartBeatTask;

    private RelativeLayout mRlLocalLive;
    private LiveView mRlRomoteLive;
    private LiveView mRlRomoteLiveB;
    private ArrayList<LiveView> mLiveViewList = new ArrayList();
    private boolean isFirstEnter = true;

    private boolean isIntRoom = false;

    /**
     * Fragment的View加载完毕的标记
     */
    private boolean isViewCreated;

    /**
     * Fragment对用户可见的标记
     */
    private boolean isUIVisible;

    /**
     * Fragment对用户已经链接服务器
     */
    private boolean isUIShow;
    private ImageView mDownGift;
    private TextView mGiftSendList;
    private PopupWindow popupWindowGiftSendList;

    private List<GiftSendNumBean> mGiftSendNumBeans = new LinkedList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.frag_room_player;
    }

    @Override
    protected void initViews(View view) {
        Bundle arguments = getArguments();
        mAnchorId = arguments.getString(RoomActivity.EXTRA_ANCHOR_ID);
        mHostAvatar = arguments.getString(RoomActivity.EXTRA_ANCHOR_AVATAR);
        mPublishNickName = arguments.getString(RoomActivity.EXTRA_ANCHOR_NICKNAME);
        mHostLevel = arguments.getString(RoomActivity.EXTRA_ANCHOR_LEVEL);
        String hostInfo = arguments.getString(ARG_ANCHOR_SUMMARY);
        playbackUrl = arguments.getString(RoomActivity.EXTRA_LIVE_PULL_URL);
        mLiveId = arguments.getString(RoomActivity.EXTRA_LIVE_ID);
        mStreamId = arguments.getString(RoomActivity.EXTRA_STREAM_ID);

        mSummary = new Gson().fromJson(hostInfo, HotAnchorSummary.ListBean.class);
        super.initViews(view);
        loadingView = (ImageView) view.findViewById(R.id.glideloadingiv);
        Glide.with(this).load(R.drawable.loading).asGif().into(loadingView);
        mVideoViewRoot = (RelativeLayout) view.findViewById(R.id.rl_videoview_root);

        surfaceFrame = (RelativeLayout) view.findViewById(R.id.room_player_frame);
        mRlLocalLive = (RelativeLayout) view.findViewById(R.id.remote_surface_view_a);
        mRlRomoteLive = (LiveView) view.findViewById(R.id.local_surface_view_a);
        mRlRomoteLiveB = (LiveView) view.findViewById(R.id.local_surface_view_b);
        addView();
        timingLogger.reset(TIMING_LOG_TAG, "PlayerFragment#initViews");
        presenter = new PlayPresenter(this);
        loginInfo = DataManager.getInstance().getLoginInfo();
        if (loginInfo != null) {
            presenter.watchLive(loginInfo.getToken(), loginInfo.getUserId(), mSummary.getUserId() + "");
        }
        getFriendLayout = (GetFriendLayout) friendLayout.findViewById(R.id.friend);
        moveInculde = $(view, R.id.room_move_view);

        //显示主播信息
        SimpleDraweeView draweeAnchor = $(view, R.id.img_user_avatar);
        draweeAnchor.setImageURI(NetManager.wrapPathToUri(mSummary.getAvatar()));
        mRoomOwner = $(view, R.id.room_owner);
//        RxView.clicks(draweeAnchor).throttleFirst(Constants.VIEW_THROTTLE_TIME,
//                TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                if (mSummary == null) {
//                    return;
//                }
//                UserInfo info = new UserInfo();
//                info.setId(mSummary.getUserId() + "");
//                info.setNickName(mSummary.getNickName());
//                info.setAvatar(mSummary.getAvatar());
//                info.setLevel(1 + "");
//                getUserInfo(info.getId());
//            }
//        });
        // 点击开始连麦
        mRlApplayConnectMic = $(view, R.id.rl_applay_connect_mic);
        mRoomImgbtnLm = $(view, R.id.room_imgbtn_lm);
        mTvMicText = $(view, R.id.tv_mic_text);
        subscribeClick(mRlApplayConnectMic, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isIntRoom) {
                    toastShort("你正在和主播连麦中");
                    mRoomImgbtnLm.setImageResource(R.drawable.lianmaizhong_dianji);
                    mTvMicText.setText("连麦中");
                    return;
                }
                if (!isCanApplayLm) {
                    toastShort("您正在申请连麦中，请等待主播回复！");
                    mRoomImgbtnLm.setImageResource(R.drawable.shenqinglianmai_dianji);
                    mTvMicText.setText("等待连麦");
                    return;
                }
                showLMRequestDialog();
            }
        });
        mScreenLandOrPortr = $(view, R.id.room_imgbtn_screen);
        // 点击切换大小屏
        if (mScreenLandOrPortr != null) {
            RxView.clicks(mScreenLandOrPortr).throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            isLand = !isLand;
                            if (isLand) {
                                mScreenLandOrPortr.setBackgroundResource(R.drawable.xiaoping);
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            } else {
                                mScreenLandOrPortr.setBackgroundResource(R.drawable.daping);
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            }
                        }
                    });
        }

        RxView.clicks($(view, R.id.room_imgbtn_gift))
                .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        llOperationBar.setVisibility(View.GONE);
                        mGiftLay.setVisibility(View.VISIBLE);
                        showAnimIn(mGiftLay);
                        recyclerPublicChat.setVisibility(View.GONE);
                    }
                });

        mGiftView = $(view, R.id.gift);
        mChargeLay = $(view, R.id.layout_gift_btn_charge);
        mChargeTv = $(view, R.id.layout_gift_charge_tv);
        mchargeinforBtn = $(view, R.id.room_gift_chargeinfor_balance);
        mGiftSentBtn = $(view, R.id.layout_gift_btn_send);
        mGiftContinue = $(view, R.id.layout_gift_btn_continue);
        mGiftLay = $(view, R.id.layout_gift);
        mGiftLay.setVisibility(View.GONE);
        mDownGift = $(view, R.id.iv_gone_gift);
        mGiftSendList = $(view, R.id.show_gift_sendlist);
        mGiftSendList.setSelected(true);

        mGiftSendNumBeans.add(new GiftSendNumBean("1314", "一生一世"));
        mGiftSendNumBeans.add(new GiftSendNumBean("520", "我爱你"));
        mGiftSendNumBeans.add(new GiftSendNumBean("188", "要抱抱"));
        mGiftSendNumBeans.add(new GiftSendNumBean("66", "一切顺利"));
        mGiftSendNumBeans.add(new GiftSendNumBean("30", "想你"));
        mGiftSendNumBeans.add(new GiftSendNumBean("10", "十全十美"));
        mGiftSendNumBeans.add(new GiftSendNumBean("1", "一心一意"));

        if (loginInfo == null) {
            mChargeTv.setText("0");
        } else {
//            String balance = DataManager.getInstance().getmUserInfo().getBalance();
            double balance = DataManager.getInstance().getLoginInfo().getTotalBalance();
            mChargeTv.setText(String.valueOf(balance));
        }
        if (loginInfo != null) {
            presenter.loadGiftList(loginInfo.getUserId());
        }
        timingLogger.addSplit("this.initView");
//          得到主播信息，为了获取当前直播状态而已
        presenter.loadUserInfo(mAnchorId, false);
        presenter.loadFriendList();

        surfaceFrame = $(view, R.id.room_player_frame);

        RxView.clicks(mchargeinforBtn)
                .throttleFirst(Constants.LIVE_ROOM_HEART_THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });
        mChargeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(RechargeActivity.createIntent(getActivity()));
            }
        });
        timingLogger.addSplit("init url");
        timingLogger.addSplit("startPlay");
        timingLogger.dumpToLog();

        RxView.clicks($(view, R.id.play_click_view)).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onRootClickAction();
            }
        });

        //关注按钮
//        subscribeClick(toptabstart, new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                LoginInfo loginInfo = DataManager.getInstance().getLoginInfo();
//                toFollow(loginInfo);
//
//            }
//        });

        startPublish();
    }

    int UPLOAD = 1;

    private void toFollow(LoginInfo loginInfo) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(Constants.MAIN_HOST_URL + "follow/attention", RequestMethod.POST);
        request.add("userId", loginInfo.getUserId());
        request.add("userIdFollow", mAnchorId);
        NoHttp.newRequestQueue().add(UPLOAD, request, OnResponse);
    }

    private OnResponseListener<JSONObject> OnResponse = new OnResponseListener<JSONObject>() {
        @Override
        public void onStart(int i) {

        }

        @Override
        public void onSucceed(int i, com.yolanda.nohttp.rest.Response<JSONObject> response) {
            if (i == UPLOAD) {
                // 请求成功
                JSONObject result = response.get();// 响应结果

                try {
                    code = result.getString("code");
                    if ("0".equals(code)) {
                        handlerTime.sendEmptyMessage(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 响应头
                Headers headers = response.getHeaders();
                headers.getResponseCode();// 响应码
                response.getNetworkMillis();// 请求花费的时间
            }
        }

        @Override
        public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {

        }

        @Override
        public void onFinish(int i) {

        }
    };
    String code;
    Handler handlerTime = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                toastShort("关注成功");
                toptabstart.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 展示发送礼物列表的popu
     *
     * @param giftSendList
     */
    private void showSendListPopu(final TextView giftSendList) {

        if (popupWindowGiftSendList == null) {
            final View inflate = LayoutInflater.from(getContext())
                    .inflate(R.layout.popup_gift_sendlist, null);
            LinearLayout giftSendNumLl = inflate.findViewById(R.id.gift_num_ll);
            if (giftSendNumLl.getChildCount() > 0) {
                giftSendNumLl.removeAllViews();
            }
            for (int i = 0; i < mGiftSendNumBeans.size(); i++) {
                GiftSendNumBean giftSendNumBean = mGiftSendNumBeans.get(i);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_gift_sendnum, null);
                giftSendNumLl.addView(view);
                final TextView tvNum = view.findViewById(R.id.send_gift_num);
                TextView tvName = view.findViewById(R.id.send_gift_name);
                tvNum.setText(giftSendNumBean.getNum());
                tvName.setText(giftSendNumBean.getName());
                RxView.clicks($(view, R.id.gift_send_listitem))
                        .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                giftComboCount = Integer.parseInt(tvNum.getText().toString());
                                mGiftSendList.setText(String.valueOf(giftComboCount));
                            }
                        });
            }

            popupWindowGiftSendList = new PopupWindow(inflate, ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            popupWindowGiftSendList.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    giftSendList.setSelected(true);
                }
            });
        }
        if (!popupWindowGiftSendList.isShowing()) {
            showPopupWindowAboveButton(popupWindowGiftSendList, giftSendList);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
//        lazyLoad();
        joinRoom();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
        } else {
            isUIVisible = false;
        }
//        lazyLoad();
    }

    /**
     * 在fragment可见时加入直播间，不可见时推出直播间
     */
    private void lazyLoad() {
        // 这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,
        // 必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            isUIShow = true;
            joinRoom();
        } else if (isViewCreated && !isUIVisible && isUIShow) {
            isUIShow = false;
            exitRoom();
        }
    }

    /**
     * 加入房间
     */
    private synchronized void joinRoom() {
        initRoom();
        initializeVideoView();
    }

    /**
     * 退出房间
     */
    public synchronized void exitRoom() {
        destroyRoom();
    }

    @Override
    protected void destroyRoom() {
        if (mRoomLiveHelp != null && isIntRoom) {
            wsService.sendRequest(WsObjectPool.newCloseCallSecondaryRequest(getContext(), mAnchorId,
                    mLiveId, mMLocalUserId));
            mRoomLiveHelp.exitRoom();
        }

        LoginInfo loginInfo = DataManager.getInstance().getLoginInfo();
        requestRoomLoginOut(loginInfo.getUserId(), loginInfo.getAvatar(), loginInfo.getNickname(),
                loginInfo.getLevel(), Constants.WEBSOCKET_ROLE_AUDIENCE, SocketConstants.EVENT_LOGOUT);
        if (null != mHearBeatTimer) {
            handler.removeCallbacks(runnable);
            mHearBeatTimer.cancel();
            mHearBeatTimer = null;
        }

        if (mVideoView != null) {
            loadingView.setVisibility(View.GONE);
            mVideoView.stopPlayback();
        }
        //这里保险一点，让计时器关闭
        mGiftContinue = null;

        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }

        super.destroyRoom();
    }

    private void addView() {
        LiveView.LiveViewListener listener = new LiveView.LiveViewListener() {
            @Override
            public void onCloseClick(String userId) {
                getHostExitInfo();
            }
        };
        mRlRomoteLive.setListener(listener);
        LiveView.LiveViewListener listenerB = new LiveView.LiveViewListener() {
            @Override
            public void onCloseClick(String userId) {
                getHostExitInfo();
            }
        };
        mRlRomoteLiveB.setListener(listenerB);
        mLiveViewList.add(mRlRomoteLive);
        mLiveViewList.add(mRlRomoteLiveB);
    }

    @Override
    protected void removeLmUser(String disConnectId) {
        //观众端不实现
    }

    private void initializeVideoView() {
        Context context = getContext();
        mVideoView = new IjkVideoView(context);
        mVideoViewRoot.addView(mVideoView);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        lp.height = getSurfaceViewHeight();
        mVideoView.setLayoutParams(lp);
        playerManager = new PlayerManager(this, mVideoView);
        playerManager.setPlayerStateListener(this);
        playerManager.play(playbackUrl);
        playerManager.setScaleType(PlayerManager.SCALETYPE_FILLPARENT);
        playerManager.start();
    }

    public static Bundle createArgs(String summary) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ANCHOR_SUMMARY, summary);
        return bundle;
    }

    private void showLMRequestDialog() {
        ApplyLMDialog dialog = new ApplyLMDialog(getActivity());
        dialog.setContent("申请与主播连麦");
        dialog.setMessageDialogListener(new ApplyLMDialog.MessageDialogListener() {
            @Override
            public void onCancelClick(ApplyLMDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onCommitClick(ApplyLMDialog dialog) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                // 发送连麦请求
                applyMicRequestToServer(mLiveId, mAnchorId, mMLocalUserId, mMLocalUserNickName, loginInfo.getAvatar());
                // 直接进房间,暂时屏蔽此操作
                //mRoomLiveHelp.enterRoom(EnterConfApi.RoomMode.ROOM_MODE_LIVE.ordinal(),2,Integer.parseInt(mAnchorId),Long.parseLong(DataManager.getInstance().getLoginInfo().getUserId()));

                isCanApplayLm = false;
                mRoomImgbtnLm.setImageResource(R.drawable.shenqinglianmai_dianji);
                mTvMicText.setText("等待连麦");
            }
        });
        dialog.show();
    }

    @Override
    protected void dealApplyMicRequest(LmAgreeOrRefuseRes bean) {
        if ("2".equals(bean.getData().getType())) {
            mRoomLiveHelp.enterRoom(EnterConfApi.RoomMode.ROOM_MODE_LIVE.ordinal(), 2, Integer.parseInt(mLiveId), Long.parseLong(DataManager.getInstance().getLoginInfo().getUserId()));
            isCanApplayLm = false;
        } else if ("3".equals(bean.getData().getType())) {
            toastShort("主播拒绝与您的连麦请求");
            mRoomImgbtnLm.setImageResource(R.drawable.shenqinglianmai_dianji);
            mTvMicText.setText(getString(R.string.apply_connect_mic));
            isCanApplayLm = true;
        }
    }

    @Override
    protected void dealApplyMicResponse(ResponseMicBean applyMicBean) {
    }

    @Override
    protected void dealDisConnectLm(String userId) {
        Log.e("PlayFragment", "观众端走了");
        if (mRoomLiveHelp != null) {
            mRoomLiveHelp.exitRoom();
        }
        //断开连麦,退会
        quitConference();
    }

    @Override
    protected void getComplainOptions(String reportId) {
        presenter.getComplainOptions(reportId);
    }

    @Override
    protected void reportUser(String reportId, String content) {
        presenter.complain(mMLocalUserId, reportId, mLiveId, content);
    }

//    protected void UpdateLoginInfo() {
//        //游客登录之后刷新本地信息
//        loginInfo = DataManager.getInstance().getLoginInfo();
//
//        String balance = DataManager.getInstance().getmUserInfo().getBalance();
//        loginInfo.setTotalBalance(Long.parseLong(balance));
//        //double balance = loginInfo.getTotalBalance();
//        mChargeTv.setText(String.valueOf(balance));
//    }

    @Override
    protected void getUserInfo(String userId) {
        presenter.loadUserInfo(userId, true);
    }

    @Override
    protected void addBlackList(String addBlackUserId) {
    }

    @Override
    protected void onMsgGagListener(UserInfo userInfo) {
    }

    @Override
    protected void onRootClickAction() {
        super.onRootClickAction();
        llOperationBar.setVisibility(View.VISIBLE);
        View view = getActivity().getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (mGiftLay.getVisibility() == View.VISIBLE) {
            mGiftLay.setVisibility(View.GONE);
        }
        recyclerPublicChat.setVisibility(View.VISIBLE);
    }

    @Override
    protected boolean shouldSendHeartRequest() {
        return true;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.height = getSurfaceViewHeight() - llHeader.getHeight() - recyclerPublicChat.getHeight() - llOperationBar.getHeight();
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                moveInculde.setLayoutParams(layoutParams);
                moveInculde.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            touchMoveX = event.getX();
                            touchMoveY = event.getY();
                            onRootClickAction();
                        }
                        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                            if (touchMoveX > event.getX() + 80 && isHideMove) {
//                        显示
                                ObjectAnimator.ofFloat(moveLayout, "X", moveLayout.getWidth(), 0).setDuration(1000).start();
                                isHideMove = false;
                            } else if (touchMoveX < event.getX() - 80 && !isHideMove) {
//                        隐藏
                                ObjectAnimator.ofFloat(moveLayout, "X", 0, moveLayout.getWidth()).setDuration(1000).start();
                                isHideMove = true;
                            }
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (touchMoveX > event.getX() + 80 && isHideMove) {
//                        显示
                                ObjectAnimator.ofFloat(moveLayout, "X", moveLayout.getWidth(), 0).setDuration(1000).start();
                                isHideMove = false;
                            } else if (touchMoveX < event.getX() - 80 && !isHideMove) {
//                        隐藏
                                ObjectAnimator.ofFloat(moveLayout, "X", 0, moveLayout.getWidth()).setDuration(1000).start();
                                isHideMove = true;
                            }
                        }
                        return true;
                    }
                });
            } else if (msg.what == 3) {

            }
        }
    };

    @Override
    protected int getRoomType() {
        return RoomActivity.TYPE_VIEW_LIVE;
    }

    public static PlayFragment newInstance(@NonNull Bundle bundle, String roomidmsg, String avatar,
                                           String nickName, String level, String pullUrl,
                                           String streamId, String liveId, boolean isPlay) {
        PlayFragment fragment = new PlayFragment();
        bundle.putString(RoomActivity.EXTRA_ANCHOR_ID, roomidmsg);
        bundle.putBoolean(ARG_ANCHOR_ISBACK, isPlay);
        bundle.putString(RoomActivity.EXTRA_ANCHOR_AVATAR, avatar);
        bundle.putString(RoomActivity.EXTRA_ANCHOR_NICKNAME, nickName);
        bundle.putString(RoomActivity.EXTRA_ANCHOR_LEVEL, level);
        bundle.putString(RoomActivity.EXTRA_LIVE_PULL_URL, pullUrl);
        bundle.putString(RoomActivity.EXTRA_LIVE_ID, liveId);
        bundle.putString(RoomActivity.EXTRA_STREAM_ID, streamId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showGiftList(final List<Gift> giftList) {
        mGiftView.setGiftDatas(giftList);
        mGiftView.setGiftSelectChangeListener(new GiftClickListener() {
            @Override
            public void onEmotionSelected(boolean isSelect, int position) {
                mGiftSentBtn.setEnabled(isSelect);
                if ("0".equals(giftList.get(position).getIsFire())) {
                    mGiftSendList.setVisibility(View.GONE);
                } else {
                    mGiftSendList.setVisibility(View.VISIBLE);
                }
            }
        });

        RxView.clicks(mGiftSentBtn)
                .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                //为避免连发过程中取消礼物的选中等导致的数据不一致的异常，这里先做一次Map和filter操作
                //保证最后发送的时候一定会成功。
                .map(new Func1<Void, Gift>() {
                    @Override
                    public Gift call(Void aVoid) {
                        return mGiftView.getSelectedGift();
                    }
                })
                .filter(new Func1<Gift, Boolean>() {
                    @Override
                    public Boolean call(Gift gift) {
                        //没有选中任何礼物则返回false
                        if (gift == null) {
                            return Boolean.FALSE;
                        }
                        double balance = loginInfo.getTotalBalance();

                        //如果一个都买不起，则返回false并提示充值
                        if (balance < Double.parseDouble(gift.getPrice())) {
                            toastShort("请先充值");
                            return Boolean.FALSE;
                        }
                        return Boolean.TRUE;
                    }
                })
                .doOnNext(new Action1<Gift>() {
                    @Override
                    public void call(Gift gift) {
                        giftComboCount = Integer.parseInt(mGiftSendList.getText().toString());

                    }
                })
                .subscribe(new Action1<Gift>() {
                    @Override
                    public void call(final Gift selectedGift) {

                        mGiftSentBtn.setEnabled(false);
                        mGiftContinue.setVisibility(View.VISIBLE);

                        if ("0".equals(selectedGift.getIsFire())) {

                            mGiftContinue.setVisibility(View.GONE);
                            //计算最大合法Combo总数
                            double balance = loginInfo.getTotalBalance();
                            double maxCombo = balance / Double.parseDouble(selectedGift.getPrice());
                            int finalCombo = giftComboCount > maxCombo ? (int) maxCombo : giftComboCount;

                            balance -= (finalCombo * Double.parseDouble(selectedGift.getPrice()));

                            //直接扣除余额
                            loginInfo.setTotalBalance(balance);
                            //更新到永存
                            DataManager.getInstance().saveLoginInfo(loginInfo);
                            //更新显示
                            mChargeTv.setText(String.valueOf(DataManager.getInstance().getLoginInfo().getTotalBalance()));
                            wsService.sendRequest(WsObjectPool.newSendGiftRequest(getContext(), mLiveId, mAnchorId, loginInfo.getUserId(), selectedGift.getPrice(), selectedGift.getId(),
                                    finalCombo, loginInfo.getNickname(), loginInfo.getAvatar(), loginInfo.getLevel(), selectedGift.getName(), selectedGift.getImgSrc()));

                            mGiftSentBtn.setEnabled(true);
                            llOperationBar.setVisibility(View.VISIBLE);
                            mGiftLay.setVisibility(View.GONE);
                            recyclerPublicChat.setVisibility(View.VISIBLE);
                        } else {
                            if (1 != giftComboCount) mGiftContinue.setVisibility(View.GONE);
                            mGiftSentBtn.setVisibility(View.GONE);
                            mGiftSendList.setVisibility(View.GONE);
                            //礼物连发
                            new MrlCountDownTimer(3000, 100) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    if (mGiftContinue == null) {
                                        this.cancel();
                                        return;
                                    }
                                    mGiftContinue.setText(getString(R.string
                                                    .layout_gift_continue_send_default,
                                            millisUntilFinished / 100));
                                }

                                @Override
                                public void onFinish() {
                                    mGiftContinue.setVisibility(View.INVISIBLE);
                                    //计算最大合法Combo总数
                                    double balance = loginInfo.getTotalBalance();
                                    double maxCombo = balance / Double.parseDouble(selectedGift.getPrice());
                                    int finalCombo = giftComboCount > maxCombo ? (int) maxCombo :
                                            giftComboCount;

                                    balance -= (finalCombo * Double.parseDouble(selectedGift.getPrice()));
                                    //直接扣除余额
                                    loginInfo.setTotalBalance(balance);
                                    //更新到永存
                                    DataManager.getInstance().saveLoginInfo(loginInfo);
                                    //更新显示
                                    mChargeTv.setText(String.valueOf(DataManager.getInstance().getLoginInfo().getTotalBalance()));
                                    if (finalCombo == 0) {
                                        finalCombo = 1;
                                    }
                                    wsService.sendRequest(WsObjectPool.newSendGiftRequest(getContext(), mLiveId, mAnchorId, loginInfo.getUserId(), selectedGift.getPrice(), selectedGift.getId(),
                                            finalCombo, loginInfo.getNickname(), loginInfo.getAvatar(), loginInfo.getLevel(), selectedGift.getName(), selectedGift.getImgSrc()));
                                    mGiftSentBtn.setEnabled(true);
                                    mGiftSentBtn.setVisibility(View.VISIBLE);
                                    mGiftSendList.setVisibility(View.VISIBLE);
                                    //连发完成置位1
                                    giftComboCount = 1;
                                    mGiftSendList.setText(String.valueOf(giftComboCount));
                                }
                            }.start();
                        }

                    }
                });

        RxView.clicks(mGiftContinue)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        giftComboCount++;
                    }
                });

        RxView.clicks(mDownGift)
                .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        llOperationBar.setVisibility(View.VISIBLE);
                        mGiftLay.setVisibility(View.GONE);
                        showAnimOut(mGiftLay);
                        recyclerPublicChat.setVisibility(View.VISIBLE);
                        mGiftSendList.setSelected(true);
                    }
                });

        RxView.clicks(mGiftSendList)
                .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .map(new Func1<Void, Gift>() {
                    @Override
                    public Gift call(Void aVoid) {
                        return mGiftView.getSelectedGift();
                    }
                })
                .filter(new Func1<Gift, Boolean>() {
                    @Override
                    public Boolean call(Gift gift) {
                        //没有选中任何礼物或者不能连发的则返回false
                        if (gift == null || "0".equals(gift.getIsFire())) {
                            return Boolean.FALSE;
                        }
                        return Boolean.TRUE;
                    }
                })
                .doOnNext(new Action1<Gift>() {
                    @Override
                    public void call(Gift gift) {
                        giftComboCount = Integer.parseInt(mGiftSendList.getText().toString());
                    }
                })

                .subscribe(new Action1<Gift>() {
                    @Override
                    public void call(Gift gift) {
                        mGiftSendList.setSelected(false);
                        showSendListPopu(mGiftSendList);
                    }
                });

    }

    @Override
    public void showUserInfo(UserInfo userInfo, boolean isPopup) {
        if (isPopup) {
            showUserInfoPopup(userInfo, false);
        } else {
            try {
                // 请求成功
                if (userInfo != null) {
                    mHostNickname.setText(TextUtils.isEmpty(userInfo.getUserName()) ? "昵称" : "" + userInfo.getNickName());
                    if (userInfo.getIsAttention() == 0) {
                        toptabstart.setText("关注");
                        toptabstart.setVisibility(View.GONE);
                    }
                    mHostFansAccount.setText("ID " + mAnchorId);
                    handler.sendEmptyMessage(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mChargeTv.setText(String.valueOf(loginInfo.getTotalBalance()));

        if (mVideoView != null) {
            mVideoView.onResume();
        }
    }

    @Override
    public void showFriendList(List<GetFriendBean> friendBeanList) {
        getFriendLayout.setFriendList(friendBeanList);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        if (mVideoView != null && mVideoView.getVisibility() == View.VISIBLE && !playerManager.isPlaying()) {
            playerManager.stop();
        }
        super.onDestroy();
    }

    @Override
    public void onSucceed(int what, Response<Bitmap> response) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
    }

    @Override
    public void enterRoomSuccess() {
        if (mVideoView != null) {
            mVideoView.pause();
            mVideoViewRoot.removeAllViews();
            mVideoView = null;
        }
        isIntRoom = true;
    }

    private void startPublish() {
        mHearBeatTimer = new Timer(true);
        mHeartBeatTask = new HeartBeatTask();
        mHearBeatTimer.schedule(mHeartBeatTask, 1000, 5 * 1000);
    }

    /**
     * 直播心跳
     */
    private class HeartBeatTask extends TimerTask {
        @Override
        public void run() {
            handler.post(runnable);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sendHearedBeat(mLiveId, loginInfo.getUserId(), Constants.WEBSOCKET_ROLE_AUDIENCE, mStreamId);
        }
    };

    @Override
    public void enterRoomFailue(int error) {
        Log.e(LOG_TAG, "enterRoomFailue");
        if (mRoomLiveHelp.isKickout) {
            mRoomLiveHelp.isKickout = false;
            return;
        }
        if (getActivity() != null) {
            mRlLocalLive.removeAllViews();
            removeAll();
            getActivity().finish();
        }
    }

    @Override
    public void onDisconnected(int errorCode) {
        Log.e(LOG_TAG, "onDisconnected");

        if (getActivity() == null) {
            return;
        }
        if (errorCode == com.wushuangtech.library.Constants.ERROR_KICK_BY_MASTER_EXIT) {
            ((RoomActivity) getActivity()).showRoomEndInfoDialog();
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    quitConference();
                }
            });
        }
    }

    /**
     * 自己退出会议 或者 异常退出 以及 被主播踢出
     */
    private void quitConference() {
        isIntRoom = false;
        mRlLocalLive.removeAllViews();
        removeAll();
        isCanApplayLm = true;
        initializeVideoView();
    }

    /**
     * 断开连麦之后的操作,断开连麦时已经做了退出房间操作(退出会议操作),此处只需要ui更新即可
     *
     * @param userId
     */
    @Override
    public void onMemberExit(long userId) {
        if (isFirstEnter) {
            return;
        }
        releaseLiveView(userId);
    }

    @Override
    public void onMemberEnter(final long userId, final EnterUserInfo userInfo) {
        Log.e(LOG_TAG, "onMemberEnter" + "userId" + userId);
        if (isFirstEnter) {
            return;
        }
        showRemoteView(userInfo);
    }

    @Override
    public void onHostEnter(long userId, EnterUserInfo userInfo) {
        Log.e(LOG_TAG, "onHostEnter" + "userId" + userId);
        mRoomLiveHelp.openRemoteVideo(mRlLocalLive, userInfo, false);
    }

    @Override
    public void onUpdateLiveView(List<EnterUserInfo> userInfos) {
        if (!isFirstEnter) {
            return;
        }
        for (EnterUserInfo userInfo : userInfos) {
            showRemoteView(userInfo);
        }
        isFirstEnter = false;
    }

    public void getHostExitInfo() {
        if (mVideoView != null && mVideoView.getVisibility() == View.VISIBLE) {
            mVideoView.pause();
        }
        wsService.sendRequest(WsObjectPool.newCloseCallSecondaryRequest(getContext(), mAnchorId,
                mLiveId, mMLocalUserId));
        mRlLocalLive.removeAllViews();
        removeAll();
    }

    /**
     * 当自己退出连麦时或断开连接时或退出时要移出全部
     */
    private void removeAll() {
        isFirstEnter = true;
        for (int i = 0; i < mLiveViewList.size(); i++) {
            if (!mLiveViewList.get(i).isFree()) {
                LiveView liveView = mLiveViewList.get(i);
                liveView.setFree(true);
                liveView.setFlagUserId(0);
                liveView.setVisibility(View.INVISIBLE);
                liveView.removeAllViews();
            }
        }
    }

    /**
     * 获取空闲的View用于播放或者发布.
     */
    protected LiveView getFreeViewLive() {
        LiveView lvFreeView = null;
        for (int i = 0, size = mLiveViewList.size(); i < size; i++) {
            LiveView viewLive = mLiveViewList.get(i);
            if (viewLive.isFree()) {
                lvFreeView = viewLive;
                break;
            }
        }
        lvFreeView.setVisibility(View.VISIBLE);
        return lvFreeView;
    }

    /**
     * 显示视频
     *
     * @param userInfo
     */
    private void showRemoteView(EnterUserInfo userInfo) {
//        if (userInfo.getRole() == 1) {
//            mRoomLiveHelp.openRemoteVideo(mRlLocalLive, userInfo, false);
//            return;
//        }
        LiveView freeViewLive = getFreeViewLive();
        freeViewLive.setFlagUserId(userInfo.getId());
        freeViewLive.setFree(false);
        if (userInfo.getId() == Long.parseLong(loginInfo.getUserId())) {
            freeViewLive.showClose(true);
            mRoomLiveHelp.openLocalVideo(freeViewLive, true);
        } else {
            freeViewLive.showClose(false);
            mRoomLiveHelp.openRemoteVideo(freeViewLive, userInfo, true);
        }
    }

    /**
     * 释放直播控件
     *
     * @param userId
     */
    private void releaseLiveView(long userId) {
        if (userId == 0) {
            return;
        }
        for (int i = 0, size = mLiveViewList.size(); i < size; i++) {
            LiveView currentViewLive = mLiveViewList.get(i);
            if (userId == currentViewLive.getFlagUserId()) {
                int j = i;
                for (; j < size - 1; j++) {
                    currentViewLive = mLiveViewList.get(j);
                    LiveView nextViewLive = mLiveViewList.get(j + 1);
                    if (nextViewLive.isFree()) {
                        break;
                    }
                    currentViewLive.removeAllViews();
                    SurfaceView mChildSurfaceView = (SurfaceView) nextViewLive.getChildAt(0);
                    nextViewLive.removeView(mChildSurfaceView);
                    currentViewLive.addView(mChildSurfaceView);
                    currentViewLive.setFlagUserId(nextViewLive.getFlagUserId());
                    currentViewLive.setFree(false);
                    nextViewLive.setFree(true);
                    nextViewLive.setFlagUserId(0);
                }
                // 标记最后一个View可用
                mLiveViewList.get(j).setFree(true);
                mLiveViewList.get(j).removeAllViews();
                break;
            }
        }
    }

    @Override
    public void onComplete() {
        Log.e("systemTime", "onComplete" + System.currentTimeMillis());
    }

    @Override
    public void onError() {

//        if (mVideoView != null && mVideoView.getVisibility() == View.VISIBLE && !playerManager.isPlaying()) {
//            playerManager.stop();
//        }
        // ((RoomActivity) getActivity()).performExitAction();
    }

    @Override
    public void onLoading() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.loading).asGif().into(loadingView);
        }
    }

    @Override
    public void onPlay() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNetChanged(Context context, int netMobile) {
//        if (mVideoView != null) {
//            mVideoView.pause();
//            mVideoViewRoot.removeAllViews();
//            mVideoView = null;
//        }
        if (netMobile != -1) {
            if (Networks.isNetworkConnected(context) && Networks.isNetAvailable(context)) {
                if (netMobile == 1) {
                    if (!isIntRoom) initializeVideoView();
                } else if (netMobile == 0) {
                    MessageDialog.MessageDialogListener listener = new MessageDialog.MessageDialogListener() {
                        @Override
                        public void onCancelClick(MessageDialog dialog) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            ((RoomActivity) getActivity()).performExitAction();

                        }

                        @Override
                        public void onCommitClick(MessageDialog dialog) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (!isIntRoom) {
                                initializeVideoView();
                            }
                        }
                    };
                    showDoneDialog("您已切换4G是否使用流量观看", listener);
                }

            }
        } else {

            toastShort("网络异常");
        }
    }

    @Override
    public void startHostResult() {

//        if (((int) toptabstart.getTag()) == 0) {
//            toptabstart.setText("已关注");
//            toptabstart.setTag(1);
//
//        } else {
//            toptabstart.setText("关注");
//            toptabstart.setTag(0);
//        }

    }

    @Override
    public void getComOptionSuccess(String reportId, List<ComplainOptionBean.ListBean> list) {
        showReportPopup(reportId, list);
    }

    @Override
    public void getHitCode(String msg) {
        toastShort(msg);
    }
}
