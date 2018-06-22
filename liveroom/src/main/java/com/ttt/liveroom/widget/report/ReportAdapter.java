package com.ttt.liveroom.widget.report;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.ttt.liveroom.R;
import com.ttt.liveroom.bean.room.ComplainOptionBean;
import com.ttt.liveroom.net.Constants;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private Context mContext;
    private List<ComplainOptionBean.ListBean> mList;
    private ReportClickListener mListener;
    private String mUserId;

    public ReportAdapter(Context context, String userId, List<ComplainOptionBean.ListBean> list,
                         ReportClickListener listener) {
        mContext = context;
        mList = list;
        mListener = listener;
        mUserId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_report, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvReportItem.setText(mList.get(position).getContent());
        RxView.clicks(holder.tvReportItem)
                .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mListener.onReportItemClick(mUserId, mList.get(position).getContent());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReportItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvReportItem = (TextView) itemView.findViewById(R.id.tv_report_item);
        }
    }

    public interface ReportClickListener {
        void onReportItemClick(String reportId, String content);
    }
}
