package com.ttt.liveroom.widget.heardanim;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Draw a static heart on canvas.
 * Created by huanzhang on 2016/5/4.
 */
public class HeartUtil {

    private static Path path, mWidPath; // 路径
    private static Paint nPaint, wPaint;

    static {
        path = new Path();
        mWidPath = new Path();
        nPaint = new Paint();
        nPaint.setAntiAlias(true);
        nPaint.setStyle(Paint.Style.FILL);

        wPaint = new Paint();
        wPaint.setAntiAlias(true);
        wPaint.setColor(Color.WHITE);
        wPaint.setStyle(Paint.Style.STROKE);
        wPaint.setStrokeWidth(1);
    }

    public static void drawHeart(Canvas canvas, float rate, int color) {
        canvas.drawColor(Color.TRANSPARENT);
        nPaint.setColor(color);
        // 重置画板
        path.reset();
        mWidPath.reset();

        int px = canvas.getWidth() / 2;
        int py = canvas.getHeight() / 2;
        // 路径的起始点
        mWidPath.moveTo(px, py - 5 * rate);
        // 根据心形函数画图
        for (double i = 0; i <= 2 * Math.PI; i += 0.01) {
            float x = (float) (16 * Math.sin(i) * Math.sin(i) * Math.sin(i));
            float y = (float) (13 * Math.cos(i) - 5 * Math.cos(2 * i) - 2 * Math.cos(3 * i) -
                    Math.cos(4 * i));
            x *= rate;
            y *= rate;
            x = px - x;
            y = py - y;
            mWidPath.lineTo(x, y);
        }
        canvas.drawPath(mWidPath, nPaint);
        canvas.drawPath(mWidPath, wPaint);
    }
}
