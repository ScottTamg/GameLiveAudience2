package com.ttt.liveroom.widget;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ttt.liveroom.R;
import com.ttt.liveroom.bean.UserInfo;

public class AdminPopup extends PopupWindow implements View.OnTouchListener,
        View.OnClickListener, View.OnKeyListener {
    private static final String TAG = "AdminPopup";
    private TextView mTvSetControl;
    private TextView mTvMsgGag;
    private TextView mTvReport;
    private TextView mTvPullBlack;
    private TextView mTvCancel;

    private UserInfo mUserInfo;

    private Context mContext;
    private View mRootView;
    private AdminPopupListener mListener;

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    public AdminPopup(Context context, UserInfo userInfo, AdminPopupListener listener) {
        this.mContext = context;
        this.mUserInfo = userInfo;
        this.mListener = listener;
        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = inflater.inflate(R.layout.popup_admin, null);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
//        ColorDrawable dw = new ColorDrawable(0x00000000);
//        this.setBackgroundDrawable(dw);
        //设置动画效果
        this.setAnimationStyle(R.style.MyPopupWindow_anim_style);
        setContentView(mRootView);

        mTvSetControl = (TextView) mRootView.findViewById(R.id.tv_set_control);
        mTvMsgGag = (TextView) mRootView.findViewById(R.id.tv_msg_gag);
        mTvReport = (TextView) mRootView.findViewById(R.id.tv_report);
        mTvPullBlack = (TextView) mRootView.findViewById(R.id.tv_pull_black);
        mTvCancel = (TextView) mRootView.findViewById(R.id.tv_cancel);

        mTvSetControl.setOnClickListener(this);
        mTvMsgGag.setOnClickListener(this);
        mTvReport.setOnClickListener(this);
        mTvPullBlack.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            dismiss();
        } else if (i == R.id.tv_set_control) {
            mListener.onSetControl(mUserInfo);
        } else if (i == R.id.tv_msg_gag) {
            mListener.onMsgGag(mUserInfo);
            dismiss();
        } else if (i == R.id.tv_report) {
            mListener.onReport(mUserInfo);
            dismiss();
        } else if (i == R.id.tv_pull_black) {
            mListener.onPullBlack(mUserInfo);
            dismiss();
        } else {

        }
    }

    //点击外部popup消失
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int height = mRootView.findViewById(R.id.rl_user_info).getTop();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (y < height) {
                dismiss();
            }
        }
        return true;
    }

    //点back键消失
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.isShowing()) {
            this.dismiss();
            return true;
        }
        return false;
    }

    public interface AdminPopupListener {
        void onSetControl(UserInfo userInfo);

        void onMsgGag(UserInfo userInfo);

        void onReport(UserInfo userInfo);

        void onPullBlack(UserInfo userInfo);
    }
}
