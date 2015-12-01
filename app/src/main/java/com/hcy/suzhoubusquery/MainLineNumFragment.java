package com.hcy.suzhoubusquery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hcy.suzhoubusquery.Adapter.LineNumBaseAdapter;
import com.hcy.suzhoubusquery.net.HttpRequest;
import com.hcy.suzhoubusquery.utils.BaseBean;
import com.hcy.suzhoubusquery.utils.InputTools;
import com.hcy.suzhoubusquery.utils.LineNumInfoPreferenceUtil;
import com.hcy.suzhoubusquery.utils.NetworkUtils;
import com.hcy.suzhoubusquery.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private ListView mListView;
    private ProgressBar mProgressBar;
    private ListView LineNumLV;

    private LineNumBaseAdapter mLineNumBaseAdapter;

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
        view.findViewById(R.id.to_search).setOnClickListener(this);
        view.findViewById(R.id.delete_fav).setOnClickListener(this);
        mListView = (ListView) view.findViewById(R.id.listview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        LineNumLV = (ListView) view.findViewById(R.id.listview_line);

        initData();
    }

    private void initData(){
        mInputET.clearFocus();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.to_search) {
            String inputStr = mInputET.getText().toString();
            if (!StringUtils.isNullOrNullStr(inputStr)) {
                getData(inputStr);
            } else {
                MyApplication.getInstances().showToast("请输入线路号");
            }
        }else if(v.getId() == R.id.delete_fav){
            //删除
        }
    }

    private void getData(String input) {
        if(InputTools.KeyBoard(mInputET)){
            InputTools.HideKeyboard(mInputET);
        }
        if (!NetworkUtils.isConnected(getActivity())) {
            MyApplication.getInstances().showToast("请检查网络连接");
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
                        try{
                            BaseBean bean = new BaseBean(new JSONObject(json));
                            if(0 == bean.getInt("errorCode")){
                                ArrayList<BaseBean> beas = (ArrayList<BaseBean>) bean.get("list");
                                LineNumLV.setVisibility(View.VISIBLE);
                                mLineNumBaseAdapter = new LineNumBaseAdapter(getActivity(),beas);
                                LineNumLV.setAdapter(mLineNumBaseAdapter);
                                LineNumLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        BaseBean bean = (BaseBean) parent.getAdapter().getItem(position);
                                        String guidStr = bean.getStr("Guid");
                                        String resetJson = "{\"LName\":\"" + bean.getStr("LName")+ "\",\"LDirection\":\"" +bean.getStr("LDirection") + "\",\"Guid\":\"" + bean.getStr("Guid")+ "\"}";
                                        collectReset(resetJson,guidStr);
                                        if(!StringUtils.isNullOrNullStr(guidStr)){
                                            LineDirectionActivity.startActivity(getActivity(),guidStr);
                                        }else{
                                            MyApplication.getInstances().showToast("该线路编号为空");
                                        }

                                    }
                                });
                            }else{
                                MyApplication.getInstances().showToast("没有该线路信息");
                            }
                        }catch (Exception e){
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
     *
     * */
    private void collectReset(String json,String Guid){
        String lineJson = LineNumInfoPreferenceUtil.getValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON,"");
        if(!StringUtils.isNullOrNullStr(lineJson)){
            try {
                BaseBean bean = new BaseBean(new JSONObject(lineJson));
                ArrayList<BaseBean> beans = (ArrayList<BaseBean>) bean.get("list");
                boolean ishave = false;
                String str;
                if(beans != null ){
                    for (int i = 0; i < beans.size(); i++) {
                        str = beans.get(i).getStr("Guid");
                        if(Guid.equals(str)){
                            ishave = true;
                            break;
                        }
                    }
                    if(!ishave){
                        lineJson = lineJson.replace("]}","");
                        lineJson = lineJson + "," + json + "]}";
                        LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON,lineJson);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            json = "{\"list\":[" +json + "]}";
            LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON,json);
        }
    }


}
