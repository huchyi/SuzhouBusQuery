package com.hcy.suzhoubusquery;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Administrator on 2015/12/3.
 *
 * BaseActivity
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstances().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstances().removeActivity(this);
    }
}
