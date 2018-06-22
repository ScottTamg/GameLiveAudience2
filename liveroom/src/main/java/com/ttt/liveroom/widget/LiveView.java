package com.ttt.liveroom.widget;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ttt.liveroom.R;
import com.ttt.liveroom.util.PixelUtil;

/**
 * Created by Iverson on 2018/1/16 上午11:24
 * 此类用于：
 */

public class LiveView extends RelativeLayout {
    private boolean isFree = true;
    private long flagUserId;
    private Context mContext;
    private LiveViewListener mListener;
    private ImageView mIvClose;
    private TextView mTextView;

    public long getFlagUserId() {
        return flagUserId;
    }

    public void setFlagUserId(long flagUserId) {
        this.flagUserId = flagUserId;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
        if (free) {
            setVisibility(INVISIBLE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    public LiveViewListener getListener() {
        return mListener;
    }

    public void setListener(LiveViewListener listener) {
        mListener = listener;
    }

    public LiveView(Context context) {
        super(context);
        this.mContext = context;
//        init();
    }

    public LiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
//        init();
    }

    public LiveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
//        init();
    }

    private void initBg() {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(layoutParams);

        ImageView imageView = new ImageView(mContext);
        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_default_head));
        int size = PixelUtil.dp2px(mContext, 70);
        LinearLayout.LayoutParams imlp = new LinearLayout.LayoutParams(size, size);
        imlp.topMargin = PixelUtil.dp2px(mContext, 14);
        imlp.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(imlp);
        ll.addView(imageView);

        mTextView = new TextView(mContext);
        mTextView.setTextColor(mContext.getResources().getColor(R.color.white));
        mTextView.setTextSize(12);
        mTextView.setText(String.valueOf(flagUserId));
        LinearLayout.LayoutParams tvlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvlp.gravity = Gravity.CENTER_HORIZONTAL;
        mTextView.setLayoutParams(tvlp);
        ll.addView(mTextView);

        addView(ll);
    }

    private void init() {
        mIvClose = new ImageButton(mContext);
        int size = PixelUtil.dp2px(mContext, 20);
        LayoutParams layoutParams = new LayoutParams(size, size);
        layoutParams.topMargin = PixelUtil.dp2px(mContext, 10);
        layoutParams.rightMargin = PixelUtil.dp2px(mContext, 10);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mIvClose.setLayoutParams(layoutParams);
        mIvClose.setBackgroundColor(Color.parseColor("#00000000"));
        mIvClose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.live_close, null));
        mIvClose.setVisibility(INVISIBLE);
        addView(mIvClose);

        mIvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCloseClick(String.valueOf(flagUserId));
                }
            }
        });
    }

    public void showClose(boolean show) {
        initBg();
        if (show) {
            init();
        }
        if (mIvClose != null) {
            mIvClose.setVisibility(show ? VISIBLE : INVISIBLE);
        }
    }

    public interface LiveViewListener {
        void onCloseClick(String userId);
    }
}
