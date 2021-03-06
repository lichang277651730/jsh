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

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsDetailActivity;
import com.cqfrozen.jsh.cart.CartManager;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 * 首页特价数据适配器
 */
public class HomeGoodsAdapter extends RecyclerView.Adapter<HomeGoodsAdapter.MyViewHolder> {

    private Context context;
    private List<GoodsInfo> goodsInfos;
    private final DisplayImageOptions defaultOptions;
    private CartManager cartManager;
    private final HttpForVolley http;

    public HomeGoodsAdapter(Context context, List<GoodsInfo> goodsInfos){
        this.context = context;
        this.goodsInfos = goodsInfos;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.img_loading_empty)
                .showImageOnFail(R.mipmap.img_loading_failed)
                .build();
        this.http = new HttpForVolley(context);
        this.cartManager =  CartManager.getInstance(context);
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
        final GoodsInfo goodsInfo = goodsInfos.get(position);
        ImageLoader.getInstance().displayImage(goodsInfo.pic_url, holder.iv_goods, defaultOptions);
        holder.tv_name.setText(goodsInfo.g_name);
        holder.tv_now_price.setText("¥" + goodsInfo.now_price);
        holder.tv_market_price.setText("¥" + goodsInfo.market_price);
        holder.tv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
//        if(goodsInfo.is_oos == 1){//不缺货
//            holder.labelview.setVisibility(View.VISIBLE);
//        }else if(goodsInfo.is_oos == 0){//缺货
//            holder.labelview.setVisibility(View.GONE);
//        }
        if(goodsInfo.is_oos == 1){//不缺货
            holder.tv_no_goods.setVisibility(View.VISIBLE);
        }else if(goodsInfo.is_oos == 0){//缺货
            holder.tv_no_goods.setVisibility(View.GONE);
        }
        //点击购物车
//        holder.iv_add_cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //TODO 要替换区域id
//                MyHttp.addcart(http, null, goodsInfo.g_id, "5", 1, new HttpForVolley.HttpTodo() {
//                    @Override
//                    public void httpTodo(Integer which, JSONObject response) {
//                        ToastUtil.showToast(context, response.optString("msg"));
//                        int code = response.optInt("code");
//                        if(code != 0){
//                            return;
//                        }
//                        cartManager.add(goodsInfo);
//                    }
//                });
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
//                intent.putExtra("g_id", goodsInfo.g_id);
                intent.putExtra("goodsInfo", goodsInfo);
                context.startActivity(intent);
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
        private TextView tv_no_goods;
//        private LabelView labelview;
//        private ImageView iv_add_cart;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_goods = (ImageView) itemView.findViewById(R.id.iv_goods);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_now_price = (TextView) itemView.findViewById(R.id.tv_now_price);
            tv_market_price = (TextView) itemView.findViewById(R.id.tv_market_price);
            tv_no_goods = (TextView) itemView.findViewById(R.id.tv_no_goods);
//            labelview = (LabelView) itemView.findViewById(R.id.labelview);
//            iv_add_cart = (ImageView) itemView.findViewById(R.id.iv_add_cart);
        }
    }
}
