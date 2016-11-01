package com.cqfrozen.jsh.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.OrderInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public class OrderConfirmLvAdapter extends BaseAdapter {

    private Context context;
    private List<OrderInfo.OrderGoodsBean> goodsList;
    private final DisplayImageOptions defaultOptions;
    public OrderConfirmLvAdapter(Context context, List<OrderInfo.OrderGoodsBean> goodsList){
        this.context = context;
        this.goodsList = goodsList;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.img_loading_empty)
                .showImageOnFail(R.mipmap.img_loading_failed)
                .build();
    }
    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public OrderInfo.OrderGoodsBean getItem(int position) {
        return goodsList.get(position);
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
        OrderInfo.OrderGoodsBean orderGoodsBean = goodsList.get(position);
        ImageLoader.getInstance().displayImage(orderGoodsBean.pic_url, holder.iv_goods, defaultOptions);
        holder.tv_name.setText(orderGoodsBean.g_name);
        holder.tv_brand.setText("品牌: " + orderGoodsBean.brand_name);
        holder.tv_size.setText("规格: " + orderGoodsBean.weight + "kg/件");
        holder.tv_price.setText("¥" + orderGoodsBean.now_price);
        holder.tv_count.setText("X" + orderGoodsBean.count);
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
