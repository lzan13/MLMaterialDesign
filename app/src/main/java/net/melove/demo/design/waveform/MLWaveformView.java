package net.melove.demo.design.waveform;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lzan13 on 2016/8/18.
 */
public class MLWaveformView extends View {
    public MLWaveformView(Context context) {
        super(context);
    }

    public MLWaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MLWaveformView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MLWaveformView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
