package net.melove.demo.design.waveform;

import android.annotation.TargetApi;
import android.content.Context;
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
import net.melove.demo.design.utils.MLDimenUtil;

/**
 * Created by lzan13 on 2016/8/18.
 * 波形测试绘制类
 */
public class MLWaveformView extends View {

    // 上下文对象
    protected Context mContext;

    protected MLWaveformCallback mWaveformCallback;

    // 点坐标集合，四个一组
    protected float[] waveformPoints;
    // 采集到的声音信息
    protected byte[] waveformBytes;
    // 当前进度位置
    protected int position;

    // 控件布局边界
    protected RectF viewBounds;
    // 控件宽高
    protected float viewWidth;
    protected float viewHeight;

    // 波形部分画笔
    protected Paint waveformPaint;
    // 触摸部分画笔
    protected Paint touchPaint;

    // 波形刻度颜色
    protected int waveformColor;
    // 波形间隔
    protected float waveformInterval;
    // 波形宽度
    protected float waveformWidth;

    // 触摸部分颜色
    protected int touchColor;
    // 触摸部分图标
    protected int touchIcon;
    // 触摸部分大小
    protected float touchSize;
    // 触摸区域中心坐标
    protected float touchCenterX;
    protected float touchCenterY;

    // 是否在触摸区域
    protected boolean isTouch = false;
    // 是否正在播放
    protected boolean isPlay = false;


    public MLWaveformView(Context context) {
        this(context, null);
    }

    public MLWaveformView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MLWaveformView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MLWaveformView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    /**
     * 初始化操作
     */
    private void init(AttributeSet attrs) {

        waveformBytes = null;

        // 初始化画笔
        waveformPaint = new Paint();
        // 设置画笔抗锯齿
        waveformPaint.setAntiAlias(true);

        // 从前边的画笔创建新的画笔
        touchPaint = new Paint(waveformPaint);

        // 波形部分默认参数
        waveformColor = 0xddff5722;
        waveformInterval = MLDimenUtil.getDimenPixel(R.dimen.ml_dimen_0_5);
        waveformWidth = MLDimenUtil.getDimenPixel(R.dimen.ml_dimen_2);

        // 触摸部分默认参数
        touchColor = 0x89ff5722;
        touchIcon = R.mipmap.ic_play_arrow_white_24dp;
        touchSize = MLDimenUtil.getDimenPixel(R.dimen.ml_dimen_32);

    }

    /**
     * 更新 FFT 频域数据
     *
     * @param bytes    采集到的数据信息
     * @param position 音源当前播放位置
     */
    public void updateFFTDate(byte[] bytes, int position) {
        waveformBytes = bytes;
        invalidate();
    }

    /**
     * 更新波形数据
     *
     * @param bytes    采集到的数据信息
     * @param position 音源当前播放位置
     */
    public void updateWaveformData(byte[] bytes, int position) {
        waveformBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawWaveform(canvas);

        drawTouchIcon(canvas);
    }

    /**
     * 回执触摸图标
     *
     * @param canvas 当前控件的画布
     */
    protected void drawTouchIcon(Canvas canvas) {
        touchPaint.setColor(touchColor);
        canvas.drawCircle(touchCenterX, touchCenterY, touchSize / 2, touchPaint);

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), touchIcon);
        canvas.drawBitmap(bitmap, touchCenterX - bitmap.getWidth() / 2, touchCenterY - bitmap.getHeight() / 2, touchPaint);
    }

    /**
     * 绘制波形
     *
     * @param canvas 当前控件的画布
     */
    private void drawWaveform(Canvas canvas) {
        // 设置画笔颜色
        waveformPaint.setColor(waveformColor);
        // 设置画笔宽度
        waveformPaint.setStrokeWidth(waveformWidth / 2);
        // 设置画笔末尾样式
        waveformPaint.setStrokeCap(Paint.Cap.ROUND);
        // 画一条基准线
        canvas.drawLine(touchCenterX, touchCenterY, viewWidth, touchCenterY, waveformPaint);

        // 波形数据如果为 null 直接 return
        if (waveformBytes == null) {
            return;
        }

        // 设置波形宽度
        waveformPaint.setStrokeWidth(waveformWidth);

        if (waveformPoints == null || waveformPoints.length < waveformBytes.length * 4) {
            waveformPoints = new float[waveformBytes.length * 4];
        }

        float baseX = viewWidth / waveformBytes.length;
        float baseY = viewHeight;

        float interval = viewWidth / waveformBytes.length;

        for (int i = 0; i < waveformBytes.length - 1; i++) {
            float waveform = ((byte) (waveformBytes[i] + 128)) * (viewHeight / 2) / 128;

            waveformPoints[4 * i] = baseX + i * interval;
            waveformPoints[4 * i + 1] = baseY;
            waveformPoints[4 * i + 2] = baseX + i * interval;
            if (waveform < 0) {
                waveformPoints[4 * i + 3] = baseY + waveform;
            } else {
                waveformPoints[4 * i + 3] = baseY - waveform * 2;
            }
        }
        canvas.drawLines(waveformPoints, waveformPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewBounds = new RectF(getLeft(), getTop(), getRight(), getBottom());

        viewWidth = viewBounds.right - viewBounds.left;
        viewHeight = viewBounds.bottom - viewBounds.top;

        touchCenterX = 0 + touchSize / 2;
        touchCenterY = viewHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 触摸点横坐标
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            // 判断按下的位置是不是在触摸区域
            if (x < touchCenterX - touchSize / 2 || x > touchCenterX + touchSize / 2
                    || y < touchCenterY - touchSize / 2 || y > touchCenterY + touchSize / 2) {
                return false;
            }
            isTouch = true;
            // 设置画笔透明度
            touchPaint.setAlpha(128);
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            if (isTouch) {
                if (isPlay) {
                    stopPlay();
                } else {
                    startPlay();
                }
            }
            // 设置画笔透明度
            touchPaint.setAlpha(255);
            postInvalidate();
            break;
        case MotionEvent.ACTION_MOVE:
            if (isTouch) {
                mWaveformCallback.onDrag((int) x);
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

    private void startPlay() {
        isPlay = true;
        mWaveformCallback.onStart();
    }

    private void stopPlay() {
        isPlay = false;
        mWaveformCallback.onStop();
    }

    /**
     * 设置回调接口
     *
     * @param callback
     */
    public void setWaveformCallback(MLWaveformCallback callback) {
        mWaveformCallback = callback;
    }

    /**
     * 控件回调接口
     */
    public interface MLWaveformCallback {
        /**
         * 开始
         */
        public void onStart();

        /**
         * 停止
         */
        public void onStop();

        /**
         * 拖动
         *
         * @param position
         */
        public void onDrag(int position);

        /**
         * 错误
         *
         * @param error
         */
        public void onError(int error);
    }
}
