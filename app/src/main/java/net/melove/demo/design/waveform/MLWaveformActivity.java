package net.melove.demo.design.waveform;

import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.View;

import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;

public class MLWaveformActivity extends MLBaseActivity {

    // 音频信息可视化类
    private Visualizer mVisualizer;

    private MLRecordView mRecordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waveform);

        initView();

        findViewById(R.id.ml_btn_test_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ml_btn_test_record).setVisibility(View.GONE);
                mRecordView.setVisibility(View.VISIBLE);
            }
        });

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
        public void onStart() {

        }

        @Override
        public void onSuccess(String path) {

        }
    };

}
