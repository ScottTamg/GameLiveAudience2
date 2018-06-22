package com.ttt.liveroom.room.play;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ttt.liveroom.R;
import com.ttt.liveroom.base.BaseObserver;
import com.ttt.liveroom.base.BaseUiInterface;
import com.ttt.liveroom.base.recycler.SimpleRecyclerHolder;
import com.ttt.liveroom.bean.BaseResponse;
import com.ttt.liveroom.bean.room.NewestAuthorBean;
import com.ttt.liveroom.net.NetManager;
import com.ttt.liveroom.room.RoomApi;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Iverson on 2018/4/8 上午11:18
 * 此类用于：
 */

public class LiveListDialog extends Dialog implements BaseUiInterface {

    private LiveListDialogListener listener;
    private RecyclerView recyclerView;
    private Activity mContext;
    private String mUserId;

    public LiveListDialog(@NonNull Activity context, String userId) {
        super(context, R.style.DialogStyle);
        mContext = context;
        mUserId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_live_list);
        findView();
    }

    private void findView() {
        recyclerView = findViewById(R.id.rv_livelist);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));

        Subscription subscription = NetManager.getInstance().create(RoomApi.class)
                .getNewestByAuthor("1", "200", mUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<NewestAuthorBean>>(this) {
                    @Override
                    public void onSuccess(BaseResponse<NewestAuthorBean> response) {
                        HotAnchorAdapter anchorAdapter = new HotAnchorAdapter(response.getData().getList());
                        recyclerView.setAdapter(anchorAdapter);
                    }
                });
    }

    @Override
    public void showNetworkException() {
    }

    @Override
    public void showUnknownException() {
    }

    @Override
    public void showDataException(String msg) {
    }

    @Override
    public void showLoadingComplete() {
    }

    @Override
    public Dialog showLoadingDialog() {
        return null;
    }

    @Override
    public void dismissLoadingDialog() {
    }

    @Override
    public void showErrorMesDialog(String msg) {
    }


    public interface LiveListDialogListener {
        void onItemClick(NewestAuthorBean.ListBean bean);
    }

    public void setLivelistDialogListener(LiveListDialogListener listener) {
        this.listener = listener;
    }


    private class HotAnchorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<NewestAuthorBean.ListBean> dataList;

        public HotAnchorAdapter(List<NewestAuthorBean.ListBean> dataList) {
            this.dataList = dataList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_hot_anchor, parent, false);
            return new HotAnchorHolder(view);


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((HotAnchorHolder) holder).displayData(mContext, dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

    }

    private class HotAnchorHolder extends SimpleRecyclerHolder<NewestAuthorBean.ListBean> {

        private SimpleDraweeView drawAvatar;
        private TextView tvTitle;
        private TextView tvTopic;

        @SuppressWarnings("unused")
        public HotAnchorHolder(View itemView) {
            super(itemView);
            drawAvatar = (SimpleDraweeView) itemView.findViewById(R.id.sd_live_list_pic);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_live_title);
        }

        @Override
        public void displayData(Context context, final NewestAuthorBean.ListBean data) {
            if (TextUtils.isEmpty(data.getTitle())) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(data.getTitle());
            }
            if (data.getAvatar() != null) {
                if (data.getAvatar().startsWith("http")) {
                    drawAvatar.setImageURI(NetManager.wrapPathWx(data.getImgSrc()));
                } else {
                    drawAvatar.setImageURI(NetManager.wrapPathToUri(data.getImgSrc()));
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(data);
                    }
                }
            });
        }
    }
}
