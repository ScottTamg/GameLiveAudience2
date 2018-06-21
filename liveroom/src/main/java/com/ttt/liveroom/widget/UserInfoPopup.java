package com.ttt.liveroom.widget;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ttt.liveroom.R;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.util.UiUtils;

public class UserInfoPopup extends PopupWindow implements View.OnTouchListener,
        View.OnClickListener, View.OnKeyListener {
    private static final String TAG = "UserInfoPopup";

    private UserInfo mUserInfo;
    private TextView mTvComplaint;
    private TextView mTvCancel;
    private TextView mTvName;
    private TextView mTvUserId;
    private TextView mTvAddress;
    private TextView mTvSignature;
    private TextView mTvStatNum;
    private TextView mTvStatNum2;
    private TextView mTvStatNum3;
    private TextView mTvStatNum4;
    private TextView mTvStat;
    private TextView mTvChat;
    private SimpleDraweeView mPhoto;
    private LinearLayout mTabBottom;

    private Context mContext;
    private View mRootView;
    private boolean mIsAdmin;
    private UserInfoListener mListener;

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        initData();
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public UserInfoPopup(Context context, UserInfo userInfo, boolean isAdmin, UserInfoListener listener) {
        this.mContext = context;
        this.mUserInfo = userInfo;
        this.mIsAdmin = isAdmin;
        this.mListener = listener;
        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = inflater.inflate(R.layout.dialog_userinfo, null);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
//        ColorDrawable dw = new ColorDrawable(0x00000000);
//        this.setBackgroundDrawable(dw);
        //设置动画效果
        this.setAnimationStyle(R.style.MyPopupWindow_anim_style);
        setContentView(mRootView);

        mTvComplaint = (TextView) mRootView.findViewById(R.id.dialog_user_info_complaint);
        mTvCancel = (TextView) mRootView.findViewById(R.id.dialog_user_info_cancel);
        mTvName = (TextView) mRootView.findViewById(R.id.dialog_user_info_name);
        mTvUserId = (TextView) mRootView.findViewById(R.id.dialog_user_info_userid);
        mTvAddress = (TextView) mRootView.findViewById(R.id.dialog_user_info_address);
        mTvSignature = (TextView) mRootView.findViewById(R.id.dialog_user_info_signature);
        mTvStatNum = (TextView) mRootView.findViewById(R.id.dialog_user_info_star_num);
        mTvStatNum2 = (TextView) mRootView.findViewById(R.id.dialog_user_info_star_num2);
        mTvStatNum3 = (TextView) mRootView.findViewById(R.id.dialog_user_info_star_num3);
        mTvStatNum4 = (TextView) mRootView.findViewById(R.id.dialog_user_info_star_num4);
        mTvStat = (TextView) mRootView.findViewById(R.id.dialog_user_info_star);
        mTvChat = (TextView) mRootView.findViewById(R.id.dialog_user_info_prv_chat);
        mPhoto = (SimpleDraweeView) mRootView.findViewById(R.id.dialog_user_info_photo);
        mTabBottom = (LinearLayout) mRootView.findViewById(R.id.dialog_user_tab_bottom);

        mTvComplaint.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        mTvStat.setOnClickListener(this);
        mTvChat.setOnClickListener(this);
        mPhoto.setOnClickListener(this);

        initData();
    }

    public void showTabBottom(boolean isShow) {
        mTabBottom.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void initData() {
        mTvComplaint.setText(mContext.getString(mIsAdmin ? R.string.complaint : R.string.jubao));
        mTvName.setText(mUserInfo.getNickName());
        mTvUserId.setText("房间号：" + mUserInfo.getRoomId());
        mTvAddress.setText(mUserInfo.getCity());
        mTvSignature.setText(TextUtils.isEmpty(mUserInfo.getDescription()) ?
                mContext.getResources().getString(R.string.this_guy_is_too_lazy_what_did_not_leave)
                : mUserInfo.getDescription());
        mTvStat.setText(mUserInfo.getIsAttention() == 0 ?
                mContext.getString(R.string.user_dialog_star)
                : mContext.getString(R.string.is_star));
        mTvStatNum.setText(mUserInfo.getExpenditure());
        mTvStatNum2.setText(mUserInfo.getIncome());
        mTvStatNum3.setText(UiUtils.NumTransform(Integer.parseInt(mUserInfo.getFollowees_cnt())));
        mTvStatNum4.setText(UiUtils.NumTransform(Integer.parseInt(mUserInfo.getFollowers_cnt())));
        mPhoto.setImageURI(Uri.parse(mUserInfo.getAvatar()));
    }

    public void setStatUser(boolean stat) {
        if (stat) {
            mTvStat.setText(mContext.getString(R.string.user_dialog_star));
            mUserInfo.setIsAttention(1);
        } else {
            mTvStat.setText(mContext.getString(R.string.is_star));
            mUserInfo.setIsAttention(0);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_user_info_cancel) {
            dismiss();
        } else if (i == R.id.dialog_user_info_complaint) {
            mListener.onComplaint(mUserInfo);
            dismiss();
        } else if (i == R.id.dialog_user_info_star) {
            mListener.onStar(mUserInfo, mUserInfo.getIsAttention() == 1);
        } else if (i == R.id.dialog_user_info_prv_chat) {
            mListener.onPrvChat(mUserInfo);
            dismiss();
        } else if (i == R.id.dialog_user_info_photo) {
            mListener.onUserPhoto(mUserInfo);
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

    public interface UserInfoListener {
        void onComplaint(UserInfo userInfo);

        void onStar(UserInfo userInfo, boolean stat);

        void onPrvChat(UserInfo userInfo);

        void onUserPhoto(UserInfo userInfo);
    }
}
