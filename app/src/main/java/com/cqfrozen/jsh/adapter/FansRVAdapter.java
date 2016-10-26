package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.FansResultInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class FansRVAdapter extends RecyclerView.Adapter<FansRVAdapter.MyViewHolder> {

    private Context context;
    private List<FansResultInfo.FansInfo> fansInfos;

    public FansRVAdapter(Context context, List<FansResultInfo.FansInfo> fansInfos){
        this.context = context;
        this.fansInfos = fansInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_fansrv, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FansResultInfo.FansInfo fansInfo = fansInfos.get(position);
        holder.tv_fans_name.setText(fansInfo.store_name);
        holder.tv_fans_huibi.setText(fansInfo.hb_count);
    }

    @Override
    public int getItemCount() {
        return fansInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_fans_name;
        private TextView tv_fans_huibi;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_fans_name = (TextView) itemView.findViewById(R.id.tv_fans_name);
            tv_fans_huibi = (TextView) itemView.findViewById(R.id.tv_fans_huibi);
        }
    }
}
