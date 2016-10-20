package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.center.AddressEditActivity;
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
        holder.cb_default.setChecked(addressInfo.is_default == 1 ? true : false);
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddressEditActivity.class);
                intent.putExtra("addressInfo", addressInfo);
                context.startActivity(intent);
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
        private CheckBox cb_default;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_edit = (TextView) itemView.findViewById(R.id.tv_edit);
            tv_delete = (TextView) itemView.findViewById(R.id.tv_delete);
            cb_default = (CheckBox) itemView.findViewById(R.id.cb_default);
        }
    }

    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener{
        void onDelete(int position, String a_id);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }
}
