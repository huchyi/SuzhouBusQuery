package com.hcy.suzhoubusquery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/2.
 * <p/>
 * StationDetailActivity
 */
public class StationDetailActivity extends BaseActivity implements View.OnClickListener {

    private String mNoteGuid;
    private String mName;

    private TextView mStationTV;
    private ListView mListView;
    private LinearLayout mProgressBar;

    private ArrayList<BaseBean> mBeans = new ArrayList<>();
    private StationDetailBaseAdapter mStationDetailBaseAdapter;

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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getParam();
        initData();
        getData();
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

        mProgressBar = (LinearLayout) findViewById(R.id.progress);
        initData();
        getData();
    }

    private void initData(){
        mStationTV.setText(mName);
    }


    private void getData() {
        if (!NetworkUtils.isConnected(this)) {
            MyApplication.getInstances().showToast("请检查网络连接");
            return;
        }
        if (StringUtils.isNullOrNullStr(mNoteGuid)) {
            MyApplication.getInstances().showToast("查询失败");
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        HttpRequest.getInstances().getDetailedNameData(mNoteGuid, new HttpRequest.ICallBack() {
            @Override
            public void onSuccess(final String json) {
                StationDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            BaseBean bean = new BaseBean(new JSONObject(json));
                            if (0 == bean.getInt("errorCode")) {
                                ArrayList<BaseBean> beans = (ArrayList<BaseBean>) bean.get("list");
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
                                                    LineDirectionActivity.startActivity(StationDetailActivity.this,bean.getStr("Guid"));
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
}
