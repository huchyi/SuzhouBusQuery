package com.hcy.suzhoubusquery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcy.suzhoubusquery.R;
import com.hcy.suzhoubusquery.utils.BaseBean;
import com.hcy.suzhoubusquery.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/1.
 *
 * LineDirectionBaseAdapter
 */
public class LineDirectionBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private ArrayList<BaseBean> items;

    public LineDirectionBaseAdapter(Context context, ArrayList<BaseBean> bean) {
        this.inflater = LayoutInflater.from(context);
        this.items = bean;
    }

    public void refreshData(ArrayList<BaseBean> bean){
        if(bean != null ){
            bean.clear();
        }
        this.items = bean;
        notifyDataSetChanged();
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
            convertView = inflater.inflate(R.layout.activity_line_direction_item, null);
            holder.car = (ImageView) convertView.findViewById(R.id.car);
            holder.startLine = (ImageView) convertView.findViewById(R.id.line_line_top);
            holder.middleStation = (TextView) convertView.findViewById(R.id.line_station);
            holder.endLine = (ImageView) convertView.findViewById(R.id.line_line_bottom);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.carLine = convertView.findViewById(R.id.car_bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.middleStation.setText(String.valueOf(position+1));
        holder.name.setText(item.getStr("SName"));

        String InTime = item.getStr("InTime");
        String InTimeStr = "";
        if(!StringUtils.isNullOrNullStr(InTime)){
            if(InTime.contains(":")){
                String[] strs = InTime.split(":");
                if(strs.length > 2){
                    InTimeStr = strs[0] + ":" + strs[1] + (position == 0 ? " 出发" : " 进站");
                }else{
                    InTimeStr = InTime;
                }
            }else{
                InTimeStr = InTime;
            }
        }
        holder.time.setText(InTimeStr);
        String BusInfo = item.getStr("BusInfo");

        holder.car.setVisibility(StringUtils.isNullOrNullStr(BusInfo) ? View.INVISIBLE: View.VISIBLE);
        holder.carLine.setVisibility(StringUtils.isNullOrNullStr(BusInfo) ? View.INVISIBLE: View.VISIBLE);

        if(position == 0){
            holder.startLine.setVisibility(View.INVISIBLE);
            holder.endLine.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
            holder.middleStation.setBackgroundResource(R.drawable.line_station_end_new_icon);
        }else if(position == (getCount()-1)){
            holder.middleStation.setBackgroundResource(R.drawable.line_station_end_new_icon);
            holder.startLine.setVisibility(View.VISIBLE);
            holder.endLine.setVisibility(View.INVISIBLE);
            holder.car.setVisibility(View.INVISIBLE);
            holder.carLine.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.INVISIBLE);
        }else{
            holder.middleStation.setBackgroundResource(R.drawable.line_station_new_icon);
            holder.startLine.setVisibility(View.VISIBLE);
            holder.endLine.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private class Holder {
        ImageView car;
        ImageView startLine;
        TextView middleStation;
        ImageView endLine;
        TextView name;
        TextView time;
        View carLine;
    }
}
