package com.hcy.suzhoubusquery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcy.suzhoubusquery.net.HttpRequest;
import com.hcy.suzhoubusquery.temp.WeatherUtils;
import com.hcy.suzhoubusquery.utils.BaseBean;
import com.hcy.suzhoubusquery.utils.MethodUtils;
import com.hcy.suzhoubusquery.utils.PagerSlidingTabStrip;
import com.hcy.suzhoubusquery.utils.StringUtils;

import org.json.JSONObject;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends FragmentActivity {

    public static String SERVER_FILE = MyApplication.getInstances().getString(R.string.method_service);// 域名

    public static String SERVER_FILE_TEMP = MyApplication.getInstances().getString(R.string.method_service_temp);// id:101190401

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
        initView();
    }


    /**
     * 天气开始
     */
    private ImageView todayTempIconIV;
    private TextView todayTempWeatherTV;
    private TextView todayTempWindTV;
    private TextView todayTempUpdatetimeTV;
    private RelativeLayout toRefreshLL;
    private ImageView rfreshIV;

    private ImageView temp2IconIV;
    private TextView temp2WeatherTV;

    private ImageView temp3IconIV;
    private TextView temp3WeatherTV;

    private ImageView temp4IconIV;
    private TextView temp4WeatherTV;

    private ImageView temp5IconIV;
    private TextView temp5WeatherTV;


    private TextView mOpenTempTV;
    private TextView mCloseTempTV;
    private RelativeLayout mTempZoneRL;


    private boolean isRefresh = false;

    private void initView() {
        mOpenTempTV = (TextView) findViewById(R.id.temp_open_btn);
        mOpenTempTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTempZoneRL.setVisibility(View.VISIBLE);
            }
        });
        mCloseTempTV = (TextView) findViewById(R.id.temp_close_btn);
        mCloseTempTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTempZoneRL.setVisibility(View.GONE);
            }
        });
        mTempZoneRL = (RelativeLayout) findViewById(R.id.title_rl_top);


        todayTempIconIV = (ImageView) findViewById(R.id.temp_today_icon);
        todayTempWeatherTV = (TextView) findViewById(R.id.temp_today_weather_tv);
        todayTempWindTV = (TextView) findViewById(R.id.temp_today_wind_tv);
        todayTempUpdatetimeTV = (TextView) findViewById(R.id.temp_today_wind_updatetime_tv);
        toRefreshLL = (RelativeLayout) findViewById(R.id.temp_refresh);
        rfreshIV = (ImageView) findViewById(R.id.temp_refresh_icon);

        temp2IconIV = (ImageView) findViewById(R.id.temp2_today_icon);
        temp2WeatherTV = (TextView) findViewById(R.id.temp2_today_weather_tv);

        temp3IconIV = (ImageView) findViewById(R.id.temp3_today_icon);
        temp3WeatherTV = (TextView) findViewById(R.id.temp3_today_weather_tv);

        temp4IconIV = (ImageView) findViewById(R.id.temp4_today_icon);
        temp4WeatherTV = (TextView) findViewById(R.id.temp4_today_weather_tv);

        temp5IconIV = (ImageView) findViewById(R.id.temp5_today_icon);
        temp5WeatherTV = (TextView) findViewById(R.id.temp5_today_weather_tv);

        toRefreshLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation ra = new RotateAnimation(0, 720, rfreshIV.getWidth() / 2, rfreshIV.getHeight() / 2);
                ra.setDuration(2000);
                rfreshIV.startAnimation(ra);
                isRefresh = true;
                initData();
            }
        });
        initData();
    }

    private void initData() {
        HttpRequest.getInstances().getTempDate(new HttpRequest.ICallBack() {
            @Override
            public void onSuccess(final String json) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BaseBean bean = new BaseBean(new JSONObject(json));
                            if (isRefresh) {
                                isRefresh = false;
                                showRefreshData(bean);
                            } else {
                                showData(bean);
                            }
                        } catch (Exception e) {
                            isRefresh = false;
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                isRefresh = false;
            }
        });
    }

    private void showRefreshData(BaseBean baseBean) {
        //now
        BaseBean beanTodaty = (BaseBean) baseBean.get("realtime");
        if (beanTodaty != null) {
            try {
                todayTempIconIV.setImageResource(WeatherUtils.getWhiteCatIconIdByTypeName(beanTodaty.getStr("weather")));
                todayTempWeatherTV.setText("今天 [ " + beanTodaty.getStr("weather") + " ] [ " + beanTodaty.getStr("temp") + "°C ]" );
                todayTempWindTV.setText(beanTodaty.getStr("WS") + "\n" + beanTodaty.getStr("WD"));
                todayTempUpdatetimeTV.setText(beanTodaty.getStr("time") + "更新");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showData(BaseBean baseBean) {
        //now
        BaseBean beanTodaty = (BaseBean) baseBean.get("realtime");
        if (beanTodaty != null) {
            try {
                todayTempIconIV.setImageResource(WeatherUtils.getWhiteCatIconIdByTypeName(beanTodaty.getStr("weather")));
                todayTempWeatherTV.setText("今天 [ " + beanTodaty.getStr("weather") + " ] [ " + beanTodaty.getStr("temp") + "°C ]");
                todayTempWindTV.setText(beanTodaty.getStr("WS") + "\n" + beanTodaty.getStr("WD"));
                todayTempUpdatetimeTV.setText(beanTodaty.getStr("time") + "更新");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //feature
        BaseBean beanFeature = (BaseBean) baseBean.get("forecast");
        if (beanFeature != null) {
            try {
                temp2IconIV.setImageResource(WeatherUtils.getWhiteIconIdByTypeName(beanFeature.getStr("weather2")));
                temp2WeatherTV.setText(beanFeature.getStr("weather2")
                        + "\n" + tempReset(beanFeature.getStr("temp2"))
                        + "\n" + "明天");

                temp3IconIV.setImageResource(WeatherUtils.getWhiteIconIdByTypeName(beanFeature.getStr("weather3")));
                temp3WeatherTV.setText(beanFeature.getStr("weather3")
                        + "\n" + tempReset(beanFeature.getStr("temp3"))
                        + "\n" + "后天");

                temp4IconIV.setImageResource(WeatherUtils.getWhiteIconIdByTypeName(beanFeature.getStr("weather4")));
                temp4WeatherTV.setText(beanFeature.getStr("weather4")
                        + "\n" + tempReset(beanFeature.getStr("temp4"))
                        + "\n" + weekDateReset(3));

                temp5IconIV.setImageResource(WeatherUtils.getWhiteIconIdByTypeName(beanFeature.getStr("weather5")));
                temp5WeatherTV.setText(beanFeature.getStr("weather5")
                        + "\n" + tempReset(beanFeature.getStr("temp5"))
                        + "\n" + weekDateReset(4));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }


    private String tempReset(String temp) {
        if (!StringUtils.isNullOrNullStr(temp)) {
            if (temp.contains("~")) {
                String[] temps = temp.split("~");
                if (temps.length > 1) {
                    temp = temps[1] + "~" + temps[0];
                }
            }
            return temp;
        }
        return "";
    }

    private String weekDateReset(int time) {
        Calendar c = Calendar.getInstance();
        int way = c.get(Calendar.DAY_OF_WEEK);
        way = way + time;
        way = way > 7 ? way - 7 : way;
        String mWay = String.valueOf(way);
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }


    /**
     * 天气结束
     */

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            CustomDialog.Builder builder = new CustomDialog.Builder(this);
//            builder.setTitle("提示");
//            builder.setMessage("确定退出吗?");
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    MyApplication.getInstances().exit();
//                    System.exit(0);
//                    finish();
//                }
//            });
//            builder.setNegativeButton("取消",
//                    new android.content.DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//
//            builder.create().show();

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("提示")
                    .setContentText("确定退出吗?")
                    .setConfirmText("确定")
                    .setCancelText("取消")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            System.exit(0);
                            sDialog.dismiss();
                            MyApplication.getInstances().exit();
                            finish();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                        }
                    })
                    .show();
        }
        return false;
    }
}