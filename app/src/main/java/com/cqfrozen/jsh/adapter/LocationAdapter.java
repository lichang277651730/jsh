package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.LocationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class LocationAdapter extends BaseAdapter {

    private Context context;
    private List<LocationInfo> locationInfos;
    private List<String> datas;
    public LocationAdapter(Context context, List<LocationInfo> locationInfos){
        this.context = context;
        this.locationInfos = locationInfos;
//        initData();
    }

    private void initData() {
        datas = new ArrayList<>();
        datas.add("渝北区");
        datas.add("巴南区");
        datas.add("九龙坡区");
        datas.add("南岸区");
        datas.add("沙坪坝区");
        datas.add("江北区");
        datas.add("黔江区");
        datas.add("渝中区");
        datas.add("大渡口区");
    }

    @Override
    public int getCount() {
        return locationInfos.size();
    }

    @Override
    public LocationInfo getItem(int position) {
        return locationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return locationInfos.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if(convertView == null){
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
            holder.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.tv_area.setText(locationInfos.get(position).area_name);
        return convertView;
    }

    class MyViewHolder{
        TextView tv_area;
    }
}
