package com.ttt.liveroom.room;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ttt.liveroom.R;


/**
 * Created by 刘景 on 2017/06/11.
 */

public class ApplyLMDialog extends Dialog implements View.OnClickListener {

    private TextView mContent;
    private Button mCancel, mCommit;
    private String mContentStr;
    private MessageDialogListener listener;

    public ApplyLMDialog(Context context) {
        super(context, R.style.DialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_apply_lm);
        findView();
        init();
    }

    public void setContent(int strId) {
        mContentStr = this.getContext().getResources().getString(strId);
    }

    public void setContent(String str) {
        mContentStr = str;
    }

    public ApplyLMDialog hideCancelOption() {
        mCancel.setVisibility(View.GONE);
        return this;
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
        void onCancelClick(ApplyLMDialog dialog);

        void onCommitClick(ApplyLMDialog dialog);
    }
}
