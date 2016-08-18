package net.melove.demo.design.sound;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;


/**
 * Created by lzan13 on 2016/8/4.
 */
public class MLSoundActivity extends MLBaseActivity {


    // 音频管理器
    private AudioManager mAudioManager;
    private SoundPool mSoundPool;
    private int soundId;
    private int loadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        mActivity = this;

        findViewById(R.id.ml_btn_play_sound).setOnClickListener(viewListener);
        findViewById(R.id.ml_btn_stop_sound).setOnClickListener(viewListener);

        // 初始化音频管理器
        mAudioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
        // 根据系统版本不同选择不同的方式初始化音频播放工具
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createSoundPoolWithBuilder();
        } else {
            createSoundPoolWithConstructor();
        }
        // 根据通话呼叫与被呼叫加载不同的提示音效
        loadId = mSoundPool.load(mActivity, R.raw.ml_call_incoming, 1);
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.ml_btn_play_sound:
                playCallSound();
                break;
            case R.id.ml_btn_stop_sound:
                stopSound();
                break;
            }
        }
    };

    /**
     * 播放呼叫通话提示音
     */
    private void playCallSound() {
        mAudioManager.setSpeakerphoneOn(true);
        // 设置音频管理器音频模式为铃音模式
        mAudioManager.setMode(AudioManager.MODE_RINGTONE);
        // 播放提示音，返回一个播放的音频id，等下停止播放需要用到
        soundId = mSoundPool.play(
                loadId, // 播放资源id；就是加载到SoundPool里的音频资源顺序，这里就是第一个，也是唯一的一个
                0.5f,   // 左声道音量
                0.5f,   // 右声道音量
                1,      // 优先级，这里至于一个提示音，不需要关注
                -1,     // 是否循环；0 不循环，-1 循环
                1);     // 播放比率；从0.5-2，一般设置为1，表示正常播放
    }


    /**
     * 停止播放音效
     */
    private void stopSound() {
        mSoundPool.stop(soundId);
    }

    /**
     * 当系统的 SDK 版本高于21时，使用另一种方式创建 SoundPool
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createSoundPoolWithBuilder() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                // 设置音频要用在什么地方，这里选择电话通知铃音
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        // 使用 build 的方式实例化 SoundPool
        mSoundPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(1).build();
    }

    /**
     * 使用构造函数实例化 SoundPool
     */
    @SuppressWarnings("deprecation")
    protected void createSoundPoolWithConstructor() {
        // 老版本使用构造函数方式实例化 SoundPool，MODE 设置为铃音 MODE_RINGTONE
        mSoundPool = new SoundPool(1, AudioManager.MODE_RINGTONE, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
