package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.common.widget.AutoTextView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.center.WebUrlActivity;
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
            holder.rl_hot.setVisibility(View.GONE);
            holder.hot_divider.setVisibility(View.GONE);
            return;
        }else {
            holder.rl_hot.setVisibility(View.VISIBLE);
            holder.hot_divider.setVisibility(View.VISIBLE);
        }
        String[] atvs = new String[notifyInfos.size()];
        for(int i = 0; i < notifyInfos.size(); i++){
            atvs[i] = notifyInfos.get(i).title;
        }
        holder.atv_notify.setText(atvs);
        holder.atv_notify.setOnItemClickListener(new AutoTextView.OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                if(!TextUtils.isEmpty(notifyInfos.get(index).content)){
                    Intent intent = new Intent(context, WebUrlActivity.class);
                    String title = notifyInfos.get(index).title.length() > 12 ?
                            notifyInfos.get(index).title.substring(0, 12) + "..." : notifyInfos.get(index).title;
                    intent.putExtra("title", title);
                    intent.putExtra("url", notifyInfos.get(index).content);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifyInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private AutoTextView atv_notify;
        private RelativeLayout rl_hot;
        private View hot_divider;
        public MyViewHolder(View itemView) {
            super(itemView);
            atv_notify = (AutoTextView) itemView.findViewById(R.id.atv_notify);
            rl_hot = (RelativeLayout) itemView.findViewById(R.id.rl_hot);
            hot_divider = (View) itemView.findViewById(R.id.hot_divider);
        }
    }
}
