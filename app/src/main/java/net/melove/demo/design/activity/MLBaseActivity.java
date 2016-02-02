package net.melove.demo.design.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.melove.demo.design.util.MLLog;

/**
 * Created by lzan13 on 2015/12/1.
 */
public class MLBaseActivity extends AppCompatActivity {

    public MLBaseActivity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MLLog.i("onCreate");
    }


    @Override
    protected void onPause() {
        super.onPause();
        MLLog.i("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        MLLog.i("onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLLog.i("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        MLLog.i("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MLLog.i("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLLog.i("onDestroy");
    }
}
