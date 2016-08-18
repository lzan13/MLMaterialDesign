package net.melove.demo.design.application;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by lzan13 on 2016/5/24
 * MLApplication类，项目的入口，做一些初始化操作
 */
public class MLApplication extends Application {

    // 全局的上下文对象
    private static Context context;

    private static RefWatcher watcher;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        // 初始化 LeakCanary
        watcher = LeakCanary.install(this);

    }

    public static Context getContext() {
        return context;
    }


    public static RefWatcher getRefWatcher() {
        return watcher;
    }

}