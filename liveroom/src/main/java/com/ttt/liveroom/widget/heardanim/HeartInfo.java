package com.ttt.liveroom.widget.heardanim;

import android.content.Context;
import android.graphics.Path;

import com.ttt.liveroom.R;


/**
 * Created by huanzhang on 4/6/16.
 */
public class HeartInfo {

    private float x;
    private float y;
    private Path path;
    private float rate = 2.2f;

    private int cruTime = 0;
    private float cruHeight = 0;
    private float height = 0;
    //Time to disappear, in milliseconds
    private int time = 1800;
    private double speed = 0;
    private int loveColor;
    private int alpha = 255;
    private double scale = 0;
    private float scaleHeight = 0;

    public void initLove(float height, Context context) {
        this.height = height;
        speed = (height / (time * 1.0));
        scaleHeight = height / 6;
        rate = context.getResources().getDimension(R.dimen.heart_rate);
//        a = 2*(speed*time-height)/(time*time);
    }

    public double getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getLoveColor() {
        return loveColor;
    }

    public void setLoveColor(int loveColor) {
        this.loveColor = loveColor;
    }

    public float getCruHeight() {
        return cruHeight;
    }


    public int getCruTime() {
        return cruTime;
    }

    public void addCruTime(int dutime) {
        this.cruTime = cruTime + dutime;
        this.cruHeight = (int) (speed * this.cruTime);
        alpha = (int) ((1 - (this.cruHeight / (double) height)) * 255);
        if (cruHeight >= scaleHeight) {
            scale = 1;
        } else {
            scale = this.cruHeight / (double) scaleHeight;
        }
    }


    public double getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getRate() {
        return (float) (rate * scale);
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Path getPath() {
        return path;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setPath(Path path) {
        this.path = path;
    }

}