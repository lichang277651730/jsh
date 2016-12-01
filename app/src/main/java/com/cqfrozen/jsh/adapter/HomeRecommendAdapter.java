package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsDetailActivity;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.util.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HomeRecommendAdapter  extends RecyclerView.Adapter<HomeRecommendAdapter.MyViewHolder>  {

    private Context context;
    private List<GoodsInfo> priceGoods;
    public HomeRecommendAdapter(Context context, List<GoodsInfo> priceGoods){
        this.context = context;
        this.priceGoods = priceGoods;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_goods3, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final GoodsInfo goodsInfo = priceGoods.get(position);
        ImageLoader.load(context, goodsInfo.pic_url, holder.iv_goods);
        holder.tv_name.setText(goodsInfo.g_name);
        holder.tv_brand.setText("品牌:" + goodsInfo.brand_name);
        holder.tv_size.setText("规格:" + goodsInfo.weight + "kg/" + goodsInfo.unit);
        holder.tv_price.setText("￥" + goodsInfo.now_price);
        holder.tv_market_price.setText("￥" + goodsInfo.market_price);
        holder.tv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("goodsInfo", goodsInfo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return priceGoods.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_goods;
        private TextView tv_name;
        private TextView tv_brand;
        private TextView tv_size;
        private TextView tv_market_price;
        private TextView tv_price;
        private ImageView iv_cart;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_goods = (ImageView) itemView.findViewById(R.id.iv_goods);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_brand = (TextView) itemView.findViewById(R.id.tv_brand);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_market_price = (TextView) itemView.findViewById(R.id.tv_market_price);
            iv_cart = (ImageView) itemView.findViewById(R.id.iv_cart);
        }

    }
}
