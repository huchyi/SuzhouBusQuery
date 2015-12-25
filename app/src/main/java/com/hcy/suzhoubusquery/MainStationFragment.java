package com.hcy.suzhoubusquery;

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

import com.hcy.dragsortlistview.DragSortListView;
import com.hcy.suzhoubusquery.Adapter.StationBaseAdapter;
import com.hcy.suzhoubusquery.Adapter.StationCollectBaseAdapter;
import com.hcy.suzhoubusquery.event.UpdateCollectStationEvent;
import com.hcy.suzhoubusquery.net.HttpRequest;
import com.hcy.suzhoubusquery.utils.BaseBean;
import com.hcy.suzhoubusquery.utils.InputTools;
import com.hcy.suzhoubusquery.utils.LineNumInfoPreferenceUtil;
import com.hcy.suzhoubusquery.utils.NetworkUtils;
import com.hcy.suzhoubusquery.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.greenrobot.event.EventBus;

/**
 * Created by hcy on 2015/11/30.
 * <p/>
 * MainStationFragment
 */
public class MainStationFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    public static MainStationFragment newInstance() {
        MainStationFragment newfragment = new MainStationFragment();
//        Bundle data = new Bundle();
//        data.putString("topic_type_id", baseBean.getInt("topic_type_id") + "");
//        newfragment.setArguments(data);
        return newfragment;
    }

    private EditText mInputET;
    private ImageView deleteText;
    private DragSortListView mHistoryListView;
    private LinearLayout mProgressBar;
    private ListView mStationListView;
    private LinearLayout mSearchContentLL;
    private LinearLayout deleteFav;
    private ImageView mHistoryEditIV;

    private StationBaseAdapter mStationBaseAdapter;

    private StationCollectBaseAdapter mStationCollectBaseAdapter;

    private boolean isEditHistory = false;

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
            rootView = inflater.inflate(R.layout.fragment_station_layout, null);
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
        deleteFav = (LinearLayout) view.findViewById(R.id.delete_fav);
        deleteFav.setOnClickListener(this);
        view.findViewById(R.id.edit_history).setOnClickListener(this);
        mHistoryEditIV = (ImageView) view.findViewById(R.id.edit_history_imageview);
        mHistoryListView = (DragSortListView) view.findViewById(R.id.dslv_history_List);
        mProgressBar = (LinearLayout) view.findViewById(R.id.progress);
        mStationListView = (ListView) view.findViewById(R.id.listview_line);
        mSearchContentLL = (LinearLayout) view.findViewById(R.id.search_result_listview_line_ll);
        mSearchContentLL.setOnClickListener(this);

        initData();
    }

    private void initData() {
        mInputET.clearFocus();
        updateCollect();
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                if (mSearchContentLL.getVisibility() != View.GONE) {
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
                    MyApplication.getInstances().showToast("请输入站台名");
                }
                break;
            case R.id.delete_fav:
                //删除
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
                                LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, "");
                                updateCollect();
                            }
                        })
                        .show();

                break;
            case R.id.delete_text:
                mInputET.setText("");
                mInputET.clearFocus();
                break;
            case R.id.edit_history:
                isEditHistory = !isEditHistory;
                deleteFav.setVisibility(isEditHistory ? View.VISIBLE: View.GONE);
                mHistoryEditIV.setBackgroundResource(isEditHistory ? R.drawable.edit_history_ok_icon : R.drawable.edit_history_icon);
                mStationCollectBaseAdapter.setDeleteButton(isEditHistory);
                mHistoryListView.setDragEnabled(isEditHistory); //设置是否可拖动。
                break;
            case R.id. search_result_listview_line_ll:
                if (mSearchContentLL.getVisibility() != View.GONE) {
                    mSearchContentLL.setVisibility(View.GONE);
                }
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
        HttpRequest.getInstances().getFuzzyNameData(input, new HttpRequest.ICallBack() {
            @Override
            public void onSuccess(final String json) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            BaseBean bean = new BaseBean(new JSONObject(json));
                            if (0 == bean.getInt("errorCode")) {
                                ArrayList<BaseBean> beans = (ArrayList<BaseBean>) bean.get("list");
                                if (beans != null) {
                                    mSearchContentLL.setVisibility(View.VISIBLE);
                                    mStationBaseAdapter = new StationBaseAdapter(getActivity(), beans);
                                    mStationListView.setAdapter(mStationBaseAdapter);
                                    mStationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            BaseBean bean = (BaseBean) parent.getAdapter().getItem(position);
                                            String guidStr = bean.getStr("NoteGuid");
                                            String resetJson = "{\"Name\":\"" + bean.getStr("Name")
                                                    + "\",\"NoteGuid\":\"" + bean.getStr("NoteGuid")
                                                    + "\",\"Canton\":\"" + bean.getStr("Canton")
                                                    + "\",\"Road\":\"" + bean.getStr("Road")
                                                    + "\",\"Direct\":\"" + bean.getStr("Direct")
                                                    + "\"}";
                                            collectReset(resetJson, guidStr);
                                            if (!StringUtils.isNullOrNullStr(guidStr)) {
                                                StationDetailActivity.startActivity(getActivity(), bean.getStr("Name"), guidStr);
                                            } else {
                                                MyApplication.getInstances().showToast("该站编号为空");
                                            }
                                            updateCollect();

                                        }
                                    });
                                } else {
                                    MyApplication.getInstances().showToast("没有该站台的信息");
                                }
                            } else {
                                MyApplication.getInstances().showToast("没有该站台的信息");
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
        String lineJson = LineNumInfoPreferenceUtil.getValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, "");
        if (!StringUtils.isNullOrNullStr(lineJson)) {
            try {
                BaseBean bean = new BaseBean(new JSONObject(lineJson));
                ArrayList<BaseBean> beans = (ArrayList<BaseBean>) bean.get("list");
                boolean ishave = false;
                String str;
                if (beans != null) {
                    for (int i = 0; i < beans.size(); i++) {
                        str = beans.get(i).getStr("NoteGuid");
                        if (Guid.equals(str)) {
                            ishave = true;
                            break;
                        }
                    }
                    if (!ishave) {
                        lineJson = lineJson.replace("]}", "");
                        lineJson = lineJson + "," + json + "]}";
                        LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, lineJson);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            json = "{\"list\":[" + json + "]}";
            LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, json);
        }
    }

    private void beanListToJson(ArrayList<BaseBean> beanList) {
        if (beanList != null && beanList.size() > 0) {
            LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, "");
            String jsonResult = "{\"list\":[";
            String json;
            for (int i = 0; i < beanList.size(); i++) {
                json = "{\"Name\":\"" + beanList.get(i).getStr("Name")
                        + "\",\"NoteGuid\":\"" + beanList.get(i).getStr("NoteGuid")
                        + "\",\"Canton\":\"" + beanList.get(i).getStr("Canton")
                        + "\",\"Road\":\"" + beanList.get(i).getStr("Road")
                        + "\",\"Direct\":\"" + beanList.get(i).getStr("Direct")
                        + "\"}";
                if (i == 0) {
                    jsonResult += json;
                } else {
                    jsonResult = jsonResult + "," + json;
                }
            }
            jsonResult = jsonResult + "]}";
            LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, jsonResult);
        }

    }



    private void updateCollect() {
        String lineJson = LineNumInfoPreferenceUtil.getValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, "");
        if (!StringUtils.isNullOrNullStr(lineJson)) {
            try {
                BaseBean bean = new BaseBean(new JSONObject(lineJson));
                ArrayList<BaseBean> beans = (ArrayList<BaseBean>) bean.get("list");
                mStationCollectBaseAdapter = new StationCollectBaseAdapter(getActivity(), beans);
                mHistoryListView.setAdapter(mStationCollectBaseAdapter);
                mHistoryListView.setDragEnabled(isEditHistory); //设置是否可拖动。
                //得到滑动listview并且设置监听器。
                mHistoryListView.setDropListener(onDrop);
                mHistoryListView.setOnItemClickListener(onItemClickListener);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (mStationCollectBaseAdapter != null) {
                ArrayList<BaseBean> beans = new ArrayList<>();
                mStationCollectBaseAdapter = new StationCollectBaseAdapter(getActivity(), beans);
                mHistoryListView.setAdapter(mStationCollectBaseAdapter);
            }
        }
    }


    //监听器在手机拖动停下的时候触发
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {//from to 分别表示 被拖动控件原位置 和目标位置
            if (from != to) {
                BaseBean item = (BaseBean) mStationCollectBaseAdapter.getItem(from);//得到listview的适配器
                mStationCollectBaseAdapter.remove(from);//在适配器中”原位置“的数据。
                mStationCollectBaseAdapter.insert(item, to);//在目标位置中插入被拖动的控件。
                beanListToJson(mStationCollectBaseAdapter.getAllData());
            }
        }
    };

    //item 的点击事件
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BaseBean bean = (BaseBean) parent.getAdapter().getItem(position);
            String guidStr = bean.getStr("NoteGuid");
            if (!StringUtils.isNullOrNullStr(guidStr)) {
                StationDetailActivity.startActivity(getActivity(), bean.getStr("Name"), guidStr);
            } else {
                MyApplication.getInstances().showToast("该站台编号为空");
            }
        }
    };

    public void onEventMainThread(UpdateCollectStationEvent event) {
        updateCollect();
    }

}
