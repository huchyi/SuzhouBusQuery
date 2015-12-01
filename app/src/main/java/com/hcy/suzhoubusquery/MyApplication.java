package com.hcy.suzhoubusquery;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.hcy.suzhoubusquery.utils.ToastUtils;

/**
 *
 */
public class MyApplication extends Application {

    private static MyApplication mInstance = null;// 单例，保证多进程引用一个newapi

    public MyApplication() {
        mInstance = this;
    }

    private Handler handler = new Handler(); // 主线程消息处理

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

    /**
     * 支持非ui线程Toast，可避免多个Toast队列等待
     *
     * @param text
     */
    public void showToast(final CharSequence text) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.show(MyApplication.getInstances(), text, Toast.LENGTH_SHORT);
                }
            });
        } else {
            ToastUtils.show(MyApplication.getInstances(), text, Toast.LENGTH_SHORT);
        }
    }

}
