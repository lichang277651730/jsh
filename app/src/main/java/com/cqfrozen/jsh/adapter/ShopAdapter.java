package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.ShopInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */
public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {

    private Context context;
    private List<ShopInfo> shopInfos;
    public ShopAdapter(Context context, List<ShopInfo> shopInfos){
        this.context = context;
        this.shopInfos = shopInfos;
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
        final ShopInfo shopInfo = shopInfos.get(position);
        holder.tv_name.setText(shopInfo.china_name);
        holder.tv_phone.setText(shopInfo.mobile_num);
        holder.tv_address.setText(shopInfo.address);

        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEditClickListener != null){
                    onEditClickListener.onEdit(position, shopInfo);
                }
            }
        });

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDeleteClickListener != null){
                    onDeleteClickListener.onDelete(position, shopInfo.s_id);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private TextView tv_phone;
        private TextView tv_address;
        private TextView tv_edit;
        private TextView tv_delete;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_edit = (TextView) itemView.findViewById(R.id.tv_edit);
            tv_delete = (TextView) itemView.findViewById(R.id.tv_delete);
        }
    }

    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener{
        void onDelete(int position, String s_id);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    private OnEditClickListener onEditClickListener;

    public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    public interface OnEditClickListener{
        void onEdit(int position, ShopInfo shopInfo);
    }
}
