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

        startVisualizerListener();
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


        mLineIndicatorView = (MLLineIndicatorView) findViewById(R.id.ml_view_lineindicator);
        mLineIndicatorView.setContent("100", "left 100", "120", "right 120");
        mLineIndicatorView.setIndicator(100, 200, 50);
        mLineIndicatorView.setProgress(50);
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
        try {
            // 判断媒体录影机是否释放，没有则释放
            if (mMediaRecorder != null) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
            // 实例化媒体录影机
            mMediaRecorder = new MediaRecorder();
            // 设置音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            /**
             * 设置音频文件编码格式，这里设置默认
             * https://developer.android.com/reference/android/media/MediaRecorder.AudioEncoder.html
             */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            /**
             * 设置音频文件输出格式
             * https://developer.android.com/reference/android/media/MediaRecorder.OutputFormat.html
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // 设置录音最大持续时间
            mMediaRecorder.setMaxDuration(maxDuration);
            // 设置音频采样率
            mMediaRecorder.setAudioSamplingRate(samplingRate);
            // 设置音频编码比特率
            mMediaRecorder.setAudioEncodingBitRate(encodingBitRate);
            // 设置输出文件路径
            mMediaRecorder.setOutputFile(voicePath);

            // 准备录制
            mMediaRecorder.prepare();
            // 开始录制
            mMediaRecorder.start();
            // 检测麦克风声音大小
            onWaveform();
            mStartTime = MLDateUtil.getCurrentMillisecond();
            MLLog.i("开始录制 %s", MLDateUtil.long2DateTime(mStartTime));
        } catch (IOException e) {
            MLLog.e("录影机准备失败 %s", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    private void stopRecordVoice() {
        mEndTime = MLDateUtil.getCurrentMillisecond();
        int duration = (int) ((mEndTime - mStartTime) / 1000);
        // 停止录制，修改录制状态为 false
        isRecording = false;
        // 清除定时器任务
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        // 释放媒体录影机
        if (mMediaRecorder != null) {
            MLLog.i("停止录制 %s, 录音时长：%d", MLDateUtil.long2DateTime(mEndTime), duration);
            // 停止录制
            mMediaRecorder.stop();
            // 重置媒体录影机
            mMediaRecorder.reset();
            // 释放媒体录影机
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    private void onWaveform() {
        isRecording = true;
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
                if (mMediaRecorder != null) {
                    double ratio = mMediaRecorder.getMaxAmplitude();
                    double decibel = 0;
                    if (ratio > 1) {
                        // 根据麦克风采集到的声音振幅计算声音分贝大小
                        decibel = 20 * Math.log10(ratio);
                    }
                    MLLog.i("麦克风监听声音分贝：%g", decibel);
                }
            }
        };
        // 开启定时器，并设置定时任务间隔时间
        mTimer.schedule(task, 500, 1000);
    }


    private void startVisualizerListener() {

    }
}
