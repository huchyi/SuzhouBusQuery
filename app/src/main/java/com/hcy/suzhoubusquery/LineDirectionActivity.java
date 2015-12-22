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
import com.hcy.suzhoubusquery.net.HttpRequest;
import com.hcy.suzhoubusquery.utils.BaseBean;
import com.hcy.suzhoubusquery.utils.NetworkUtils;
import com.hcy.suzhoubusquery.utils.StringUtils;
import com.hcy.suzhoubusquery.view.RefreshableView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/1.
 * <p/>
 * LineDirectionActivity
 */
public class LineDirectionActivity extends BaseActivity implements View.OnClickListener, RefreshableView.PullToRefreshListener {

    private ListView mListView;
    private LineDirectionBaseAdapter mLineDirectionBaseAdapter;
    private String mGuid;

    private TextView lineNumTV;
    private TextView toWhereTV;
    private RefreshableView mRefreshableView;
    private boolean isToRefresh = false;

    private LinearLayout mProgressBar;

    private ArrayList<BaseBean> mBeans = new ArrayList<>();

    public static void startActivity(Context context, String id) {
        MyApplication.getInstances().checkActivity((Activity) context);
        Intent intent = new Intent(context, LineDirectionActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    private void getParam() {
        Intent intent = getIntent();
        if (intent != null) {
            mGuid = getIntent().getStringExtra("id");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_direction);
        getParam();
        mListView = (ListView) findViewById(R.id.listview);
        findViewById(R.id.title_bar).setOnClickListener(this);
        findViewById(R.id.car_line_refresh).setOnClickListener(this);
        mRefreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
        mRefreshableView.setOnRefreshListener(this, 1);

        lineNumTV = (TextView) findViewById(R.id.car_line_num);
        toWhereTV = (TextView) findViewById(R.id.car_line_to);

        mProgressBar = (LinearLayout) findViewById(R.id.progress);
        getData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_bar) {
            finish();
        } else if (v.getId() == R.id.car_line_refresh) {
            getData();
        }
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
        if(!isToRefresh){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        HttpRequest.getInstances().getLineDirectionData(mGuid, new HttpRequest.ICallBack() {


            @Override
            public void onSuccess(final String json) {

                LineDirectionActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        if(isToRefresh){
                            isToRefresh = false;
                            mRefreshableView.finishRefreshing();
                        }
                        try {
                            BaseBean bean = new BaseBean(new JSONObject(json));
                            if (0 == bean.getInt("errorCode")) {
                                BaseBean bean1 = (BaseBean) bean.get("list");
                                if (bean1 != null) {
                                    lineNumTV.setText(bean1.getStr("LName"));
                                    toWhereTV.setText(bean1.getStr("LDirection"));
                                    ArrayList<BaseBean> beas = (ArrayList<BaseBean>) bean1.get("StandInfo");
                                    if(beas != null){
                                        if (mLineDirectionBaseAdapter == null) {
                                            mBeans = beas;
                                            mLineDirectionBaseAdapter = new LineDirectionBaseAdapter(LineDirectionActivity.this, mBeans);
                                            mListView.setAdapter(mLineDirectionBaseAdapter);
                                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    BaseBean bean = (BaseBean) parent.getAdapter().getItem(position);
                                                    if(bean != null){
                                                        StationDetailActivity.startActivity(LineDirectionActivity.this,bean.getStr("SName"),bean.getStr("SCode"));
                                                    }
                                                }
                                            });
                                        } else {
                                            //mLineDirectionBaseAdapter.refreshData(beas);
                                            mBeans.clear();
                                            mBeans.addAll(beas);
                                            mLineDirectionBaseAdapter.notifyDataSetChanged();
                                            MyApplication.getInstances().showToast("刷新成功");
                                        }
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
                LineDirectionActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        if(isToRefresh){
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
    public void onRefresh() {
        isToRefresh = true;
        getData();
    }
}
