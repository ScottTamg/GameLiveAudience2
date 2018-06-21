package com.ttt.liveroom.room;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TimingLogger;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ttt.liveroom.R;
import com.ttt.liveroom.base.BaseActivity;
import com.ttt.liveroom.base.BaseFragment;
import com.ttt.liveroom.bean.WebSocketInfoBean;
import com.ttt.liveroom.net.Constants;
import com.ttt.liveroom.room.play.PlayFragment;
import com.ttt.liveroom.websocket.SocketConstants;
import com.ttt.liveroom.websocket.WebSocketService;
import com.ttt.liveroom.widget.viewpager.VerticalViewPager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import rx.Subscription;

/**
 * @author liujing
 * Created by 刘景 on 2017/06/05.
 */
public class RoomActivity extends BaseActivity implements HomeUiInterface{

    private final String LOG_TAG = RoomActivity.class.getSimpleName();

    /**
     * 观看直播
     */
    public static final int TYPE_VIEW_LIVE = 1;
    /**
     * 直播
     */
    public static final int TYPE_PUBLISH_LIVE = 2;
    /**
     * 回播
     */
    public static final int TYPE_VIEW_REPLAY = 3;

    @IntDef({TYPE_VIEW_LIVE, TYPE_PUBLISH_LIVE, TYPE_VIEW_REPLAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RoomType {
    }

    private RoomFragment roomPFragment;

    public static final String EXTRA_ROOM_TYPE = "live_type";
    public static final String EXTRA_ANCHOR_ID = "anchor_id";
    public static final String EXTRA_STREAM_ID = "stream_id";
    public static final String EXTRA_ANCHOR_AVATAR = "anchor_avatar";
    public static final String EXTRA_ANCHOR_NICKNAME = "anchor_nick_name";
    public static final String EXTRA_ANCHOR_LEVEL = "anchor_level";

    public static final String EXTRA_LIVE_TITLE = "title";
    public static final String EXTRA_LIVE_PULL_URL = "pull_rtmpurl";
    public static final String EXTRA_LIVE_IS_RECODING = "is_recoding";
    public static final String EXTRA_LIVE_ID = "live_id";
    private static final int FRAG_CONTAINER = R.id.room_container;

    @RoomType
    public static int mRoomType;
    private Bundle mBundleArgs;

    private String mAnchorId;
    private String mStreamId;
    private String hostAvatar;
    private String hostNickName;
    private String hostLevel;
    public static String mRoomTitle;
    private String mLiveId;
    private String mPullRtmp;
    public static boolean mIsRecoding;
    private Subscription subscription;
    private FrameLayout mContentLayout;

    private boolean mIsFrom;

    private VerticalViewPager mViewPager;
    private boolean isConnectSocket;

    private WebSocketService wsService;
    private HomePresenter presenter;
    private RoomFragment fragment;

    private ServiceConnection wsConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            wsService = ((WebSocketService.ServiceBinder) service).getService();
            isConnectSocket = true;
            startFragment();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnectSocket = false;
        }
    };

    /**
     * 连接websocket之后再打开页面,用户体验不太还,暂时不考虑
     */
    private void startFragment() {
        if (!mIsFrom) {
            mViewPager = (VerticalViewPager) findViewById(R.id.vp_content);
//            if (mRoomType == TYPE_VIEW_LIVE) {
//                SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//                mSectionsPagerAdapter.addData(mBundleArgs, mAnchorId, hostAvatar, hostNickName,
//                        hostLevel, mPullRtmp, mLiveId, mStreamId);
//                mViewPager.setVisibility(View.VISIBLE);
//                mViewPager.setAdapter(mSectionsPagerAdapter);
//            } else {
                mViewPager.setVisibility(View.GONE);
                TimingLogger logger = new TimingLogger("timing", "RoomActivity");
                getSupportFragmentManager().beginTransaction()
                        .add(FRAG_CONTAINER, createFragmentByType())
                        .commit();
                logger.addSplit("add fragment");
                logger.dumpToLog();
//            }
        }
    }

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return R.layout.activity_room;
    }

    @Override
    protected void findViews() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mContentLayout = (FrameLayout) findViewById(R.id.room_container);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressWarnings("unused")
    public static Intent createIntent(Context context,
                                      @RoomType int roomType,
                                      @NonNull String streamId,
                                      @NonNull String anchorId,
                                      @NonNull String avatar,
                                      @NonNull String nickName,
                                      @NonNull String leval,
                                      @NonNull String mRoomTitle,
                                      @NonNull String pullRtmp,
                                      @NonNull boolean isRecoding,
                                      @NonNull String roomId,
                                      @NonNull String type,
                                      Bundle args) {
        Intent intent = new Intent(context, RoomActivity.class);
        intent.putExtra(EXTRA_STREAM_ID, streamId);
        intent.putExtra(EXTRA_ROOM_TYPE, roomType);
        intent.putExtra(EXTRA_ANCHOR_ID, anchorId);
        intent.putExtra(EXTRA_ANCHOR_AVATAR, avatar);
        intent.putExtra(EXTRA_ANCHOR_NICKNAME, nickName);
        intent.putExtra(EXTRA_ANCHOR_LEVEL, leval);
        intent.putExtra(EXTRA_LIVE_TITLE, mRoomTitle);
        intent.putExtra(EXTRA_LIVE_PULL_URL, pullRtmp);
        intent.putExtra(EXTRA_LIVE_IS_RECODING, isRecoding);
        intent.putExtra(EXTRA_LIVE_ID, roomId);
        Constants.IS_LIVE = type;
        if (args != null) {
            intent.putExtras(args);
        }
        return intent;
    }

    @Override
    protected void parseIntentData(Intent intent, boolean isFrom) {
        super.parseIntentData(intent, isFrom);
        mBundleArgs = intent.getExtras();
        @RoomType int roomType = intent.getIntExtra(EXTRA_ROOM_TYPE, 0);
        mRoomType = roomType;
        if (mRoomType == TYPE_PUBLISH_LIVE) {
            mAnchorId = intent.getStringExtra(EXTRA_ANCHOR_ID);
            hostAvatar = intent.getStringExtra(EXTRA_ANCHOR_AVATAR);
            hostNickName = intent.getStringExtra(EXTRA_ANCHOR_NICKNAME);
            hostLevel = intent.getStringExtra(EXTRA_ANCHOR_LEVEL);
            mRoomTitle = intent.getStringExtra(EXTRA_LIVE_TITLE);
            mPullRtmp = intent.getStringExtra(EXTRA_LIVE_PULL_URL);
            mIsRecoding = intent.getBooleanExtra(EXTRA_LIVE_IS_RECODING, false);
            mLiveId = intent.getStringExtra(EXTRA_LIVE_ID);
            mStreamId = intent.getStringExtra(EXTRA_STREAM_ID);
        } else {
            mAnchorId = intent.getStringExtra(EXTRA_ANCHOR_ID);
            hostAvatar = intent.getStringExtra(EXTRA_ANCHOR_AVATAR);
            hostNickName = intent.getStringExtra(EXTRA_ANCHOR_NICKNAME);
            hostLevel = intent.getStringExtra(EXTRA_ANCHOR_LEVEL);
            mRoomTitle = intent.getStringExtra(EXTRA_LIVE_TITLE);
            mPullRtmp = intent.getStringExtra(EXTRA_LIVE_PULL_URL);
            mIsRecoding = intent.getBooleanExtra(EXTRA_LIVE_IS_RECODING, false);
            mLiveId = intent.getStringExtra(EXTRA_LIVE_ID);
            mStreamId = intent.getStringExtra(EXTRA_STREAM_ID);
        }

        mIsFrom = isFrom;

        presenter = new HomePresenter(this);
        Log.e("RoomActivity", "mLiveId==" + "" + mLiveId + " rtmp=" + mPullRtmp);

        presenter.getWebSocket(mLiveId);
    }

    @Override
    public void getWebSocketSuccess(WebSocketInfoBean bean) {
        Constants.SOCKET_URL = getResources().getString(R.string.web_socket_url, bean.getRoomServer().getHost(), bean.getRoomServer().getPort());
        bindService(WebSocketService.createIntent(this), wsConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onDataFailure(String msg) {
        toastShort(msg);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragment != null) {
            fragment.destroyRoom();
        }
        if (subscription != null) {
            subscription.unsubscribe();
        }
        if (wsService != null) {
            wsService.prepareShutdown();
            unbindService(wsConnection);
        }
    }

    @Override
    public void onBackPressed() {
        exitLiveRoom(true);
    }

    public void exitLiveRoom(boolean needConfirm) {
        if (needConfirm) {
            showFinishConfirmDialog();
            return;
        }
        finishRoomActivity();
    }

    /**
     * 判断是否需要显示收入等信息
     *
     * @param needConfirm
     */
    public void toDoOutLive(boolean needConfirm) {
        if (needConfirm) {
            performExitAction();
        } else {
            finishRoomActivity();
        }
    }

    public void showFinishConfirmDialog() {
        MessageDialog dialog = new MessageDialog(this);
        dialog.setContent(R.string.back_room_tip);
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
                performExitAction();
            }
        });
        dialog.show();
    }

    /**
     * 这个方法是在点击退出确认后的操作，对于观众来说应该什么都不做，而对于主播来说可以看到收入的信息框。
     */
    public void performExitAction() {
//        if (mRoomType == TYPE_PUBLISH_LIVE && ((PublishFragment) roomPFragment).isEnterRoomSucess) {
//            showRoomEndInfoDialog();
//            return;
//        }
        finishRoomActivity();
    }

    public void finishRoomActivity() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f instanceof PlayFragment) {
                ((PlayFragment) f).exitRoom();
            }
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(FRAG_CONTAINER);
        if ((fragment instanceof HasInputLayout)) {
            ViewGroup inputLayout = ((HasInputLayout) fragment).getInputLayout();
            if (inputLayout != null && inputLayout.isShown()) {
                ((HasInputLayout) fragment).showInputLayout(false);
                return;
            }
        }
        finish();
        //不是通过Finish，而是start，保持本Activity不被销毁  -----------这里严重怀疑会出现无法退出的bug
        overridePendingTransition(R.anim.fragment_slide_right_in, R.anim.fragment_slide_right_out);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(FRAG_CONTAINER);
        if (!(fragment instanceof HasInputLayout)) {
            return super.dispatchTouchEvent(ev);
        }

        HasInputLayout inputFragment = (HasInputLayout) fragment;

        //For safety
        ViewGroup inputLayout = inputFragment.getInputLayout();
        //xiaohuoban
        RelativeLayout friendLayout = inputFragment.getFriendLayout();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (friendLayout.isShown() || friendLayout.getVisibility() == View.VISIBLE) {
                View view = getCurrentFocus();
                if (isHideInput(friendLayout, ev) && view != null) {
                    inputFragment.hideFriendLayout();
                }
            }
        }
        if (inputLayout == null || (!inputLayout.isShown())) {
//            L.d(LOG_TAG, "InputLayout is%s null and not shown.", inputLayout == null ? "" : " not");
            return super.dispatchTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isHideInput(inputLayout, ev) && v != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
//                    switchSoftInputStatus();
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    inputFragment.showInputLayout(false);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            View view = getCurrentFocus();
            int aaa = view.findFocus().getId();
            int[] leftTop = {0, 0};
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    private boolean isHideInput(ViewGroup viewGroup, MotionEvent event) {
        int[] leftTop = {0, 0};
        //获取输入框当前的location位置
        viewGroup.getLocationInWindow(leftTop);
        Log.e(LOG_TAG, "view gettop" + viewGroup.getTop());
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + viewGroup.getHeight();
        int right = left + viewGroup.getWidth();
        Log.e(LOG_TAG, "left:" + left + " top:" + top + " bottom:" + bottom + "right:" + right);
        Log.e(LOG_TAG, "x:" + event.getX() + " y:" + event.getY());
//        这里判断的是否是 触摸了viewGrop的 触摸了返回false  没有则返回true 不要问我为什么最后 只需要event.getY() > top 请动脑 毕竟不是我的代码
        return !(event.getX() > left && event.getX() < right
                && event.getY() > top);
    }

    public interface HasInputLayout {
        void showInputLayout(boolean show);

        ViewGroup getInputLayout();

        RelativeLayout getFriendLayout();

        void hideFriendLayout();
    }

    public void showRoomEndInfoDialog() {
        //先停止Preview
        Fragment fragment = getSupportFragmentManager().findFragmentById(FRAG_CONTAINER);
        if (fragment instanceof PlayFragment) {
            PlayFragment playFragment = (PlayFragment) fragment;
            playFragment.getHostExitInfo();
        }
//        else {
//            PublishFragment publishFragment = (PublishFragment) fragment;
//            publishFragment.prepareExit();
//        }
//        如果是直播的才发出logoOut其他人发送不走这里
        if (mRoomType == TYPE_PUBLISH_LIVE) {
            roomPFragment.requestRoomLoginOut(mAnchorId, hostAvatar, hostNickName, hostLevel,
                    Constants.WEBSOCKET_ROLE_HOST, SocketConstants.EVENT_LOGOUT);
        }
    }

    private BaseFragment createFragmentByType() {
        switch (mRoomType) {
            //播放间
            case TYPE_VIEW_LIVE:
                fragment = PlayFragment.newInstance(mBundleArgs, mAnchorId, hostAvatar,
                        hostNickName, hostLevel, mPullRtmp, mStreamId, mLiveId, false);
                fragment.setAnchorId(mAnchorId);
                fragment.mRoomType = TYPE_VIEW_LIVE;
                roomPFragment = (RoomFragment) fragment;
                roomPFragment.setAnchorId(mAnchorId);
                break;
//            直播间
//            case TYPE_PUBLISH_LIVE:
//                fragment = PublishFragment.newInstance(mBundleArgs, hostAvatar, hostNickName,
//                        hostLevel, mRoomTitle, mAnchorId, mStreamId);
//                fragment.setAnchorId(mAnchorId);
//                fragment.mRoomType = TYPE_PUBLISH_LIVE;
//                roomPFragment = (RoomFragment) fragment;
//                roomPFragment.mRoomType = TYPE_PUBLISH_LIVE;
//                roomPFragment.setAnchorId(mAnchorId);
//                break;
//            case TYPE_VIEW_REPLAY:
//                fragment = PlayFragment.newInstance(mBundleArgs, mAnchorId, hostAvatar,
//                        hostNickName, hostLevel, mPullRtmp, mStreamId, mLiveId, true);
//                RoomFragment roomFragmentRePlay = (RoomFragment) fragment;
//                roomFragmentRePlay.mRoomType = TYPE_VIEW_REPLAY;
//                roomFragmentRePlay.setAnchorId(mAnchorId);
//                break;
            default:
                throw new IllegalArgumentException("Wrong room type: " + mRoomType);
        }

        return fragment;
    }

    public WebSocketService getWsService() {
        return wsService;
    }


}
