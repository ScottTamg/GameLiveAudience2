package com.ttt.liveroom.widget.heardanim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.ttt.liveroom.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Anim view for player.
 * Created by huanzhang on 4/6/16.
 */
public class HeartAnim extends View {
    private static Context context;
    public int[] HEART_COLORS = new int[]{
            R.color.md_red_500,
            R.color.md_orange_500,
            R.color.md_yellow_500,
            R.color.md_green_500,
            R.color.md_blue_500,
            R.color.md_pink_500,
            R.color.md_purple_500,
            R.color.md_white
    };

    private static int defaultColor = 0;
    /**
     * 小球集合
     */
    private List<HeartInfo> mLoves = new ArrayList<>();

    private float rate = 3; // 半径变化率
    private Path mWidPath; // 路径
    private AnimThread at;
    private Paint nPaint, wPaint;
    /**
     * 测量路径的坐标位置
     */
    private PathMeasure pathMeasure = null;
    private int width, height;
    /**
     * 曲线高度个数分割
     */
    private int quadCount = 2;
    /**
     * 曲度
     */
    private float intensity = 0.5f;
    private static int UNIT_TIME = 30;

    public HeartAnim(Context context) {
        this(context, null);
        this.context = context.getApplicationContext();
    }

    public HeartAnim(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context.getApplicationContext();
    }

    public HeartAnim(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context.getApplicationContext();
        init();
    }

    private void init() {
        pathMeasure = new PathMeasure();
        //         初始化画笔
        at = new AnimThread();

        defaultColor = HEART_COLORS[(int) (Math.random() * HEART_COLORS.length)];
        nPaint = new Paint();
        nPaint.setAntiAlias(true);
        nPaint.setColor(Color.BLUE);
        nPaint.setStyle(Paint.Style.FILL);

        wPaint = new Paint();
        wPaint.setAntiAlias(true);
        wPaint.setColor(Color.WHITE);
        wPaint.setStyle(Paint.Style.STROKE);
        wPaint.setStrokeWidth(3);

        // 创建一个路径
//        path = new Path();
        mWidPath = new Path();
        at.start();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        L.v(false, VIEW_LOG_TAG, "the widthMeasureSpec is " + widthMeasureSpec + " ,heightMeasureSpec is" + heightMeasureSpec);
        this.height = getMeasuredHeight();
        this.width = getMeasuredWidth();

    }

    /**
     * 创建Love
     */
    private void builderLove(int color) {

//        width = this.getMeasuredWidth();
//        height = getMeasuredHeight();
//        L.v(false, VIEW_LOG_TAG, "width--" + width + "--height--" + height);
        int min = (int) (width / 2f);
        Path path = new Path();
        CPoint cPoint = new CPoint(min, height);
        List<CPoint> points = builderPath(cPoint);
        drawLovePath(path, points);
        HeartInfo love = new HeartInfo();
        love.setPath(path);
        pathMeasure.setPath(love.getPath(), false);
        love.initLove(pathMeasure.getLength(), getContext());
        love.setLoveColor(color);
        mLoves.add(love);

    }

    /**
     * 画曲线
     */
    private void drawLovePath(Path path, List<CPoint> points) {
        if (points.size() > 1) {
            for (int j = 0; j < points.size(); j++) {

                CPoint point = points.get(j);

                if (j == 0) {
                    CPoint next = points.get(j + 1);
                    point.dx = ((next.x - point.x) * intensity);
                    point.dy = ((point.y - next.y) * intensity);
                } else if (j == points.size() - 1) {
                    CPoint prev = points.get(j - 1);
                    point.dx = ((point.x - prev.x) * intensity);
                    point.dy = ((prev.y - point.y) * intensity);
                } else {
                    CPoint next = points.get(j + 1);
                    CPoint prev = points.get(j - 1);
                    point.dx = ((next.x - prev.x) * intensity);
                    point.dy = ((prev.y - next.y) * intensity);
                }

                // create the cubic-spline path
                if (j == 0) {
                    path.moveTo(point.x, point.y);
                } else {
                    CPoint prev = points.get(j - 1);
//                    L.v(false, VIEW_LOG_TAG, "point " + j + ">>>" + point.x + "," + point.y);
                    path.cubicTo(prev.x + prev.dx, (prev.y - prev.dy), point.x
                            - point.dx, (point.y + point.dy), point.x, point.y);

                }
            }
        }
    }

    /**
     * 曲线摇摆的幅度
     */
    private int range = (int) TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
                    .getDisplayMetrics());

    /**
     * 画路径
     */
    private List<CPoint> builderPath(CPoint point) {
        List<CPoint> points = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < quadCount; i++) {
            if (i == 0) {
                points.add(point);
            } else {
                CPoint tmp = new CPoint(0, 0);
                if (random.nextInt(100) % 2 == 0) {
                    tmp.x = point.x + random.nextInt(range);
                } else {
                    tmp.x = point.x - random.nextInt(range);
                }
                tmp.y = (int) (height / (float) quadCount * (quadCount - i - 1));
//                L.v(false, VIEW_LOG_TAG, "the point is " + i + ":" + tmp.x + "," + tmp.y);
                points.add(tmp);
            }
        }
        return points;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        builderLove();
        drawLoves(canvas, mLoves);
        addTime();
    }

    private void addTime() {
        for (Iterator it = mLoves.iterator(); it.hasNext(); ) {
            HeartInfo value = (HeartInfo) it.next();
            if (value.getCruTime() >= value.getTime()) {
                it.remove();  // ok
            }
        }
        for (HeartInfo love : mLoves) {
            love.addCruTime(UNIT_TIME);
        }
    }

    private void drawLoves(Canvas canvas, List<HeartInfo> loves) {
        for (HeartInfo love : loves) {
            float[] pos = new float[2];
            pathMeasure.setPath(love.getPath(), false);
            pathMeasure.getPosTan(love.getCruHeight(), pos, null);

//            L.v(false, VIEW_LOG_TAG, "pos[0]:" + pos[0] + "   pos[1]:" + pos[1]);
            this.nPaint.setColor(love.getLoveColor());
            this.nPaint.setAlpha(love.getAlpha());
            this.wPaint.setAlpha(love.getAlpha());

            this.rate = love.getRate();
            drawLove(canvas, pos[0], pos[1]);

        }
    }


    private void drawLove(Canvas canvas, float px, float py) {
        // 重置画板
//        path.reset();

        // 重置画板
        mWidPath.reset();
        // 路径的起始点
        mWidPath.moveTo(px, py - 5 * rate);
        // 根据心形函数画图
        for (double i = 0; i <= 2 * Math.PI; i += 0.1) {
            float x = (float) (16 * Math.sin(i) * Math.sin(i) * Math.sin(i));
            float y = (float) (13 * Math.cos(i) - 5 * Math.cos(2 * i) - 2 * Math.cos(3 * i) - Math.cos(4 * i));
            x *= rate;
            y *= rate;
            x = px - x;
            y = py - y;
            mWidPath.lineTo(x, y);
        }
        canvas.drawPath(mWidPath, nPaint);
        canvas.drawPath(mWidPath, wPaint);
    }

    public void addLove(int color) {
        if (color == 0) {
            color = defaultColor;
        }
        builderLove(color);
    }


    private class AnimThread extends Thread {

        public AnimThread() {
            super("AnimThread");
        }

        @Override
        public void run() {
            while (true) {

                try {
                    Thread.sleep(UNIT_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 刷新画布
                postInvalidate();
            }
        }
    }


    private class CPoint {

        public float x = 0f;
        public float y = 0f;

        /**
         * x-axis distance
         */
        public float dx = 0f;

        /**
         * y-axis distance
         */
        public float dy = 0f;

        public CPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }


}
