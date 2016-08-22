package net.melove.demo.design.waveform;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import net.melove.demo.design.R;
import net.melove.demo.design.utils.MLDateUtil;
import net.melove.demo.design.utils.MLDimenUtil;
import net.melove.demo.design.utils.MLLog;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lzan13 on 2016/8/18.
 * 自定义录音控件
 */
public class MLRecordView extends View {

    // 上下文对象
    protected Context mContext;
    // 录音控件回调接口
    protected MLRecordCallback mRecordCallback;

    // 波形信息集合
    protected LinkedList<Integer> waveformList;

    // 自定义View画笔
    protected Paint viewPaint;
    protected int transparentColor = 0x00ffffff;

    // 控件边界，就是大小
    protected RectF viewBounds;
    // 控件宽高比
    protected float viewRatio;
    // 控件宽高
    protected float viewWidth;
    protected float viewHeight;

    // 定时器
    protected Timer mTimer;
    // 录制开始时间
    protected long startTime = 0L;
    // 录制持续时间
    protected long recordTime = 0L;

    // 触摸区域颜色
    protected int touchColor;
    // 触摸区域图标资源
    protected int touchIcon;
    // 触摸区域大小
    protected int touchSize;
    // 触摸区域中心点位置
    protected float centerX;

    // 波形刻度颜色
    protected int waveformColor;
    // 波形刻度间隔
    protected int waveformInterval;
    // 波形刻度宽度
    protected int waveformWidth;

    // 指示器颜色
    protected int indicatorColor;
    // 指示器大小
    protected int indicatorSize;

    // 文字颜色
    protected int textColor;
    // 文字大小
    protected int textSize;


    public MLRecordView(Context context) {
        this(context, null);
    }

    public MLRecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MLRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MLRecordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(context, attrs);
    }

    /**
     * 控件初始化方法
     */
    protected void init(Context context, AttributeSet attrs) {
        mTimer = new Timer();

        waveformList = new LinkedList<Integer>();

        // 默认高度 56dp
        viewHeight = MLDimenUtil.dp2px(R.dimen.ml_dimen_56);
        // 初始化自定义控件的宽高比
        viewRatio = 360.0f / 56.0f;

        // 触摸区域相关参数
        touchColor = 0xdd2384fe;
        touchSize = MLDimenUtil.dp2px(R.dimen.ml_dimen_56);
        touchIcon = R.mipmap.ic_call_white_24dp;

        // 波形刻度相关参数
        waveformColor = 0xddff5722;
        waveformInterval = MLDimenUtil.dp2px(R.dimen.ml_dimen_1);
        waveformWidth = MLDimenUtil.dp2px(R.dimen.ml_dimen_2);

        // 默认指示器相关参数
        indicatorColor = 0xddd22a14;
        indicatorSize = MLDimenUtil.dp2px(R.dimen.ml_dimen_8);

        // 文字相关参数
        textColor = 0x89212121;
        textSize = MLDimenUtil.dp2px(R.dimen.ml_size_14);

        // 获取控件的属性值
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MLRecordView);
            // 获取自定义属性值，如果没有设置就是默认值
            touchColor = array.getColor(R.styleable.MLRecordView_ml_touch_color, touchColor);
            touchIcon = array.getResourceId(R.styleable.MLRecordView_ml_touch_icon, touchIcon);
            touchSize = array.getDimensionPixelOffset(R.styleable.MLRecordView_ml_touch_radius, touchSize);

            waveformColor = array.getColor(R.styleable.MLRecordView_ml_waveform_color, waveformColor);
            waveformInterval = array.getDimensionPixelOffset(R.styleable.MLRecordView_ml_waveform_interval, waveformInterval);
            waveformWidth = array.getDimensionPixelOffset(R.styleable.MLRecordView_ml_waveform_width, waveformWidth);

            indicatorColor = array.getColor(R.styleable.MLRecordView_ml_indicator_color, indicatorColor);
            indicatorSize = array.getDimensionPixelOffset(R.styleable.MLRecordView_ml_indicator_radius, indicatorSize);

            textColor = array.getColor(R.styleable.MLRecordView_ml_text_color, textColor);
            textSize = array.getDimensionPixelOffset(R.styleable.MLRecordView_ml_text_size, textSize);
            // 回收资源
            array.recycle();
        }

        // 实例化画笔
        viewPaint = new Paint();
        // 设置抗锯齿
        viewPaint.setAntiAlias(true);
        // 设置画笔线条结尾样式
        viewPaint.setStrokeCap(Paint.Cap.ROUND);

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
    protected void drawTimeText(Canvas canvas) {
        viewPaint.setColor(textColor);
        viewPaint.setTextSize(textSize);
        String timeText = "";
        int minute = (int) (recordTime / 1000 / 60);
        if (minute < 10) {
            timeText = "0" + minute;
        } else {
            timeText = "" + minute;
        }
        int seconds = (int) (recordTime / 1000 % 60);
        if (seconds < 10) {
            timeText = timeText + ":0" + seconds;
        } else {
            timeText = timeText + ":" + seconds;
        }
        int millisecond = (int) (recordTime % 1000 / 100);
        timeText = timeText + "." + millisecond;
        float textWidth = viewPaint.measureText(timeText);
        canvas.drawText(timeText, viewHeight / 2 + textWidth / 2, viewHeight / 2 + textSize / 2, viewPaint);
    }

    /**
     * 绘制指示器
     * shi
     *
     * @param canvas
     */
    protected void drawIndicator(Canvas canvas) {
        // 设置画笔模式
        viewPaint.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        viewPaint.setColor(indicatorColor);

        canvas.drawCircle(viewHeight / 2, viewHeight / 2, indicatorSize / 2, viewPaint);
    }

    /**
     * 绘制波形
     */
    protected void drawWaveform(Canvas canvas) {
        // 设置画笔模式
        viewPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔宽度
        viewPaint.setStrokeWidth(waveformWidth);
        // 设置画笔颜色
        viewPaint.setColor(waveformColor);
        int count = (int) (viewWidth / 2 / (waveformWidth + waveformInterval));
        for (int i = 0; i < count; i++) {
            float startX = viewWidth / 4 + i * (waveformInterval + waveformWidth);
            float waveformHeight = 0;
            if (i < waveformList.size()) {
                waveformHeight = waveformList.get(i);
            }
            waveformHeight = waveformHeight * viewHeight / 10 + 2;
            canvas.drawLine(startX, viewHeight / 2 - waveformHeight / 2, startX, viewHeight / 2 + waveformHeight / 2, viewPaint);
        }
    }

    /**
     * 绘制触摸时圆形区域
     */
    protected void drawCircle(Canvas canvas) {
        // 设置画笔模式
        viewPaint.setStyle(Paint.Style.FILL);
        // 根据录音机工作状态设置画笔颜色
        if (MLRecorder.getInstance().isRecording()) {
            viewPaint.setColor(touchColor);
        } else {
            viewPaint.setColor(transparentColor);
        }
        // 绘制触摸区域的圆
        canvas.drawCircle(centerX, viewHeight / 2, touchSize / 2, viewPaint);

        // 绘制触摸区域的图标
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), touchIcon);
        viewPaint.setColor(waveformColor);
        canvas.drawBitmap(bitmap, centerX - bitmap.getWidth() / 2, viewHeight / 2 - bitmap.getHeight() / 2, viewPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewBounds = new RectF(getLeft(), getTop(), getRight(), getBottom());

        viewWidth = viewBounds.right - viewBounds.left;
        viewHeight = viewBounds.bottom - viewBounds.top;

        centerX = viewWidth - viewHeight / 2;
    }

    /**
     * 开始录制
     */
    public void startRecord(String path) {
        // 调用录音机开始录制音频
        int recordError = MLRecorder.getInstance().startRecordVoice(path);
        if (recordError == MLRecorder.ERROR_NONE) {
            // 开始录音
            // 初始化开始录制时间
            startTime = MLDateUtil.getCurrentMillisecond();
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
                    int decibel = MLRecorder.getInstance().getVoiceWaveform();
                    waveformList.addFirst(decibel);
                    MLLog.i("麦克风监听声音分贝：%d", decibel);
                    postInvalidate();
                }
            };
            // 开启定时器，并设置定时任务间隔时间
            mTimer.schedule(task, 500, 100);
        } else if (recordError == MLRecorder.ERROR_RECORDING) {
            // 录音进行中
        } else if (recordError == MLRecorder.ERROR_SYSTEM) {
            // 媒体录音器准备失败，调用取消
            MLRecorder.getInstance().cancelRecordVoice();
            mRecordCallback.onFailed(recordError);
        } else {

        }

    }

    /**
     * 停止录制
     */
    public void stopRecord() {
        // 调用录音机停止录制
        int recordError = MLRecorder.getInstance().stopRecordVoice();
        // 清除定时器任务
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if (recordError == MLRecorder.ERROR_NONE) {
            // 录音成功
            mRecordCallback.onSuccess(MLRecorder.getInstance().getRecordFilePath());
        } else if (recordError == MLRecorder.ERROR_FAILED) {
            // 录音失败
            mRecordCallback.onFailed(recordError);
        }
        recordTime = 0L;
        // 刷新UI
        postInvalidate();
    }

    /**
     * 取消录制
     */
    public void cancelRecord() {
        // 调用录音机停止录制
        int recordError = MLRecorder.getInstance().cancelRecordVoice();
        // 清除定时器任务
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if (recordError == MLRecorder.ERROR_CANCEL) {
            // 取消录音
            mRecordCallback.onCancel();
        }
        recordTime = 0L;
        postInvalidate();
    }

    /**
     * 重写 onTouchEvent 监听方法，用来响应控件触摸
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 触摸点横坐标
        float x = event.getX();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            MLLog.i("touch x %g", x);
            // 按下后更改触摸区域半径
            touchSize = MLDimenUtil.dp2px(R.dimen.ml_dimen_96);
            if (x < viewWidth - viewHeight) {
                return false;
            }
            // 触摸开始录音
            startRecord(null);
            postInvalidate();
            break;
        case MotionEvent.ACTION_UP:
            centerX = viewWidth - viewHeight / 2;
            // 抬起后更改触摸区域半径
            touchSize = MLDimenUtil.dp2px(R.dimen.ml_dimen_56);
            // 根据向左滑动的距离判断是正常停止录制，还是取消录制
            if (x > viewWidth / 3 * 2) {
                // 抬起停止录制
                stopRecord();
            } else {
                // 取消录制
                cancelRecord();
            }
            postInvalidate();
            break;
        case MotionEvent.ACTION_MOVE:
            if (x < viewWidth - viewHeight / 2) {
                centerX = x;
            } else {
                centerX = viewWidth - viewHeight / 2;
            }
            postInvalidate();
            break;
        default:
            break;
        }
        // 这里不调用系统的onTouchEvent方法，防止抬起时画面无法重绘
        // return super.onTouchEvent(event);
        return true;
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
            height = (int) (width * 1.0f / viewRatio);
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
     * 设置录音回调
     *
     * @param callback
     */
    public void setRecordCallback(MLRecordCallback callback) {
        mRecordCallback = callback;
    }

    /**
     * 录音控件的回调接口，用于回调给调用者录音结果
     */
    public interface MLRecordCallback {

        /**
         * 录音取消
         */
        public void onCancel();

        /**
         * 录音失败
         *
         * @param error
         */
        public void onFailed(int error);

        /**
         * 录音成功
         *
         * @param path
         */
        public void onSuccess(String path);

    }

}
