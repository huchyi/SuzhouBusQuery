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
            holder.startRadio = (ImageView) convertView.findViewById(R.id.line_start);
            holder.middleRadio = (ImageView) convertView.findViewById(R.id.line_station);
            holder.endRadio = (ImageView) convertView.findViewById(R.id.line_end);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.carLine = (TextView) convertView.findViewById(R.id.car_bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if(position == 0){
            holder.startRadio.setVisibility(View.VISIBLE);
            holder.middleRadio.setVisibility(View.GONE);
            holder.endRadio.setVisibility(View.GONE);
        }else if(position == (getCount()-1)){
            holder.startRadio.setVisibility(View.GONE);
            holder.middleRadio.setVisibility(View.GONE);
            holder.endRadio.setVisibility(View.VISIBLE);
        }else{
            holder.startRadio.setVisibility(View.GONE);
            holder.middleRadio.setVisibility(View.VISIBLE);
            holder.endRadio.setVisibility(View.GONE);
        }

        holder.name.setText(item.getStr("SName"));
        String InTime = item.getStr("InTime");
        holder.time.setText(StringUtils.isNullOrNullStr(InTime) ? "": InTime);
        String BusInfo = item.getStr("BusInfo");
        holder.car.setVisibility(StringUtils.isNullOrNullStr(BusInfo) ? View.GONE: View.VISIBLE);
        holder.carLine.setVisibility(StringUtils.isNullOrNullStr(BusInfo) ? View.GONE: View.VISIBLE);
        return convertView;
    }

    private class Holder {
        ImageView car;
        ImageView startRadio;
        ImageView middleRadio;
        ImageView endRadio;
        TextView name;
        TextView time;
        TextView carLine;
    }
}
