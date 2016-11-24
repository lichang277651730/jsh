package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.common.widget.AutoTextView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.HomeNotifyInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HomeNotifyAdapter extends RecyclerView.Adapter<HomeNotifyAdapter.MyViewHolder> {

    private Context context;
    private List<HomeNotifyInfo> notifyInfos;
    public HomeNotifyAdapter(Context context, List<HomeNotifyInfo> notifyInfos){
        this.context = context;
        this.notifyInfos = notifyInfos;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_hot, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(notifyInfos.size() == 0){
            holder.headLayout.setVisibility(View.GONE);
            holder.hot_divider.setVisibility(View.GONE);
            return;
        }else {
            holder.headLayout.setVisibility(View.VISIBLE);
            holder.hot_divider.setVisibility(View.VISIBLE);
        }
        String[] atvs = new String[notifyInfos.size()];
        for(int i = 0; i < notifyInfos.size(); i++){
            atvs[i] = notifyInfos.get(i).title;
        }
        holder.atv_notify.setText(atvs);
    }

    @Override
    public int getItemCount() {
        return notifyInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private AutoTextView atv_notify;
        private LinearLayout headLayout;
        private View hot_divider;
        public MyViewHolder(View itemView) {
            super(itemView);
            atv_notify = (AutoTextView) itemView.findViewById(R.id.atv_notify);
            headLayout = (LinearLayout) itemView.findViewById(R.id.headLayout);
            hot_divider = (View) itemView.findViewById(R.id.hot_divider);
        }
    }
}
