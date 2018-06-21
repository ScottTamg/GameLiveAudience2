package com.ttt.liveroom.util.roomanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ttt.liveroom.R;

import java.util.Random;

/**
 * Created by 刘景 on 2017/06/09.
 */

public class MrlLove extends RelativeLayout {
    private Random random = new Random();//用于实现随机功能
    private int loveHeight;//爱心的高度
    private int loveWidth;//爱心的宽度
    private int layoutHeight;//FavorLayout的高度
    private int layoutWidth;//FavorLayout的宽度

    //定义一个LayoutParams 用它来控制子view的位置
    private LayoutParams lp;

    private Drawable img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12;
    private Drawable[] drawables;

    private Interpolator line = new LinearInterpolator();//线性
    private Interpolator acc = new AccelerateInterpolator();//加速
    private Interpolator dce = new DecelerateInterpolator();//减速
    private Interpolator accdec = new AccelerateDecelerateInterpolator();//先加速后减速
    // 在init中初始化
    private Interpolator[] interpolators;


    public MrlLove(Context context) {
        super(context);
        initDrawable();
        init();
    }

    public MrlLove(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init里做一些初始化变量的操作
        initDrawable();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        layoutHeight = getMeasuredHeight();
        layoutWidth = getMeasuredWidth();
    }

    private void init() {
        //获取图的宽高 用于后面的计算
        //注意 我这里3张图片的大小都是一样的,所以我只取了一个
        loveHeight = 70;
        loveWidth = 70;

        // 初始化插补器
        interpolators = new Interpolator[4];
        interpolators[0] = line;
        interpolators[1] = acc;
        interpolators[2] = dce;
        interpolators[3] = accdec;

        //底部 并且 水平居中
        lp = new LayoutParams(loveHeight, loveWidth);
//        横向居中
        lp.addRule(CENTER_HORIZONTAL, TRUE); //这里的TRUE 要注意 不是true
//        底部
        lp.addRule(ALIGN_PARENT_TOP, TRUE);
        //好了,之后只要给子view设置LayoutParams就可以实现了
    }


    private void initDrawable() {
        //接下去我们初始化:
        //初始化显示的图片
        drawables = new Drawable[12];
        img1 = getResources().getDrawable(R.drawable.planelw1);
        drawables[0] = img1;
        img2 = getResources().getDrawable(R.drawable.planelw2);
        drawables[1] = img2;
        img3 = getResources().getDrawable(R.drawable.planelw3);
        drawables[2] = img3;
        img4 = getResources().getDrawable(R.drawable.planelw4);
        drawables[3] = img4;
        img5 = getResources().getDrawable(R.drawable.planelw12);
        drawables[4] = img5;
        img6 = getResources().getDrawable(R.drawable.planelw6);
        drawables[5] = img6;
        img7 = getResources().getDrawable(R.drawable.planelw7);
        drawables[6] = img7;
        img8 = getResources().getDrawable(R.drawable.planelw8);
        drawables[7] = img8;
        img9 = getResources().getDrawable(R.drawable.planelw9);
        drawables[8] = img9;
        img10 = getResources().getDrawable(R.drawable.planelw10);
        drawables[9] = img10;
        img11 = getResources().getDrawable(R.drawable.planelw11);
        drawables[10] = img11;
        img12 = getResources().getDrawable(R.drawable.planelw12);
        drawables[11] = img12;
    }

    //我封装了一个方法  利用ObjectAnimator AnimatorSet来实现 alpha以及x,y轴的缩放功能
//target就是爱心
    private AnimatorSet getEnterAnimtor(final View target) {

        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);
        enter.setTarget(target);
        return enter;
    }

    public void addFavor() {
        ImageView imageView = new ImageView(getContext());
        //随机选一个
        imageView.setImageDrawable(drawables[random.nextInt(12)]);
        imageView.setLayoutParams(lp);

        addView(imageView);

        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }

    private ValueAnimator getBezierValueAnimator(View target) {

        //初始化一个BezierEvaluator
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));

        //这里最好画个图 理解一下 传入了起点 和 终点
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF((layoutWidth - loveWidth) / 2, 0), new PointF(random.nextInt(getWidth()), layoutHeight - loveHeight));//随机
        animator.addUpdateListener(new BezierListenr(target));
        animator.setTarget(target);
        animator.setDuration(3500);
        return animator;
    }

    private Animator getAnimator(View target) {
        AnimatorSet set = getEnterAnimtor(target);

        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setInterpolator(interpolators[random.nextInt(4)]);//实现随机变速
        finalSet.setTarget(target);
        return finalSet;
    }

//这里涉及到另外一个方法:getPointF(),这个是我用来获取途径的两个点
// 这里的取值可以随意调整,调整到你希望的样子就好

    /**
     * 获取中间的两个 点
     *
     * @param scale
     */
    private PointF getPointF(int scale) {

        PointF pointF = new PointF();
        Log.i("mrl", layoutHeight + "   " + layoutWidth + "这个里有问题");
        pointF.x = random.nextInt((layoutWidth - 100));//减去100 是为了控制 x轴活动范围,看效果 随意~~
        //再Y轴上 为了确保第二个点 在第一个点之上,我把Y分成了上下两半 这样动画效果好一些  也可以用其他方法
        pointF.y = random.nextInt((layoutHeight - 100)) / scale;
        return pointF;
    }

    //我们自定义一个BezierEvaluator 实现 TypeEvaluator
//由于我们view的移动需要控制x y 所以就传入PointF 作为参数,是不是感觉完全契合??
    public class BezierEvaluator implements TypeEvaluator<PointF> {


        private PointF pointF1;//途径的两个点
        private PointF pointF2;

        public BezierEvaluator(PointF pointF1, PointF pointF2) {
            this.pointF1 = pointF1;
            this.pointF2 = pointF2;
        }

        @Override
        public PointF evaluate(float time, PointF startValue,
                               PointF endValue) {

            float timeLeft = 1.0f - time;
            PointF point = new PointF();//结果

            PointF point0 = (PointF) startValue;//起点

            PointF point3 = (PointF) endValue;//终点
            //代入公式
            point.x = timeLeft * timeLeft * timeLeft * (point0.x)
                    + 3 * timeLeft * timeLeft * time * (pointF1.x)
                    + 3 * timeLeft * time * time * (pointF2.x)
                    + time * time * time * (point3.x);

            point.y = timeLeft * timeLeft * timeLeft * (point0.y)
                    + 3 * timeLeft * timeLeft * time * (pointF1.y)
                    + 3 * timeLeft * time * time * (pointF2.y)
                    + time * time * time * (point3.y);
            return point;
        }
    }

    private class BezierListenr implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public BezierListenr(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            // 这里偷个懒,顺便做一个alpha动画,这样alpha渐变也完成啦
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }

    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            //因为不停的add 导致子view数量只增不减,所以在view动画结束后remove掉
            removeView((target));
        }
    }
}
