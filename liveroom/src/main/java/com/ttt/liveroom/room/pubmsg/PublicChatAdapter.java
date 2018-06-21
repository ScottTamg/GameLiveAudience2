package com.ttt.liveroom.room.pubmsg;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.ttt.liveroom.R;
import com.ttt.liveroom.base.recycler.SimpleRecyclerAdapter;
import com.ttt.liveroom.bean.websocket.RoomPublicMsg;

import java.util.List;

/**
 * Created by 刘景 on 2017/06/06.
 */

public class PublicChatAdapter extends SimpleRecyclerAdapter<RoomPublicMsg, PublicChatHolder> {

    //    item的点击监听
    private OnItemClickListener mListener;

    //   item的点击事件
    public interface OnItemClickListener {
        void onItemClick(int position, RoomPublicMsg data);
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public PublicChatAdapter(Context context, List<RoomPublicMsg> roomPublicMsgs) {
        super(context, roomPublicMsgs);
    }

    public void appendData(@NonNull RoomPublicMsg data) {
//        for (int i = 0; i < getDataList().size(); i++) {
//            Log.i("listsss", getDataList().get(i).toString());
//        }
        getDataList().add(data);
        notifyDataSetChanged();
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_room_public_chat;
    }

    @NonNull
    @Override
    protected PublicChatHolder createHolder(View view) {
        return new PublicChatHolder(view);
    }

    @Override
    public void onBindViewHolder(PublicChatHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (mListener == null) {
            return;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position, getDataList().get(position));
            }
        });
    }
}
