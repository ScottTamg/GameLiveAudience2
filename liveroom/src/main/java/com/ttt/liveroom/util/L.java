package com.ttt.liveroom.util;

import android.support.annotation.NonNull;
import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Application-based log class.
 * <p>
 * Usages of this class is same as android.util.Log, but you can modify the log
 * implementation of this class by just modify the methods, e.g. call a third-class log class.
 * </p>
 *
 * @author Muyangmin
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class L implements HttpLoggingInterceptor.Logger {
    //a non-strict singleton
    public static final L INSTANCE = new L();

    @Override
    public void log(String message) {
    }

    /**
     * This method logs verbose information.
     *
     * @param
     */
    public static void v(@NonNull String tag, @NonNull String msg) {
        Log.v(tag, msg);
    }

    public static void wtf(@NonNull String tag, @NonNull String msg) {
        Log.wtf(tag, msg);
    }

    /**
     * call {@link #e(String, String, Throwable)} with shouldReport set to false.
     */
    public static void e(@NonNull String tag, @NonNull String msg, Throwable tr) {
        Log.e(tag, msg, tr);
    }


    public static void v(@NonNull String tag, @NonNull String format, Object... args) {
        Log.v(tag, String.format(format, args));
    }

    public static void v(boolean shouldReport, @NonNull String tag, @NonNull String format,
                         Object... args) {
        v(tag, String.format(format, args));
    }

    public static void d(@NonNull String tag, @NonNull String format, Object... args) {
    }

    public static void i(@NonNull String tag, @NonNull String format, Object... args) {
       // Log.i(tag, String.format(format, args));
    }

    public static void w(@NonNull String tag, @NonNull String format, Object... args) {
        Log.w(tag, String.format(format, args));
    }

    public static void e(@NonNull String tag, @NonNull String format, Object... args) {
        Log.e(tag, String.format(format, args));
    }

}
