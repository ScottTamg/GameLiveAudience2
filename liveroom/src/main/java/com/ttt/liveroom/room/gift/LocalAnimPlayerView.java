package com.ttt.liveroom.room.gift;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ttt.liveroom.BuildConfig;
import com.ttt.liveroom.R;
import com.ttt.liveroom.bean.gift.SendGiftAction;
import com.ttt.liveroom.net.NetManager;
import com.ttt.liveroom.util.L;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Muyangmin
 * @since 1.0.0
 */
public class LocalAnimPlayerView extends RelativeLayout implements IGiftAnimPlayer {

    private Animation animWholeIn;
    private Animation animGiftPicIn;
    private Animation animCombo;
    private Animation animWholeOut;
    private boolean canJoin;
    private boolean available;
    /**
     * 这个是需要到达的总Combo数。
     */
    private int totalCombo;
    private IAnimController mAnimController;

    private SendGiftAction currentAction;
    private CountDownTimer finishCountDown;

    private TextView tvCombo;
    private TextView tvNickname, tvDescription;
    private SimpleDraweeView draweeCreator, draweeGift;

    private Animation currentAnim;

    private Subscription comboSubscription;

    public LocalAnimPlayerView(Context context) {
        super(context);
        init(context);
    }

    public LocalAnimPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LocalAnimPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public LocalAnimPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_gift_local_anim, this);
        tvCombo = (TextView) view.findViewById(R.id.gift_anim_tv_combo);
        tvNickname = (TextView) findViewById(R.id.gift_anim_tv_nickname);
        tvDescription = (TextView) findViewById(R.id.gift_anim_tv_gift_action);
        draweeGift = (SimpleDraweeView) findViewById(R.id.gift_anim_drawee_gift);
        draweeCreator = (SimpleDraweeView) findViewById(R.id.gift_anim_drawee_creator);
        initAnimAsync(context);
    }

    private void initAnimAsync(final Context context) {
        draweeGift.setVisibility(GONE);
        //异步初始化动画
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                subscriber.onStart();
                if (BuildConfig.DEBUG) {
                    L.v(false, VIEW_LOG_TAG, "Start loading animation...");
                }

                animWholeIn = AnimationUtils.loadAnimation(context, R.anim.local_gift_whole_in);
                //This is not a copy error
                animGiftPicIn = AnimationUtils.loadAnimation(context, R.anim.local_gift_pic_in);

                animCombo = AnimationUtils.loadAnimation(context, R.anim.local_gift_combo);
                animWholeOut = AnimationUtils.loadAnimation(context, R.anim.local_gift_whole_out);

                animWholeIn.setAnimationListener(new AnimListenerAdapter("animWholeIn") {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        super.onAnimationStart(animation);
                        setVisibility(VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        super.onAnimationEnd(animation);
                        draweeGift.setVisibility(VISIBLE);
                        startAnimWrapper(draweeGift, animGiftPicIn);
                    }
                });

                animGiftPicIn.setAnimationListener(new AnimListenerAdapter("animGiftPicIn") {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        super.onAnimationStart(animation);
//                        draweeGift.setVisibility(INVISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        super.onAnimationEnd(animation);
                        startComboAnimation();
//                        draweeGift.setVisibility(VISIBLE);
                    }
                });
                animCombo.setAnimationListener(new AnimListenerAdapter("animCombo") {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        super.onAnimationEnd(animation);
                        //DO nothing; finish this on countdown timer.
                    }
                });
                animWholeOut.setAnimationListener(new AnimListenerAdapter("animWholeOut") {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        super.onAnimationEnd(animation);
                        //取消计数器的interval,否则将无限计数，无形中浪费资源且可能内存泄露
                        if (comboSubscription != null) {
                            comboSubscription.unsubscribe();
                            comboSubscription = null;
                        }
                        //设置下个动画可加入
                        available = true;
                        //已经结束，设置同类动画不可Join
                        canJoin = false;
                        //重置TotalCombo
                        totalCombo = 0;
                        //重置文字，消除下次显示时的残留
                        tvCombo.setText("");
                        //通知Controller
                        if (mAnimController != null) {
                            mAnimController.onPlayerAvailable();
                        }
                        //设置为不可见状态
                        setVisibility(INVISIBLE);
                        draweeGift.setVisibility(INVISIBLE);
                    }
                });

                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aBoolean) {
                        if (BuildConfig.DEBUG) {
                            L.v(false, VIEW_LOG_TAG, "Animation init finished.");
                        }
                        available = true;
                        //通知第一次可用，但由于是异步操作，执行到这里时有可能还未完成绑定操作。
                        if (mAnimController != null) {
                            mAnimController.onPlayerAvailable();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        L.e(VIEW_LOG_TAG, "Animation load failed! this view may be forever " +
                                "unavailable!", throwable);
                        //Do nothing
                    }
                });
    }

    private void startComboAnimation() {
        L.d(VIEW_LOG_TAG, "Start Combo Animation:totalCombo=" + totalCombo);
        if (comboSubscription!=null){
            comboSubscription.unsubscribe();
        }
        comboSubscription = Observable.interval(1, 500, TimeUnit.MILLISECONDS, AndroidSchedulers
                .mainThread())
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long currentCombo) {
                        L.v(false, VIEW_LOG_TAG, "Filtering: currentCombo=%d", currentCombo);
                        return currentCombo > 0 && currentCombo <= totalCombo;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long integer) {
                        if (finishCountDown != null) {
                            finishCountDown.cancel();
                        }
                        finishCountDown = createCountDownTimer();
                        finishCountDown.start();
                        tvCombo.setText(String.format(Locale.US, "X%d", integer));
                        startAnimWrapper(tvCombo, animCombo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //Ignore
                        L.e(VIEW_LOG_TAG, "Error while displaying combo animation!",
                                throwable);
                    }
                });
    }

    private CountDownTimer createCountDownTimer() {
        return new CountDownTimer(600, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Empty
                if (BuildConfig.DEBUG){
                    L.v(false, VIEW_LOG_TAG, "Count down tick: %d ms left.", millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                startAnimWrapper(animWholeOut);
            }
        };
    }

    @Override
    public void bindAnimController(IAnimController controller) {
        mAnimController = controller;
    }

    @Override
    public boolean canJoin(@NonNull SendGiftAction action) {
        //如果当前为空闲状态则currentAction为空，此时不应该走join流程，而是新的动画。
        if (currentAction==null){
            return false;
        }
        boolean fromSameUser = currentAction.getFromUid().equals(action.getFromUid());
        boolean isSameGift = currentAction.getGiftName().equals(action.getGiftName());
        return canJoin && fromSameUser && isSameGift;
    }

    @Override
    public boolean available() {
        return available && (!isShown());
    }

    @Override
    public void startAnim(@NonNull SendGiftAction action) {
        L.i(VIEW_LOG_TAG, "-----startAnim： %s-----", action);
        //开始动画，设置忙碌标记
        available = false;
        canJoin = true;
        currentAction = action;
        totalCombo = action.getCombo();

        //完成数据显示
        Context context = getContext();
        tvDescription.setText(context.getString(R.string.room_gift_description, action.getGiftName()));
        if (!TextUtils.isEmpty(action.getNickname())){
            tvNickname.setText(action.getNickname());
        }
        if (!TextUtils.isEmpty(action.getAvatar())){
            draweeCreator.setImageURI(NetManager.wrapPathToUri(action.getAvatar()));
        }
        if (!TextUtils.isEmpty(action.getGiftIcon())){
            draweeGift.setImageURI(NetManager.wrapPathToUri(action.getGiftIcon()));
        }

        //第一个动画，播放整体View从左到右的淡入位移效果。
        startAnimWrapper(animWholeIn);
    }

    @Override
    public synchronized void joinAnim(@NonNull SendGiftAction action) {
        //目前无需更新currentAction，因为始终都是同一种类型的
        totalCombo += action.getCombo();
    }

    private void startAnimWrapper(Animation animation){
        startAnimWrapper(this, animation);
    }

    private void startAnimWrapper(View view, Animation animation){
        view.startAnimation(animation);
        currentAnim = animation;
    }

    @Override
    public void cancelAnim() {
        if (currentAnim!=null && currentAnim.isInitialized()){
            currentAnim.cancel();
        }
    }

    private static class AnimListenerAdapter implements Animation.AnimationListener {

        private String animTag;

        public AnimListenerAdapter(String animTag) {
            this.animTag = animTag;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            logIfDebug("onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            logIfDebug("onAnimationEnd");
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            logIfDebug("onAnimationRepeat");
        }

        private void logIfDebug(String msg) {
            if (BuildConfig.DEBUG) {
                L.v(false, VIEW_LOG_TAG, " %s: ----- %s -----", animTag, msg);
            }
        }
    }
}
