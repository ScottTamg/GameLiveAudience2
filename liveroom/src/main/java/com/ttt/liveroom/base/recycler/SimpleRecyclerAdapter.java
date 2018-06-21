package com.ttt.liveroom.base.recycler;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class SimpleRecyclerAdapter<Data, Holder extends SimpleRecyclerHolder<Data>>
        extends RecyclerView.Adapter<Holder> {

    private List<Data> dataList;
    private Context context;

    public SimpleRecyclerAdapter(Context context, List<Data> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    public final void setDataList(List<Data> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public final void appendData(List<Data> appends) {
        this.dataList.addAll(appends);
        notifyDataSetChanged();
    }

    public List<Data> getDataList(){
        return this.dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType),
                parent, false);
        return createHolder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.displayData(context, dataList.get(position));
    }

    @LayoutRes
    protected abstract int getItemLayoutId(int viewType);

    @NonNull
    protected abstract Holder createHolder(View view);

}
