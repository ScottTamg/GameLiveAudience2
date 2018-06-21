package com.ttt.liveroom.room;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ttt.liveroom.R;
import com.ttt.liveroom.base.BaseObserver;
import com.ttt.liveroom.base.DataManager;
import com.ttt.liveroom.bean.BaseResponse;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.net.NetManager;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 刘景 on 2017/06/11.
 */

public class ResponseLMDialog extends Dialog implements View.OnClickListener {

    private RoomActivity activity;
    private TextView mContent;
    private Button mCancel, mCommit;
    private String mContentStr;
    private MessageDialogListener listener;

    private String mUserId = "";
    private Subscription subscription;

    private SimpleDraweeView mRequestImg;
    private TextView mRequestNickname, mRequestUserId;

    public ResponseLMDialog(RoomActivity context, String userId) {
        super(context, R.style.DialogStyle);
        activity = context;
        mUserId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_response_lm);
        findView();
        init();
    }

    public void setContent(int strId) {
        mContentStr = this.getContext().getResources().getString(strId);
    }

    public void setContent(String str) {
        mContentStr = str;
    }


    public void setMessageDialogListener(MessageDialogListener listener) {
        this.listener = listener;
    }

    private void init() {
        mContent.setText(mContentStr + "");
        mCancel.setOnClickListener(this);
        mCommit.setOnClickListener(this);
    }

    private void findView() {
        mContent = (TextView) findViewById(R.id.dialog_message_content);
        mCancel = (Button) findViewById(R.id.dialog_message_cancel);
        mCommit = (Button) findViewById(R.id.dialog_message_commit);

        mRequestImg = (SimpleDraweeView) findViewById(R.id.dialog_response_img);
        mRequestNickname = (TextView) findViewById(R.id.dialog_response_nickname);
        mRequestUserId = (TextView) findViewById(R.id.dialog_response_userid);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (subscription != null) {
                    subscription.unsubscribe();
                }
            }
        });
        requestUserInfo(mUserId, DataManager.getInstance().getLoginInfo().getUserId());
    }

    /**
     * 获取请求连麦的个人信息
     *
     * @param mUserId
     */
    private void requestUserInfo(final String mUserId, String observerUserId) {
        NetManager.getInstance().create(RoomApi.class).getUserInfo(mUserId, observerUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<UserInfo>>(activity) {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> response) {
                        if (response.getData().getAvatar() != null) {
                            mRequestImg.setImageURI(NetManager.wrapPathToUri(response.getData().getAvatar()));
                        }
                        mRequestNickname.setText(TextUtils.isEmpty(response.getData().getUserName()) ? "昵称" : response.getData().getUserName());
                        mRequestUserId.setText("ID: " + (TextUtils.isEmpty(mUserId) ? "" : "" + mUserId));
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == mCancel) {
            if (listener != null) {
                listener.onCancelClick(this);
            }
        } else {
            if (listener != null) {
                listener.onCommitClick(this);
            }
        }
    }

    public interface MessageDialogListener {
        void onCancelClick(ResponseLMDialog dialog);

        void onCommitClick(ResponseLMDialog dialog);
    }
}
