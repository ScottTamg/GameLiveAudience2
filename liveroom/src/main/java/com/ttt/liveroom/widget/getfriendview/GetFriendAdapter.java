package com.ttt.liveroom.widget.getfriendview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ttt.liveroom.R;
import com.ttt.liveroom.bean.GetFriendBean;
import com.ttt.liveroom.net.NetManager;
import com.ttt.liveroom.util.FrescoUtil;

import java.util.List;


/**
 * Created by 刘景 on 2017/06/11.
 */

public class GetFriendAdapter extends RecyclerView.Adapter<GetFriendAdapter.Holder> {

    private List<GetFriendBean> list;
    private Context context;
    private LayoutInflater mInflater;
    public Holder viewHolder;

    public GetFriendAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public GetFriendAdapter(List<GetFriendBean> list, Context context) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<GetFriendBean> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.getfriend_item_layout, parent, false);
        viewHolder = new Holder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        if (!TextUtils.isEmpty(list.get(position).getF_pic())) {
            FrescoUtil.frescoResize(NetManager.wrapPathToUri(list.get(position).getF_pic()), (int) context.getResources().getDimension(R.dimen.item_gift_icon_size),
                    (int) context.getResources().getDimension(R.dimen.item_gift_icon_size), holder.logo);
        }
        holder.name.setText(list.get(position).getF_name());
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
//          这里item设置我们系统的点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    context.startActivity(SimpleWebViewActivity.createIntent(context,list.get(position).getF_url(),""));
                    //                    然后这里调用接口里的点击抽象方法，并传入 View类型的参数 和当前点击的是哪个
                    mOnItemClickLitener.onItemClick(list.get(position).getF_url());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    //    ========================1=========================点击监听 需要我们自己来写，但是依然需要用到系统的点击回调
//    监听依旧需要一个回调接口
    public interface OnItemClickLitener {
        void onItemClick(String url);
    }

    private OnItemClickLitener mOnItemClickLitener;

    //   提供一个公开的方法用来进行回调，参数就是我们的回调接口，让需要调用的Activity来实现这个接口里的俩个抽象方法
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    //    内部类holder
    public class Holder extends RecyclerView.ViewHolder {
        public TextView name;
        public SimpleDraweeView logo;

        public Holder(View itemView) {
            super(itemView);
            logo = (SimpleDraweeView) itemView.findViewById(R.id.friend_logo);
            name = (TextView) itemView.findViewById(R.id.friend_name);
        }
    }
}
