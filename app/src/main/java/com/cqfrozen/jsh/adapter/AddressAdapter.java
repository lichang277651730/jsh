package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.AddressInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private Context context;
    private List<AddressInfo> addressInfos;
    public AddressAdapter(Context context, List<AddressInfo> addressInfos){
        this.context = context;
        this.addressInfos = addressInfos;
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
        final AddressInfo addressInfo = addressInfos.get(position);
        holder.tv_name.setText(addressInfo.china_name);
        holder.tv_phone.setText(addressInfo.mobile_num);
        holder.tv_address.setText(addressInfo.address);

        if(addressInfo.is_default == 1){
            holder.iv_default.setImageResource(R.mipmap.cart_checked);
        }else {
            holder.iv_default.setImageResource(R.mipmap.cart_uncheched);
        }

        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onEditClickListener != null){
                    onEditClickListener.onEdit(position, addressInfo);
                }
            }
        });

        holder.iv_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCheckClickListener != null){
                    onCheckClickListener.onCheck(position, addressInfo.a_id, addressInfo.is_default);
                }
            }
        });

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDeleteClickListener != null){
                    onDeleteClickListener.onDelete(position, addressInfo.a_id);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressInfos.size();
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

    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener{
        void onDelete(int position, String a_id);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    private OnEditClickListener onEditClickListener;

    public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    public interface OnEditClickListener{
        void onEdit(int position, AddressInfo addressInfo);
    }

    private OnCheckClickListener onCheckClickListener;

    public void setOnCheckClickListener(OnCheckClickListener onCheckClickListener) {
        this.onCheckClickListener = onCheckClickListener;
    }

    public interface OnCheckClickListener{
        void onCheck(int position, String a_id, int is_default);
    }

}
