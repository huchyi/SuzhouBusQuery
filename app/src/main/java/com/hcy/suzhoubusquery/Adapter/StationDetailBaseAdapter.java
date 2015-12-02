package com.hcy.suzhoubusquery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hcy.suzhoubusquery.MyApplication;
import com.hcy.suzhoubusquery.R;
import com.hcy.suzhoubusquery.utils.BaseBean;
import com.hcy.suzhoubusquery.utils.StringUtils;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/12/2.
 *
 *
 */
public class StationDetailBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private ArrayList<BaseBean> items;

    public StationDetailBaseAdapter(Context context, ArrayList<BaseBean> bean) {
        this.inflater = LayoutInflater.from(context);
        this.items = bean;
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
            convertView = inflater.inflate(R.layout.activity_station_detail_item, null);
            holder.carLine = (TextView) convertView.findViewById(R.id.station_car_line);
            holder.carDistince = (TextView) convertView.findViewById(R.id.station_car_distince);
            holder.carDirection = (TextView) convertView.findViewById(R.id.station_car_direction);
            holder.carAlready = (TextView) convertView.findViewById(R.id.station_car_about_already);
            holder.carNow = (TextView) convertView.findViewById(R.id.station_car_now);
            holder.carTime = (TextView) convertView.findViewById(R.id.station_car_time);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.carLine.setText(item.getStr("LName"));

        String carDistinceStr = item.getStr("Distince");
        if("-1".equals(carDistinceStr)){
            holder.carDistince.setText("准备发车");
            holder.carDistince.setTextSize(MyApplication.getInstances().getResources().getDimension(R.dimen.station_detail_item_text_s));
        }else if( "0".equals(carDistinceStr)){
            holder.carDistince.setText("到站");
            holder.carDistince.setTextSize(MyApplication.getInstances().getResources().getDimension(R.dimen.station_detail_item_text_s));
        }else{
            holder.carDistince.setTextSize(MyApplication.getInstances().getResources().getDimension(R.dimen.station_detail_item_text_b));
            holder.carDistince.setText(carDistinceStr);
        }

        String carDirectionStr = "开往:  " + item.getStr("LDirection");
        holder.carDirection.setText(carDirectionStr);

        if(isNumeric(carDistinceStr)){
            try{
                int num = Integer.parseInt(carDistinceStr);
                String time = "约"+ (num * 3) +"分钟到站";
                holder.carAlready.setText(time);
                holder.carAlready.setVisibility(View.VISIBLE);
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            holder.carAlready.setVisibility(View.GONE);
        }

        String carNowStr = item.getStr("SName");
        if(!StringUtils.isNullOrNullStr(carNowStr)){
            carNowStr = "到达:" + carNowStr;
            holder.carNow.setText(carNowStr);
            holder.carNow.setVisibility(View.VISIBLE);
        }else{
            holder.carNow.setVisibility(View.GONE);
        }

        String carTimeSrr = item.getStr("InTime");
        if(!StringUtils.isNullOrNullStr(carTimeSrr)){
            holder.carTime.setText(carTimeSrr);
            holder.carTime.setVisibility(View.VISIBLE);
        }else{
            holder.carTime.setVisibility(View.GONE);
        }
        return convertView;
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    private class Holder {
        TextView carLine;
        TextView carDistince;
        TextView carDirection;
        TextView carAlready;
        TextView carNow;
        TextView carTime;
    }
}