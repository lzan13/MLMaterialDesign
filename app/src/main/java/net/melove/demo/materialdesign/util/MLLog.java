package net.melove.demo.materialdesign.util;

import android.util.Log;

/**
 * Created by lzan13 on 2015/12/1.
 * <p/>
 * log日志输出封装类
 */
public class MLLog {
    private static final String TAG = "melove ";
    private static boolean isDebug = true;

    private static MLLogPrinter mPrinter = new MLLogPrinter();

    /**
     * 设置debug模式是否开启
     *
     * @param mode
     */
    public static void setDebugMode(boolean mode) {
        isDebug = mode;
    }

    private static String getClassName() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        String result = elements[2].getClassName();
        return result;
    }

    public static void i(String log) {
        if (isDebug) {
            Log.i(TAG, getClassName() + " i: " + log);
        }
    }

    public static void d(String log) {
        if (isDebug) {
            Log.i(TAG, "d: " + log);
        }
    }

    public static void e(String log) {
        if (isDebug) {
            Log.i(TAG, "e: " + log);
        }
    }

    public static void i(String tag, String log) {
        if (isDebug) {
            Log.i(tag, "i: " + log);
        }
    }

    public static void d(String tag, String log) {
        if (isDebug) {
            Log.i(tag, "d: " + log);
        }
    }

    public static void e(String tag, String log) {
        if (isDebug) {
            Log.i(tag, "e: " + log);
        }
    }

    private static class MLLogPrinter {


    }
}
