package com.hcy.suzhoubusquery;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.hcy.suzhoubusquery.utils.MethodUtils;
import com.hcy.suzhoubusquery.utils.PagerSlidingTabStrip;
import com.hcy.suzhoubusquery.view.CustomDialog;

public class MainActivity extends FragmentActivity {

    public static String SERVER_FILE = "http://content.2500city.com";// 域名

    public static String DEVICE_ID;

    public static String APP_VERSION = "3.6";

    private MainLineNumFragment mMainLineNumFragment;
    private MainStationFragment mMainStationFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DEVICE_ID = MethodUtils.IMEIMath(MainActivity.this);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.layout_tabs);
        ViewPager pagers = (ViewPager) findViewById(R.id.layout_pagers);

        mMainLineNumFragment = MainLineNumFragment.newInstance();
        mMainStationFragment = MainStationFragment.newInstance();

        MyStoryPagerAdapter pagerAdapter = new MyStoryPagerAdapter(getSupportFragmentManager());
        pagers.setAdapter(pagerAdapter);
        pagers.setOffscreenPageLimit(2);
        tabs.setViewPager(pagers);

    }

    public class MyStoryPagerAdapter extends FragmentStatePagerAdapter {
        FragmentManager fm;

        public MyStoryPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "线路查询";
                case 1:
                    return "站点查询";
                default:
                    return null;
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mMainLineNumFragment;
                case 1:
                    return mMainStationFragment;
                default:
                    return mMainLineNumFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK ){
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("确定退出吗?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    MyApplication.getInstances().exit();
                    System.exit(0);
                    finish();
                }
            });
            builder.setNegativeButton("取消",
                    new android.content.DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            builder.create().show();
        }
        return false;
    }
}