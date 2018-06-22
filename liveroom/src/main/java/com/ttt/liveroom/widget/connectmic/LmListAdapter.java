package com.ttt.liveroom.widget.connectmic;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding.view.RxView;
import com.ttt.liveroom.R;
import com.ttt.liveroom.bean.websocket.ResponseMicBean;
import com.ttt.liveroom.net.Constants;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;


public class LmListAdapter extends RecyclerView.Adapter<LmListAdapter.ViewHolder> {
    private static final int LM_USER_MAX_NUM = 2;
    private static final String AGREE_LM = "2";
    public static final String CLOSE_LM = "4";

    private Context mContext;
    private List<ResponseMicBean.DataBean> mList;
    private LmClickListener mListener;
    private int mLmUserCount = 0;

    public LmListAdapter(Context context, List<ResponseMicBean.DataBean> list,
                         LmClickListener listener) {
        mContext = context;
        mList = list;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_lm, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mAvatar.setImageURI(Uri.parse(mList.get(position).getAvatar()));
        holder.mIntroduction.setText("申请连麦");
        holder.mNickName.setText(mList.get(position).getNickName());
        if (mLmUserCount >= LM_USER_MAX_NUM) {
            holder.mTvDone.setVisibility(View.GONE);
            if (AGREE_LM.equals(mList.get(position).getType())) {
                holder.mDisconnect.setText("连麦中");
                holder.mDisconnect.setEnabled(false);
            } else {
                holder.mDisconnect.setText("拒绝");
                holder.mDisconnect.setEnabled(true);
            }
        } else {
            holder.mTvDone.setText("同意");
            if (AGREE_LM.equals(mList.get(position).getType())) {
                holder.mDisconnect.setText("连麦中");
                holder.mDisconnect.setEnabled(false);
                holder.mTvDone.setVisibility(View.GONE);
            } else {
                holder.mTvDone.setVisibility(View.VISIBLE);
                holder.mDisconnect.setText("拒绝");
                holder.mDisconnect.setEnabled(true);
            }
        }

        RxView.clicks(holder.mTvDone)
                .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mList.get(position).setType(AGREE_LM);
                        notifyItemMoved(position, 0);
                        notifyDataSetChanged();
                        mLmUserCount++;
                        mListener.onDoneClick(mList.get(position));
                    }
                });
        RxView.clicks(holder.mDisconnect)
                .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ResponseMicBean.DataBean dataBean = mList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mList.size());
                        mListener.onDisConnectClick(dataBean);
                    }
                });
    }

    public void addAllData(List<ResponseMicBean.DataBean> list, int lmNum) {
        this.mList = list;
        mLmUserCount = lmNum;
        notifyDataSetChanged();
    }

    public void addData(ResponseMicBean.DataBean dataBean) {
        mList.add(dataBean);
        notifyDataSetChanged();
    }

    public void removeData(String userId, int lmNum) {
        int position = -1;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getUserId().equals(userId)) {
                position = i;
                break;
            }
        }
        if (position > -1) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
        mLmUserCount = lmNum;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView mAvatar;
        TextView mNickName;
        TextView mIntroduction;
        TextView mDisconnect;
        TextView mTvDone;

        public ViewHolder(View itemView) {
            super(itemView);
            mAvatar = (SimpleDraweeView) itemView.findViewById(R.id.avatar_img);
            mNickName = (TextView) itemView.findViewById(R.id.nickName_tv);
            mIntroduction = (TextView) itemView.findViewById(R.id.introduction_tv);
            mDisconnect = (TextView) itemView.findViewById(R.id.disconnect);
            mTvDone = (TextView) itemView.findViewById(R.id.tv_done);
        }
    }

    public interface LmClickListener {
        void onDisConnectClick(ResponseMicBean.DataBean dataBean);

        void onDoneClick(ResponseMicBean.DataBean dataBean);
    }
}
