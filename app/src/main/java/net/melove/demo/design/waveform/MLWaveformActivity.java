package net.melove.demo.design.waveform;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;

import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;

public class MLWaveformActivity extends MLBaseActivity {


    private MediaPlayer mMediaPlayer;
    // 音频信息可视化类
    private Visualizer mVisualizer;

    private MLRecordView mRecordView;


    private MLWaveformView mWaveformView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waveform);

        initView();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Create the MediaPlayer
        mMediaPlayer = MediaPlayer.create(this, R.raw.ml_xw_cjdn);

        setupVisualizerFxAndUI();

        // Make sure the visualizer is enabled only when you actually want to receive data, and
        // when it makes sense to receive data.
        mVisualizer.setEnabled(true);

        // 媒体播放结束监听
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
            }
        });

        mMediaPlayer.start();
    }

    private void setupVisualizerFxAndUI() {
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                mWaveformView.updateByte(bytes);
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {}
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mActivity = this;

        mWaveformView = (MLWaveformView) findViewById(R.id.ml_view_waveform);


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
        public void onStart() {

        }

        @Override
        public void onSuccess(String path, int time) {

        }
    };

}
