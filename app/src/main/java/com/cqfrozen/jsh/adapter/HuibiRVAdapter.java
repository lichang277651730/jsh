package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.HuibiInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class HuibiRVAdapter extends RecyclerView.Adapter<HuibiRVAdapter.MyViewHolder> {

    private Context context;
    private List<HuibiInfo> huibiInfos ;
    public HuibiRVAdapter(Context context, List<HuibiInfo> huibiInfos){
        this.context = context;
        this.huibiInfos = huibiInfos;
    }

    @Override
    public HuibiRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_huibi, parent, false));
    }

    @Override
    public void onBindViewHolder(HuibiRVAdapter.MyViewHolder holder, int position) {
        HuibiInfo huibiInfo = huibiInfos.get(position);
        holder.tv_desc.setText(huibiInfo.title);
        holder.tv_time.setText(huibiInfo.add_time);
        holder.tv_count.setText(huibiInfo.hb_count);
    }

    @Override
    public int getItemCount() {
        return huibiInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_desc;
        private TextView tv_time;
        private TextView tv_count;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
        }
    }
}
