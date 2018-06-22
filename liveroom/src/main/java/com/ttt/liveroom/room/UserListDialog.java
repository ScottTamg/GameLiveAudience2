package com.ttt.liveroom.room;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.magiepooh.recycleritemdecoration.ItemDecorations;
import com.ttt.liveroom.R;
import com.ttt.liveroom.base.recycler.SimpleRecyclerAdapter;
import com.ttt.liveroom.base.recycler.SimpleRecyclerHolder;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.bean.websocket.SystemWelcome;
import com.ttt.liveroom.net.NetManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrliu on 2018/6/11.
 * 此类用于:用户列表
 */

public class UserListDialog extends Dialog {

    private List<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> dataList = new ArrayList<>();
    private Context context;
    private RecyclerView mRecyclerView;
    private TextView mLimtUserListCount;
    private boolean mIsAdmin;
    private DialogListener listener;

    public UserListDialog(@NonNull Context context, List<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> dataList, boolean isAndim) {
        super(context, R.style.DialogStyle);
        this.dataList = dataList;
        this.context = context;
        this.mIsAdmin = isAndim;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_user_list);
        initLayout();
        initView();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.user_list_rl);
        mLimtUserListCount = findViewById(R.id.limt_user_list_count);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mRecyclerView.addItemDecoration(ItemDecorations.vertical(context)
                .type(0, R.drawable.divider_decoration_transparent_h1)
                .create());
        UserListAdapter adapter = new UserListAdapter(context, dataList);
        mRecyclerView.setAdapter(adapter);

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
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.y = 0;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }

    private class UserListAdapter extends SimpleRecyclerAdapter<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList, UserListHolder> {
        private List<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> mDataLists = new ArrayList<>();

        public UserListAdapter(Context context, List list) {
            super(context, list);
            this.mDataLists = list;
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return R.layout.item_user_list;
        }

        @Override
        public int getItemCount() {
            if (mDataLists.size() == 100) {
                mLimtUserListCount.setVisibility(View.VISIBLE);
            } else {
                mLimtUserListCount.setVisibility(View.GONE);
            }
            return super.getItemCount();
        }

        @NonNull
        @Override
        protected UserListHolder createHolder(View view) {
            return new UserListHolder(view);
        }

    }

    private class UserListHolder extends SimpleRecyclerHolder<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> {

        private SimpleDraweeView mSimpleDraweeView;
        private TextView mNickName, complaint;

        public UserListHolder(View itemView) {
            super(itemView);
            mSimpleDraweeView = itemView.findViewById(R.id.user_list_item_sdv);
            mNickName = itemView.findViewById(R.id.user_list_item_des);
            complaint = itemView.findViewById(R.id.user_list_item_complaint_tv);
        }

        @Override
        public void displayData(Context context, SystemWelcome.SystemWelcomeData.SystemWelcomeDataList data) {
            mNickName.setText(data.getNickName());
            mSimpleDraweeView.setImageURI(NetManager.wrapPathToUri(data.getAvatar()));
            if (mIsAdmin) {
                complaint.setVisibility(View.VISIBLE);
                final UserInfo userInfo = new UserInfo();
                userInfo.setAvatar(data.getAvatar());
                userInfo.setId(data.getUserId());
                userInfo.setNickName(data.getNickName());
                complaint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onComplaint(UserListDialog.this, userInfo);
                    }
                });
            } else {
                complaint.setVisibility(View.GONE);
            }
        }
    }

    public interface DialogListener {
        void onComplaint(UserListDialog dialog, UserInfo userInfo);
    }

    public void setItemClickListener(DialogListener listener) {
        this.listener = listener;
    }

}
