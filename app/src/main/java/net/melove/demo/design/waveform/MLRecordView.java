package net.melove.demo.design.waveform;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import net.melove.demo.design.utils.MLLog;

/**
 * Created by lzan13 on 2016/8/18.
 * 自定义录音控件
 */
public class MLRecordView extends View {

    // 上下文对象
    private Context mContext;

    // 控件画笔
    private Paint mPaint;
    // 控件边界，就是大小
    private RectF mBounds;

    // 控件宽高
    private float mWidth;
    private float mHeight;
    // 控件宽高比
    private float mRatio;

    // 半径
    private float mRadius;
    // 刻度大小
    private float mSmallScale;
    private float mLargeScale;

    public MLRecordView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MLRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MLRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MLRecordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    /**
     * 控件初始化方法
     */
    private void init() {
        // 初始化自定义控件的宽高比
        mRatio = 360.0f / 56.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //        super.onDraw(canvas);
        mPaint = new Paint();
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        // 设置画笔宽度
        mPaint.setStrokeWidth(5.0f);
        // 设置画笔模式
        mPaint.setStyle(Paint.Style.STROKE);
        // 移动画布(0, 0)到控件中心
        canvas.translate(mWidth / 2, mHeight / 2);
        // 画圆圈，参数：1、中心点X坐标，2、中心点Y坐标，3、圆的半径，4、画笔
        canvas.drawCircle(0, 0, mRadius, mPaint);

        //使用 Path 绘制路径文字
        //        canvas.save();
        //        canvas.translate(-mRadius, -mRadius);
        //        Path path = new Path();
        //        path.addArc(new RectF(0, 0, mRadius, mRadius), -180, 360);
        //        Paint citePaint = new Paint(mPaint);
        //        citePaint.setTextSize(24);
        //        citePaint.setStrokeWidth(2);
        //        canvas.drawTextOnPath("Hi! 这是一条测试圆弧路径文字的内容，看看能画成什么样！怎么两遍的不一样呢？", path, 56, 0, citePaint);
        //        canvas.restore();

        Paint tmpPaint = new Paint(mPaint); //小刻度画笔对象
        tmpPaint.setStrokeWidth(4.0f);
        tmpPaint.setStyle(Paint.Style.FILL);
        float y = -mRadius;
        //总刻度数
        int count = 60;

        for (int i = 0; i < count; i++) {
            if (i % 5 == 0) {
                canvas.drawLine(0f, y, 0, y - 36.0f, mPaint);
                tmpPaint.setTextSize(36);
                canvas.drawText(String.valueOf(i / 5 + 1), -12f, y - 72.0f, tmpPaint);

            } else {
                canvas.drawLine(0f, y, 0f, y - 12f, tmpPaint);
            }
            canvas.rotate(360 / count, 0f, 0f); //旋转画纸
        }

        //绘制指针
        tmpPaint.setColor(0x89727272);
        tmpPaint.setStrokeWidth(4);
        canvas.drawCircle(0, 0, 24, tmpPaint);
        tmpPaint.setStyle(Paint.Style.FILL);
        tmpPaint.setColor(0x89212121);
        canvas.drawCircle(0, 0, 12, tmpPaint);
        canvas.drawLine(0, 36, 0, -192, mPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds = new RectF(getLeft(), getTop(), getRight(), getBottom());

        mWidth = mBounds.right - mBounds.left;
        mHeight = mBounds.bottom - mBounds.top;

        if (mWidth > mHeight) {
            mRadius = mHeight / 3;
        } else {
            mRadius = mWidth / 3;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultWidth = Integer.MAX_VALUE;
        /**
         * 测量模式
         * 父布局希望子布局的大小,如果布局里面设置的是固定值,这里取布局里面的固定值和父布局大小值中的最小值.
         * 如果设置的是match_parent,则取父布局的大小
         */
        // 父容器传过来的宽度方向上的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 父控件提供的空间大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // 父容器传过来的高度方向上的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // 父控件提供的空间大小
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 去除 Padding 后的父容器传过来的宽和高，就是父控件允许子空间施展的空间大小
        int spaceWidth = widthSize - getPaddingLeft() - getPaddingRight();
        int spaceHeight = heightSize - getPaddingTop() - getPaddingBottom();

        int width = 0;
        int height = 0;

        // 根据控件模式计算宽度
        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            width = spaceWidth;
        } else {
            // 自己计算模式，需要手动根据控件内容计算出来
            width = defaultWidth;
        }

        // 根据控件模式计算高度
        if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            height = spaceHeight;
        } else {
            // 如果高度不能确定，就根据宽度的大小按比例计算出来
            height = (int) (width * 1.0f / mRatio);
        }

        // 最后调用父类方法,把View的大小告诉父布局。
        setMeasuredDimension(width, height);

        MLLog.i("width:" + width + "| height:" + height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


}
