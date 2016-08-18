package net.melove.demo.design.application;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;

import net.melove.demo.design.utils.MLLog;


/**
 * Created by lzan13 on 2016/5/24
 * Activity 的基类，定义一些子类公共的方法
 */
public class MLBaseActivity extends AppCompatActivity {

    // 当前布局RootView
    protected View mRootView;

    // 当前界面的上下文菜单对象
    protected MLBaseActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MLLog.i("onCreate");
        mActivity = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 自定义返回方法
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onFinish() {

        mActivity.finish();

        // 根据不同的系统版本选择不同的 finish 方法
        //        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
        //            mActivity.finish();
        //        } else {
        //            mActivity.finishAfterTransition();
        //        }
    }

    /**
     * 公用的 Activity 跳转方法
     * 基类定义并实现的方法，为了以后方便扩展
     *
     * @param intent 跳转的意图
     */
    public void superJump(Intent intent) {
        startActivity(intent);
        /**
         * 5.0以上的跳转方法
         * ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
         * ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
         */
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLLog.i("onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MLLog.i("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLLog.i("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActivity = this;
        MLLog.i("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        MLLog.i("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLLog.i("onDestroy");
        RefWatcher refWatcher = MLApplication.getRefWatcher();
        refWatcher.watch(this);
    }
}
