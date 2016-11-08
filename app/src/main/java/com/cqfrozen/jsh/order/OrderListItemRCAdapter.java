package com.cqfrozen.jsh.order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.OrderResultInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */
public class OrderListItemRCAdapter extends RecyclerView.Adapter<OrderListItemRCAdapter.MyViewHolder> {

    private Context context;
    private List<OrderResultInfo.OrderGoodsInfo> orderGoodsList;
    private final DisplayImageOptions defaultOptions;
    public OrderListItemRCAdapter(Context context, List<OrderResultInfo.OrderGoodsInfo> orderGoodsList){
        this.context = context;
        this.orderGoodsList = orderGoodsList;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.img_loading_empty)
                .showImageOnFail(R.mipmap.img_loading_failed)
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
        final OrderResultInfo.OrderGoodsInfo orderGoodsInfo = orderGoodsList.get(position);
        ImageLoader.getInstance().displayImage(orderGoodsInfo.pic_url, holder.iv_goods, defaultOptions);
        holder.tv_name.setText(orderGoodsInfo.goods_name);
        holder.tv_brand.setText("品牌: " + orderGoodsInfo.brand_name);
        holder.tv_size.setText("规格: " + orderGoodsInfo.weight + "kg/件");
        holder.tv_price.setText("¥" + orderGoodsInfo.now_price);
        holder.tv_count.setText("X" + orderGoodsInfo.goods_count);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("o_id", orderGoodsInfo.o_id);
                intent.putExtra("from", OrderDetailActivity.FROM.FROM_ORDER_LIST);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderGoodsList.size();
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
