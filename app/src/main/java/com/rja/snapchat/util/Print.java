package com.rja.snapchat.util;

import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by rjaylward on 3/4/17
 */


public class Print {

    private static boolean mIsDebugMode = true;
    private static String mAppTag = "";
    private static CrashReporter mCrashReporter;

    public static void init(String appTag, boolean debug, @Nullable CrashReporter crashReporter) {
        mIsDebugMode = debug;

        if(mAppTag != null)
            mAppTag = appTag;

        mCrashReporter = crashReporter;
    }

    public static void i(String tag, Object... objects) {
        if(mIsDebugMode) Log.i(tag, join(objects));
    }
    public static void e(String tag, Throwable throwable, Object... objects) {
        if(mIsDebugMode)
            Log.e(tag, join(objects), throwable);
        else if(mCrashReporter != null)
            mCrashReporter.logException(new Exception(join(objects), throwable));
    }
    public static void e(String tag, Object... objects) {
        if(mIsDebugMode)
            Log.e(tag, join(objects));
        else if(mCrashReporter != null)
            mCrashReporter.logException(new Exception(join(objects)));
    }
    public static void d(String tag, Object... objects) {
        if(mIsDebugMode) Log.d(tag, join(objects));
    }
    public static void v(String tag, Object... objects) {
        if(mIsDebugMode) Log.v(tag, join(objects));
    }
    public static void w(String tag, Object... objects) {
        if(mIsDebugMode) Log.w(tag, join(objects));
    }

    public static String join(Object... objects) {
        StringBuilder s = new StringBuilder();
        if(objects != null) {
            for(Object object : objects) {
                s.append(object).append(" ");
            }
        }
        else {
            s.append((String) null);
        }

        return s.toString();
    }

    public interface CrashReporter {
        void log(String message);
        void logException(Exception e);
    }
}
