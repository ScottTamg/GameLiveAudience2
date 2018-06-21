package com.ttt.liveroom.util.roomanim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ttt.liveroom.R;


/**
 * Created by 刘景 on 2017/06/09.
 */

public class CarView extends RelativeLayout {

    private ImageView imgbody, imglightone, imglighttwo, imgfirst, imgback, imgbodytwo, imgari, imgfirsttwo, imgbacktwo;
    private Context context;
    private AnimationDrawable animDrawableFrist, animDrawableBack, animDrawableFristTwo, animDrawableBackTwo;
    private GitfSpecialsStop animsopt;

    public CarView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        imgbody = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(489, 194);
        imgbody.setLayoutParams(layoutParams);
        imgbody.setImageResource(R.drawable.carbody);
        this.addView(imgbody);

        imgfirst = new ImageView(context);
        imgfirst.setBackgroundResource(R.drawable.lunziz);
        imgfirst.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LayoutParams layoutParamsfirst = new LayoutParams(54, 63);
        imgfirst.setLayoutParams(layoutParamsfirst);
        this.addView(imgfirst);

        imgback = new ImageView(context);
        imgback.setBackgroundResource(R.drawable.lunziz);
        imgback.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imgback.setLayoutParams(layoutParamsfirst);
        this.addView(imgback);

        imglighttwo = new ImageView(context);
        imglighttwo.setBackgroundResource(R.drawable.carlighttwo);
        this.addView(imglighttwo);

        imglightone = new ImageView(context);
        imglightone.setBackgroundResource(R.drawable.carlightone);
        this.addView(imglightone);

        imgbodytwo = new ImageView(context);
        imgbodytwo.setBackgroundResource(R.drawable.carbodytwo);
        this.addView(imgbodytwo);

        imgari = new ImageView(context);
        imgari.setBackgroundResource(R.drawable.carari);
        this.addView(imgari);

        imgfirsttwo = new ImageView(context);
        imgfirsttwo.setBackgroundResource(R.drawable.lunzit);
        this.addView(imgfirsttwo);

        imgbacktwo = new ImageView(context);
        imgbacktwo.setBackgroundResource(R.drawable.lunzizt);
        this.addView(imgbacktwo);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        598 286
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(600, 260);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        横向全部加121
        imgbody.layout(119, 0, 617, 194);
        imgfirst.layout(294, 97, 351, 160);
        imgback.layout(538, 48, 581, 108);
        imglighttwo.layout(12, 54, 266, 255);
        imglightone.layout(24, 5, 185, 190);
        imgbodytwo.layout(0, 0, 494, 232);
        imgari.layout(369, 119, 512, 247);
        imgfirsttwo.layout(15, 49, 39, 108);
        imgbacktwo.layout(173, 101, 227, 196);
//        61 49  155 51
//        103 52    16,76 38 76  24 49 24 105            173 142 226 142 193 100 190
//        415 77 457 77  439 47 437 107
    }

    //    旋转
    public void rotatedemo() {

        imgfirsttwo.setVisibility(GONE);
        imgbacktwo.setVisibility(GONE);
        imgbodytwo.setVisibility(GONE);
        imgari.setVisibility(GONE);


        imgfirst.setVisibility(VISIBLE);
        imgbody.setVisibility(VISIBLE);
        imglightone.setVisibility(VISIBLE);
        imglighttwo.setVisibility(VISIBLE);
        imgback.setVisibility(VISIBLE);

        ObjectAnimator.ofFloat(imgfirst, "rotationY", 0f, 180).setDuration(0).start();
        ObjectAnimator.ofFloat(imgback, "rotationY", 0f, 180).setDuration(0).start();
        animDrawableFrist = (AnimationDrawable) imgfirst.getBackground();
        animDrawableFrist.start();
        animDrawableBack = (AnimationDrawable) imgback.getBackground();
        animDrawableBack.start();


    }

    //    旋转 二
    public void rotatetwo() {
        animDrawableFrist.stop();
        animDrawableFrist = null;
        animDrawableBack.stop();
        animDrawableBack = null;

        imgfirsttwo.setVisibility(VISIBLE);
        imgbacktwo.setVisibility(VISIBLE);
        imgbodytwo.setVisibility(VISIBLE);

        imgfirst.setVisibility(GONE);
        imgbody.setVisibility(GONE);
        imglightone.setVisibility(GONE);
        imglighttwo.setVisibility(GONE);
        imgback.setVisibility(GONE);

        animDrawableFristTwo = (AnimationDrawable) imgfirsttwo.getBackground();
        animDrawableFristTwo.start();
        animDrawableBackTwo = (AnimationDrawable) imgbacktwo.getBackground();
        animDrawableBackTwo.start();


    }

    public void initAnim(float with, float hight) {
        rotatedemo();
        initmoves(with, hight);
    }

    private void initmoves(float with, float hight) {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this, "translationX", with, with / 2 - 300);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this, "translationY", 0f, hight / 2);
        final ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(this, "translationX", with / 2 - 300, -600).setDuration(1500);
        ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(this, "translationY", hight / 2, hight);
        final ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(this, "alpha", 1f, 1f).setDuration(1500);
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator3).with(objectAnimator5);
        AnimatorSet set = new AnimatorSet();
        set.play(objectAnimator1).with(objectAnimator2);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                objectAnimator4.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator4.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.setDuration(1000).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

//        第二次入场
        ObjectAnimator objectAnimatortwo1 = ObjectAnimator.ofFloat(this, "translationX", with, with / 2 - 300);
        ObjectAnimator objectAnimatortwo2 = ObjectAnimator.ofFloat(this, "translationY", hight, hight / 2);
        final ObjectAnimator objectAnimatortwo3 = ObjectAnimator.ofFloat(this, "alpha", 1f, 1f).setDuration(1500);
        ObjectAnimator objectAnimatortwo4 = ObjectAnimator.ofFloat(this, "translationX", with / 2 - 300, -600);
        ObjectAnimator objectAnimatortwo5 = ObjectAnimator.ofFloat(this, "translationY", hight / 2, 0f);
        final AnimatorSet settwo1 = new AnimatorSet();
        settwo1.play(objectAnimatortwo1).with(objectAnimatortwo2);
        final AnimatorSet settwo2 = new AnimatorSet();
        settwo2.play(objectAnimatortwo4).with(objectAnimatortwo5);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rotatetwo();
                settwo1.setDuration(1500).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        settwo1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                objectAnimatortwo3.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimatortwo3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imgari.setVisibility(VISIBLE);
                settwo2.setDuration(1000).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        settwo2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                imgfirsttwo.setVisibility(GONE);
                imgbacktwo.setVisibility(GONE);
                imgbodytwo.setVisibility(GONE);
                imgari.setVisibility(GONE);

                animDrawableFristTwo.stop();
                animDrawableFristTwo = null;
                animDrawableBackTwo.stop();
                animDrawableBackTwo = null;

                if (animsopt != null) {
                    animsopt.animend();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.setDuration(2000).start();
    }

    public void setAnimsopt(GitfSpecialsStop animsopt) {
        this.animsopt = animsopt;
    }


}
