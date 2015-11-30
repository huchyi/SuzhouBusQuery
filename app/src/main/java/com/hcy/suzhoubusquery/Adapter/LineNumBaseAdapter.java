package com.hcy.suzhoubusquery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hcy.suzhoubusquery.R;
import com.hcy.suzhoubusquery.utils.BaseBean;

import java.util.ArrayList;

/**
 * Created by hcy on 2015/11/30.
 *
 * LineNumBaseAdapter
 *
 */
public class LineNumBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private ArrayList<BaseBean> items;

    public LineNumBaseAdapter(Context context, ArrayList<BaseBean> items) {
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
            convertView = inflater.inflate(R.layout.fragment_line_num_layout_item, null);
            holder.lineNum = (TextView) convertView.findViewById(R.id.line_num);
            holder.linNumToWhere = (TextView) convertView.findViewById(R.id.to_where);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.lineNum.setText(item.getStr("LName")+"");
        holder.linNumToWhere.setText(item.getStr("LDirection")+"");
        return convertView;
    }


    private class Holder {
        TextView lineNum;
        TextView linNumToWhere;
    }
}
