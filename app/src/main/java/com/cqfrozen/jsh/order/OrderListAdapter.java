package com.cqfrozen.jsh.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.OrderResultInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {

    private Context context;
    private List<OrderResultInfo.OrderSearchInfo> orderSearchInfos;
    private final DisplayImageOptions defaultOptions;
    public OrderListAdapter(Context context, List<OrderResultInfo.OrderSearchInfo> orderSearchInfos){
        this.context = context;
        this.orderSearchInfos = orderSearchInfos;
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
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_orderlist, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final OrderResultInfo.OrderSearchInfo orderSearchInfo = orderSearchInfos.get(position);
        holder.tv_time.setText("下单时间: " + orderSearchInfo.add_time);
        holder.tv_result_status.setText(orderSearchInfo.status_name);
        OrderResultInfo.OrderGoodsInfo orderGoodsInfo = orderSearchInfo.orderinfo.get(0);
        ImageLoader.getInstance().displayImage(orderGoodsInfo.pic_url, holder.iv_goods, defaultOptions);
        holder.tv_name.setText(orderGoodsInfo.goods_name);
        holder.tv_brand.setText("品牌: " + orderGoodsInfo.brand_name);
        holder.tv_size.setText("规格: " + orderGoodsInfo.weight + "kg/件");
        holder.tv_price.setText("¥" + orderGoodsInfo.now_price);
        holder.tv_count.setText("X" + orderGoodsInfo.goods_count);
        holder.tv_order_count.setText(orderSearchInfo.count);
        holder.tv_order_sum.setText("¥" + orderSearchInfo.order_amount);


        // 0所有按钮都不显示，
        // 1取消、去支付(未付款),
        // 2取消（货到付款未出库），
        // 3确认收货（已发货）,
        // 4去评价(已收货、未评价)，
        // 5删除（取消订单、已完成评价订单）
        switch (orderSearchInfo.btn_type) {
            case 0:
                holder.btn_cancel.setVisibility(View.GONE);
                holder.btn_confirm.setVisibility(View.GONE);
                break;
            case 1:
                holder.btn_cancel.setVisibility(View.VISIBLE);
                holder.btn_confirm.setVisibility(View.VISIBLE);
                holder.btn_confirm.setText("去支付");
                break;
            case 2:
                holder.btn_cancel.setVisibility(View.VISIBLE);
                holder.btn_confirm.setVisibility(View.GONE);
                break;
            case 3:
                holder.btn_cancel.setVisibility(View.GONE);
                holder.btn_confirm.setVisibility(View.VISIBLE);
                holder.btn_confirm.setText("确认收货");
                break;
            case 4:
                holder.btn_cancel.setVisibility(View.GONE);
                holder.btn_confirm.setVisibility(View.VISIBLE);
                holder.btn_confirm.setText("去评价");
                break;
            case 5:
                holder.btn_cancel.setVisibility(View.GONE);
                holder.btn_confirm.setVisibility(View.VISIBLE);
                holder.btn_confirm.setText("删除");
                break;
        }

        holder.include_item_order_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position, orderSearchInfo);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderSearchInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_time;
        private TextView tv_result_status;
        private TextView tv_order_count;
        private TextView tv_order_sum;
        private Button btn_cancel;
        private Button btn_confirm;
        private ImageView iv_goods;
        private TextView tv_name;
        private TextView tv_brand;
        private TextView tv_size;
        private TextView tv_price;
        private TextView tv_count;
        private LinearLayout include_item_order_sumbit;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_result_status = (TextView) itemView.findViewById(R.id.tv_result_status);
            include_item_order_sumbit = (LinearLayout) itemView.findViewById(R.id.include_item_order_sumbit);
            tv_order_count = (TextView) itemView.findViewById(R.id.tv_order_count);
            tv_order_sum = (TextView) itemView.findViewById(R.id.tv_order_sum);
            btn_cancel = (Button) itemView.findViewById(R.id.btn_cancel);
            btn_confirm = (Button) itemView.findViewById(R.id.btn_confirm);
            iv_goods = (ImageView) itemView.findViewById(R.id.iv_goods);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_brand = (TextView) itemView.findViewById(R.id.tv_brand);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position, OrderResultInfo.OrderSearchInfo orderSearchInfo);
    }
}
