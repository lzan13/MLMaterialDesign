package net.melove.demo.design.waveform;

import android.graphics.Paint;

/**
 * Created by lzan13 on 2016/8/19.
 */
public class MLViewUtils {


    /**
     * 计算圆弧长度
     *
     * @param radius 圆半径
     * @param angle  夹角度数（非弧度）
     * @return
     */
    public static float getCirclePathLength(float radius, float angle) {
        angle = changeAngleToSingle(angle);
        return (float) (Math.PI * radius * angle / 180);
    }

    /**
     * 将度数转换成0～360之间的值
     *
     * @param angle
     * @return
     */
    public static float changeAngleToSingle(float angle) {
        while (angle >= 360) {
            angle -= 360;
        }
        while (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    /**
     * 获取文字的宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 计算文字的高度
     *
     * @param paint
     * @return
     */
    public static int getTextHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    public static int getTextFullHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.bottom - fm.top);
    }

    public static int getTextBaseLine(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.abs(fm.bottom);
    }
}
