package com.ttt.liveroom.room.play;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ttt.liveroom.Config;
import com.ttt.liveroom.R;
import com.ttt.liveroom.bean.room.NewestAuthorBean;
import com.ttt.liveroom.room.RoomActivity;
import com.ttt.liveroom.room.window.PlayerActivityManager;

import ijkplayer.IjkVideoView;

/**
 * Created by Iverson on 2018/4/8 下午6:37
 * 此类用于：
 */

public class SmallScreenHelp implements View.OnClickListener {

    private static Activity mContext;
    private static SmallScreenHelp instance;
    private WindowManager.LayoutParams wmParams;
    private WindowManager mWindowManager;
    private View mWindowView;


    private int mStartX;
    private int mStartY;
    private int mEndX;
    private int mEndY;
    private RelativeLayout mRootWindowView;
    private RelativeLayout mRlVideoRoot;
    private IjkVideoView mVideoView;
    private ImageView mIvClose;
    private ImageView mIvChangeBig;
    private ImageView mIvChangeSmall;


    private ImageView mIvSzhangkai;
    private ImageView mIvSChangeMiddle;
    private ImageView mIvSExit;
    private ImageView mIvSsuoxiao;
    private TextView mTvSName;
    private View mShowview;
    private View mSHowNoview;
    private RelativeLayout mRlSmallVideoRoot;


    private void SmallScreenHelp() {
    }

    public static SmallScreenHelp init(Activity activity) {
        mContext = activity;
        return getInstance();
    }

    public static SmallScreenHelp getInstance() {
        if (instance == null) {
            synchronized (SmallScreenHelp.class) {
                if (instance == null) {
                    instance = new SmallScreenHelp();
                }
            }
        }
        return instance;
    }


    private void initWindowParams() {
        mWindowManager = (WindowManager) mContext.getApplication().getSystemService(Activity.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        // 更多type：https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#TYPE_PHONE
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        wmParams.format = PixelFormat.TRANSLUCENT;
        // 更多falgs:https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#FLAG_NOT_FOCUSABLE
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }


    private void initView() {
        mWindowView = LayoutInflater.from(mContext.getApplication()).inflate(R.layout.window_view, null);

        mShowview = mWindowView.findViewById(R.id.rl_video_show);
        mSHowNoview = mWindowView.findViewById(R.id.rl_video_noshow);

        mRootWindowView = mWindowView.findViewById(R.id.rl_window_root);
        mRlVideoRoot = mWindowView.findViewById(R.id.rl_video_root);
//        mVideoView = mWindowView.findViewById(R.id.ijk_video);
        mIvClose = mWindowView.findViewById(R.id.iv_m_close);
        mIvChangeBig = mWindowView.findViewById(R.id.iv_m_big);
        mIvChangeSmall = mWindowView.findViewById(R.id.iv_m_small);
        mRlSmallVideoRoot = mWindowView.findViewById(R.id.rl_small_video_root);

        mIvSzhangkai = mWindowView.findViewById(R.id.iv_s_zhangkai);
        mIvSChangeMiddle = mWindowView.findViewById(R.id.iv_s_change_middle);
        mIvSExit = mWindowView.findViewById(R.id.iv_s_exit);
        mIvSsuoxiao = mWindowView.findViewById(R.id.iv_s_suoxiao);
        mTvSName = mWindowView.findViewById(R.id.tv_live_name);

        mIvClose.setOnClickListener(this);
        mIvChangeBig.setOnClickListener(this);
        mIvChangeSmall.setOnClickListener(this);

        mIvSzhangkai.setOnClickListener(this);
        mIvSChangeMiddle.setOnClickListener(this);
        mIvSExit.setOnClickListener(this);
        mIvSsuoxiao.setOnClickListener(this);
    }


    public void starChangeSmall() {
        initWindowParams();
        initView();
        addWindowView2Window();
        initClick();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_m_close) {
            removeWindowView();

        } else if (i == R.id.iv_m_big) {
            NewestAuthorBean.ListBean bean = Config.LIVE_DATA;
            String hostInfo = new Gson().toJson(bean);
            mContext.startActivity(RoomActivity.createIntent(mContext,
                    RoomActivity.TYPE_VIEW_LIVE,
                    bean.getId(),
                    String.valueOf(bean.getUserId()),
                    bean.getAvatar(),
                    bean.getNickName(),
                    String.valueOf(bean.getLevel()),
                    bean.getTitle(),
                    bean.getPullRtmp(),
                    false,
                    bean.getRoomId(),
                    bean.getType(),
                    PlayFragment.createArgs(hostInfo)));
            removeWindowView();

        } else if (i == R.id.iv_m_small) {
            mShowview.setVisibility(View.GONE);
            mSHowNoview.setVisibility(View.VISIBLE);
            mRlVideoRoot.removeView(mVideoView);
            mRlSmallVideoRoot.addView(mVideoView, 0);
            smallViewChange(false);

        } else if (i == R.id.iv_s_zhangkai) {
            smallViewChange(true);

        } else if (i == R.id.iv_s_change_middle) {
            mRlSmallVideoRoot.removeView(mVideoView);
            mRlVideoRoot.removeAllViews();
            mRlVideoRoot.addView(mVideoView, 0);
            mShowview.setVisibility(View.VISIBLE);
            mSHowNoview.setVisibility(View.GONE);
            if (mVideoView != null) {
                mVideoView.stopPlayback();
                mRlVideoRoot.removeView(mVideoView);
                mVideoView = null;
                addVideoView(mRlVideoRoot);
            }

        } else if (i == R.id.iv_s_exit) {
            removeWindowView();
        } else if (i == R.id.iv_s_suoxiao) {
            smallViewChange(false);

        }
    }


    public void onResume() {
        if (mWindowView != null) {
            mWindowView.setVisibility(View.VISIBLE);
        }
    }

    public void onPause() {
        if (mWindowView != null) {
            mWindowView.setVisibility(View.GONE);
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        removeWindowView();
    }

    public void addVideoView(ViewGroup viewGroup) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        lp.height = Config.SCREEND_HEIGHT;
        mVideoView = new IjkVideoView(mContext);
        mVideoView.setLayoutParams(lp);
        PlayerActivityManager playerManager = new PlayerActivityManager(mContext, mVideoView);
        playerManager.play(Config.LIVE_PULL_URL);
        playerManager.setScaleType(PlayerActivityManager.SCALETYPE_FITXY);
        playerManager.start();
        viewGroup.addView(mVideoView);
    }

    /**
     * flag 最小化的状态 true:放开 false：缩小
     *
     * @param flag
     */
    private void smallViewChange(boolean flag) {
        mIvSzhangkai.setVisibility(flag ? View.GONE : View.VISIBLE);
        mIvSChangeMiddle.setVisibility(flag ? View.VISIBLE : View.GONE);
        mIvSExit.setVisibility(flag ? View.VISIBLE : View.GONE);
        mIvSsuoxiao.setVisibility(flag ? View.VISIBLE : View.GONE);
        mTvSName.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    private void removeWindowView() {
        if (mWindowView != null) {
            if (mVideoView != null) {
                mVideoView.stopPlayback();
            }
            if (mWindowManager != null) {
                mWindowManager.removeView(mWindowView);
                mWindowManager = null;
            }
        }
    }

    private void addWindowView2Window() {
        if (!mContext.isFinishing()) {
            addVideoView(mRlVideoRoot);
            mWindowManager.addView(mWindowView, wmParams);
        }
    }

    private void initClick() {
        mRootWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartX = (int) event.getRawX();
                        mStartY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mEndX = (int) event.getRawX();
                        mEndY = (int) event.getRawY();
                        if (needIntercept()) {
                            //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                            wmParams.x = (int) event.getRawX() - mWindowView.getMeasuredWidth() / 2;
                            wmParams.y = (int) event.getRawY() - mWindowView.getMeasuredHeight() / 2;
                            mWindowManager.updateViewLayout(mWindowView, wmParams);
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (needIntercept()) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    /**
     * 是否拦截
     *
     * @return true:拦截;false:不拦截.
     */
    private boolean needIntercept() {
        if (Math.abs(mStartX - mEndX) > 30 || Math.abs(mStartY - mEndY) > 30) {
            return true;
        }
        return false;
    }


}
