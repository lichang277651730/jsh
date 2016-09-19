package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cqfrozen.jsh.entity.GoodsInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 * 首页特价数据适配器
 */
public class HomeGoodsAdapter extends RecyclerView.Adapter<HomeGoodsAdapter.MyViewHolder> {

    private Context context;
    private List<GoodsInfo> goodsInfos;
    public HomeGoodsAdapter(Context context, List<GoodsInfo> goodsInfos){
        this.context = context;
        this.goodsInfos = goodsInfos;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
