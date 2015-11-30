package com.hcy.suzhoubusquery;

import android.app.Application;

/**
 *
 */
public class MyApplication extends Application {

    private static MyApplication mInstance = null;// 单例，保证多进程引用一个newapi

    public MyApplication() {
        mInstance = this;
    }

    /**
     * 获取Application单例
     */
    public static MyApplication getInstances() {
        if (null == mInstance) {
            mInstance = new MyApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

}
