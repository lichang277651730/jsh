package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.HuibiInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class HuibiAdapter extends BaseAdapter {

    private Context context;
    private List<HuibiInfo> huibiInfos;
    public HuibiAdapter(Context context, List<HuibiInfo> huibiInfos){
        this.context = context;
        this.huibiInfos = huibiInfos;
    }
    @Override
    public int getCount() {
        return huibiInfos.size();
    }

    @Override
    public HuibiInfo getItem(int position) {
        return huibiInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if(convertView == null){
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_huibi, parent, false);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        HuibiInfo huibiInfo = huibiInfos.get(position);
        holder.tv_desc.setText(huibiInfo.title);
        holder.tv_time.setText(huibiInfo.add_time);
        holder.tv_count.setText("+" + huibiInfo.hb_count);
        return convertView;
    }

    public class MyViewHolder{
        public TextView tv_desc;
        public TextView tv_time;
        public TextView tv_count;
    }
}
