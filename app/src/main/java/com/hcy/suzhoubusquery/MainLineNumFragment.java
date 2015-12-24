package com.hcy.suzhoubusquery;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hcy.suzhoubusquery.Adapter.CollectBaseAdapter;
import com.hcy.suzhoubusquery.Adapter.LineNumBaseAdapter;
import com.hcy.suzhoubusquery.event.UpdateCollectEvent;
import com.hcy.suzhoubusquery.event.UpdateCollectStationEvent;
import com.hcy.suzhoubusquery.net.HttpRequest;
import com.hcy.suzhoubusquery.utils.BaseBean;
import com.hcy.suzhoubusquery.utils.InputTools;
import com.hcy.suzhoubusquery.utils.LineNumInfoPreferenceUtil;
import com.hcy.suzhoubusquery.utils.NetworkUtils;
import com.hcy.suzhoubusquery.utils.StringUtils;
import com.hcy.suzhoubusquery.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.greenrobot.event.EventBus;

/**
 * Created by hcy on 2015/11/30.
 * <p/>
 * MainFragment
 */
public class MainLineNumFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    public static MainLineNumFragment newInstance() {
        MainLineNumFragment newfragment = new MainLineNumFragment();
//        Bundle data = new Bundle();
//        data.putString("topic_type_id", baseBean.getInt("topic_type_id") + "");
//        newfragment.setArguments(data);
        return newfragment;
    }

    private EditText mInputET;
    private ImageView deleteText;
    private ListView mListView;
    private LinearLayout mProgressBar;
    private ListView mSearchContenLV;
    private LinearLayout mSearchContentLL;

    private LineNumBaseAdapter mLineNumBaseAdapter;

    private CollectBaseAdapter mCollectBaseAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_line_num_layout, null);
            initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initView(View view) {
        mInputET = (EditText) view.findViewById(R.id.input);
        mInputET.addTextChangedListener(watcher);
        deleteText = (ImageView) view.findViewById(R.id.delete_text);
        deleteText.setOnClickListener(this);
        view.findViewById(R.id.to_search).setOnClickListener(this);
        view.findViewById(R.id.delete_fav).setOnClickListener(this);
        mListView = (ListView) view.findViewById(R.id.listview);
        mProgressBar = (LinearLayout) view.findViewById(R.id.progress);
        mSearchContenLV = (ListView) view.findViewById(R.id.listview_line);
        mSearchContentLL = (LinearLayout) view.findViewById(R.id.search_result_listview_line_ll);
        

        initData();
    }

    private void initData() {
        mInputET.clearFocus();
        updateCollect();
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                if(mSearchContentLL.getVisibility() != View.GONE){
                    mSearchContentLL.setVisibility(View.GONE);
                }
                deleteText.setVisibility(View.GONE);
            } else {
                deleteText.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.to_search:
                String inputStr = mInputET.getText().toString();
                if (!StringUtils.isNullOrNullStr(inputStr)) {
                    getData(inputStr);
                } else {
                    MyApplication.getInstances().showToast("请输入线路号");
                }
                break;
            case R.id.delete_fav:
//                //删除
//                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
//                builder.setMessage("确定删除所有的历史记录吗？");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON, "");
//                    }
//                });
//                builder.setNegativeButton("取消",
//                        new android.content.DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                builder.create().show();


                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("确定删除所有的历史内容吗？")
                        .setCancelText("取消")
                        .setConfirmText("确定")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("取消!")
                                        .setContentText("你取消了删除")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("删除!")
                                        .setContentText("你已经删除了所有内容")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                //content
                                LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON, "");
                                updateCollect();
                            }
                        })
                        .show();



                break;
            case R.id.delete_text:
                mInputET.setText("");
                mInputET.clearFocus();
                break;
            default:
                break;
        }
    }

    int a;
    private void getData(String input) {
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
        if (InputTools.KeyBoard(mInputET)) {
            InputTools.HideKeyboard(mInputET);
        }
        if (!NetworkUtils.isConnected(getActivity())) {
            MyApplication.getInstances().showToast(MyApplication.getInstances().getString(R.string.please_check_newwork));
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        HttpRequest.getInstances().getLineNumData(input, new HttpRequest.ICallBack() {
            @Override
            public void onSuccess(final String json) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            BaseBean bean = new BaseBean(new JSONObject(json));
                            if (0 == bean.getInt("errorCode")) {
                                ArrayList<BaseBean> beas = (ArrayList<BaseBean>) bean.get("list");
                                if(beas != null ){
                                    mSearchContentLL.setVisibility(View.VISIBLE);
                                    mLineNumBaseAdapter = new LineNumBaseAdapter(getActivity(), beas);
                                    mSearchContenLV.setAdapter(mLineNumBaseAdapter);
                                    mSearchContenLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            BaseBean bean = (BaseBean) parent.getAdapter().getItem(position);
                                            String guidStr = bean.getStr("Guid");
                                            String resetJson = "{\"LName\":\"" + bean.getStr("LName") + "\",\"LDirection\":\"" + bean.getStr("LDirection") + "\",\"Guid\":\"" + bean.getStr("Guid") + "\"}";
                                            collectReset(resetJson, guidStr);
                                            if (!StringUtils.isNullOrNullStr(guidStr)) {
                                                LineDirectionActivity.startActivity(getActivity(), guidStr);
                                            } else {
                                                MyApplication.getInstances().showToast("该线路编号为空");
                                            }
                                            updateCollect();

                                        }
                                    });
                                }else{
                                    MyApplication.getInstances().showToast("没有该线路的时时信息");
                                }
                            } else {
                                MyApplication.getInstances().showToast("没有该线路信息");
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
                getActivity().runOnUiThread(new Runnable() {
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


    /**
     * json重组
     */
    private void collectReset(String json, String Guid) {
        String lineJson = LineNumInfoPreferenceUtil.getValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON, "");
        if (!StringUtils.isNullOrNullStr(lineJson)) {
            try {
                BaseBean bean = new BaseBean(new JSONObject(lineJson));
                ArrayList<BaseBean> beans = (ArrayList<BaseBean>) bean.get("list");
                boolean ishave = false;
                String str;
                if (beans != null) {
                    for (int i = 0; i < beans.size(); i++) {
                        str = beans.get(i).getStr("Guid");
                        if (Guid.equals(str)) {
                            ishave = true;
                            break;
                        }
                    }
                    if (!ishave) {
                        lineJson = lineJson.replace("]}", "");
                        lineJson = lineJson + "," + json + "]}";
                        LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON, lineJson);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            json = "{\"list\":[" + json + "]}";
            LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON, json);
        }
    }

    private void updateCollect() {
        String lineJson = LineNumInfoPreferenceUtil.getValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON, "");
        if (!StringUtils.isNullOrNullStr(lineJson)) {
            try {
                BaseBean bean = new BaseBean(new JSONObject(lineJson));
                ArrayList<BaseBean> beans = (ArrayList<BaseBean>) bean.get("list");
                mCollectBaseAdapter = new CollectBaseAdapter(getActivity(), beans);
                mListView.setAdapter(mCollectBaseAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BaseBean bean = (BaseBean) parent.getAdapter().getItem(position);
                        String guidStr = bean.getStr("Guid");
                        if (!StringUtils.isNullOrNullStr(guidStr)) {
                            LineDirectionActivity.startActivity(getActivity(), guidStr);
                        } else {
                            MyApplication.getInstances().showToast("该线路编号为空");
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (mCollectBaseAdapter != null) {
                ArrayList<BaseBean> beans = new ArrayList<>();
                mCollectBaseAdapter = new CollectBaseAdapter(getActivity(), beans);
                mListView.setAdapter(mCollectBaseAdapter);
            }
        }
    }

    public void onEventMainThread(UpdateCollectEvent event) {
        updateCollect();
    }
}
