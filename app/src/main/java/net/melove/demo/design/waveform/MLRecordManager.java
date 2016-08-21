package net.melove.demo.design.waveform;

import android.media.MediaRecorder;

import net.melove.demo.design.utils.MLDateUtil;
import net.melove.demo.design.utils.MLFileUtil;
import net.melove.demo.design.utils.MLLog;

import java.io.IOException;
import java.util.Timer;

/**
 * Created by lz on 2016/8/20.
 * 定义的录音功能单例类，主要处理录音的相关操作
 */
public class MLRecordManager {

    // 单例类的实例
    private static MLRecordManager instance;

    // 媒体录影机，可以录制音频和视频
    private MediaRecorder mMediaRecorder;

    // 录制文件保存路径
    private String recordFilePath;

    // 录音最大持续时间 10 分钟
    private int maxDuration = 10 * 60 * 1000;
    // 音频采样率 单位 Hz
    private int samplingRate = 8000;
    // 音频编码比特率
    private int encodingBitRate = 64;

    // 定时器，用来定时检测麦克风大小
    private Timer mTimer;
    // 录制系统当前状态
    private MLRecordStatus recordStatus;

    // 录制开始时间
    private long mStartTime = 0L;
    // 录制结束时间
    private long mEndTime = 0L;

    /**
     * 单例类的私有构造方法
     */
    private MLRecordManager() {
        // 初始化录制系统为空闲状态
        recordStatus = MLRecordStatus.RECORD_IDLE;
    }

    /**
     * 获取单例类的实例方法
     *
     * @return
     */
    public static MLRecordManager getInstance() {
        if (instance == null) {
            instance = new MLRecordManager();
        }
        return instance;
    }

    /**
     * 自定义设置录制文件保存路径，一定要设置在{start}方法之前，否则设置无效
     *
     * @param path
     */
    public void setRecordFilePath(String path) {
        recordFilePath = path;
    }

    /**
     * -------------------------------------- record vocie start -----------------------------------
     * 初始化录制音频
     */
    public void initVoiceRecorder() {
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
    }

    /**
     * 开始录制声音文件
     */
    public MLRecordError startRecordVoice(String path) {
        // 判断录制系统是否空闲
        if (recordStatus != MLRecordStatus.RECORD_IDLE) {
            return MLRecordError.ERROR_RECORDING;
        }
        if (path == null || path.equals("")) {
            // 这里默认保存在 /sdcard/android/data/packagename/files/下
            recordFilePath = MLFileUtil.getFilesFromSDCard() + MLDateUtil.getCurrentMillisecond() + ".amr";
        } else {
            recordFilePath = path;
        }

        // 设置为录制音频中
        recordStatus = MLRecordStatus.RECORD_VOICE;
        // 判断媒体录影机是否释放，没有则释放
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }

        // 释放之后重新初始化
        initVoiceRecorder();

        // 设置输出文件路径
        mMediaRecorder.setOutputFile(recordFilePath);
        try {
            // 准备录制
            mMediaRecorder.prepare();
            // 开始录制
            mMediaRecorder.start();
            MLLog.i("开始录制");
        } catch (IOException e) {
            MLLog.e("录影机准备失败 %s", e.getMessage());
            e.printStackTrace();
            return MLRecordError.ERROR_SYSTEM;
        }
        return MLRecordError.ERROR_NONE;
    }

    /**
     * 停止录音
     */
    public MLRecordError stopRecordVoice() {
        recordStatus = MLRecordStatus.RECORD_IDLE;
        // 释放媒体录影机
        if (mMediaRecorder != null) {
            // 停止录制
            mMediaRecorder.stop();
            // 重置媒体录影机
            mMediaRecorder.reset();
            // 释放媒体录影机
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        // 根据录制结果判断录音是否成功
        if (MLFileUtil.isFileExists(recordFilePath)) {
            return MLRecordError.ERROR_FAILED;
        }
        return MLRecordError.ERROR_NONE;
    }
    /**
     * ===================================== record voice end ======================================
     */

    /**
     * -------------------------------------- record video start -----------------------------------
     * TODO 录制视频功能，待实现
     * 初始化录制视频
     */
    public void initVideoRecorder() {

    }

    /**
     * 开始录制视频文件
     */
    public void startRecordVideo(String path) {
        // 判断录制系统是否空闲
        if (recordStatus != MLRecordStatus.RECORD_IDLE) {
            return;
        }
        if (path == null || path.equals("")) {
            // 这里默认保存在 /sdcard/android/data/packagename/files/下
            recordFilePath = MLFileUtil.getFilesFromSDCard() + MLDateUtil.getCurrentMillisecond() + ".mp4";
        } else {
            recordFilePath = path;
        }
        // 设置为录制视频中
        recordStatus = MLRecordStatus.RECORD_VIDEO;
    }
    /**
     * ===================================== record video end ======================================
     */


    /**
     * 获取声音分贝信息
     *
     * @return
     */
    public double getVoiceDecibel() {
        if (mMediaRecorder != null) {
            double ratio = mMediaRecorder.getMaxAmplitude();
            double decibel = 0;
            if (ratio > 1) {
                // 根据麦克风采集到的声音振幅计算声音分贝大小
                decibel = 20 * Math.log10(ratio);
            }
            return decibel;
        }
        return 0;
    }

    /**
     * 录制系统当前状态
     */
    public enum MLRecordStatus {
        RECORD_IDLE,    // 空闲
        RECORD_VOICE,   // 录制音频中
        RECORD_VIDEO;   // 录制视频中
    }

    /**
     * 录制系统使用错误码
     */
    public enum MLRecordError {
        ERROR_NONE,         // 没有错误
        ERROR_SYSTEM,       // 系统错误
        ERROR_FAILED,       // 录制失败
        ERROR_RECORDING;    // 正在录制
    }

}
