package com.hcy.suzhoubusquery.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hcy.suzhoubusquery.MyApplication;
import com.hcy.suzhoubusquery.R;
import com.hcy.suzhoubusquery.event.UpdateCollectEvent;
import com.hcy.suzhoubusquery.event.UpdateCollectStationEvent;
import com.hcy.suzhoubusquery.utils.BaseBean;
import com.hcy.suzhoubusquery.utils.LineNumInfoPreferenceUtil;
import com.hcy.suzhoubusquery.view.CustomDialog;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/12/2.
 *
 * StationCollectBaseAdapter
 */
public class StationCollectBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private ArrayList<BaseBean> items;
    private Context mContext;

    public StationCollectBaseAdapter(Context context, ArrayList<BaseBean> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.mContext = context;
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
            convertView = inflater.inflate(R.layout.fragment_station_collect_layout_item, null);
            holder.stationName = (TextView) convertView.findViewById(R.id.station_name);
            holder.stationArea = (TextView) convertView.findViewById(R.id.station_area);
            holder.stationStreet = (TextView) convertView.findViewById(R.id.station_street);
            holder.stationPosition = (TextView) convertView.findViewById(R.id.station_position);
            holder.delete = (LinearLayout) convertView.findViewById(R.id.delete);
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

        holder.stationPosition.setText(Html.fromHtml("<font color=" + MyApplication.getInstances().getResources().getColor(R.color.font_main_aaaaaa)
                + ">方位:</font>"
                + "<font color=" + MyApplication.getInstances().getResources().getColor(R.color.font_blue_text) + ">" + item.getStr("Direct")
                + "</font> "));

        holder.delete.setOnClickListener(onClickListener);
        holder.delete.setTag(position);
        return convertView;
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            int pos = (int) v.getTag();
            final BaseBean item = items.get(pos);
            CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
            builder.setMessage("确定删除（"+item.getStr("Name") +"）吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String resetJson = "{\"Name\":\"" + item.getStr("Name")
                            + "\",\"NoteGuid\":\"" + item.getStr("NoteGuid")
                            + "\",\"Canton\":\"" + item.getStr("Canton")
                            + "\",\"Road\":\"" + item.getStr("Road")
                            + "\",\"Direct\":\"" + item.getStr("Direct")
                            + "\"}";
                    String lineJson = LineNumInfoPreferenceUtil.getValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, "");
                    if (lineJson.contains(resetJson)) {
                        lineJson = lineJson.replace(resetJson,"");
                        if(lineJson.contains(",]}")){
                            lineJson = lineJson.replace(",]}","]}");
                        }
                        if(lineJson.contains(",,")){
                            lineJson = lineJson.replace(",,", ",");
                        }
                        if(lineJson.contains("{\"list\":[,{")){
                            lineJson = lineJson.replace("{\"list\":[,{", "{\"list\":[{");
                        }
                        if(lineJson.length() < 20){
                            LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, "");
                        }else{
                            LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, lineJson);
                        }
                        EventBus.getDefault().post(new UpdateCollectStationEvent());
                    }else{
                        MyApplication.getInstances().showToast("删除失败");
                    }
                }
            });
            builder.setNegativeButton("取消",
                    new android.content.DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            builder.create().show();
        }
    };



    private class Holder {
        TextView stationName;
        TextView stationArea;
        TextView stationStreet;
        TextView stationPosition;
        LinearLayout delete;
    }
}