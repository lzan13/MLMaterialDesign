package net.melove.demo.design.waveform;

import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;
import net.melove.demo.design.utils.MLDateUtil;
import net.melove.demo.design.utils.MLFileUtil;
import net.melove.demo.design.widget.MLToast;

import java.util.Timer;

public class MLWaveformActivity extends MLBaseActivity {

    // 音频信息可视化类
    private Visualizer mVisualizer;

    private MLLineIndicatorView mLineIndicatorView;
    private MLRecordView mRecordView;

    // 媒体录影机，可以录制音频和视频
    private MediaRecorder mMediaRecorder;

    // 录音最大持续时间 10 分钟
    private int maxDuration = 10 * 60 * 1000;
    // 音频采样率 单位 Hz
    private int samplingRate = 8000;
    // 音频编码比特率
    private int encodingBitRate = 64;

    // 定时器，用来定时检测麦克风大小
    private Timer mTimer;
    // 是否正在录制
    private boolean isRecording = false;

    // 录制开始时间
    private long mStartTime = 0L;
    // 录制结束时间
    private long mEndTime = 0L;

    // 界面按钮
    private Button mStartRecordBtn;
    private Button mStopRecordBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waveform);

        initView();

    }

    /**
     * 初始化界面
     */
    private void initView() {
        mActivity = this;

        mRecordView = (MLRecordView) findViewById(R.id.ml_view_record);
        mRecordView.setRecordCallback(callback);
    }

    private MLRecordView.MLRecordCallback callback = new MLRecordView.MLRecordCallback() {
        @Override
        public void onCancel() {
            
        }

        @Override
        public void onFailed(int error) {

        }

        @Override
        public void onSuccess(String path) {

        }
    };

}
