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
import net.melove.demo.design.utils.MLDateUtil;
import net.melove.demo.design.utils.MLDimenUtil;
import net.melove.demo.design.utils.MLLog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lzan13 on 2016/8/18.
 * 自定义录音控件
 */
public class MLRecordView extends View {

    // 上下文对象
    private Context mContext;

    // 定时器
    private Timer mTimer;
    // 录制开始时间
    private long startTime = 0L;
    // 录制持续时间
    private long recordTime = 0L;

    // 控件边界，就是大小
    private RectF mBounds;
    // 控件宽高
    private float mWidth;
    private float mHeight;

    // 录制触摸区域画笔
    private Paint mPaint;

    // 控件宽高比
    private float ratio;
    // 触摸区域颜色
    private int touchColor = 0xdd2384fe;

    // 录制触摸区域半径
    private float mDialRadius;
    // 波形刻度宽度
    private float dialWidth;
    // 波形刻度间隔宽度
    private float dialInterval;
    // 波形刻度颜色
    private int dialColor = 0x89ff5722;

    // 指示器颜色
    private int indicatorColor = 0xdde62f17;
    // 指示器大小
    private int indicatorRadius;

    // 文字颜色
    private int textColor = 0xdd212121;
    private int textSize = MLDimenUtil.dp2px(R.dimen.ml_size_14);
    private String timeText = "00:00";


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
        mTimer = new Timer();
        // 初始化自定义控件的宽高比
        ratio = 360.0f / 56.0f;
        mDialRadius = MLDimenUtil.dp2px(R.dimen.ml_dimen_56) / 2;

        // 波形刻度相关参数
        dialInterval = MLDimenUtil.dp2px(R.dimen.ml_dimen_1);
        dialWidth = MLDimenUtil.dp2px(R.dimen.ml_dimen_2);

        // 指示器相关参数
        indicatorRadius = MLDimenUtil.dp2px(R.dimen.ml_dimen_8);

        // 实例化画笔
        mPaint = new Paint();
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
        // 设置画笔线条结尾样式
        mPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制触摸区域
        drawCircle(canvas);
        // 绘制波形刻度部分
        drawWaveform(canvas);
        // 绘制指示器
        drawIndicator(canvas);
        // 绘制文字
        drawTimeText(canvas);
    }

    /**
     * 画文字
     *
     * @param canvas
     */
    private void drawTimeText(Canvas canvas) {
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);

        float textWidth = mPaint.measureText(timeText);
        canvas.drawText(timeText, mHeight / 2 + textWidth / 2, mHeight / 2 + textSize / 2, mPaint);
    }

    /**
     * 绘制指示器
     *
     * @param canvas
     */
    private void drawIndicator(Canvas canvas) {
        // 设置画笔模式
        mPaint.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        mPaint.setColor(indicatorColor);

        canvas.drawCircle(mHeight / 2, mHeight / 2, indicatorRadius, mPaint);
    }

    /**
     * 绘制波形
     */
    private void drawWaveform(Canvas canvas) {
        // 设置画笔模式
        mPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔宽度
        mPaint.setStrokeWidth(dialWidth);
        // 设置画笔颜色
        mPaint.setColor(dialColor);
        int count = (int) (mWidth / 2 / (dialWidth + dialInterval));
        for (int i = 0; i < count; i++) {
            float startX = mWidth / 4 + i * (dialInterval + dialWidth);
            float dialHeight = (float) (Math.random() * 90);
            canvas.drawLine(startX, mHeight / 2 - dialHeight / 2, startX, mHeight / 2 + dialHeight / 2, mPaint);
        }
    }

    /**
     * 绘制触摸时圆形区域
     */
    private void drawCircle(Canvas canvas) {
        // 设置画笔模式
        mPaint.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        mPaint.setColor(touchColor);
        float cx = mWidth - mDialRadius;
        float cy = mHeight / 2;
        canvas.drawCircle(cx, cy, mDialRadius, mPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds = new RectF(getLeft(), getTop(), getRight(), getBottom());

        mWidth = mBounds.right - mBounds.left;
        mHeight = mBounds.bottom - mBounds.top;

        //        if (mWidth > mHeight) {
        //            mDialRadius = mHeight / 3;
        //        } else {
        //            mDialRadius = mWidth / 3;
        //        }
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
            height = (int) (width * 1.0f / ratio);
        }

        // 最后调用父类方法,把View的大小告诉父布局。
        setMeasuredDimension(width, height);
        MLLog.i("width:" + width + "| height:" + height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 开始录制
     */
    public MLRecordManager.MLRecordError startRecord(String path) {
        // 调用录制系统开始录制音频
        MLRecordManager.MLRecordError recordError = MLRecordManager.getInstance().startRecordVoice(path);
        if (recordError != MLRecordManager.MLRecordError.ERROR_NONE) {
            return recordError;
        }
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
                recordTime = MLDateUtil.getCurrentMillisecond() - startTime;
                double decibel = MLRecordManager.getInstance().getVoiceDecibel();
                MLLog.i("麦克风监听声音分贝：%g", decibel);
                postInvalidate();
            }
        };
        // 开启定时器，并设置定时任务间隔时间
        mTimer.schedule(task, 500, 500);
        return recordError;
    }

    /**
     * 停止录制
     */
    public MLRecordManager.MLRecordError stopRecord() {
        // 调用录制系统停止录制方法
        MLRecordManager.MLRecordError recordError = MLRecordManager.getInstance().stopRecordVoice();
        // 清除定时器任务
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        postInvalidate();
        return recordError;
    }


}
