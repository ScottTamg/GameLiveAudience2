package com.ttt.liveroom.room.pubmsg;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ttt.liveroom.R;
import com.ttt.liveroom.base.recycler.SimpleRecyclerHolder;
import com.ttt.liveroom.bean.websocket.LightHeartMsg;
import com.ttt.liveroom.bean.websocket.RoomPublicMsg;
import com.ttt.liveroom.bean.websocket.SendGiftMsg;
import com.ttt.liveroom.bean.websocket.SystemMsg;
import com.ttt.liveroom.bean.websocket.SystemWelcome;
import com.ttt.liveroom.bean.websocket.UserPublicMsg;
import com.ttt.liveroom.util.L;

/**
 * @author Muyangmin
 * @since 1.0.0
 * Recycle的viewholder
 */
public class PublicChatHolder extends SimpleRecyclerHolder<RoomPublicMsg> {

    private TextView tvContent;

    public PublicChatHolder(View itemView) {
        super(itemView);
        tvContent = (TextView) itemView.findViewById(R.id.item_public_chat_tv);
    }

    @Override
    public void displayData(Context context, RoomPublicMsg data) {
        CharSequence sequence = null;
        L.i("Sequence", "data class=" + data.getClass().getSimpleName());
        if (data instanceof UserPublicMsg) {
            sequence = buildUserChatSequence(context, (UserPublicMsg) data);
        } else if (data instanceof LightHeartMsg) {
            sequence = buildLightHeartSequence(context, (LightHeartMsg) data);
        } else if (data instanceof SendGiftMsg) {
            sequence = buildGiftSequence(context, (SendGiftMsg) data);
        } else if (data instanceof SystemMsg) {
            sequence = buildSysChatSequence(context, (SystemMsg) data);
        } else if (data instanceof SystemWelcome) {
            sequence = buildWelcomeChatSequence(context, (SystemWelcome) data);
        } else {
            L.e("Sequence", "Unsupported type!");
        }
        if (!TextUtils.isEmpty(sequence)) {
            tvContent.setText(sequence);
        }
    }


    private CharSequence buildUserChatSequence(Context context, UserPublicMsg msg) {
        MsgUtils utils = MsgUtils.getInstance(context);
        CharSequence level = utils.buildLevel(context, 1);
        //if level is not legal
        if (level == null) {
            level = "";
        }
        return TextUtils.concat(
                level,
                utils.buildimUserName(msg.getData().getNickName()),
                utils.buildPublicMsgContent(msg.getMessage()));
    }

    //    系统警告信息显示
    private CharSequence buildSysChatSequence(Context context, SystemMsg msg) {
        MsgUtils utils = MsgUtils.getInstance(context);
//        CharSequence level = utils.buildLevel(msg.getLevel());
//        //if level is not legal
//        if (level == null) {
//            level = "";
//        }
//      这里可能会用到
        return TextUtils.concat(
                utils.buildPublicSysMsgContent(msg.getContent()));
    }

    //    系统欢迎信息显示
    private CharSequence buildWelcomeChatSequence(Context context, SystemWelcome msg) {
        MsgUtils utils = MsgUtils.getInstance(context);
        CharSequence level = utils.buildLevel(context, msg.getData().getLevel());
        //if level is not legal
        if (level == null) {
            level = "";
        }
        return TextUtils.concat(
                utils.buildPublicSysMsgWelcome(""),
                level,
                utils.buildPublicSysMsgName(msg.getData().getNickName()),
                utils.buildPublicSysMsgWelcome("  来了"));
    }


    private CharSequence buildLightHeartSequence(Context context, LightHeartMsg msg) {
        MsgUtils utils = MsgUtils.getInstance(context);
        CharSequence level = utils.buildLevel(context, msg.getLevel());
        //if level is not legal
        if (level == null) {
            level = "";
        }
        return TextUtils.concat(
                level,
                utils.buildUserName(msg.getFromClientName()),
                utils.buildPublicMsgContent("我点亮了"),
                utils.buildHeart(context, msg.getColor())
        );
    }

    private CharSequence buildGiftSequence(Context context, SendGiftMsg msg) {
        MsgUtils utils = MsgUtils.getInstance(context);
        CharSequence level = utils.buildLevel(context, Integer.parseInt(msg.getData().getLevel()));
        //if level is not legal
        if (level == null) {
            level = "";
        }
        return TextUtils.concat(
                level,
                utils.buildUserName(msg.getData().getNickName()),
                utils.buildPublicMsgContent("送出"+msg.getData().getNum()+"个"),
                msg.getData().getGiftName()
        );
    }

}
