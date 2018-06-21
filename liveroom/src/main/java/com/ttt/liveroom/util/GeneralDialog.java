package com.ttt.liveroom.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ttt.liveroom.R;


/**
 * Created by mrliu on 2018/5/18.
 * 此类用于: 错误通用的dialog
 */

public class GeneralDialog extends Dialog implements View.OnClickListener {

    private TextView mContent, mConfrim;
    private String mContentStr;

    public GeneralDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genaral_dialog);

        findView();
        init();
    }

    private void findView() {
        mContent = findViewById(R.id.msg);
        mConfrim = findViewById(R.id.confirm);
    }

    public void setContent(String str) {
        mContentStr = str;
    }

    private void init() {
        setCancelable(false);
        mContent.setText(mContentStr + "");
        mConfrim.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == mConfrim) {
            this.cancel();
        }
    }
}
