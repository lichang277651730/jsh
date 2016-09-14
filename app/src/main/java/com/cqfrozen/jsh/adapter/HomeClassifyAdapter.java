package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.HomeClassifyInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class HomeClassifyAdapter extends RecyclerView.Adapter<HomeClassifyAdapter.MyViewHolder> {


    private String[] titles = new String[]{"我要订货", "常用采购", "我的积分", "我的订单"};
    private int[] imgs = new int[]{R.mipmap.u44_normal, R.mipmap.u46_normal, R.mipmap.u47_normal, R.mipmap.u49_normal};
    private Context context;
    private List<HomeClassifyInfo> classifyInfos;
    public HomeClassifyAdapter(Context context, List<HomeClassifyInfo> classifyInfos){
        this.context = context;
        this.classifyInfos = classifyInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_homeclassify, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_classify.setText(titles[position - 1]);
        holder.iv_classify.setImageResource(imgs[position - 1]);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_classify;
        private TextView tv_classify;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_classify = (ImageView) itemView.findViewById(R.id.iv_classify);
            tv_classify = (TextView) itemView.findViewById(R.id.tv_classify);
        }
    }
}
