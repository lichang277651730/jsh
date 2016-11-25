package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsDetailActivity;
import com.cqfrozen.jsh.cart.CartManager;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.util.CustomToast;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/10/8.
 */
public class NormalBuyAdapter extends RecyclerView.Adapter<NormalBuyAdapter.MyViewHolder> {

    private Context context;
    private List<GoodsInfo> goodsInfos;
    private final DisplayImageOptions defaultOptions;
    private final HttpForVolley http;
    private CartManager cartManager;
    public NormalBuyAdapter(Context context, List<GoodsInfo> goodsInfos){
        this.context = context;
        this.goodsInfos = goodsInfos;
        this.http = new HttpForVolley(context);
        this.cartManager =  CartManager.getInstance(context);
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.img_loading_empty)
                .showImageOnFail(R.mipmap.img_loading_failed)
                .build();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_normalbuy1, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final GoodsInfo goodsInfo = goodsInfos.get(position);
        ImageLoader.getInstance().displayImage(goodsInfo.pic_url, holder.iv_goods, defaultOptions);
        holder.tv_name.setText(goodsInfo.g_name);
        holder.tv_brand.setText("品牌:" + goodsInfo.brand_name);
        holder.tv_size.setText("规格:" + goodsInfo.weight + "kg/" + goodsInfo.unit);
        holder.tv_price.setText("￥" + goodsInfo.now_price);

        if(goodsInfo.is_oos == 0){//不缺货
            holder.ll_no_goods.setVisibility(View.GONE);
        }else if(goodsInfo.is_oos == 1){//缺货
            holder.ll_no_goods.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("goodsInfo", goodsInfo);
                context.startActivity(intent);
            }
        });

        holder.iv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goodsInfo.is_oos == 1){
                    return;
                }
                holder.iv_cart.setEnabled(false);
//                holder.iv_cart.setImageResource(R.mipmap.icon_cart_no_pre);
                MyHttp.addcart(http, null, goodsInfo.g_id, 1, new HttpForVolley.HttpTodo() {
                    @Override
                    public void httpTodo(Integer which, JSONObject response) {
                        holder.iv_cart.setEnabled(true);
//                        holder.iv_cart.setImageResource(R.mipmap.icon_cart);
//                        ToastUtil.showToast(context, response.optString("msg"));
                        int code = response.optInt("code");
                        if(code != 0){
                            return;
                        }
                        CustomToast.getInstance(context).showToast(response.optString("msg"), R.mipmap.icon_add_cart_success);
                        cartManager.add(goodsInfo);
                    }
                });
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
        private TextView tv_brand;
        private TextView tv_size;
        private TextView tv_price;
        private ImageView iv_cart;
        private LinearLayout ll_no_goods;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_goods = (ImageView) itemView.findViewById(R.id.iv_goods);
            ll_no_goods = (LinearLayout) itemView.findViewById(R.id.ll_no_goods);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_brand = (TextView) itemView.findViewById(R.id.tv_brand);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            iv_cart = (ImageView) itemView.findViewById(R.id.iv_cart);
        }
    }
}
