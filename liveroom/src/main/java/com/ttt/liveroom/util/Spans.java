package com.ttt.liveroom.util;

import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

/**
 * 该类主要提供创建Spannable字符串的各种辅助方法。
 * Created by Muyangmin on 15-8-27.
 */
@SuppressWarnings("unused")
public final class Spans {
    /**
     * 构造一个Span字符串，所有的span对象将被应用于后半部分。
     *
     * @param content 第一部分内容。
     * @param suffix  第二部分内容。
     * @param spans   要设置的span对象，可以有一个或多个。如果一个参数都没有，将会抛出异常。
     * @return 返回构造的字符串。
     */
    public static CharSequence createSpan(@NonNull CharSequence content, @NonNull CharSequence suffix,
                                          Object...
            spans) {
        if (spans.length == 0) {
            throw new IllegalArgumentException("At lease one span object is required!");
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int start = ssb.append(content).length();
        int end = ssb.append(suffix).length();
        for (Object span : spans) {
            ssb.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }

    /**
     * 使用三个子串构造一个Span字符串，所有的span对象将被应用于中间部分。
     *
     * @param prefix  前缀。
     * @param content 即将被设定span的内容。
     * @param suffix  后缀。
     * @param spans   要设置的span对象。
     * @return 返回构造的Span字符串。
     */
    public static CharSequence createSpan(@NonNull CharSequence prefix, @NonNull CharSequence content,
                                          CharSequence suffix, Object... spans) {
        if (spans.length == 0) {
            throw new IllegalArgumentException("At lease one span object is required!");
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int start = ssb.append(prefix).length();
        int end = ssb.append(content).length();
        ssb.append(suffix);
        for (Object span : spans) {
            ssb.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }
}