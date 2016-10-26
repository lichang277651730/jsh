package com.cqfrozen.jsh.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.OrderDetailPageInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class OrderDetailRVAdapter extends RecyclerView.Adapter<OrderDetailRVAdapter.MyViewHolder> {

    private Context context;
    private List<OrderDetailPageInfo.OrderDetailPageBean> orderDetailPageBeanList;
    private final DisplayImageOptions defaultOptions;
    public OrderDetailRVAdapter(Context context, List<OrderDetailPageInfo.OrderDetailPageBean> orderDetailPageBeanList){
        this.context = context;
        this.orderDetailPageBeanList = orderDetailPageBeanList;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.solid_goods)
                .showImageOnFail(R.mipmap.solid_goods)
                .build();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_submit, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderDetailPageInfo.OrderDetailPageBean detailPageBean = orderDetailPageBeanList.get
                (position);
        ImageLoader.getInstance().displayImage(detailPageBean.pic_url, holder.iv_goods, defaultOptions);
        holder.tv_name.setText(detailPageBean.g_name);
        holder.tv_brand.setText("品牌: " + detailPageBean.brand_name);
        holder.tv_size.setText("规格: " + detailPageBean.weight + "kg/件");
        holder.tv_price.setText("¥" + detailPageBean.now_price);
        holder.tv_count.setText("X" + detailPageBean.count);
    }

    @Override
    public int getItemCount() {
        return orderDetailPageBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_goods;
        private TextView tv_name;
        private TextView tv_brand;
        private TextView tv_size;
        private TextView tv_price;
        private TextView tv_count;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_goods = (ImageView) itemView.findViewById(R.id.iv_goods);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_brand = (TextView) itemView.findViewById(R.id.tv_brand);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
        }
    }
}
