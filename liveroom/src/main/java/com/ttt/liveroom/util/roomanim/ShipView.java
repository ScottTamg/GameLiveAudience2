package com.ttt.liveroom.util.roomanim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ttt.liveroom.R;

/**
 * Created by 刘景 on 2017/06/09.
 */

public class ShipView extends RelativeLayout {
    private ImageView waterbg,ship,shipback;
    private Context context;
    private float shipY,waterY;
    private AnimatorSet waterSet;
    private int whitd;
    private GitfSpecialsStop gitfSpecialsStop;
    private ShipIn shipIn;
    public ShipView(Context context) {
        super(context);
        this.context=context;
        initView();
    }


    public ShipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView();
    }

    private void initView() {
        waterbg=new ImageView(context);
        waterbg.setBackgroundResource(R.drawable.yacht_water_one);
        this.addView(waterbg);

        shipIn=new ShipIn(context);
        this.addView(shipIn);

        shipY=ship.getY();
        waterY=waterbg.getY();
    }

    public void initAnim(int withd) {
        ShipView.this.setVisibility(VISIBLE);
//        moveShip(ship,withd);
        moveShip(shipIn,withd);
        waterMove(waterbg);
    }

    public void moveShip(View view, int withd){
        ObjectAnimator objectAnimator1=ObjectAnimator.ofFloat(view,"translationY",shipY,shipY+30).setDuration(500);
        ObjectAnimator objectAnimator2= ObjectAnimator.ofFloat(view,"translationX",-450,withd/2-450).setDuration(2000);
        final ObjectAnimator objectAnimator3=ObjectAnimator.ofFloat(view,"alpha",1f,1f).setDuration(3000);
        final ObjectAnimator objectAnimator4=ObjectAnimator.ofFloat(view,"translationX",withd/2-450,withd).setDuration(4000);
        objectAnimator1.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator1.setRepeatMode(ObjectAnimator.REVERSE);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator2).with(objectAnimator1);
        animatorSet.play(objectAnimator3).after(objectAnimator2);

        objectAnimator3.addListener(new Animator.AnimatorListener() {
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
                waterSet.cancel();
                animatorSet.cancel();
                ShipView.this.setVisibility(GONE);
                if (gitfSpecialsStop!=null) {
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
        animatorSet.start();

    }

    public void waterMove(View view){
        ObjectAnimator objectAnimator1 =ObjectAnimator.ofFloat(view,"translationY",waterY,waterY+20).setDuration(500);
        ObjectAnimator objectAnimator2= ObjectAnimator.ofFloat(view,"translationX",0,180).setDuration(8000);
        objectAnimator1.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator1.setRepeatMode(ObjectAnimator.REVERSE);

        waterSet = new AnimatorSet();
        waterSet.play(objectAnimator2).with(objectAnimator1);
        waterSet.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        whitd=MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(whitd,600);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        waterbg.layout(0,100,whitd,600);
        shipIn.layout(0,0,450,600);

    }

    public void setGitfSpecialsStop(GitfSpecialsStop gitfSpecialsStop) {
        this.gitfSpecialsStop = gitfSpecialsStop;
    }



    public class ShipIn extends RelativeLayout{

        public ShipIn(Context context) {
            super(context);
            ShipinitView();
        }

        private void ShipinitView() {

            shipback=new ImageView(context);
            shipback.setBackgroundResource(R.drawable.yacht_shadow);
            this.addView(shipback);

            ship=new ImageView(context);
            ship.setBackgroundResource(R.drawable.yacht_hull);
            this.addView(ship);
        }

        public ShipIn(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(450,450);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            shipback.layout(50,300,360,410);
            ship.layout(0,130,420,343);
        }
    }
}
