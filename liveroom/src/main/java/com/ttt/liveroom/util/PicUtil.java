package com.ttt.liveroom.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

/**
 * Created by huanzhang on 2016/4/16.
 */
public class PicUtil {

    /**
     * 根据等级数查找相应的图片。
     */
    @DrawableRes
    public static int getLevelImageId(Context context, int level){
        return context.getResources().getIdentifier("rank_" + level, "drawable",
                context.getPackageName());
    }

    public static void TextViewSpandImg(Context context, TextView textView, int imgId){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgId);
        ImageSpan imgSpan = new ImageSpan(context, bitmap);
        SpannableString spanString = new SpannableString("icon");
        spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(spanString);
    }

    public static void TextViewSpandImg(Context context, TextView textView, int imgId, int widthDimen, int heightDimen){
        //use customize size
        int width = context.getResources().getDimensionPixelSize(widthDimen);
        int height = context.getResources().getDimensionPixelSize(heightDimen);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgId);
        if(bitmap == null){
            return;
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        ImageSpan span = new ImageSpan(context, scaledBitmap);
        SpannableStringBuilder ssb = new SpannableStringBuilder("icon");
        ssb.setSpan(span, 0, ssb.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.append(ssb);
    }
}

