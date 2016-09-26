package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.widget.AutoTextView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.HomeNotifyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HomeNotifyAdapter extends RecyclerView.Adapter<HomeNotifyAdapter.MyViewHolder> {

    private Context context;
    private List<String> datas;
    private String[] ary = new String[]{"全场购满399面运费", "中秋国庆放假不发货", "热卖中的商品"};
    private List<HomeNotifyInfo> notifyInfos;
    public HomeNotifyAdapter(Context context, List<HomeNotifyInfo> notifyInfos){
        this.context = context;
        this.notifyInfos = notifyInfos;
//        initData();
    }

    private void initData() {
        datas = new ArrayList<>();
        datas.add("全场购满399面运费");
        datas.add("中秋国庆放假不发货");
        datas.add("热卖中的商品");
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
            return;
        }
        String[] atvs = new String[notifyInfos.size()];
        for(int i = 0; i < notifyInfos.size(); i++){
            atvs[i] = notifyInfos.get(i).title;
        }
        holder.atv_notify.setText(atvs);
//        holder.atv_notify.setText(null);
    }

    @Override
    public int getItemCount() {
        return notifyInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private AutoTextView atv_notify;
        public MyViewHolder(View itemView) {
            super(itemView);
            atv_notify = (AutoTextView) itemView.findViewById(R.id.atv_notify);
        }
    }
}