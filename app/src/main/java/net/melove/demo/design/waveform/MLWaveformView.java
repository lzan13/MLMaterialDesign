package net.melove.demo.design.waveform;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lzan13 on 2016/8/18.
 */
public class MLWaveformView extends View {

    protected byte[] mBytes;
    protected float[] mPoints;
    protected Rect mRect = new Rect();

    protected Paint mPaint;


    public MLWaveformView(Context context) {
        this(context, null);
    }

    public MLWaveformView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MLWaveformView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MLWaveformView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mBytes = null;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0xdddf3824);
    }

    /**
     * 更新采集的数据源
     *
     * @param bytes
     */
    public void updateByte(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBytes == null) {
            return;
        }

        if (mPoints == null || mPoints.length < mBytes.length * 4) {
            mPoints = new float[mBytes.length * 4];
        }

        mRect.set(0, 0, getWidth(), getHeight());

        for (int i = 0; i < mBytes.length - 1; i++) {
            mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
            mPoints[i * 4 + 1] = mRect.height();
            mPoints[i * 4 + 2] = mRect.width() * i / (mBytes.length - 1);
            mPoints[i * 4 + 3] = mRect.height() + (mBytes[i] + 128);
        }
        canvas.drawLines(mPoints, mPaint);
    }
}
