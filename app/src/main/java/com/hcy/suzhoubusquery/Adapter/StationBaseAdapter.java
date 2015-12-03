package com.hcy.suzhoubusquery.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hcy.suzhoubusquery.MyApplication;
import com.hcy.suzhoubusquery.R;
import com.hcy.suzhoubusquery.utils.BaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/2.
 *
 * StationBaseAdapter
 *
 *
 *         "Name": "苏大北校区",
 "NoteGuid": "AUE",
 "Canton": "沧浪区",
 "Road": "干将东路",
 "Sect": "东环路－莫邪路",
 "Direct": "南"
 */
public class StationBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private ArrayList<BaseBean> items;

    public StationBaseAdapter(Context context, ArrayList<BaseBean> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BaseBean item = items.get(position);
        if (null == item) {
            return convertView;
        }
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.fragment_station_layout_item, null);
            holder.stationName = (TextView) convertView.findViewById(R.id.station_name);
            holder.stationArea = (TextView) convertView.findViewById(R.id.station_area);
            holder.stationStreet = (TextView) convertView.findViewById(R.id.station_street);
            holder.stationPosition = (TextView) convertView.findViewById(R.id.station_position);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.stationName.setText(Html.fromHtml("<font  color=\""
                + MyApplication.getInstances().getResources().getColor(R.color.font_main_aaaaaa)
                + "\">站名:</font><font color=\""
                + MyApplication.getInstances().getResources().getColor(R.color.font_blue_text)
                + "\">"
                + item.getStr("Name")
                + " </font> "));

        holder.stationArea.setText(item.getStr("Canton"));

        holder.stationStreet.setText("街道:" + item.getStr("Road"));

        holder.stationPosition.setText(Html.fromHtml("<font color="+ MyApplication.getInstances().getResources().getColor(R.color.font_main_aaaaaa)
                + ">方位:</font>"
                +"<font color=" + MyApplication.getInstances().getResources().getColor(R.color.font_blue_text)+ ">"+ item.getStr("Direct")
                + "</font> "));
        return convertView;
    }


    private class Holder {
        TextView stationName;
        TextView stationArea;
        TextView stationStreet;
        TextView stationPosition;
    }
}