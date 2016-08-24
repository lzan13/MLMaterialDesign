package net.melove.demo.design.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;


import net.melove.demo.design.application.MLApplication;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2015/4/15.
 */
public class MLDimenUtil {

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
    private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";


    public MLDimenUtil() {

    }

    /**
     * 获取屏幕大小
     *
     * @return
     */
    public static Point getScreenSize() {
        WindowManager wm = (WindowManager) MLApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
        return outSize;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        Resources res = MLApplication.getContext().getResources();
        int height = res.getIdentifier("status_bar_height", "dimen", "android");
        height = res.getDimensionPixelSize(height);
        MLLog.i("statusBar.h." + height);

        return height;
    }

    /**
     * 获取NavigationBar的高度（在NavigationBar 存在的情况下）
     *
     * @return
     */
    public static int getNavigationBarHeight() {
        Resources res = MLApplication.getContext().getResources();
        int height = 0;
        if (hasNavigationBar()) {
            String key = NAV_BAR_HEIGHT_RES_NAME;
            height = getInternalDimensionSize(res, key);
        }
        //        MLLog.i("navigationbar.h." + height);
        return height;
    }

    /**
     * 获取系统栏高度
     *
     * @return
     */
    public static int getSystemBarHeight() {
        Resources res = MLApplication.getContext().getResources();
        int height = res.getIdentifier("system_bar_height", "dimen", "android");
        height = res.getDimensionPixelSize(height);
        MLLog.i("systembar.h." + height);

        return height;
    }

    /**
     * 获取ToolBar高度
     *
     * @return
     */
    public static int getToolbarHeight() {
        //        int toolbarHeight = mActivity.getActionBar().getHeight();
        int height = 0;
        if (height != 0) {
            return height;
        }
        TypedValue tv = new TypedValue();
        if (MLApplication.getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            height = TypedValue.complexToDimensionPixelSize(tv.data, MLApplication.getContext().getResources().getDisplayMetrics());
        }
        MLLog.i("toolbar.h." + height);
        return height;
    }


    /**
     * 获取系统控件尺寸方法
     *
     * @param res 资源
     * @param key 获取系统尺寸的key
     * @return
     */
    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 判断是否有虚拟导航栏NavigationBar，
     *
     * @return
     */
    private static boolean hasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = MLApplication.getContext().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            MLLog.e(e.getMessage());
        }
        return hasNavigationBar;
    }

    /**
     * 将控件尺寸的资源转换为像素尺寸
     *
     * @param id 尺寸资源id
     * @return
     */
    public static int getDimenPixel(int id) {
        Resources res = MLApplication.getContext().getResources();
        int result = res.getDimensionPixelSize(id);
        return result;
    }

    /**
     * 将控件尺寸大小转为当前设备下的像素大小
     *
     * @param dp 控件尺寸大小
     * @return
     */
    public static int dp2px(int dp) {
        Resources res = MLApplication.getContext().getResources();
        float density = res.getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * 将字体尺寸大小转为当前设备下的像素尺寸大小
     *
     * @param sp 字体的尺寸大小
     * @return
     */
    public static float sp2px(int sp) {
        Resources res = MLApplication.getContext().getResources();
        float density = res.getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5f);
    }

    /**
     * 获取文字的宽度
     *
     * @param paint 绘制文字的画笔
     * @param str   需要计算宽度的字符串
     * @return 返回字符串宽度
     */
    public static float getTextWidth(Paint paint, String str) {
        float textWidth = 0;
        if (str != null && str.length() > 0) {
            // 记录字符串中每个字符宽度的数组
            float[] widths = new float[str.length()];
            // 获取字符串中每个字符的宽度到数组
            paint.getTextWidths(str, widths);
            for (int i = 0; i < str.length(); i++) {
                textWidth += (float) Math.ceil(widths[i]);
            }
        }
        return textWidth;
    }

    /**
     * 计算文字的高度
     *
     * @param paint 绘制文字的画笔
     * @return 返回字符串高度
     */
    public static float getTextHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }
}
