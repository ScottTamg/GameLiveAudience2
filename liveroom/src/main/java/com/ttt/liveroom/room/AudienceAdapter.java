package com.ttt.liveroom.room;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ttt.liveroom.R;
import com.ttt.liveroom.base.DataManager;
import com.ttt.liveroom.base.recycler.SimpleRecyclerAdapter;
import com.ttt.liveroom.base.recycler.SimpleRecyclerHolder;
import com.ttt.liveroom.bean.LoginInfo;
import com.ttt.liveroom.bean.UserInfo;
import com.ttt.liveroom.bean.websocket.SystemWelcome;
import com.ttt.liveroom.net.NetManager;

import java.util.List;

/**
 * @author txw
 */
public class AudienceAdapter extends SimpleRecyclerAdapter<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList, AudienceAdapter.AudienceHolder> {

    /**
     * 创建一个布尔值判断只对userPublicMsg进行一次设置
     */
    private boolean initUserPublicMsg = false;
    private int[] mStartIcon;
    private OnItemClickListener mOnItemClickListener;

    public AudienceAdapter(Context context,
                           List<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> audienceInfoList,
                           int[] startIcon, OnItemClickListener listener) {
        super(context, audienceInfoList);
        this.mStartIcon = startIcon;
        this.mOnItemClickListener = listener;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_online_audience;
    }

    @Override
    protected AudienceHolder createHolder(View view) {
        return new AudienceHolder(view);
    }

    class AudienceHolder extends SimpleRecyclerHolder<SystemWelcome.SystemWelcomeData.SystemWelcomeDataList> {

        private SimpleDraweeView draweeAvatar;
        private ImageView icon;
        private UserInfo mInfo;

        public AudienceHolder(View itemView) {
            super(itemView);
            draweeAvatar = (SimpleDraweeView) itemView.findViewById(R.id.img_user_avatar);
            icon = (ImageView) itemView.findViewById(R.id.img_user_star_type);
        }

        @Override
        public void displayData(Context context, final SystemWelcome.SystemWelcomeData.SystemWelcomeDataList data) {
//            判断当前用户是哪个观众，然后设置一下userPublicMsg的信息
            LoginInfo loginInfo = DataManager.getInstance().getLoginInfo();
            if (loginInfo != null && loginInfo.getUserId().equals(data.getUserId()) && !initUserPublicMsg) {
                initUserPublicMsg = true;
            }
            String avatar = data.getAvatar();
            mInfo = new UserInfo();
            mInfo.setId(data.getUserId());
            mInfo.setAvatar(data.getAvatar());
            mInfo.setNickName(data.getNickName());
            mInfo.setLevel(data.getLevel());
            if (!TextUtils.isEmpty(avatar)) {
                draweeAvatar.setImageURI(NetManager.wrapPathToUri(avatar));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(mInfo);
                }
            });
            icon.setImageResource(mStartIcon[(int) (Math.random() * 4)]);
        }
    }

    interface OnItemClickListener {
        /**
         * item 点击回调
         *
         * @param userInfo
         */
        void onItemClick(UserInfo userInfo);
    }
}