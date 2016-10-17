package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.GoodsInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class HomePopAdapter extends RecyclerView.Adapter<HomePopAdapter.MyViewHolder> {

    private Context context;
    private List<GoodsInfo> priceGoods;
    public HomePopAdapter(Context context, List<GoodsInfo> priceGoods){
//        this.context = context;
        this.priceGoods = priceGoods;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_homepop, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.rv_homeprice.setOverScrollMode(View.OVER_SCROLL_NEVER);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.rv_homeprice.setLayoutManager(manager);
        holder.rv_homeprice.setAdapter(new HomeGoodsAdapter(context, priceGoods));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView rv_homeprice;
        public MyViewHolder(View itemView) {
            super(itemView);
            rv_homeprice = (RecyclerView) itemView.findViewById(R.id.rv_homeprice);
        }
    }
}
