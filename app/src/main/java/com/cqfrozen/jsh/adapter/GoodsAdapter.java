package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.cart.CartManager;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.util.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.MyViewHolder> {

    private Context context;
    private List<GoodsInfo> goodsInfos;
    private final DisplayImageOptions defaultOptions;
    private CartManager cartManager;

    public GoodsAdapter(Context context, List<GoodsInfo> goodsInfos){
        this.context = context;
        this.goodsInfos = goodsInfos;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.solid_goods)
                .showImageOnFail(R.mipmap.solid_goods)
                .build();
        this.cartManager =  CartManager.getInstance(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_goods, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final GoodsInfo goodsInfo = goodsInfos.get(position);
        ImageLoader.getInstance().displayImage(goodsInfo.pic_url, holder.iv_goods, defaultOptions);
        holder.tv_name.setText(goodsInfo.g_name);
        holder.tv_now_price.setText("¥" + goodsInfo.now_price);
        holder.tv_market_price.setText("¥" + goodsInfo.market_price);
        holder.tv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        //点击购物车
        holder.iv_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManager.add(goodsInfo);
                ToastUtil.showToast(context, "添加购物车成功");
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodsInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_goods;
        private TextView tv_name;
        private TextView tv_now_price;
        private TextView tv_market_price;
        private ImageView iv_add_cart;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_goods = (ImageView) itemView.findViewById(R.id.iv_goods);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_now_price = (TextView) itemView.findViewById(R.id.tv_now_price);
            tv_market_price = (TextView) itemView.findViewById(R.id.tv_market_price);
            iv_add_cart = (ImageView) itemView.findViewById(R.id.iv_add_cart);
        }
    }
}
