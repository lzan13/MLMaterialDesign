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

/**
 * Created by lzan13 on 2016/8/18.
 * 自定义录音控件
 */
public class MLRecordView extends View {

    // 控件画笔
    private Paint mPaint;
    // 控件边界，就是大小
    private RectF mBounds;

    // 控件宽高
    private float mWidth;
    private float mHeight;

    // 半径
    private float mRadius;
    // 刻度大小
    private float mSmallScale;
    private float mLargeScale;

    public MLRecordView(Context context) {
        super(context);
    }

    public MLRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MLRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MLRecordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        //        canvas.translate(canvas.getWidth() / 2, 200); //将位置移动画纸的坐标点:150,150
        // 画圆圈
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), mRadius, mPaint);

        //使用path绘制路径文字
        canvas.save();
        canvas.translate(-75, -75);
        Path path = new Path();
        path.addArc(new RectF(0, 0, 150, 150), -180, 180);
        Paint citePaint = new Paint(mPaint);
        citePaint.setTextSize(14);
        citePaint.setStrokeWidth(1);
        canvas.drawTextOnPath("http://www.android777.com", path, 28, 0, citePaint);
        canvas.restore();

        Paint tmpPaint = new Paint(mPaint); //小刻度画笔对象
        tmpPaint.setStrokeWidth(1);

        float y = 100;
        int count = 60; //总刻度数

        for (int i = 0; i < count; i++) {
            if (i % 5 == 0) {
                canvas.drawLine(0f, y, 0, y + 12f, mPaint);
                canvas.drawText(String.valueOf(i / 5 + 1), -4f, y + 25f, tmpPaint);

            } else {
                canvas.drawLine(0f, y, 0f, y + 5f, tmpPaint);
            }
            canvas.rotate(360 / count, 0f, 0f); //旋转画纸
        }

        //绘制指针
        tmpPaint.setColor(Color.GRAY);
        tmpPaint.setStrokeWidth(4);
        canvas.drawCircle(0, 0, 7, tmpPaint);
        tmpPaint.setStyle(Paint.Style.FILL);
        tmpPaint.setColor(Color.YELLOW);
        canvas.drawCircle(0, 0, 5, tmpPaint);
        canvas.drawLine(0, 10, 0, -65, mPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds = new RectF(getLeft(), getTop(), getRight(), getBottom());

        mWidth = mBounds.right - mBounds.left;
        mHeight = mBounds.bottom = mBounds.top;

        if (mWidth > mHeight) {
            mRadius = mHeight / 3;
        } else {
            mRadius = mWidth / 3;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 测量模式
         * 父布局希望子布局的大小,如果布局里面设置的是固定值,这里取布局里面的固定值和父布局大小值中的最小值.
         * 如果设置的是match_parent,则取父布局的大小
         */
        // 父容器传过来的宽度方向上的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // 父容器传过来的高度方向上的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 父容器传过来的宽和高，就是父控件允许子空间施展的空间大小
        int spaceWidth = widthSize - getPaddingLeft() - getPaddingRight();
        int spaceHeight = heightSize - getPaddingTop() - getPaddingBottom();

        int width = 0, height = 0;

        // 根据控件模式计算宽度
        if (widthMode == MeasureSpec.EXACTLY) {
            width = spaceWidth;
        } else {
            // 自己计算模式，需要手动根据控件内容计算出来
            // mWidth = getDefaultWidth();
            if (widthMode == MeasureSpec.AT_MOST) {
                // 父控件指定了最大尺寸模式
//                width = Math.min(width, spaceWidth);
            }
        }

        // 根据控件模式计算高度
        if (heightMode == MeasureSpec.EXACTLY) {
            height = spaceHeight;
        } else {
            // 自己计算模式，需要手动根据控件内容计算出来
            // mHeight = getDefaultHeight();
            if (heightMode == MeasureSpec.AT_MOST) {
                // 父控件指定了最大尺寸模式
//                height = Math.min(height, spaceHeight);
            }
        }

        // 最后调用父类方法,把View的大小告诉父布局。
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


}
