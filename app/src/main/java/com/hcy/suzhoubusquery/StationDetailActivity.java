package com.hcy.suzhoubusquery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hcy.suzhoubusquery.Adapter.LineDirectionBaseAdapter;
import com.hcy.suzhoubusquery.Adapter.StationDetailBaseAdapter;
import com.hcy.suzhoubusquery.net.HttpRequest;
import com.hcy.suzhoubusquery.utils.BaseBean;
import com.hcy.suzhoubusquery.utils.NetworkUtils;
import com.hcy.suzhoubusquery.utils.StringUtils;
import com.hcy.suzhoubusquery.view.RefreshableView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/2.
 * <p/>
 * StationDetailActivity
 */
public class StationDetailActivity extends BaseActivity implements View.OnClickListener, RefreshableView.PullToRefreshListener {

    private String mNoteGuid;
    private String mName;

    private TextView mStationTV;
    private ListView mListView;
    private LinearLayout mProgressBar;
    private RefreshableView mRefreshableView;
    private boolean isToRefresh = false;
    private boolean isAutoRefresh = false;

    private ArrayList<BaseBean> mBeans = new ArrayList<>();
    private StationDetailBaseAdapter mStationDetailBaseAdapter;

    private Handler handler = new Handler();

    public static void startActivity(Context context, String name, String id) {
        MyApplication.getInstances().checkActivity((Activity) context);
        Intent intent = new Intent(context, StationDetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    private void getParam() {
        Intent intent = getIntent();
        if (intent != null) {
            mNoteGuid = getIntent().getStringExtra("id");
            mName = getIntent().getStringExtra("name");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);
        getParam();
        mStationTV = (TextView) findViewById(R.id.car_station);
        mListView = (ListView) findViewById(R.id.listview);
        findViewById(R.id.title_bar).setOnClickListener(this);
        findViewById(R.id.car_station_refresh).setOnClickListener(this);
        mRefreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
        mRefreshableView.setOnRefreshListener(this, 0);

        mProgressBar = (LinearLayout) findViewById(R.id.progress);
        initData();
        getData();
        handler.postDelayed(runnable, 3 * 60 * 1000);//3分钟自动刷新一次
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);//停止定时器
        super.onDestroy();
    }

    private void initData() {
        mStationTV.setText(mName);
    }

    int a;

    private void getData() {
        switch (a) {
            case 101:
                JSONObject jsoObj;
                String date = null;
                String second = null;
                try {
                    jsoObj = new JSONObject();
                    date = jsoObj.getString("date");
                    second = jsoObj.getString("version");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

        if (!NetworkUtils.isConnected(this)) {
            MyApplication.getInstances().showToast(MyApplication.getInstances().getString(R.string.please_check_newwork));
            return;
        }
        if (StringUtils.isNullOrNullStr(mNoteGuid)) {
            MyApplication.getInstances().showToast("查询失败");
            return;
        }
        if (!isToRefresh || isAutoRefresh) {
            isAutoRefresh = false;
            mProgressBar.setVisibility(View.VISIBLE);
        }
        HttpRequest.getInstances().getDetailedNameData(mNoteGuid, new HttpRequest.ICallBack() {
            @Override
            public void onSuccess(final String json) {
                StationDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        if (isToRefresh) {
                            isToRefresh = false;
                            mRefreshableView.finishRefreshing();
                        }
                        try {
                            BaseBean bean = new BaseBean(new JSONObject(json));
                            if (0 == bean.getInt("errorCode")) {
                                BaseBean b = (BaseBean) bean.get("data");
                                ArrayList<BaseBean> beans = (ArrayList<BaseBean>) b.get("list");
                                if (beans != null) {
                                    if (mStationDetailBaseAdapter == null) {
                                        mBeans = beans;
                                        mStationDetailBaseAdapter = new StationDetailBaseAdapter(StationDetailActivity.this, mBeans);
                                        mListView.setAdapter(mStationDetailBaseAdapter);
                                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                BaseBean bean = (BaseBean) parent.getAdapter().getItem(position);
                                                if (bean != null) {
                                                    LineDirectionActivity.startActivity(StationDetailActivity.this, bean.getStr("Guid"));
                                                }
                                            }
                                        });
                                    } else {
                                        mBeans.clear();
                                        mBeans.addAll(beans);
                                        mStationDetailBaseAdapter.notifyDataSetChanged();
                                        MyApplication.getInstances().showToast("刷新成功");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            MyApplication.getInstances().showToast("解析失败");
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onFailure(final String msg) {
                StationDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        if (isToRefresh) {
                            isToRefresh = false;
                            mRefreshableView.finishRefreshing();
                        }
                        if (StringUtils.isNullOrNullStr(msg)) {
                            MyApplication.getInstances().showToast("请求错误");
                        } else {
                            MyApplication.getInstances().showToast(msg);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_bar) {
            finish();
        } else if (v.getId() == R.id.car_station_refresh) {
            getData();
        }
    }

    @Override
    public void onRefresh() {
        isToRefresh = true;
        getData();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isAutoRefresh = true;
            getData();
            handler.postDelayed(runnable, 3 * 60 * 1000);//3分钟自动刷新一次
        }
    };
}
