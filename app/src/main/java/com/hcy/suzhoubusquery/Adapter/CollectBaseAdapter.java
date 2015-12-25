package com.hcy.suzhoubusquery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hcy.suzhoubusquery.R;
import com.hcy.suzhoubusquery.event.UpdateCollectEvent;
import com.hcy.suzhoubusquery.utils.BaseBean;
import com.hcy.suzhoubusquery.utils.LineNumInfoPreferenceUtil;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/12/1.
 * <p/>
 * CollectBaseAdapter
 */
public class CollectBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private ArrayList<BaseBean> items;
    private Context mContext;
    private boolean isShowDelete = false;

    public CollectBaseAdapter(Context context, ArrayList<BaseBean> items) {
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
            convertView = inflater.inflate(R.layout.fragment_line_num_collect_layout_item, null);
            holder.lineNum = (TextView) convertView.findViewById(R.id.line_num);
            holder.linNumToWhere = (TextView) convertView.findViewById(R.id.to_where);
            holder.delete = (LinearLayout) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.lineNum.setText(item.getStr("LName") + "");
        holder.linNumToWhere.setText(item.getStr("LDirection") + "");
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

            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("提示")
                    .setContentText("确定删除线路（"+item.getStr("LName") +"）吗？")
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
                            String strContent = "你已经删除了线路("+item.getStr("LName") +")";
                            String resetJson = "{\"LName\":\"" + item.getStr("LName") + "\",\"LDirection\":\"" + item.getStr("LDirection") + "\",\"Guid\":\"" + item.getStr("Guid") + "\"}";
                            String lineJson = LineNumInfoPreferenceUtil.getValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON, "");
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
                                    LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON, "");
                                }else{
                                    LineNumInfoPreferenceUtil.setValue(LineNumInfoPreferenceUtil.LineNumKey.LINE_NUM_JSON, lineJson);
                                }
                                EventBus.getDefault().post(new UpdateCollectEvent());
                            }else{
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
        TextView lineNum;
        TextView linNumToWhere;
        LinearLayout delete;
    }
}
