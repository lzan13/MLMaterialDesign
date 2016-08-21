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
import net.melove.demo.design.utils.MLLog;
import net.melove.demo.design.widget.MLToast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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

        mStartRecordBtn = (Button) findViewById(R.id.ml_btn_start_record);
        mStopRecordBtn = (Button) findViewById(R.id.ml_btn_stop_record);

        mStartRecordBtn.setOnClickListener(viewListener);
        mStopRecordBtn.setOnClickListener(viewListener);

        mRecordView = (MLRecordView) findViewById(R.id.ml_view_record);
    }

    /**
     * 界面控件点击监听
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            case R.id.ml_btn_start_record:
                if (isRecording) {
                    MLToast.makeToast("正在录制").show();
                } else {
                    startRecordVoice();
                }
                break;
            case R.id.ml_btn_stop_record:
                if (!isRecording) {
                    MLToast.makeToast("还没开始录制").show();
                } else {
                    stopRecordVoice();
                }
                break;
            }
        }
    };

    /**
     * 开始录音
     */
    private void startRecordVoice() {
        String voicePath = MLFileUtil.getFilesFromSDCard() + MLDateUtil.getDateTimeNoSpacing() + ".amr";
        MLRecordManager.MLRecordError recordError = mRecordView.startRecord(voicePath);
        if (recordError == MLRecordManager.MLRecordError.ERROR_NONE) {
            isRecording = true;
        }
    }

    /**
     * 停止录音
     */
    private void stopRecordVoice() {
        mRecordView.stopRecord();
        isRecording = false;
    }


}
