package com.ttt.liveroom.widget.report;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.magiepooh.recycleritemdecoration.ItemDecorations;
import com.ttt.liveroom.R;
import com.ttt.liveroom.bean.room.ComplainOptionBean;

import java.util.List;

public class ReportPopup extends PopupWindow implements View.OnTouchListener,
        View.OnClickListener, View.OnKeyListener {
    private static final String TAG = "ReportPopup";

    private String mUserId;

    private Context mContext;
    private View mRootView;
    private ReportAdapter.ReportClickListener mListener;
    private List<ComplainOptionBean.ListBean> mList;
    private RecyclerView mRecyclerView;
    private TextView mTvCancel;

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public ReportPopup(Context context, String userId, List<ComplainOptionBean.ListBean> list,
                       ReportAdapter.ReportClickListener listener) {
        this.mContext = context;
        this.mUserId = userId;
        this.mListener = listener;
        this.mList = list;
        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = inflater.inflate(R.layout.popup_report, null);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
//        ColorDrawable dw = new ColorDrawable(0x00000000);
//        this.setBackgroundDrawable(dw);
        //设置动画效果
        this.setAnimationStyle(R.style.MyPopupWindow_anim_style);
        setContentView(mRootView);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_report);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ReportAdapter adapter = new ReportAdapter(mContext, mUserId, mList, mListener);
        mRecyclerView.addItemDecoration(ItemDecorations.vertical(mContext)
                .type(0, R.drawable.divider_decoration_transparent_h1)
                .create());
        mRecyclerView.setAdapter(adapter);
        mTvCancel = (TextView) mRootView.findViewById(R.id.tv_cancel);

        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            dismiss();
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
}
