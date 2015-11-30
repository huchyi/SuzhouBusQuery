package com.hcy.suzhoubusquery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hcy.suzhoubusquery.utils.BaseBean;

/**
 * Created by hcy on 2015/11/30.
 *
 *MainFragment
 *
 */
public class MainStationFragment extends Fragment{

    private View rootView;

    public static MainStationFragment newInstance() {
        MainStationFragment newfragment = new MainStationFragment();
//        Bundle data = new Bundle();
//        data.putString("topic_type_id", baseBean.getInt("topic_type_id") + "");
//        newfragment.setArguments(data);
        return newfragment;
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

    }


}
