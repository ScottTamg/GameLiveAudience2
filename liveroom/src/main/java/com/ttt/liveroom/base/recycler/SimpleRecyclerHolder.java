package com.ttt.liveroom.base.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class SimpleRecyclerHolder<DataType> extends RecyclerView.ViewHolder {

    public SimpleRecyclerHolder(View itemView) {
        super(itemView);
    }

    public abstract void displayData(Context context, DataType data);
}