package com.cqfrozen.jsh.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.OrderInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class OrderAddressAdapter extends RecyclerView.Adapter<OrderAddressAdapter.MyViewHolder> {

    private Context context;
    private List<OrderInfo.OrderAddressBean> orderAddressList;
    public OrderAddressAdapter(Context context, List<OrderInfo.OrderAddressBean> orderAddressList){
        this.context = context;
        this.orderAddressList = orderAddressList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final OrderInfo.OrderAddressBean orderAddressBean = orderAddressList.get(position);
        holder.tv_name.setText(orderAddressBean.china_name);
        holder.tv_phone.setText(orderAddressBean.mobile_num);
        holder.tv_address.setText(orderAddressBean.address);
        if(orderAddressBean.is_default == 1){
            holder.iv_default.setImageResource(R.mipmap.cart_checked);
        }else {
            holder.iv_default.setImageResource(R.mipmap.cart_uncheched);
        }
        holder.tv_edit.setVisibility(View.GONE);
        holder.tv_delete.setVisibility(View.GONE);
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

        private TextView tv_name;
        private TextView tv_phone;
        private TextView tv_address;
        private TextView tv_edit;
        private TextView tv_delete;
        private ImageView iv_default;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_edit = (TextView) itemView.findViewById(R.id.tv_edit);
            tv_delete = (TextView) itemView.findViewById(R.id.tv_delete);
            iv_default = (ImageView) itemView.findViewById(R.id.iv_default);
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
