package com.cqfrozen.jsh.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.OrderDetailPageInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public class OrderDetailLvAdapter extends BaseAdapter {

    private Context context;
    private List<OrderDetailPageInfo.OrderDetailPageBean> orderDetailPageBeanList;
    private final DisplayImageOptions defaultOptions;
    public OrderDetailLvAdapter(Context context, List<OrderDetailPageInfo.OrderDetailPageBean> orderDetailPageBeanList){
        this.context = context;
        this.orderDetailPageBeanList = orderDetailPageBeanList;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.img_loading_empty)
                .showImageOnFail(R.mipmap.img_loading_failed)
                .build();
    }

    @Override
    public int getCount() {
        return orderDetailPageBeanList.size();
    }

    @Override
    public OrderDetailPageInfo.OrderDetailPageBean getItem(int position) {
        return orderDetailPageBeanList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_submit, parent, false);
            holder.iv_goods = (ImageView) convertView.findViewById(R.id.iv_goods);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_brand = (TextView) convertView.findViewById(R.id.tv_brand);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        OrderDetailPageInfo.OrderDetailPageBean detailPageBean = orderDetailPageBeanList.get
                (position);
        ImageLoader.getInstance().displayImage(detailPageBean.pic_url, holder.iv_goods, defaultOptions);
        holder.tv_name.setText(detailPageBean.g_name);
        holder.tv_brand.setText("品牌: " + detailPageBean.brand_name);
        holder.tv_size.setText("规格: " + detailPageBean.weight + "kg/件");
        holder.tv_price.setText("¥" + detailPageBean.now_price);
        holder.tv_count.setText("X" + detailPageBean.count);
        return convertView;
    }

    public class MyViewHolder{
        public ImageView iv_goods;
        public TextView tv_name;
        public TextView tv_brand;
        public TextView tv_size;
        public TextView tv_price;
        public TextView tv_count;
    }
}
