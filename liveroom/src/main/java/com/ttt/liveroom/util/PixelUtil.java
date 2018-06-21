package com.ttt.liveroom.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by huanzhang on 2016/4/15.
 */
public final class PixelUtil {

    public static final int NORMAL_DENSITY = 160;
    public static final float HALF_ADJUST = 0.5f;

    private PixelUtil() {
    }

    /**
     * dp to px.
     *
     * @param value the value
     * @return the int
     */
    public static int dp2px(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / NORMAL_DENSITY) + HALF_ADJUST);
    }

    /**
     * px to dp.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static int px2dp(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) ((value * NORMAL_DENSITY) / scale + HALF_ADJUST);
    }

    /**
     * sp to px.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static int sp2px(Context context, float value) {
        Resources r;
        if (context == null) {
            r = Resources.getSystem();
        } else {
            r = context.getResources();
        }
        float spvalue = value * r.getDisplayMetrics().scaledDensity;
        return (int) (spvalue + HALF_ADJUST);
    }

    /**
     * px to sp.
     *
     * @param value   the value
     * @param context the context
     * @return the int
     */
    public static int px2sp(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value / scale + HALF_ADJUST);
    }

    /**
     * 获取屏
     * @param context
     * @return
     */
    public static int[] getDisplaySize(Context context) {
        int[] size = new int[2];
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        size[0] = dm.heightPixels;
        size[1] = dm.widthPixels;

        return size;
    }

}