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
        mMediaPlayer = MediaPlayer.create(this, R.raw.ml_record_voice);
        // 设置是否循环播放 默认为 false
        mMediaPlayer.setLooping(true);

        // 媒体播放结束监听
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                // 取消激活 Visualizer
                mVisualizer.setEnabled(false);
            }
        });

        onSetupVisualizer();
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

    /**
     * 初始化 Visualizer
     */
    private void onSetupVisualizer() {
        // 实例化可视化观察器，参数为 MediaPlayer 将要播放的音频ID
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        // 设置捕获大小
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1] / 2);
        // 为 Visualizer 设置监听器
        /**
         * 这个监听又四个参数
         * Visualizer.setDataCaptureListener(OnDataCaptureListener listener, int rate, boolean waveform, boolean fft);
         * 		listener，表监听函数，匿名内部类实现该接口，该接口需要实现两个函数
         *      rate， 表示采样的周期，即隔多久采样一次，联系前文就是隔多久采样128个数据
         *      iswave，是波形信号
         *      isfft，是FFT信号，表示是获取波形信号还是频域信号
         *
         */
        mVisualizer.setDataCaptureListener(
                // 数据采集监听
                new Visualizer.OnDataCaptureListener() {
                    /**
                     * 采集快速傅里叶变换有关的数据
                     *
                     * @param visualizer   采集器
                     * @param fft          采集的傅里叶变换数据
                     * @param samplingRate 采样率
                     */
                    @Override
                    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                        // 用 FFT 频域傅里叶变换数据更新 mVisualizerView 组件
                        mWaveformView.updateFFTData(fft, mMediaPlayer.getCurrentPosition());
                        MLLog.i("fft.lenght:%d, samplingRate:%d, position:%d, duration:%d",
                                fft.length, samplingRate, mMediaPlayer.getCurrentPosition(), mMediaPlayer.getDuration());
                    }

                    /**
                     * 采集波形数据
                     *
                     * @param visualizer   采集器
                     * @param waveform     采集的波形数据
                     * @param samplingRate 采样率
                     */
                    @Override
                    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                        // 用 Waveform 波形数据更新 mVisualizerView 组件
                        mWaveformView.updateWaveformData(waveform, mMediaPlayer.getCurrentPosition());
                        MLLog.i("waveform.lenght:%d, samplingRate:%d, position:%d, duration:%d",
                                waveform.length, samplingRate, mMediaPlayer.getCurrentPosition(), mMediaPlayer.getDuration());
                    }
                },
                // 最大采样率
                Visualizer.getMaxCaptureRate() / 2,
                // 是否采集波形数据
                true,
                // 是否采集傅里叶变换数据
                false);
    }

    private MLWaveformView.MLWaveformCallback waveformCallback = new MLWaveformView.MLWaveformCallback() {
        @Override
        public void onStart() {
            mMediaPlayer.start();

            // 激活 Visualizer，确保你需要采集数据的时候才激活他
            mVisualizer.setEnabled(true);
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
