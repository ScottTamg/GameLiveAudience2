package com.ttt.liveroom.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.ttt.liveroom.R;
import com.ttt.liveroom.net.Constants;
import com.ttt.liveroom.util.CustomToast;
import com.ttt.liveroom.util.L;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by Iverson on 2016/12/23 下午10:16
 * 此类用于：Fragment的基类
 *
 * @author Iverson
 */
@SuppressWarnings("unused")
public abstract class BaseFragment extends Fragment implements BaseUiInterface {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private ProgressDialog mProgressDialog;
    protected FragmentActivity mContext;
    protected Unbinder mUnbinder;
    protected float mDensity;
    protected int mDensityDpi;
    protected int mWidth;
    protected int mAvatarSize;
    protected View mRootView;

    /**
     * 初始化title的布局
     */
    protected ImageButton mBtTitleLeft;
    protected ImageButton mBtTitleRight;
    protected TextView mTvTitleName;
    protected TextView mTvTitleLeft;
    protected TextView mTvTitleRight;

    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;
        mDensityDpi = dm.densityDpi;
        mWidth = dm.widthPixels;
        mAvatarSize = (int) (50 * mDensity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), null);
        mUnbinder = ButterKnife.bind(this, mRootView);
        initViews(mRootView);
        return mRootView;
    }

    @SuppressWarnings("unchecked")
    protected final <T extends View> T $(@NonNull View view, @IdRes int id) {
        return (T) (view.findViewById(id));
    }

    /**s
     * 指定销毁activity
     */
    protected void finishAllActivity() {
//        for (Activity activity : MainApplication.getInstance().activities) {
//            activity.finish();
//        }
    }

    protected abstract int getLayoutId();

    protected abstract void initViews(View view);

    @Override
    public void showDataException(String msg) {
        toastShort(msg);
    }

    @Override
    public void showNetworkException() {
        toastShort(R.string.msg_network_error);
    }

    @Override
    public void showUnknownException() {
        toastShort(R.string.msg_unknown_error);
    }

    @Override
    public void showLoadingComplete() {
        //Empty implementation
        dismissLoadingDialog();
    }

    @Override
    public Dialog showLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.Please_wait), true, false);
        return mProgressDialog;
    }

    @Override
    public void showErrorMesDialog(String msg) {

    }

    @Override
    public void dismissLoadingDialog() {
        if (mProgressDialog == null || (!mProgressDialog.isShowing())) {
            return;
        }
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    protected void toastShort(@StringRes int msg) {
        if (getActivity() != null) {
            CustomToast.makeCustomText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    protected void toastShort(@NonNull String msg) {
        if (getActivity() != null) {
            CustomToast.makeCustomText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    protected void toastLong(@NonNull String msg) {
        if (getActivity() != null) {
            CustomToast.makeCustomText(getActivity(), msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        //注销消息接收
        L.v(LOG_TAG, "----- onDestroy -----");
        //注销消息接收
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    /**
     * 左边的布局ImageButton
     *
     * @param t
     * @param <T>
     */
    protected <T> void setBtGlobalLeft(T t) {
        if (t instanceof Integer) {
            mBtTitleLeft.setImageResource((Integer) t);
        } else if (t instanceof Drawable) {
            mBtTitleLeft.setImageDrawable((Drawable) t);
        }
        mBtTitleLeft.setVisibility(View.VISIBLE);
    }

    /**
     * 右边的布局ImageButton
     *
     * @param t
     * @param <T>
     */
    protected <T> void setBtGlobalRight(T t) {
        if (t instanceof Integer) {
            mBtTitleRight.setImageResource((Integer) t);
        } else if (t instanceof Drawable) {
            mBtTitleRight.setImageDrawable((Drawable) t);
        }
        mBtTitleRight.setVisibility(View.VISIBLE);
    }

    /**
     * 左边的布局TextView
     *
     * @param t
     * @param <T>
     */
    protected <T> void setTvGlobalLeft(T t) {
        if (t instanceof Integer) {
            mTvTitleLeft.setText((Integer) t);
        } else if (t instanceof String) {
            mTvTitleLeft.setText((String) t);
        }
        mTvTitleLeft.setVisibility(View.VISIBLE);
    }

    /**
     * 右边的布局TextView
     *
     * @param t
     * @param <T>
     */
    protected <T> void setTvGlobalRight(T t) {
        if (t instanceof Integer) {
            mTvTitleRight.setText((Integer) t);
        } else if (t instanceof String) {
            mTvTitleRight.setText((String) t);
        }
        mTvTitleRight.setVisibility(View.VISIBLE);
    }


    /**
     * 中间的布局title的TextView
     *
     * @param t
     * @param <T>
     */
    protected <T> void setTvGlobalTitleName(T t) {
        if (t instanceof Integer) {
            mTvTitleName.setText((Integer) t);
        } else if (t instanceof String) {
            mTvTitleName.setText((String) t);
        }
        mTvTitleName.setVisibility(View.VISIBLE);
    }

    /**
     * 使用默认的throttle设置来注册点击事件。
     *
     * @param view    要注册的View
     * @param action1 点击后执行的事件
     */
    protected void subscribeClick(View view, Action1<Void> action1) {
        RxView.clicks(view)
                .throttleFirst(Constants.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(action1);
    }

    public abstract void onSucceed(int what, Response<Bitmap> response);
}
