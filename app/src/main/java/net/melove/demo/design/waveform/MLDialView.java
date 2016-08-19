package net.melove.demo.design.waveform;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import net.melove.demo.design.R;
import net.melove.demo.design.utils.MLDimenUtil;
import net.melove.demo.design.utils.MLLog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lzan13 on 2016/8/18.
 * 自定义刻度盘控件
 */
public class MLDialView extends View {

    // 上下文对象
    private Context mContext;

    // 定时器
    private Timer mTimer;
    private int time = 0;

    // 控件画笔
    private Paint mPaint;
    // 控件边界，就是大小
    private RectF mBounds;

    // 控件宽高
    private float mWidth;
    private float mHeight;
    // 控件宽高比
    private float mRatio;


    // 刻度盘大刻度和小刻度
    private float smallDialLenght = MLDimenUtil.dp2px(R.dimen.ml_dimen_8);
    private float largeDialLenght = MLDimenUtil.dp2px(R.dimen.ml_dimen_16);
    // 刻度盘刻度的宽度
    private float dialWidth = MLDimenUtil.dp2px(R.dimen.ml_dimen_2);
    // 刻度盘文字大小
    private float dialTextSize = MLDimenUtil.dp2px(R.dimen.ml_size_14);
    private float dialPointer = MLDimenUtil.dp2px(R.dimen.ml_dimen_96);

    // 半径
    private float mRadius;
    // 刻度大小
    private float mSmallScale;
    private float mLargeScale;

    public MLDialView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MLDialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MLDialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MLDialView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    /**
     * 控件初始化方法
     */
    private void init() {
        mTimer = new Timer();
        // 初始化自定义控件的宽高比
        mRatio = 360.0f / 56.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawDial(canvas);

        drawPointer(canvas);

    }

    /**
     * 绘制刻度盘
     */
    private void drawDial(Canvas canvas) {
        mPaint = new Paint();
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        // 设置画笔宽度
        mPaint.setStrokeWidth(dialWidth);
        // 设置画笔模式
        mPaint.setStyle(Paint.Style.STROKE);
        // 移动画布(0, 0)到控件中心
        //        canvas.translate(mWidth / 2, mHeight / 2);
        // 画圆圈，参数：1、中心点X坐标，2、中心点Y坐标，3、圆的半径，4、画笔
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaint);

        // 设置画笔填充样式
        mPaint.setStyle(Paint.Style.FILL);
        //总刻度数
        int count = 60;

        for (int i = 0; i < count; i++) {
            if (i % 5 == 0) {
                mPaint.setStrokeWidth(dialWidth);
                mPaint.setColor(0xddd22a14);
                canvas.drawLine(mWidth / 2, mHeight / 2 - mRadius, mWidth / 2, mHeight / 2 - mRadius - largeDialLenght, mPaint);
                //                canvas.drawText(text, mWidth / 2, mHeight / 2 - mRadius + largeDialLenght, mPaint);
            } else {
                mPaint.setStrokeWidth(dialWidth);
                mPaint.setColor(0xdd212121);
                canvas.drawLine(mWidth / 2, mHeight / 2 - mRadius, mWidth / 2, mHeight / 2 - mRadius - smallDialLenght, mPaint);
            }
            // 旋转画布，1、旋转角度、旋转中心
            canvas.rotate(360 / count, mWidth / 2, mHeight / 2);
        }

        //绘制时间文字
        mPaint.setTextSize(dialTextSize);
        for (int i = 0; i < 12; i++) {
            String text = String.valueOf(i + 1);
            float startX = mWidth / 2 - mPaint.measureText(text) / 3;
            float startY = mHeight / 2 - mRadius + 36;
            float textR = (float) Math.sqrt(Math.pow(mWidth / 2 - startX, 2) + Math.pow(mHeight / 2 - startY, 2));
            float x = (float) (startX + Math.sin(Math.PI / 6 * i) * textR);
            float y = (float) (startY + textR - Math.cos(Math.PI / 6 * i) * textR);
            if (i != 11 && i != 10 && i != 0) {
                y = y + mPaint.measureText(text) / 2;
            } else {
                x = x - mPaint.measureText(text) / 4;
                y = y + mPaint.measureText(text) / 4;
            }
            canvas.drawText(text, x, y, mPaint);
        }
    }

    /**
     * 绘制指针
     */
    private void drawPointer(Canvas canvas) {
        mPaint.setStrokeWidth(4.0f);
        mPaint.setStyle(Paint.Style.FILL);
        //绘制指针
        mPaint.setColor(0x89727272);
        mPaint.setStrokeWidth(4);
        canvas.drawCircle(mWidth / 2, mHeight / 2, 24, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xffe62f17);
        canvas.drawCircle(mWidth / 2, mHeight / 2, 12, mPaint);
        canvas.rotate(time * 6, mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - dialPointer, mPaint);
        canvas.rotate(-time * 6, mWidth / 2, mHeight / 2);
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

    public void startRecord() {
        // 判断定时器是否存在
        if (mTimer == null) {
            mTimer = new Timer();
        } else {
            // 清除定时器任务
            mTimer.purge();
        }
        // 创建定时器任务
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                time++;
                postInvalidate();
            }
        };
        // 开启定时器，并设置定时任务间隔时间
        mTimer.schedule(task, 1000, 1000);
    }

    public void stopRecord() {
        // 清除定时器任务
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        time = 0;
        postInvalidate();
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
