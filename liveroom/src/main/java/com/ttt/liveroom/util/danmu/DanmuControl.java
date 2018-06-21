package com.ttt.liveroom.util.danmu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;

import com.ttt.liveroom.bean.Danmu;
import com.ttt.liveroom.util.DpOrSp2PxUtil;
import com.ttt.liveroom.util.danmu.danmuview.CenteredImageSpan;
import com.ttt.liveroom.util.danmu.danmuview.CircleDrawable;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * Created by feiyang on 16/3/2.
 */
public class DanmuControl {

    private static final String TAG = "DanmuControl";

    //弹幕显示的时间(如果是list的话，会 * i)，记得加上mDanmakuView.getCurrentTime()
    private static final long ADD_DANMU_TIME = 2000;

    private static final int PINK_COLOR = 0xffff5a93;//粉红 楼主
    private static final int ORANGE_COLOR = 0xffff815a;//橙色 我
    private static final int BLACK_COLOR = 0xb2000000;//黑色 普通

    private int BITMAP_WIDTH = 40;//头像的大小
    private int BITMAP_HEIGHT = 40;
    private float DANMU_TEXT_SIZE = 11f;//弹幕字体的大小
//    private int   EMOJI_SIZE      = 14;//emoji的大小

    //这两个用来控制两行弹幕之间的间距
    private int DANMU_PADDING = 8;
    private int DANMU_PADDING_INNER = 6;
    private int DANMU_RADIUS = 15;//圆角半径

    private final int mGoodUserId = 1;
    private final int mMyUserId = 2;

    private Context mContext;
    private IDanmakuView mDanmakuView;
    private DanmakuContext mDanmakuContext;

    public DanmuControl(Context context) {
        this.mContext = context;
        setSize(context);
        initDanmuConfig();
    }

    /**
     * 对数值进行转换，适配手机，必须在初始化之前，否则有些数据不会起作用
     */
    private void setSize(Context context) {
        BITMAP_WIDTH = DpOrSp2PxUtil.dp2pxConvertInt(context, BITMAP_HEIGHT);
        BITMAP_HEIGHT = DpOrSp2PxUtil.dp2pxConvertInt(context, BITMAP_HEIGHT);
//        EMOJI_SIZE = DpOrSp2PxUtil.dp2pxConvertInt(context, EMOJI_SIZE);
        DANMU_PADDING = DpOrSp2PxUtil.dp2pxConvertInt(context, DANMU_PADDING);
        DANMU_PADDING_INNER = DpOrSp2PxUtil.dp2pxConvertInt(context, DANMU_PADDING_INNER);
        DANMU_RADIUS = DpOrSp2PxUtil.dp2pxConvertInt(context, DANMU_RADIUS);
        DANMU_TEXT_SIZE = DpOrSp2PxUtil.sp2px(context, DANMU_TEXT_SIZE);
    }

    /**
     * 初始化配置
     */
    private void initDanmuConfig() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示2行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuContext = DanmakuContext.create();
        mDanmakuContext
                .setDanmakuStyle(IDisplayer.DANMAKU_STYLE_NONE)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1f)//越大速度越慢
                .setScaleTextSize(1.2f)
                .setCacheStuffer(new BackgroundCacheStuffer(), mCacheStufferAdapter)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
    }

    /**
     * 绘制背景(自定义弹幕样式)
     */
    private float iconWidth;

    private class BackgroundCacheStuffer extends SpannedCacheStuffer {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
//            danmaku.padding = 20;  // 在背景绘制模式下增加padding
            if (danmaku.text instanceof Spanned) {
                if (mProxy != null) {
                    mProxy.prepareDrawing(danmaku, fromWorkerThread);
                }

                CharSequence text = danmaku.text;
                if (text != null) {
                    StaticLayout staticLayout = new StaticLayout(text, paint, (int) StaticLayout.getDesiredWidth(danmaku.text, paint), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                    int iconi = text.toString().indexOf(" ");
//                    头像的为了得到头像的宽度
                    CharSequence textimg = text.subSequence(0, iconi - 1);
                    iconWidth = StaticLayout.getDesiredWidth(textimg, paint);
//                    后面的字符串
                    String iconStr = text.toString().substring(iconi + 1, text.length());
//                    得到文字中间的:分割出名字和内容
                    int namei = iconStr.indexOf(":");

//                    然后得到名字的:符号
                    CharSequence textname = iconStr.toString().substring(0, namei + 1);
//                    然后截取内容的字符串了
                    CharSequence textcontent = iconStr.toString().substring(namei + 1, iconStr.length());
                    if (textcontent.length() == 0) {
//                        为了防止空弹幕
                        textcontent = " ";
                    }

                    StaticLayout staticLayouttop = new StaticLayout(textname, 0, namei, paint, (int) StaticLayout.getDesiredWidth(textname, paint), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                    StaticLayout staticLayoutdown = new StaticLayout(textcontent, namei + 1, textcontent.length(), paint, (int) StaticLayout.getDesiredWidth(textcontent, paint), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
//                    对比名字和内容得出谁最长然后加上头像的长度
                    if (staticLayouttop.getWidth() > staticLayoutdown.getWidth()) {
                        danmaku.paintWidth = staticLayouttop.getWidth() + iconWidth + DANMU_PADDING_INNER;
                    } else {
                        danmaku.paintWidth = staticLayoutdown.getWidth() + iconWidth + DANMU_PADDING_INNER;
                    }
                    danmaku.paintHeight = staticLayouttop.getHeight() * 2;
                    danmaku.obj = new SoftReference<>(staticLayout);

                    return;
                }
            }
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            paint.setAntiAlias(true);
            if (!danmaku.isGuest && danmaku.userId == mGoodUserId && mGoodUserId != 0) {
                paint.setColor(0x26000000);//粉红 楼主
            } else if (!danmaku.isGuest && danmaku.userId == mMyUserId
                    && danmaku.userId != 0) {
                paint.setColor(0x26000000);//橙色 我
            } else {
                paint.setColor(0x26000000);//黑色 普通
            }
            if (danmaku.isGuest) {//如果是赞 就不要设置背景
                paint.setColor(0x26000000);
            }
            canvas.drawRoundRect(new RectF(left + 40, top + DANMU_PADDING_INNER
                            , left + danmaku.paintWidth - DANMU_PADDING_INNER + 6,
                            top + danmaku.paintHeight - DANMU_PADDING_INNER + 6),//+6 主要是底部被截得太厉害了，+6是增加padding的效果
                    DANMU_RADIUS, DANMU_RADIUS, paint);
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
        }

        @Override
        public void drawText(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, TextPaint paint, boolean fromWorkerThread) {
            if (danmaku.obj == null) {
                super.drawText(danmaku, lineText, canvas, left, top, paint, fromWorkerThread);
                return;
            }
            SoftReference<StaticLayout> reference = (SoftReference<StaticLayout>) danmaku.obj;
            StaticLayout staticLayout = reference.get();
            StaticLayout staticLayout1 = reference.get();
            ;
            boolean requestRemeasure = 0 != (danmaku.requestFlags & BaseDanmaku.FLAG_REQUEST_REMEASURE);
            boolean requestInvalidate = 0 != (danmaku.requestFlags & BaseDanmaku.FLAG_REQUEST_INVALIDATE);

            if (requestInvalidate || staticLayout == null) {
                if (requestInvalidate) {
                    danmaku.requestFlags &= ~BaseDanmaku.FLAG_REQUEST_INVALIDATE;
                } else if (mProxy != null) {
                    mProxy.prepareDrawing(danmaku, fromWorkerThread);
                }
                CharSequence text = danmaku.text;
                if (text != null) {
                    if (requestRemeasure) {
                        danmaku.paintWidth = staticLayout.getWidth();
                        danmaku.paintHeight = staticLayout.getHeight();
                        danmaku.requestFlags &= ~BaseDanmaku.FLAG_REQUEST_REMEASURE;
                    } else {
                        staticLayout = new StaticLayout(text, paint, (int) danmaku.paintWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                    }
                    danmaku.obj = new SoftReference<>(staticLayout);
                } else {
                    return;
                }
            }
            boolean needRestore = false;
            if (left != 0 && top != 0) {
                canvas.save();
                canvas.translate(left, top + paint.ascent());
                needRestore = true;
            }
//            这里先分出名字和头像
            if (danmaku.text != null) {
//         这个是得到得到名字和内容中间的那个分割父
                int icon = danmaku.text.toString().indexOf(":");
//            先拆分出内容
                CharSequence str = danmaku.text.toString().substring(icon + 1, danmaku.text.length());
//              这个是上半部分
                paint.setColor(0xffff59a5);
                staticLayout = new StaticLayout(danmaku.text, 0, icon + 1, paint, (int) StaticLayout.getDesiredWidth(danmaku.text, paint), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                staticLayout.draw(canvas);
//            移动画布显示内容
                paint.setColor(0xffffffff);
                canvas.translate(iconWidth, staticLayout.getHeight());
                staticLayout1 = new StaticLayout(str, paint, (int) StaticLayout.getDesiredWidth(str, paint), Layout.Alignment.ALIGN_OPPOSITE, 1.0f, 0.0f, true);
                staticLayout1.draw(canvas);
                if (needRestore) {
                    canvas.restore();
                }
            }
        }
    }

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
//            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
//            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
            if (danmaku.text instanceof Spanned) {
                danmaku.text = "";
            }
        }
    };

    public void setDanmakuView(IDanmakuView danmakuView) {
        this.mDanmakuView = danmakuView;
        initDanmuView();
    }

    private void initDanmuView() {
        if (mDanmakuView != null) {
            mDanmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    mDanmakuView.start();
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void drawingFinished() {

                }
            });
        }

        mDanmakuView.prepare(new BaseDanmakuParser() {

            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        }, mDanmakuContext);
        mDanmakuView.enableDanmakuDrawingCache(true);
    }

    public void pause() {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    public void hide() {
        if (mDanmakuView != null) {
            mDanmakuView.hide();
        }
    }

    public void show() {
        if (mDanmakuView != null) {
            mDanmakuView.show();
        }
    }

    public void resume() {
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    public void destroy() {
        if (mDanmakuView != null) {
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    public void addDanmuList(final List<Danmu> danmuLists) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < danmuLists.size(); i++) {
                    addDanmu(danmuLists.get(i), i);
                }
            }
        }).start();
    }

    public void addDanmu(Danmu danmu, int i) {
        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.userId = danmu.userId;
        danmaku.isGuest = danmu.type.equals("Like");//isGuest此处用来判断是赞还是评论
        SpannableStringBuilder spannable;
        Bitmap bitmap = getDefaultBitmap(danmu.avatarUrl);
        CircleDrawable circleDrawable = new CircleDrawable(mContext, bitmap, danmaku.isGuest);
        circleDrawable.setBounds(0, 0, BITMAP_WIDTH, BITMAP_HEIGHT);
        spannable = createSpannable(circleDrawable, danmu.content);
        danmaku.text = spannable;
        danmaku.padding = DANMU_PADDING;
        danmaku.priority = 0;  // 1:一定会显示, 一般用于本机发送的弹幕,但会导致行数的限制失效
        danmaku.isLive = false;
        danmaku.time = mDanmakuView.getCurrentTime() + (i * ADD_DANMU_TIME);
        danmaku.textSize = DANMU_TEXT_SIZE/* * (mDanmakuContext.getDisplayer().getDensity() - 0.6f)*/;
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        mDanmakuView.addDanmaku(danmaku);
    }

    private Bitmap getDefaultBitmap(Bitmap bitmap) {
        Bitmap mDefauleBitmap = null;
//        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawableId);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Log.d(TAG, "width = " + width);
            Log.d(TAG, "height = " + height);
            Matrix matrix = new Matrix();
            matrix.postScale(((float) BITMAP_WIDTH) / width, ((float) BITMAP_HEIGHT) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            Log.d(TAG, "mDefauleBitmap getWidth = " + mDefauleBitmap.getWidth());
            Log.d(TAG, "mDefauleBitmap getHeight = " + mDefauleBitmap.getHeight());
        }
        return mDefauleBitmap;
    }

    private SpannableStringBuilder createSpannable(Drawable drawable, String content) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        CenteredImageSpan span = new CenteredImageSpan(drawable);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (!TextUtils.isEmpty(content)) {
            spannableStringBuilder.append(" ");
            spannableStringBuilder.append(content.trim());
        }
        return spannableStringBuilder;
    }
}
