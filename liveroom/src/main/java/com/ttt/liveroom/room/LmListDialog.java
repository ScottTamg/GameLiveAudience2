package com.ttt.liveroom.room;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.magiepooh.recycleritemdecoration.ItemDecorations;
import com.ttt.liveroom.R;
import com.ttt.liveroom.base.recycler.SimpleRecyclerHolder;
import com.ttt.liveroom.bean.websocket.ResponseMicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrliu on 2018/1/27.
 * 此类用于:此类用于连麦用户列表
 */

public class LmListDialog extends Dialog implements View.OnClickListener {

    private TextView mEmpty;
    private RecyclerView mRecyclerView;
    private TextView mCancle;
    private TextView mComplete;
    private Context context;
    private List<ResponseMicBean.DataBean> mresponse;
    private LmAdapter adapter;

    private DialogListener listener;
    private ArrayList<String> mHadLmIdList = new ArrayList<>();

    public LmListDialog(@NonNull Context context, List<ResponseMicBean.DataBean> response, ArrayList<String> hadLmIdList) {
        super(context, R.style.DialogStyle);
        this.context = context;
        this.mresponse = response;
        this.mHadLmIdList = hadLmIdList;
        this.setContentView(R.layout.dialog_lm_list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initLayout();
        init();
    }

    private void initLayout() {

        //获取当前Activity所在的窗体
        Window dialogWindow = this.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.y = 0;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }

    private void findView() {
        mEmpty = (TextView) findViewById(R.id.tv_empty);
        mRecyclerView = (RecyclerView) findViewById(R.id.lm_recycler);
        mCancle = (TextView) findViewById(R.id.tv_cancle);
        mComplete = (TextView) findViewById(R.id.tv_complete);

    }

    private void init() {
        //dislmList.clear();
        mCancle.setOnClickListener(this);
        mComplete.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(ItemDecorations.vertical(context)
                .type(0, R.drawable.divider_decoration_transparent_h1)
                .create());

        if (mresponse == null || mresponse.size() == 0) {
            adapter = new LmAdapter();
        } else {
            adapter = new LmAdapter(mresponse);

        }
        mRecyclerView.setAdapter(adapter);
    }

    private class LmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<ResponseMicBean.DataBean> userList = new ArrayList<>();

        public LmAdapter(List<ResponseMicBean.DataBean> userList) {
            this.userList = userList;
            notifyDataSetChanged();
        }

        public LmAdapter() {
        }

        public void remove(int position) {
            notifyItemRemoved(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.recyclerview_item_lm, parent, false);
            return new LmHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (mHadLmIdList.size() >= 2) {
                ((LmHolder) holder).mDisconnect.setVisibility(View.VISIBLE);
                ((LmHolder) holder).mDisconnect.setText("拒绝");
                ((LmHolder) holder).mTvDone.setVisibility(View.GONE);
                for (int i = 0; i < mHadLmIdList.size(); i++) {
                    if (userList.get(position).getUserId().equals(mHadLmIdList.get(i))) {
                        ((LmHolder) holder).mDisconnect.setText("连麦中");
                        break;
                    }
                }
            } else {
                ((LmHolder) holder).mDisconnect.setVisibility(View.VISIBLE);
                ((LmHolder) holder).mDisconnect.setText("拒绝");
                ((LmHolder) holder).mTvDone.setVisibility(View.VISIBLE);
                ((LmHolder) holder).mTvDone.setText("接受");
                if (mHadLmIdList != null && mHadLmIdList.size() > 0) {
                    for (int i = 0; i < mHadLmIdList.size(); i++) {
                        if (userList.get(position).getUserId().equals(mHadLmIdList.get(i))) {
                            ((LmHolder) holder).mDisconnect.setText("连麦中");
                            ((LmHolder) holder).mTvDone.setVisibility(View.GONE);
                            break;
                        }
                    }
                }
            }

            ((LmHolder) holder).displayData(context, userList.get(position));
        }

        @Override
        public int getItemCount() {
            if (userList == null || userList.size() == 0) {
                mEmpty.setVisibility(View.VISIBLE);
            } else {
                mEmpty.setVisibility(View.GONE);
            }
            return userList.size();
        }
    }

    private class LmHolder extends SimpleRecyclerHolder<ResponseMicBean.DataBean> {

        private SimpleDraweeView mAvater;
        private TextView mNickName, mIntroduction, mDisconnect;
        private TextView mTvDone;
        private LinearLayout mLmListll;
        private int count = 0;

        public LmHolder(View itemView) {
            super(itemView);
            mLmListll = (LinearLayout) itemView.findViewById(R.id.lm_list_ll);
            mAvater = (SimpleDraweeView) itemView.findViewById(R.id.avatar_img);
            mNickName = (TextView) itemView.findViewById(R.id.nickName_tv);
            mIntroduction = (TextView) itemView.findViewById(R.id.introduction_tv);
            mDisconnect = (TextView) itemView.findViewById(R.id.disconnect);
            mTvDone = (TextView) itemView.findViewById(R.id.tv_done);
        }

        @Override
        public void displayData(final Context context, final ResponseMicBean.DataBean data) {
            if (!TextUtils.isEmpty(data.getAvatar())) {
                mAvater.setImageURI(Uri.parse(data.getAvatar()));
            }
            mIntroduction.setText("申请连麦");
            if (!TextUtils.isEmpty(data.getNickName())) {
                mNickName.setText(data.getNickName());
            }
            mTvDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((count + mHadLmIdList.size() > 2)) {
                        Toast.makeText(context, "最多选择二个人", Toast.LENGTH_SHORT).show();
                    } else {
                        mDisconnect.setVisibility(View.VISIBLE);
                        mDisconnect.setEnabled(false);
                        mDisconnect.setText("连麦中");
                        mTvDone.setVisibility(View.GONE);
                        count++;
                        listener.onSelecorClick(LmListDialog.this, data.getUserId(), 1);
                    }
                }
            });

            mDisconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHadLmIdList != null && mHadLmIdList.size() > 0) {
                        for (int i = 0; i < mHadLmIdList.size(); i++) {
                            if (data.getUserId().equals(mHadLmIdList.get(i))) {
                                listener.disConnectList(LmListDialog.this, data.getUserId());
                                break;
                            } else {
                                listener.onSelecorClick(LmListDialog.this, data.getUserId(), 0);
                            }
                        }
                    } else {
                        listener.onSelecorClick(LmListDialog.this, data.getUserId(), 0);
                    }
                    //移除当前条目
                    adapter.remove(getLayoutPosition());
                    mresponse.remove(data);
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
//取消或者完成
        if (v == mCancle || v == mComplete) {
            if (listener != null) {
                listener.onCancelClick(this);
            }
        }
    }

    public interface DialogListener {

        void onCancelClick(LmListDialog dialog);//返回点击回调

        //void onCompleteClick(LmListDialog dialog,ArrayList<String> disConncetList);//点击完成回调

        void onSelecorClick(LmListDialog dialog, String id, int type);//选择连麦的点击回调

        void disConnectList(LmListDialog dialog, String disConnectId);//断开连麦的点击回调
    }

    public void setItemClickListener(DialogListener listener) {
        this.listener = listener;
    }
}
