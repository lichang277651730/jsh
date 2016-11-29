package com.cqfrozen.jsh.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.OrderInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public class OrderShopAdapter extends RecyclerView.Adapter<OrderShopAdapter.MyViewHolder>{

    private Context context;
    private List<OrderInfo.OrderAddressBean> orderAddressList;
    public OrderShopAdapter(Context context, List<OrderInfo.OrderAddressBean> orderAddressList){
        this.context = context;
        this.orderAddressList = orderAddressList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final OrderInfo.OrderAddressBean orderAddressBean = orderAddressList.get(position);
        holder.tv_men_name.setText("店长:" + orderAddressBean.china_name);
        holder.tv_shop_name.setText("店铺:" + orderAddressBean.store_name);
        holder.tv_phone.setText(orderAddressBean.mobile_num);
        holder.tv_address.setText("收货地址:" + orderAddressBean.address);
        if(orderAddressBean.is_main_store == 1){
            holder.tv_main_shop_no.setVisibility(View.GONE);
            holder.tv_main_shop_yes.setVisibility(View.VISIBLE);
        }else if(orderAddressBean.is_main_store == 0){
            holder.tv_main_shop_no.setVisibility(View.VISIBLE);
            holder.tv_main_shop_yes.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position, orderAddressBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderAddressList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_shop_name;
        private TextView tv_main_shop_yes;
        private TextView tv_main_shop_no;
        private TextView tv_men_name;
        private TextView tv_phone;
        private TextView tv_address;
        private TextView tv_edit;
        private TextView tv_delete;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_men_name = (TextView) itemView.findViewById(R.id.tv_men_name);
            tv_shop_name = (TextView) itemView.findViewById(R.id.tv_shop_name);
            tv_main_shop_yes = (TextView) itemView.findViewById(R.id.tv_main_shop_yes);
            tv_main_shop_no = (TextView) itemView.findViewById(R.id.tv_main_shop_no);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_edit = (TextView) itemView.findViewById(R.id.tv_edit);
            tv_delete = (TextView) itemView.findViewById(R.id.tv_delete);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position, OrderInfo.OrderAddressBean orderAddressBean);
    }
}
