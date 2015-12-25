package com.hcy.suzhoubusquery.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hcy.suzhoubusquery.MyApplication;
import com.hcy.suzhoubusquery.R;
import com.hcy.suzhoubusquery.event.UpdateCollectStationEvent;
import com.hcy.suzhoubusquery.utils.BaseBean;
import com.hcy.suzhoubusquery.utils.LineNumInfoPreferenceUtil;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/12/2.
 * <p/>
 * StationCollectBaseAdapter
 */
public class StationCollectBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private ArrayList<BaseBean> items;
    private Context mContext;
    private boolean isShowDelete = false;

    public StationCollectBaseAdapter(Context context, ArrayList<BaseBean> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.mContext = context;
    }


    public void remove(int arg0) {//删除指定位置的item
        items.remove(arg0);
        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源
    }

    public void insert(BaseBean item, int arg0) {//在指定位置插入item
        items.add(arg0, item);
        this.notifyDataSetChanged();
    }

    public void setDeleteButton(boolean isShow){
        this.isShowDelete = isShow;
        notifyDataSetChanged();
    }

    public ArrayList<BaseBean> getAllData(){
        return items;
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

        if(isShowDelete){
            holder.delete.setOnClickListener(onClickListener);
            holder.delete.setTag(position);
            holder.delete.setVisibility(View.VISIBLE);
        }else{
            holder.delete.setVisibility(View.GONE);
        }
        return convertView;
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            int pos = (int) v.getTag();
            final BaseBean item = items.get(pos);
//            CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
//            builder.setMessage("确定删除（"+item.getStr("Name") +"）吗？");
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    String resetJson = "{\"Name\":\"" + item.getStr("Name")
//                            + "\",\"NoteGuid\":\"" + item.getStr("NoteGuid")
//                            + "\",\"Canton\":\"" + item.getStr("Canton")
//                            + "\",\"Road\":\"" + item.getStr("Road")
//                            + "\",\"Direct\":\"" + item.getStr("Direct")
//                            + "\"}";
//                    String lineJson = LineNumInfoPreferenceUtil.getValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, "");
//                    if (lineJson.contains(resetJson)) {
//                        lineJson = lineJson.replace(resetJson,"");
//                        if(lineJson.contains(",]}")){
//                            lineJson = lineJson.replace(",]}","]}");
//                        }
//                        if(lineJson.contains(",,")){
//                            lineJson = lineJson.replace(",,", ",");
//                        }
//                        if(lineJson.contains("{\"list\":[,{")){
//                            lineJson = lineJson.replace("{\"list\":[,{", "{\"list\":[{");
//                        }
//                        if(lineJson.length() < 20){
//                            LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, "");
//                        }else{
//                            LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, lineJson);
//                        }
//                        EventBus.getDefault().post(new UpdateCollectStationEvent());
//                    }else{
//                        MyApplication.getInstances().showToast("删除失败");
//                    }
//                }
//            });
//            builder.setNegativeButton("取消",
//                    new android.content.DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//
//            builder.create().show();


            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("提示")
                    .setContentText("确定删除站台（" + item.getStr("Name") + "）吗？")
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
                            String strContent = "你已经删除了站台(" + item.getStr("Name") + ")";

                            String resetJson = "{\"Name\":\"" + item.getStr("Name")
                                    + "\",\"NoteGuid\":\"" + item.getStr("NoteGuid")
                                    + "\",\"Canton\":\"" + item.getStr("Canton")
                                    + "\",\"Road\":\"" + item.getStr("Road")
                                    + "\",\"Direct\":\"" + item.getStr("Direct")
                                    + "\"}";
                            String lineJson = LineNumInfoPreferenceUtil.getValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, "");
                            if (lineJson.contains(resetJson)) {
                                lineJson = lineJson.replace(resetJson, "");
                                if (lineJson.contains(",]}")) {
                                    lineJson = lineJson.replace(",]}", "]}");
                                }
                                if (lineJson.contains(",,")) {
                                    lineJson = lineJson.replace(",,", ",");
                                }
                                if (lineJson.contains("{\"list\":[,{")) {
                                    lineJson = lineJson.replace("{\"list\":[,{", "{\"list\":[{");
                                }
                                if (lineJson.length() < 20) {
                                    LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, "");
                                } else {
                                    LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_STATION_JSON, lineJson);
                                }
                                EventBus.getDefault().post(new UpdateCollectStationEvent());
                            } else {
                                strContent = "删除失败";
                            }

                            sDialog.setTitleText("删除!")
                                    .setContentText(strContent)
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    })
                    .show();
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