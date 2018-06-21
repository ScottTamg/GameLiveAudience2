package com.ttt.liveroom.util.roomanim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ttt.liveroom.R;


/**
 * Created by 刘景 on 2017/06/09.
 */
public class PlaneImagerView extends RelativeLayout {
    private ImageView bg, img1, img2, img3, img4, img5, bgy;
    private MrlLove mrlLove;
    private Context context;
    private GitfSpecialsStop gitfSpecialsStop;

    public PlaneImagerView(Context context) {
        super(context);
        this.context = context;
        thisAddView();
    }

    public PlaneImagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        thisAddView();
    }

    private void thisAddView() {
        bg = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(598, 286);
        bg.setLayoutParams(layoutParams);
        bg.setImageResource(R.drawable.plane_body);
        this.addView(bg);
        img1 = new ImageView(context);
        LayoutParams layoutParams1 = new LayoutParams(60, 60);
        img1.setLayoutParams(layoutParams1);
        img1.setImageResource(R.drawable.plane_airscrew);
        this.addView(img1);

        img2 = new ImageView(context);
        LayoutParams layoutParams2 = new LayoutParams(40, 40);
        img2.setLayoutParams(layoutParams2);
        img2.setImageResource(R.drawable.plane_airscrew);
        this.addView(img2);

        img3 = new ImageView(context);
        LayoutParams layoutParams3 = new LayoutParams(70, 70);
        img3.setLayoutParams(layoutParams3);
        img3.setImageResource(R.drawable.plane_airscrew);
        this.addView(img3);

        img4 = new ImageView(context);
        img4.setLayoutParams(layoutParams2);
        img4.setImageResource(R.drawable.plane_airscrew);
        this.addView(img4);

        img5 = new ImageView(context);
        img5.setLayoutParams(layoutParams1);
        img5.setImageResource(R.drawable.plane_airscrew);
        this.addView(img5);

        bgy = new ImageView(context);
        LayoutParams layoutParamsbgy = new LayoutParams(424, 220);
        bgy.setLayoutParams(layoutParamsbgy);
        bgy.setImageResource(R.drawable.plane_shadow);
        this.addView(bgy);

        mrlLove = new MrlLove(context);
        LayoutParams layoutParamsLove = new LayoutParams(400, 500);
        mrlLove.setLayoutParams(layoutParamsLove);
        this.addView(mrlLove);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        598 286
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(600, 800);
    }

    @Override
    protected void onDraw(Canvas canvas) {
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        46 110 40   424,220
        bg.layout(0, 0, 598, 286);
        img1.layout(39, 135, 99, 195);
        img2.layout(94, 182, 134, 222);
        img3.layout(73, 194, 143, 264);
        img4.layout(200, 226, 240, 266);
        img5.layout(270, 232, 330, 292);
        bgy.layout(176, 580, 600, 800);
        mrlLove.layout(100, 300, 500, 800);
    }

    public void initAnim(float with) {
        this.setVisibility(VISIBLE);
        rotatedemo(img1);
        rotatedemo(img2);
        rotatedemo(img3);
        rotatedemo(img4);
        rotatedemo(img5);
        movethis(this, with);
    }

    //    旋转
    public void rotatedemo(View view) {
        ObjectAnimator.ofFloat(view, "rotationY", 0f, -30f).setDuration(0).start();
        ObjectAnimator.ofFloat(view, "rotation", 0f, 7200f).setDuration(8000).start();
    }

    //    飞机
    public void movethis(final View view, final float with) {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view, "translationX", with, with / 2 - 300);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view, "translationY", 0f, 200f);
        final ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(view, "translationX", with / 2 - 300, -600).setDuration(1500);
        final ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(view, "alpha", 1f, 1f).setDuration(4300);
        AnimatorSet set = new AnimatorSet();
//        注意这里最好别设置多种动画在同一个set了比如下面不注释第一个setplaytogether
//        同时执行
//        set.playTogether(objectAnimator1,objectAnimator2,objectAnimator3);
//        按顺序执行
//        set.playSequentially(objectAnimator1,objectAnimator2,objectAnimator3);
//        有特殊顺序的动画集合
        set.play(objectAnimator1).with(objectAnimator2);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                objectAnimator4.start();
                for (int i = 0; i < 40; i++) {
                    mrlLove.addFavor();
                }
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
                objectAnimator3.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                PlaneImagerView.this.setVisibility(GONE);
                if (gitfSpecialsStop != null) {
                    gitfSpecialsStop.animend();
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

    public void setGitfSpecialsStop(GitfSpecialsStop gitfSpecialsStop) {
        this.gitfSpecialsStop = gitfSpecialsStop;
    }
}
