package com.ttt.liveroom.room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TimingLogger;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.magiepooh.recycleritemdecoration.ItemDecorations;
import com.jakewharton.rxbinding.view.RxView;
import com.ttt.liveroom.R;
import com.ttt.liveroom.base.BaseActivity;
import com.ttt.liveroom.base.BaseFragment;
import com.ttt.liveroom.base.DataManager;
import com.ttt.liveroom.bean.Danmu;
import com.ttt.liveroom.bean.LoginInfo;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.bean.gift.SendGiftAction;
import com.ttt.liveroom.bean.room.ComplainOptionBean;
import com.ttt.liveroom.bean.websocket.BlackListRes;
import com.ttt.liveroom.bean.websocket.CloseCallSecondaryRes;
import com.ttt.liveroom.bean.websocket.DisConnectLmMsg;
import com.ttt.liveroom.bean.websocket.GagResResponse;
import com.ttt.liveroom.bean.websocket.LmAgreeOrRefuseRes;
import com.ttt.liveroom.bean.websocket.ResponseMicBean;
import com.ttt.liveroom.bean.websocket.RoomPublicMsg;
import com.ttt.liveroom.bean.websocket.SendGiftMsg;
import com.ttt.liveroom.bean.websocket.SystemWelcome;
import com.ttt.liveroom.bean.websocket.UserPublicMsg;
import com.ttt.liveroom.bean.websocket.WsGiftMsg;
import com.ttt.liveroom.bean.websocket.WsLoginMsg;
import com.ttt.liveroom.bean.websocket.WsLoginOutMsg;
import com.ttt.liveroom.bean.websocket.WsPongMsg;
import com.ttt.liveroom.net.Constants;
import com.ttt.liveroom.net.nohttp.HttpListener;
import com.ttt.liveroom.room.gift.IAnimController;
import com.ttt.liveroom.room.gift.IGiftAnimPlayer;
import com.ttt.liveroom.room.gift.LocalAnimQueue;
import com.ttt.liveroom.room.play.RoomNetBroadcastReceiver.NetEvevt;
import com.ttt.liveroom.room.pubmsg.PublicChatAdapter;
import com.ttt.liveroom.room.utils.RoomLiveHelp;
import com.ttt.liveroom.room.utils.RoomLiveInterface;
import com.ttt.liveroom.util.L;
import com.ttt.liveroom.util.danmu.DanmuControl;
import com.ttt.liveroom.util.roomanim.CarView;
import com.ttt.liveroom.util.roomanim.FireworksView;
import com.ttt.liveroom.util.roomanim.GenView;
import com.ttt.liveroom.util.roomanim.GitfSpecialsStop;
import com.ttt.liveroom.util.roomanim.PlaneImagerView;
import com.ttt.liveroom.util.roomanim.ShipView;
import com.ttt.liveroom.websocket.SocketConstants;
import com.ttt.liveroom.websocket.WebSocketService;
import com.ttt.liveroom.websocket.WsListener;
import com.ttt.liveroom.websocket.WsObjectPool;
import com.ttt.liveroom.widget.AdminPopup;
import com.ttt.liveroom.widget.BeautyPopup;
import com.ttt.liveroom.widget.report.ReportAdapter;
import com.ttt.liveroom.widget.report.ReportPopup;
import com.ttt.liveroom.widget.UserInfoPopup;
import com.ttt.liveroom.widget.heardanim.HeartAnim;
import com.yolanda.nohttp.rest.Response;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.ui.widget.DanmakuView;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.ttt.liveroom.room.RoomActivity.TYPE_VIEW_LIVE;

/**
 * @author liujing
 * Created by 刘景 on 2017/06/07.
 */
public abstract class RoomFragment extends BaseFragment implements RoomActivity.HasInputLayout,
        HttpListener<Bitmap>, GitfSpecialsStop, RoomLiveInterface, NetEvevt {

    protected final String LOG_TAG = getClass().getSimpleName();

    private UIHandler mUIHandler = new UIHandler(this);

    private HandlerThread mThread;

    protected final String TIMING_LOG_TAG = "timing";

    protected boolean isMeSend = false;
    /**
     * 用于监测启动和退出时的执行时间，为避免多次创建对象，一个Fragment实例仅使用一个对象。
     * 父类和子类都需要在启动和退出方法中调用reset和dump，以确保每次打点的消息一定能被dump。
     */
    protected final TimingLogger timingLogger = new TimingLogger(TIMING_LOG_TAG, "Not Initialized");

    protected WebSocketService wsService;
    protected String mLiveId;
    protected String mStreamId;
    private LoginInfo mLoginInfo;

    protected TextView tvGold;
    protected TextView mPrvChat;

    private IAnimController localGiftController;

    public LinearLayout llOperationBar;
    public LinearLayout llChatBar;
    public LinearLayout llHeader, mRankLay;
    public NestedScrollView mRoomScroll;
    public EditText edtChatContent;
    private PublicChatAdapter publicChatAdapter;

    protected RecyclerView recyclerPublicChat;
    private HeartAnim mHeartAnim;
    protected RelativeLayout mRoot;
    /**
     * 实时在线观看人数。
     */
    protected TextView tvOnlineCount;
    protected TextView mHostNickname;
    protected TextView mHostFansAccount;

    private RecyclerView recyclerAudienceList;
    private AudienceAdapter audienceAdapter;

    protected int[] heartColorArray;
    private int defaultColorIndex;

    private boolean isKicked;

    /**
     * 创建一个用户发送信息对象用来直接显示
     */
    private UserPublicMsg userPublicMsg;
    /**
     * id
     */
    private String publicMsgId;
    /**
     * name
     */
    private String publicMsgName;
    /**
     * 主播的id
     */
    protected String mAnchorId;
    /**
     * 主播头像
     */
    protected String mHostAvatar;
    /**
     * 主播昵称
     */
    protected String mPublishNickName;
    /**
     * 主播等级
     */
    protected String mHostLevel;
    /**
     * 弹幕开关
     */
    private boolean danmuopenis = false;
    /**
     * 根据弹幕开关显示对应的弹幕开关样式
     */
    private ImageView danmu_layout_close;
    private ImageView danmu_layot_open;
    /**
     * 弹幕view
     */
    public DanmakuView mDanmakuView;
    /**
     * 弹幕context
     */
    public DanmakuContext mContext;
    /**
     * 弹幕对象
     */
    public Danmu danmu;
    /**
     * 头像栏的关注
     */
    protected TextView toptabstart;
    /**
     * 通过socket监听主播退出
     */
    public boolean isloginout = false;
    /**
     * 头像右下角的小角角
     */
    private int[] starticon;
    /**
     * 头像右下角的星星
     */
    private ImageView iconstart;
    /**
     * 小伙伴
     */
    public RelativeLayout friendLayout;
    /**
     * 关闭小伙伴
     */
    public Button mFriendClose;
    /**
     * 礼物动画
     */
    private RelativeLayout animLayout;
    /**
     * 烟花
     */
    private FireworksView fireworks;
    /**
     * 灰机
     */
    private PlaneImagerView plane;
    /**
     * 汽车
     */
    private CarView car;
    /**
     * 船
     */
    private ShipView ship;
    /**
     * 移动
     */
    public RelativeLayout moveLayout;
    /**
     * 记录大礼物
     */
    private List<Integer> gitfSpecials;
    /**
     * 大礼物动画结束
     */
    private boolean isgiftend = true;
    /**
     * 记录死亡
     */
    private boolean isdeas = false;
    /**
     * 砖石
     */
    private GenView gen;

    protected RoomLiveHelp mRoomLiveHelp;
    protected String mMLocalUserId;
    protected String mMLocalUserNickName;
    protected boolean mMsgGag = true;

    private View mRedView;

    @RoomActivity.RoomType
    public int mRoomType;
    public static NetEvevt evevt;

    @Override
    protected void initViews(View view) {
        mThread = new HandlerThread("Work on RoomActivity");
        mThread.start();

        userPublicMsg = new UserPublicMsg();

        gitfSpecials = new ArrayList<>();
//        小星星
        starticon = new int[]{R.drawable.global_star_1,
                R.drawable.global_star_2,
                R.drawable.global_star_3,
                R.drawable.global_star_4,
                R.drawable.global_star_5};
        iconstart = (ImageView) view.findViewById(R.id.img_user_star_type);
        if (iconstart != null) {
            iconstart.setImageResource(starticon[(int) (Math.random() * 4)]);
        }
        publicMsgId = DataManager.getInstance().getLoginInfo().getUserId();
        publicMsgName = DataManager.getInstance().getLoginInfo().getNickname();
//        publicMsgName = DataManager.getInstance().getmUserInfo().getNickName();
        if (timingLogger != null) {
            timingLogger.reset(TIMING_LOG_TAG, "RoomFragment#initViews");
            timingLogger.addSplit("parseArguments");
            timingLogger.addSplit("init heart color.");
        }

        heartColorArray = getResources().getIntArray(R.array.room_heart_colors);
        llChatBar = (LinearLayout) view.findViewById(R.id.room_ll_chat_bar);
        mPrvChat = (TextView) view.findViewById(R.id.dialog_user_info_prv_chat);
        llOperationBar = $(view, R.id.room_ll_operation_bar);
        mHeartAnim = $(view, R.id.room_heart_view);
        llHeader = $(view, R.id.room_header);
        mRankLay = $(view, R.id.room_coin_rank_lay);
        mRoot = $(view, R.id.room_fragment_root);
        mRoomScroll = $(view, R.id.room_scroll);
        tvGold = $(view, R.id.txt_gold_count);
        toptabstart = $(view, R.id.room_top_bar_start_tv);
        toptabstart.setVisibility(View.GONE);

        mRoomLiveHelp = new RoomLiveHelp(this, ((RoomActivity) getActivity()), mAnchorId);

        moveLayout = $(view, R.id.room_move_layout);

        if (mHeartAnim != null && mRoot != null && mRoomScroll != null) {
            //点亮自己
//            RxView.clicks(mRoomScroll)
////                    .throttleFirst(Constants.LIVE_ROOM_HEART_THROTTLE, TimeUnit.MILLISECONDS)
////                    .subscribe(new Action1<Void>() {
////                        @Override
////                        public void call(Void aVoid) {
////                            onRootClickAction();
////                        }
////                    });
        }

        if (llChatBar != null) {
            edtChatContent = $(view, R.id.room_edt_chat);
            edtChatContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (danmuopenis && edtChatContent.getText().toString().length() > 20) {
                        toastShort("弹幕的消息字数不能超过20个字！");
                        return;
                    }
                }
            });
            TextView btnSendChat = $(view, R.id.room_btn_send);

            RxView.clicks(btnSendChat).throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit
                    .MILLISECONDS)
                    .filter(new Func1<Void, Boolean>() {
                        @Override
                        public Boolean call(Void aVoid) {
                            userPublicMsg = new UserPublicMsg();
                            UserPublicMsg.UserPublicMsgData msgData = new UserPublicMsg.UserPublicMsgData();
                            msgData.setUserId(publicMsgId);
                            msgData.setNickName(publicMsgName);
                            userPublicMsg.setMessage(edtChatContent.getText().toString());
                            userPublicMsg.setData(msgData);

                            return !TextUtils.isEmpty(edtChatContent.getText().toString());
                        }
                    })
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            String msg = edtChatContent.getText().toString();
                            if (TextUtils.isEmpty(msg)) {
                                toastShort("发送消息不能为空");
                                return;
                            }

                            if (danmuopenis) {
                                if (msg.length() > 20) {
                                    toastShort("您的弹幕的字数超过了20个字了！");
                                    return;
                                }
                                wsService.sendRequest(WsObjectPool.newPublicMsgRequest(getContext(),
                                        mLiveId, msg, Constants.WEBSOCKET_ROLE_HOST));
                            } else {
                                wsService.sendRequest(WsObjectPool.newPublicMsgRequest(getContext(),
                                        mLiveId, msg, Constants.WEBSOCKET_ROLE_AUDIENCE));
                            }

                            publicChatAdapter.appendData(userPublicMsg);
                            //Auto scroll to last
                            recyclerPublicChat.scrollToPosition(publicChatAdapter.getItemCount() - 1);
                            isMeSend = true;

                            edtChatContent.getText().clear();
                        }
                    });
        }
        danmu_layout_close = $(view, R.id.room_danmu_close);
        danmu_layot_open = $(view, R.id.room_danmu_open);
        RxView.clicks($(view, R.id.room_danmu))
                .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (danmuopenis) {
                            danmu_layout_close.setVisibility(View.VISIBLE);
                            danmu_layot_open.setVisibility(View.INVISIBLE);
                            danmuopenis = false;
                        } else {
                            danmu_layot_open.setVisibility(View.VISIBLE);
                            danmu_layout_close.setVisibility(View.INVISIBLE);
                            danmuopenis = true;
                        }
                    }
                });

        //贡献榜
//        subscribeClick(tvGold, new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                Intent intent = new Intent();
//                intent.setAction("ContributionActivity");
//                Bundle bundle = new Bundle();
//                bundle.putString("id", mAnchorId);
//                startActivity(intent);
//            }
//        });
        TextView tvbtn = $(view, R.id.room_tvbtn_public_chat);

        if (tvbtn != null) {
            RxView.clicks(tvbtn).throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            if (mMsgGag) {
                                showInputLayout(true);
                            } else {
                                toastShort("你已经被禁言");
                            }
                        }
                    });
        }

        LinearLayout giftLayout = $(view, R.id.room_ll_gift_bar);
        if (giftLayout != null) {
            List<IGiftAnimPlayer> playerViews = new ArrayList<>();
            int childCount = giftLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                IGiftAnimPlayer player = (IGiftAnimPlayer) giftLayout.getChildAt(i);
                playerViews.add(player);
            }
            localGiftController = new LocalAnimQueue(playerViews);
        }

        recyclerPublicChat = $(view, R.id.room_recycler_chat);
        if (recyclerPublicChat != null) {
            recyclerPublicChat.setLayoutManager(new LinearLayoutManager(getActivity()));
            publicChatAdapter = new PublicChatAdapter(getContext(), new ArrayList<RoomPublicMsg>());
            recyclerPublicChat.setAdapter(publicChatAdapter);
        }

        animLayout = $(view, R.id.room_live_show_anim_layout);
        // 小伙伴
        friendLayout = $(view, R.id.layout_friend);
        friendLayout.setVisibility(View.GONE);
        ImageButton imgbtnfriend = $(view, R.id.room_imgbtn_friend);
        if (imgbtnfriend != null) {
            RxView.clicks(imgbtnfriend)
                    .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            llOperationBar.setVisibility(View.GONE);
                            friendLayout.setVisibility(View.VISIBLE);
                            showAnimIn(friendLayout);
                            recyclerPublicChat.setVisibility(View.GONE);
                        }
                    });
        }

        mFriendClose = $(view, R.id.layout_friend_btn_close);
        mFriendClose.setEnabled(true);

        mFriendClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendLayout.setVisibility(View.GONE);
                llOperationBar.setVisibility(View.VISIBLE);
                recyclerPublicChat.setVisibility(View.VISIBLE);
            }
        });

        initAudienceBar(view);
        subscribeCloseBtn(view);

        if (timingLogger != null) {
            timingLogger.addSplit("init view & set listener");
            timingLogger.addSplit("initWebSocket");
            timingLogger.dumpToLog();
        }

        initDanmuku(view);
//        设置按钮
        if (mHeartAnim != null && mRoot != null && mRoomScroll != null && mDanmakuView != null) {
            RxView.clicks((View) mDanmakuView)
                    .throttleFirst(Constants.LIVE_ROOM_HEART_THROTTLE, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            onRootClickAction();
                        }
                    });
        }
        evevt = this;
        initData();
        if (mRoomType != TYPE_VIEW_LIVE) {
            initRoom();
        }

    }

    private void initData() {
        mLoginInfo = DataManager.getInstance().getLoginInfo();
        mMLocalUserId = mLoginInfo.getUserId();
        mMLocalUserNickName = mLoginInfo.getNickname();
    }

    public void setAnchorId(String mAnchorId) {
        this.mAnchorId = mAnchorId;
    }

    /**
     * 直播心跳
     *
     * @param roomId
     * @param userId
     * @param isMaster
     */
    public void sendHearedBeat(String roomId, String userId, String isMaster, String streamId) {
        if (wsService != null) {
            wsService.sendRequest(WsObjectPool.newPongRequest(getContext(), roomId, userId,
                    isMaster, streamId));
        }
    }

    /**
     * 退出房间wedsocket
     *
     * @param id
     * @param Avatar
     * @param NickName
     * @param Level
     * @param websocketRoleHost
     * @param eventLogout
     */
    public void requestRoomLoginOut(String id, String Avatar, String NickName, String Level,
                                    String websocketRoleHost, String eventLogout) {
        if (wsService != null) {
            wsService.sendRequest(WsObjectPool.newLogoutRequest(getContext(),
                    mLiveId, id, Avatar, NickName, Level, websocketRoleHost, eventLogout));
        }
    }

    /**
     * 申请连麦
     *
     * @param hostId
     */
    public void applyMicRequestToServer(String liveId, String hostId, String userId, String nickName, String avatar) {
        wsService.sendRequest(WsObjectPool.newApplyMicRequest(getContext(),
                liveId, hostId, userId, nickName, avatar));
    }

    /**
     * 响应连麦(新)
     */
    public void newLmAgreeOrRefuseRequest(String adminUserId,
                                          String roomId, String userId,
                                          String type) {
        wsService.sendRequest(WsObjectPool.newLmAgreeOrRefuseRequest(getContext(), adminUserId,
                roomId, userId, type));
    }

    /**
     * 断开连麦
     */
    public void disConnectLm(final String roomId, final String adminUserId, final String userId) {
        MessageDialog.MessageDialogListener listener = new MessageDialog.MessageDialogListener() {
            @Override
            public void onCancelClick(MessageDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onCommitClick(MessageDialog dialog) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    removeLmUser(userId);
                    wsService.sendRequest(WsObjectPool.disConnectLmRequest(getContext(), roomId, adminUserId, userId));
                }
            }
        };
        showDoneDialog("确认结束连线", listener);
    }

    private MessageDialog mMessageDialog;

    /**
     * 显示通用的 确定
     *
     * @param content
     * @param listener
     */
    public void showDoneDialog(String content, MessageDialog.MessageDialogListener listener) {
        if (mMessageDialog != null && mMessageDialog.isShowing()) {
            return;
        }
        if (mMessageDialog == null) {
            mMessageDialog = new MessageDialog(getContext());
        }

        mMessageDialog.setContent(content);
        mMessageDialog.setMessageDialogListener(listener);
        mMessageDialog.show();
    }

    /**
     * 主播断开连麦用户
     * @param disConnectId
     */
    protected abstract void removeLmUser(String disConnectId);

    /**
     * 处理连麦请求
     */
    protected abstract void dealApplyMicRequest(LmAgreeOrRefuseRes bean);

    /**
     * 处理连麦应答
     */
    protected abstract void dealApplyMicResponse(ResponseMicBean response);

    /**
     * 获取一下用户信息
     *
     * @param userId
     */
    protected abstract void getUserInfo(String userId);

    /**
     * 拉黑
     *
     * @param addBlackUserId
     */
    protected abstract void addBlackList(String addBlackUserId);

    /**
     * 禁言
     *
     * @param userInfo
     */
    protected abstract void onMsgGagListener(UserInfo userInfo);

    /**
     * 断开连麦响应
     */
    protected abstract void dealDisConnectLm(String userId);

    /**
     * 获取举报选项
     */
    protected abstract void getComplainOptions(String reportId);

    /**
     * 举报
     *
     * @param reportId
     * @param content
     */
    protected abstract void reportUser(String reportId, String content);

    /**
     * 网络监听变化
     *
     * @param netMobile
     */
    protected abstract void onNetChanged(Context context, int netMobile);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 检查操作权限
     *
     * @return
     */
    protected boolean canOperate() {
        if (DataManager.getInstance().getLoginInfo() == null) {
            showLoginDialog();
            return false;
        }
        return true;
    }

    /**
     * 弹出登录对话框
     */
    private void showLoginDialog() {
        MessageDialog dialog = new MessageDialog(getActivity());
        dialog.setContent("您还未登录，是否登录！");
        dialog.setCancelable(false);
        dialog.setMessageDialogListener(new MessageDialog.MessageDialogListener() {
            @Override
            public void onCancelClick(MessageDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onCommitClick(MessageDialog dialog) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
//                startActivityForResult(LoginActivity.createIntent(getContext()), UPDATE_LOGIN_INFO);
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    protected void initRoom() {
        if (mRoomLiveHelp != null) {
            mRoomLiveHelp.initBroadcast();
        }

        initWebSocket();
    }

    protected void  destroyRoom() {
        Log.e(LOG_TAG, "退出了");

        if (timingLogger != null) {
            timingLogger.reset(TIMING_LOG_TAG, "RoomFragment#onDestroyView");
            timingLogger.addSplit("super.onDestroyView");
        }

        //清除动画
        if (localGiftController != null) {
            localGiftController.removeAll();
        }

        //Release ws service binding
        if (wsService == null) {
            L.e(LOG_TAG, "Ws service reference has been null, logout action cannot perform!");
            return;
        }
        if (isKicked) {
            wsService.sendRequest(
                    WsObjectPool.newLogoutRequest(getContext(), mLiveId, mLoginInfo.getUserId(),
                            mLoginInfo.getAvatar(),
                            mLoginInfo.getNickname(), mLoginInfo.getLevel(),
                            Constants.WEBSOCKET_ROLE_AUDIENCE, SocketConstants.EVENT_LOGOUT));
        }
        wsService.removeAllListeners();
        if (timingLogger != null) {
            timingLogger.addSplit("reset webSocket");
            timingLogger.dumpToLog();
        }

        isdeas = true;

        //退出直播
        if (mRoomLiveHelp != null) {
            mRoomLiveHelp.exitHelp();
            mRoomLiveHelp = null;
        }
        if (mThread != null && mThread.getLooper() != null) {
            mThread.getLooper().quit();
        }
        if (mUIHandler != null) {
            mUIHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        if (mRoomType != TYPE_VIEW_LIVE) {
            destroyRoom();
        }

        super.onDestroyView();
    }

    @Override
    public void onResume() {
        isdeas = false;
        super.onResume();
    }

    @Override
    public void onStop() {
        isdeas = true;
        super.onStop();
    }

    @Override
    public void onPause() {
        isdeas = true;
        super.onPause();
    }

    /*************************初始化数据********************************************/
    /**
     * 初始化webSocket,绑定websocket
     */
    protected void initWebSocket() {
        wsService = ((RoomActivity) getActivity()).getWsService();
        if (!mAnchorId.equals(mLoginInfo.getUserId())) {
            wsService.sendRequest(WsObjectPool.newLoginRequest(getContext(),
                    mLiveId, mAnchorId, mHostAvatar, mPublishNickName, mHostLevel,
                    Constants.WEBSOCKET_ROLE_AUDIENCE, "0"));
        }
        if (wsService != null) {
            initWsListeners();
        }
    }

    private void subscribeShowBtn(View view) {
        RxView.clicks($(view, R.id.room_imgbtn_prvchat))
                .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 暂不实现
                        llOperationBar.setVisibility(View.GONE);
                        recyclerPublicChat.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 关闭的逻辑实现
     *
     * @param view
     */
    private void subscribeCloseBtn(View view) {
        RxView.clicks($(view, R.id.room_imgbtn_close))
                .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //观众点击叉叉时不需要提示
                        ((RoomActivity) getActivity()).exitLiveRoom(getRoomType() != TYPE_VIEW_LIVE);
                    }
                });
    }

    /**
     * 观看人数
     *
     * @param view
     */
    private void initAudienceBar(View view) {
        tvOnlineCount = $(view, R.id.room_tv_live_user_count);

        mHostNickname = $(view, R.id.room_tv_label_user_count);
        mHostFansAccount = $(view, R.id.room_tv_live_user_job);

        recyclerAudienceList = $(view, R.id.room_recycler_audience);
        recyclerAudienceList.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        recyclerAudienceList.addItemDecoration(ItemDecorations.horizontal(getActivity())
                .type(0, R.drawable.divider_decoration_transparent_w5)
                .create());

        audienceAdapter = new AudienceAdapter(getContext(),
                new ArrayList<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList>(),
                starticon, new AudienceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserInfo userInfo) {
//                getUserInfo(userInfo.getId());
            }
        });
        recyclerAudienceList.setAdapter(audienceAdapter);
        tvOnlineCount.setText("0");
//        subscribeClick(tvOnlineCount, new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                if (mRoomType == RoomActivity.TYPE_PUBLISH_LIVE) {
//                    showUserListDialog(audienceAdapter.getDataList(), true);
//                } else {
//                    showUserListDialog(audienceAdapter.getDataList(), false);
//                }
//            }
//        });
    }

    protected void onRootClickAction() {
        friendLayout.setVisibility(View.GONE);

        boolean shouldSend = shouldSendHeartRequest();
        if (shouldSend) {
            defaultColorIndex = (int) (Math.random() * heartColorArray.length);
            mHeartAnim.addLove(heartColorArray[defaultColorIndex]);
            // wsService.sendRequest(WsObjectPool.newPublicMsgRequest(mRoomId,""+defaultColorIndex,"1"));
        }
    }

    /**
     * 是否发送点赞（心）
     *
     * @return
     */
    protected abstract boolean shouldSendHeartRequest();

    /**
     * 辅助方法， 用于将PopupWindow显示在图标上方。
     */
    protected final void showPopupWindowAboveButton(@NonNull PopupWindow window, @NonNull View anchor) {
        View popupView = window.getContentView();
        //执行一次measure，避免第一次无法获取位置的问题。
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = popupView.getMeasuredWidth();
        int popupHeight = popupView.getMeasuredHeight();
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        //To fix the bug of inability for automatically dismiss pop window when touch outside
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);
        window.showAtLocation(anchor, Gravity.NO_GRAVITY,
                (location[0] + anchor.getWidth() / 2) - popupWidth / 2,
                location[1] - popupHeight);
    }

    /**
     * 时间转换类哦
     *
     * @param time
     * @return
     */
    public String refFormatNowDate(long time) {
        Date nowTime = new Date(time);
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy.MM.dd");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        return retStrFormatNowDate;
    }

    private static class UIHandler extends Handler {
        private final WeakReference<RoomFragment> mActivity;

        public UIHandler(RoomFragment activity) {
            mActivity = new WeakReference<RoomFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    protected UserInfoPopup mUserInfoPopup;
    protected AdminPopup mAdminPopup;
    protected BeautyPopup mBeautyPopup;
    protected ReportPopup mReportPopup;

    /**
     * 展示用户信息的弹出框
     */
    protected void showUserInfoPopup(@NonNull final UserInfo info, final boolean isAdmin) {
        if (info == null) {
            toastShort("用户信息错误");
            return;
        }
        if (TextUtils.isEmpty(info.getId())) {
            toastShort("错误的用户ID");
            return;
        }

        if (mUserInfoPopup == null) {
            UserInfoPopup.UserInfoListener listener = new UserInfoPopup.UserInfoListener() {
                @Override
                public void onComplaint(UserInfo userInfo) {
                    if (isAdmin) {
                        showAdminPopuo(userInfo);
                    } else {
                        getComplainOptions(userInfo.getId());
                    }
                }

                @Override
                public void onStar(UserInfo userInfo, boolean stat) {
                }

                @Override
                public void onPrvChat(UserInfo userInfo) {
                }

                @Override
                public void onUserPhoto(UserInfo userInfo) {
                }
            };
            mUserInfoPopup = new UserInfoPopup(getContext(), info, isAdmin, listener);
        } else {
            mUserInfoPopup.setUserInfo(info);
        }
        mUserInfoPopup.showTabBottom(!mMLocalUserId.equals(info.getId()));
        if (!mUserInfoPopup.isShowing()) {
            mUserInfoPopup.showAtLocation(mRootView,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    /**
     * 关闭展示用户信息的弹出框
     */
    protected void closeUserInfoPopup() {
        if (mUserInfoPopup != null && mUserInfoPopup.isShowing()) {
            mUserInfoPopup.dismiss();
        }
    }

    /**
     * 显示管理弹出框
     *
     * @param info
     */
    protected void showAdminPopuo(@NonNull UserInfo info) {
        if (info == null) {
            toastShort("用户信息错误");
            return;
        }
        if (TextUtils.isEmpty(info.getId())) {
            toastShort("错误的用户ID");
            return;
        }

        if (mAdminPopup == null) {
            AdminPopup.AdminPopupListener listener = new AdminPopup.AdminPopupListener() {
                @Override
                public void onSetControl(UserInfo userInfo) {
                }

                @Override
                public void onMsgGag(UserInfo userInfo) {
                    onMsgGagListener(userInfo);
                }

                @Override
                public void onReport(UserInfo userInfo) {
                    getComplainOptions(userInfo.getId());
                }

                @Override
                public void onPullBlack(UserInfo userInfo) {
                    addBlackList(userInfo.getId());
                }
            };
            mAdminPopup = new AdminPopup(getContext(), info, listener);
        } else {
            mAdminPopup.setUserInfo(info);
        }
        if (!mAdminPopup.isShowing()) {
            mAdminPopup.showAtLocation(mRootView,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    /**
     * 关闭管理弹出框
     */
    protected void closeAdminPopup() {
        if (mAdminPopup != null && mAdminPopup.isShowing()) {
            mAdminPopup.dismiss();
        }
    }

    /**
     * 显示美颜弹出框
     */
    protected void showBeautyPopup() {
        if (mBeautyPopup == null) {
            mBeautyPopup = new BeautyPopup(getContext(), mRoomLiveHelp);
        }
        if (!mBeautyPopup.isShowing()) {
            mBeautyPopup.showAtLocation(mRootView,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    /**
     * 关闭美颜弹出框
     */
    protected void closeBeautyPopup() {
        if (mBeautyPopup != null && mBeautyPopup.isShowing()) {
            mBeautyPopup.dismiss();
        }
    }

    /**
     * 显示举报弹出框
     *
     * @param
     */
    protected void showReportPopup(@NonNull String userId, List<ComplainOptionBean.ListBean> list) {
        if (mReportPopup == null) {
            ReportAdapter.ReportClickListener listener = new ReportAdapter.ReportClickListener() {
                @Override
                public void onReportItemClick(String reportId, String content) {
                    reportUser(reportId, content);
                    closeReportPopup();
                }
            };
            mReportPopup = new ReportPopup(getContext(), userId, list, listener);
        }
        if (!mReportPopup.isShowing()) {
            mReportPopup.showAtLocation(mRootView,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    /**
     * 关闭举报弹出框
     */
    protected void closeReportPopup() {
        if (mReportPopup != null && mReportPopup.isShowing()) {
            mReportPopup.dismiss();
        }
    }

    /**
     * 返回子类的房间类型，用于直接处理一些简单公用的属性。
     */
    @RoomActivity.RoomType
    protected abstract int getRoomType();

    protected void showAnimIn(View v) {
        Animation headerAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.room_buttom_in);
        v.startAnimation(headerAnim);
    }

    protected void showAnimOut(View v) {
        Animation headerAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.room_buttom_out);
        v.startAnimation(headerAnim);
    }

    protected int getSurfaceViewHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        if (getActivity() != null) {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        }
        //因为部分（乐视）手机的系统栏高度不是标准的25/24dp，所以首选获得系统内置的高度，出现异常时则用预定义的高度。
        int statusBarHeight;
        try {
            statusBarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier
                    ("status_bar_height", "dimen", "android"));
        } catch (Exception e) {
            statusBarHeight = (int) getResources().getDimension(R.dimen.status_bar_height);
        }
        //窗口的宽度
        return dm.heightPixels - statusBarHeight;
    }

    /**
     * 因为WebSocket能连接的时间具有不确定性，所以必须在ServiceConnection里初始化。
     */
    protected void initWsListeners() {
        //进入房间给本人发的消息
        WsListener<WsLoginMsg> loginListener = new WsListener<WsLoginMsg>() {

            @Override
            public void handleData(WsLoginMsg wsLoginMsg) {
                Log.e("wsLoginMsg==", wsLoginMsg.getData().getNickName() + "mrliu");
            }
        };
        wsService.registerListener(SocketConstants.EVENT_LOGIN_RSP, loginListener);

        //  进入房间群发消息
        if (recyclerAudienceList != null) {
            WsListener<SystemWelcome> welcomeListenet = new WsListener<SystemWelcome>() {
                @Override
                public void handleData(SystemWelcome systemMsg) {
                    publicChatAdapter.appendData(systemMsg);
                    //Auto scroll to last
                    recyclerPublicChat.scrollToPosition(publicChatAdapter.getItemCount() - 1);

                    if (recyclerAudienceList != null && audienceAdapter != null) {
                        List<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> clients = systemMsg.getData().getUserList();

                        //Collections.copy cause lots of bugs.
                        List<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> total = new ArrayList<>(clients);

                        screenAudienList(audienceAdapter.getDataList(), total, audienceAdapter);
                    }
                    tvOnlineCount.setText(String.valueOf(systemMsg.getData().getCount()));
                    tvGold.setText(String.valueOf(systemMsg.getData().getIncome()));
                }
            };
            wsService.registerListener(SocketConstants.EVENT_SYSWElCOME, welcomeListenet);
        }

        WsListener<WsLoginOutMsg> loginOutMsgWsListener = new WsListener<WsLoginOutMsg>() {
            @Override
            public void handleData(WsLoginOutMsg wsLoginOutMsg) {
                if (Integer.parseInt(mAnchorId) == wsLoginOutMsg.getData().getUserId()) {
                    isloginout = true;
                    ((RoomActivity) getActivity()).showRoomEndInfoDialog();
                }
                List<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> userTotal = new ArrayList<>();
                List<WsLoginOutMsg.WsLoginOutMsgData.WsLoginOutMsgList> userList = wsLoginOutMsg.getData().getUserList();
                SystemWelcome.SystemWelcomeData systemWelcomeData = new SystemWelcome.SystemWelcomeData();
                SystemWelcome.SystemWelcomeData.SystemWelcomeDataList systemWelcomeDataList = null;
                for (int i = 0; i < userList.size(); i++) {
                    systemWelcomeDataList = new SystemWelcome.SystemWelcomeData.SystemWelcomeDataList();
                    systemWelcomeDataList.setAvatar(userList.get(i).getAvatar());
                    systemWelcomeDataList.setLevel(userList.get(i).getLevel() + "");
                    systemWelcomeDataList.setNickName(userList.get(i).getNickName());
                    systemWelcomeDataList.setUserId(userList.get(i).getUserId());
                    systemWelcomeDataList.setRole(userList.get(i).getRole() + "");
                    userTotal.add(systemWelcomeDataList);
                }
                systemWelcomeData.setUserList(userTotal);
                List<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> total = new ArrayList<>(userTotal);
                if (recyclerAudienceList != null && audienceAdapter != null) {
                    screenAudienList(audienceAdapter.getDataList(), total, audienceAdapter);
                }
                // 用户退出 检查连麦状态
//                onMemberExit(wsLoginOutMsg.getData().getUserId());
                tvOnlineCount.setText(String.valueOf(wsLoginOutMsg.getData().getCount()));

            }
        };
        wsService.registerListener(SocketConstants.EVENT_LOGOUT_RSP, loginOutMsgWsListener);

        WsListener<ResponseMicBean> responseyMicWsListener = new WsListener<ResponseMicBean>() {

            @Override
            public void handleData(ResponseMicBean applyMicBean) {
                if (applyMicBean == null) {
                    return;
                }
                if (DataManager.getInstance().getLoginInfo().getUserId().equals(mAnchorId)) {
                    dealApplyMicResponse(applyMicBean);
                }
            }
        };
        wsService.registerListener(SocketConstants.APPLY_MIC_RESPONSE, responseyMicWsListener);

        //断开连麦的websocket
        WsListener<DisConnectLmMsg> disConnectLmMsgWsListener = new WsListener<DisConnectLmMsg>() {
            @Override
            public void handleData(DisConnectLmMsg disConnectLmMsg) {
                dealDisConnectLm(disConnectLmMsg.getData().getUserId());
            }
        };
        wsService.registerListener(SocketConstants.DISCONNECT_LM_REQUEST_RSP, disConnectLmMsgWsListener);

        //心跳
        WsListener<WsPongMsg> pongMsgWsListener = new WsListener<WsPongMsg>() {
            private AlertDialog alertDialog;

            @Override
            public void handleData(WsPongMsg wsPongMsg) {
                if (-1 == wsPongMsg.getCode()) {
                    // if (alertDialog == null) {
                    BaseActivity baseActivity = (BaseActivity) getActivity();
                    String msg = TextUtils.isEmpty(wsPongMsg.getMessage())
                            ? "播放内容要健康" : wsPongMsg.getMessage();
                    alertDialog = new AlertDialog.Builder(baseActivity)
                            .setCancelable(false)
                            .setMessage(msg)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create();
                    alertDialog.show();
                    // }
                } else if (-2 == wsPongMsg.getCode()) {
                    //如果是主播的话禁止直播
                    if (mAnchorId.equals(wsPongMsg.getData().getUserId())) {
                        //if (alertDialog == null) {
                        BaseActivity baseActivity = (BaseActivity) getActivity();
                        String msg = TextUtils.isEmpty(wsPongMsg.getMessage())
                                ? "你已被管理员禁播" : wsPongMsg.getMessage();
                        msg = msg + "，五秒后自动退出";
                        new Thread(new ThreadShow()).start();

                        alertDialog = new AlertDialog.Builder(baseActivity)
                                .setCancelable(false)
                                .setMessage(msg)
                                .setPositiveButton("立即退出", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FINISH_ROOM = true;
                                        ((RoomActivity) getActivity()).performExitAction();
                                    }
                                })
                                //.setNegativeButton("取消", null)
                                .create();
                        alertDialog.show();
                        //}

                    } else {
                        //否则是观众踢出房间
                        if (alertDialog == null) {
                            final BaseActivity activity = (BaseActivity) getActivity();
                            String sysmsg = TextUtils.isEmpty(wsPongMsg.getMessage())
                                    ? "你被管理员请出房间" : wsPongMsg.getMessage();
                            sysmsg = sysmsg + "，五秒后自动退出";
                            new Thread(new ThreadShow()).start();
                            alertDialog = new AlertDialog.Builder(activity)
                                    .setCancelable(false)
                                    .setMessage(sysmsg)
                                    .setPositiveButton("立即退出", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                        是否被踢出 设为true
                                            isKicked = true;
                                            FINISH_ROOM = true;
                                            ((RoomActivity) getActivity()).finishRoomActivity();
                                        }
                                    })
                                    .create();
                            alertDialog.show();
                        }
                    }
                }
            }
        };
        wsService.registerListener(SocketConstants.EVENT_PONG_RSP, pongMsgWsListener);

//        接受用户发出的消息
        WsListener<UserPublicMsg> chatListener = new WsListener<UserPublicMsg>() {
            @Override
            public void handleData(UserPublicMsg msg) {
                if (1 == msg.getData().getFly()) {
                    if (isMeSend) {
                        showInputLayout(false);
                    }
                    danmu = new Danmu(0, (int) (Math.random() * (3)), "Comment", msg.getData().getNickName() + ": " + msg.getMessage());
                    if (!TextUtils.isEmpty(msg.getData().getAvatar())) {
                        Glide.with(RoomFragment.this).load(msg.getData().getAvatar()).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                danmu.setAvatarUrl(resource);
                                mDanmuControl.addDanmu(danmu, 1);
                            }
                        });
                    } else {
                        danmu.setAvatarUrl(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_launcher_author));
                        mDanmuControl.addDanmu(danmu, 1);
                    }
                }
                if (DataManager.getInstance().getLoginInfo() != null) {
                    //如果不是用户本人的信息则添加到Recycleview
                    if (!msg.getData().getUserId().equals(DataManager.getInstance().getLoginInfo().getUserId())) {
                        publicChatAdapter.appendData(msg);
                        //Auto scroll to last
                        recyclerPublicChat.scrollToPosition(publicChatAdapter.getItemCount() - 1);
                    }
                }
                isMeSend = false;
            }
        };
        wsService.registerListener(SocketConstants.EVENT_PUB_MSG_RSP, chatListener);

        //礼物群发通知
        if (localGiftController != null) {
            WsListener<SendGiftMsg> giftAnimListener = new WsListener<SendGiftMsg>() {
                @Override
                public void handleData(SendGiftMsg sendGiftMsg) {
//                    try {
//                        int giftCount = sendGiftMsg.getData().getNum();
//
//                        for (int i = 0; i < giftCount; i++) {
//                            gitfSpecials.add(Integer.parseInt(sendGiftMsg.getData().getGiftId()));
//                        }
//                        if (isgiftend == true) {
//                            isgiftend = false;
//                            showAnim(gitfSpecials.get(0));
//                        }
//                    } catch (NumberFormatException e) {
//                        toastShort("服务器开小差咯");
//                    }
                    for (int i = 0; i < sendGiftMsg.getData().getNum(); i++) {
                        publicChatAdapter.appendData(sendGiftMsg);
                    }
                    tvGold.setText(sendGiftMsg.getData().getIncome());
                    recyclerPublicChat.scrollToPosition(publicChatAdapter.getItemCount() - 1);
                    localGiftController.enqueue(adapter(sendGiftMsg));
                }

                private SendGiftAction adapter(SendGiftMsg msg) {
                    SendGiftAction action = new SendGiftAction();
                    action.setAvatar(msg.getData().getAvatar());
                    action.setFromUid(msg.getData().getUserId());
                    action.setCombo(msg.getData().getNum());
                    action.setNickname(msg.getData().getNickName());
                    action.setGiftIcon(msg.getData().getGiftImg());
                    action.setGiftName(msg.getData().getGiftName());
                    return action;
                }
            };
            wsService.registerListener(SocketConstants.EVENT_NOTIFY_GIFT_RSP, giftAnimListener);
        }
        //发送礼物回调给自己的
        WsListener<WsGiftMsg> wsGiftMsgWsListener = new WsListener<WsGiftMsg>() {
            @Override
            public void handleData(WsGiftMsg wsGiftMsg) {
                Log.e("wsGiftMsgWsListener", mLoginInfo.getUserId());
                if (mLoginInfo != null && wsGiftMsg.getData().getUserId().equals(mLoginInfo.getUserId())) {
                    //用户余额
                    mLoginInfo.setTotalBalance(Double.valueOf(wsGiftMsg.getData().getBalance()));
                    DataManager.getInstance().saveLoginInfo(mLoginInfo);
                }

            }
        };
        wsService.registerListener(SocketConstants.EVENT_SEND_GIFT_RSP, wsGiftMsgWsListener);

        //禁言
        WsListener<GagResResponse> gagResResponseWsListener = new WsListener<GagResResponse>() {
            @Override
            public void handleData(GagResResponse gagResResponse) {
                if (mLoginInfo != null && mLoginInfo.getUserId().equals(gagResResponse.getData().getUserId())
                        && mLiveId.equals(gagResResponse.getData().getRoomId())) {
                    toastShort("你已经被禁言");
                    UserPublicMsg.UserPublicMsgData data = new UserPublicMsg.UserPublicMsgData();
                    data.setNickName(mLoginInfo.getNickname());
                    data.setAvatar(mLoginInfo.getAvatar());
                    data.setUserId(mLoginInfo.getUserId());
                    data.setRoomId(mLiveId);
                    UserPublicMsg publicMsg = new UserPublicMsg();
                    publicMsg.setCode("0");
                    publicMsg.setMessageType(SocketConstants.EVENT_PUB_MSG);
                    publicMsg.setData(data);
                    publicMsg.setMessage("你已经被禁言");
                    publicChatAdapter.appendData(publicMsg);
                    mMsgGag = false;
                }
            }
        };
        wsService.registerListener(SocketConstants.GAG_RES_RESPONSE, gagResResponseWsListener);

        //拉黑
        WsListener<BlackListRes> blackListResWsListener = new WsListener<BlackListRes>() {
            @Override
            public void handleData(BlackListRes blackListRes) {
                if (mLoginInfo != null && mLoginInfo.getUserId().equals(blackListRes.getData().getBlacklistUserId())
                        && mLiveId.equals(blackListRes.getData().getRoomId())) {
                    toastShort("你已经被拉黑");
                    ((RoomActivity) getActivity()).exitLiveRoom(getRoomType() != TYPE_VIEW_LIVE);
                }
            }
        };
        wsService.registerListener(SocketConstants.BLACK_LIST_RES, blackListResWsListener);

        //连麦响应
        WsListener<LmAgreeOrRefuseRes> lmAgreeOrRefuseResWsListener = new WsListener<LmAgreeOrRefuseRes>() {
            @Override
            public void handleData(LmAgreeOrRefuseRes lmAgreeOrRefuseRes) {
                if (mLoginInfo != null && mLoginInfo.getUserId().equals(lmAgreeOrRefuseRes.getData().getUserId())
                        && mLiveId.equals(lmAgreeOrRefuseRes.getData().getRoomId())) {
                    dealApplyMicRequest(lmAgreeOrRefuseRes);
                }
            }
        };
        wsService.registerListener(SocketConstants.LM_AGREE_OR_REFUSE_RES, lmAgreeOrRefuseResWsListener);

        //副播断开连麦
        WsListener<CloseCallSecondaryRes> closeCallSecondaryResWsListener = new WsListener<CloseCallSecondaryRes>() {
            @Override
            public void handleData(CloseCallSecondaryRes closeCallSecondaryRes) {
                toastShort("副播断开连麦");
                Log.e(LOG_TAG, closeCallSecondaryRes.toString());
            }
        };
        wsService.registerListener(SocketConstants.CLOSE_CALL_SECONDARY_RES, closeCallSecondaryResWsListener);
    }

    /**
     * 展示用户列表
     *
     * @param dataList
     */
    private void showUserListDialog(List<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> dataList, boolean isAndim) {
        if (dataList.size() > 100) {
            dataList = dataList.subList(0, dataList.size());
        }
        UserListDialog userListDialog = new UserListDialog(getContext(), dataList, isAndim);
        userListDialog.setItemClickListener(new UserListDialog.DialogListener() {
            @Override
            public void onComplaint(UserListDialog dialog, UserInfo userInfo) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                showAdminPopuo(userInfo);
            }
        });
        userListDialog.show();
    }

    /**
     * 做推流停止操作和拉流的操作
     *
     * @param showContent
     */
    public void doStopLive(String showContent) {
        toastLong(showContent);
        ((RoomActivity) getActivity()).toDoOutLive(getRoomType() == RoomActivity.TYPE_PUBLISH_LIVE);
    }

    /**
     * 对比适配器里面的集合，和传过来的集合
     *
     * @param oldList
     * @param newList
     * @param adapter
     */
    public void screenAudienList(List<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> oldList,
                                 List<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> newList, AudienceAdapter adapter) {
        //  如果观众出去了
        if (oldList.size() > newList.size()) {
            int i;
            for (i = 0; i < newList.size(); i++) {
                if (!newList.get(i).getUserId().equals(oldList.get(i).getUserId())) {
                    oldList.set(i, newList.get(i));
                    adapter.notifyItemChanged(i);
                }
            }
            while (oldList.size() > i) {
                adapter.notifyItemRemoved(i);
                oldList.remove(i);
                i++;
            }
        } else {
            int i;
            for (i = 0; i < oldList.size(); i++) {
                if (!newList.get(i).getUserId().equals(oldList.get(i).getUserId())) {
                    oldList.set(i, newList.get(i));
                    adapter.notifyItemChanged(i);
                }
            }
            while (newList.size() > i) {
                oldList.add(newList.get(i));
                adapter.notifyItemChanged(i);
                i++;
            }
        }
    }

    public void showAnim(int isread) {
        if (isdeas) {
            return;
        }
        switch (isread) {
//            烟花
            case 2:
                fireworks = new FireworksView((RoomActivity) getActivity());
                fireworks.setAnimsopt(this);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                animLayout.addView(fireworks, layoutParams);
                break;
//            钻石
            case 3:
                gen = new GenView((RoomActivity) getActivity());
                gen.setAnimsopt(this);
                RelativeLayout.LayoutParams layoutParamsw = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParamsw.addRule(RelativeLayout.CENTER_IN_PARENT);
                animLayout.addView(gen, layoutParamsw);
                break;
//            汽车
            case 4:
                car = new CarView(getActivity());
                car.setAnimsopt(this);
                animLayout.addView(car);
                car.initAnim(animLayout.getMeasuredWidth(), animLayout.getMeasuredHeight());
                break;
//            飞机
            case 5:
                plane = new PlaneImagerView(getActivity());
                plane.setGitfSpecialsStop(this);
                animLayout.addView(plane);
                plane.initAnim(getActivity().getWindowManager().getDefaultDisplay().getWidth());
                break;
//            游艇
            case 6:
                ship = new ShipView(getActivity());
                ship.setGitfSpecialsStop(this);
                RelativeLayout.LayoutParams layoutParamss =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParamss.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                animLayout.addView(ship, layoutParamss);
                ship.initAnim(getActivity().getWindowManager().getDefaultDisplay().getWidth());
                break;
            default:
                break;
        }
    }

    @Override
    public void animend() {
        isgiftend = true;
        clearAnim(gitfSpecials.get(0));
        gitfSpecials.remove(0);
        if (gitfSpecials.size() > 0 && isgiftend) {
            isgiftend = false;
            showAnim(gitfSpecials.get(0));
            return;
        }
    }

    public void clearAnim(int giftisread) {
        animLayout.removeAllViews();
        switch (giftisread) {
            case 2:
                fireworks = null;
                break;
            case 3:
                gen = null;
                break;
            case 4:
                car = null;
                break;
            case 5:
                plane = null;
                break;
            case 6:
                ship = null;
                break;
            default:
                break;
        }
    }

    //    是否执行
    private boolean FINISH_ROOM = false;

    /**
     * d计时器
     */
    class ThreadShow implements Runnable {
        @Override
        public void run() {
            int i = 0;
            while (i < 5 && !FINISH_ROOM) {
                try {
                    i++;
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShort("未知错误");
                }
            }
        }
    }

    @Override
    public void showInputLayout(boolean show) {
        L.v(false, LOG_TAG, "showInputLayout:%s", show);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (show) {
            llChatBar.setVisibility(View.VISIBLE);
            llHeader.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    llHeader.setVisibility(View.INVISIBLE);
                    Animation headerAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.top_out);
                    llHeader.startAnimation(headerAnim);
                }
            }, 100);

            edtChatContent.requestFocus();
            imm.showSoftInput(edtChatContent, InputMethodManager.SHOW_IMPLICIT);

            llOperationBar.setVisibility(View.INVISIBLE);
        } else {
            llOperationBar.setVisibility(View.VISIBLE);
            /**
             * 使用handler来处理
             */
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    llHeader.setVisibility(View.VISIBLE);
                    Animation headerAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.top_in);
                    llHeader.startAnimation(headerAnim);
                }

            }, 50);

            llChatBar.setVisibility(View.INVISIBLE);

            imm.hideSoftInputFromWindow(edtChatContent.getWindowToken(), InputMethodManager
                    .HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public ViewGroup getInputLayout() {
        return llChatBar;
    }

    @Override
    public RelativeLayout getFriendLayout() {
        return friendLayout;
    }

    /**
     * 隐藏
     */
    @Override
    public void hideFriendLayout() {
        llOperationBar.setVisibility(View.VISIBLE);
        friendLayout.setVisibility(View.GONE);
        recyclerPublicChat.setVisibility(View.VISIBLE);
    }

    private DanmuControl mDanmuControl;

    private void initDanmuku(View view) {
        mDanmuControl = new DanmuControl(getActivity());
//        弹幕实力话
        mDanmakuView = (DanmakuView) view.findViewById(R.id.mrl_danmaku);
        mDanmuControl.setDanmakuView(mDanmakuView);
        mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
            @Override
            public void onDanmakuClick(BaseDanmaku latest) {
            }

            @Override
            public void onDanmakuClick(IDanmakus danmakus) {
            }
        });
    }

    /**
     * 成功的请求
     *
     * @param what
     * @param response
     */
    @Override
    public void onSucceed(int what, Response<Bitmap> response) {
        danmu.setAvatarUrl(response.get());
        mDanmuControl.addDanmu(danmu, 1);
    }

    /**
     * 失败
     *
     * @param what
     * @param url
     * @param tag
     * @param exception
     * @param responseCode
     * @param networkMillis
     */
    @Override
    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

    }

    public void showRedView() {
        mRedView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetChange(Context context, int netMobile) {
        Log.e("onNetChange", "onNetChange==" + "走了吗?");
        onNetChanged(context, netMobile);
    }
}
