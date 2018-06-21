package com.ttt.liveroom.widget.connectmic;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.github.magiepooh.recycleritemdecoration.ItemDecorations;
import com.ttt.liveroom.R;
import com.ttt.liveroom.bean.websocket.ResponseMicBean;
import com.ttt.liveroom.util.PixelUtil;

import java.util.List;

/**
 * @author txw
 * @version 1.0
 * @date
 */
public class LmListPopup extends PopupWindow implements View.OnTouchListener,
        View.OnKeyListener {
    private static final String TAG = "LmListPopup";

    private Context mContext;
    private View mRootView;
    private LmListAdapter mAdapter;
    private LmListAdapter.LmClickListener mListener;
    private List<ResponseMicBean.DataBean> mList;
    private RecyclerView mRecyclerView;

    public LmListPopup(Context context, List<ResponseMicBean.DataBean> list,
                       LmListAdapter.LmClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mList = list;
        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = inflater.inflate(R.layout.popup_lm, null);
        this.setHeight(PixelUtil.dp2px(mContext, 280));
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
//        ColorDrawable dw = new ColorDrawable(0x00000000);
//        this.setBackgroundDrawable(dw);
        //设置动画效果
        this.setAnimationStyle(R.style.MyPopupWindow_anim_style);
        setContentView(mRootView);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_lm);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new LmListAdapter(mContext, mList, mListener);
        mRecyclerView.addItemDecoration(ItemDecorations.vertical(mContext)
                .type(0, R.drawable.divider_decoration_transparent_h1)
                .create());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void updateLmUserList(List<ResponseMicBean.DataBean> list, int lmNum) {
        if (mAdapter != null) {
            mAdapter.addAllData(list, lmNum);
        }
    }

    public void addLmUser(ResponseMicBean.DataBean dataBean) {
        mAdapter.addData(dataBean);
    }

    public void removeLmUser(String userId, int lmNum) {
        mAdapter.removeData(userId, lmNum);
    }

    //点击外部popup消失
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int height = mRootView.findViewById(R.id.rl_user_info).getTop();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (y < height) {
                dismiss();
            }
        }
        return true;
    }

    //点back键消失
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.isShowing()) {
            this.dismiss();
            return true;
        }
        return false;
    }
}
