package net.melove.demo.design.waveform;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;

import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;
import net.melove.demo.design.utils.MLLog;

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
        mMediaPlayer = MediaPlayer.create(this, R.raw.ml_call_incoming);
        mMediaPlayer.setLooping(true);

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

    }

    private void setupVisualizerFxAndUI() {
        // 实例化可视化观察器，参数为 MediaPlayer 将要播放的音频ID
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        // 设置捕获大小
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                mWaveformView.updateByte(bytes, mMediaPlayer.getCurrentPosition());
                MLLog.i("bytes.lenght:%d, position:%d, duration:%d", bytes.length, mMediaPlayer.getCurrentPosition(), mMediaPlayer.getDuration());
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {}
        }, Visualizer.getMaxCaptureRate() / 4, true, false);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mActivity = this;

        mWaveformView = (MLWaveformView) findViewById(R.id.ml_view_waveform);
        mWaveformView.setWaveformCallback(waveformCallback);


        mRecordView = (MLRecordView) findViewById(R.id.ml_view_record);
        mRecordView.setRecordCallback(recordCallback);

    }

    private MLWaveformView.MLWaveformCallback waveformCallback = new MLWaveformView.MLWaveformCallback() {
        @Override
        public void onStart() {
            mMediaPlayer.start();
        }

        @Override
        public void onStop() {
            mMediaPlayer.stop();
        }

        @Override
        public void onDrag(int position) {

        }

        @Override
        public void onError(int error) {

        }
    };

    private MLRecordView.MLRecordCallback recordCallback = new MLRecordView.MLRecordCallback() {
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
