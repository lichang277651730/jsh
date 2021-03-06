package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
import com.cqfrozen.jsh.util.CustomMiddleToast;
import com.cqfrozen.jsh.util.CustomSimpleDialog;
import com.cqfrozen.jsh.util.CustomToast;
import com.cqfrozen.jsh.util.ImageLoader;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.MyViewHolder> {

    private Context context;
    private List<GoodsInfo> goodsInfos;
    private CartManager cartManager;
    private final HttpForVolley http;
    private boolean isAddCartCanClick = true;
    private CustomSimpleDialog goodNotDialog;

    public GoodsAdapter(Context context, List<GoodsInfo> goodsInfos) {
        this.context = context;
        this.goodsInfos = goodsInfos;
        this.http = new HttpForVolley(context);
        this.cartManager = CartManager.getInstance(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_goods, null));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GoodsInfo goodsInfo = goodsInfos.get(position);
        ImageLoader.load(context, goodsInfo.pic_url, holder.iv_goods);
        holder.tv_name.setText(goodsInfo.g_name);
        holder.tv_now_price.setText("¥" + goodsInfo.now_price);
        holder.tv_size.setText(goodsInfo.weight + "kg/" + goodsInfo.unit);
        holder.tv_market_price.setText("¥" + goodsInfo.market_price);
        holder.tv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (goodsInfo.is_oos == 0) {//不缺货
            holder.ll_no_goods.setVisibility(View.GONE);
            holder.iv_add_cart.setImageResource(R.mipmap.icon_cart);
        } else if (goodsInfo.is_oos == 1) {//缺货
            holder.ll_no_goods.setVisibility(View.VISIBLE);
            holder.iv_add_cart.setImageResource(R.mipmap.icon_cart_grey);
        }

        //点击购物车
        holder.iv_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddCartCanClick) {
                    isAddCartCanClick = false;
                    if (goodsInfo.is_oos == 1) {
                        isAddCartCanClick = true;
                        goodNotDialog = new CustomSimpleDialog.Builder(context)
                                .setMessage("该产品暂时缺货")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        dialog.cancel();
                                    }
                                })
                                .create();
                        goodNotDialog.show();
                        return;
                    }
                    holder.iv_add_cart.setEnabled(false);
                    MyHttp.addcart(http, null, goodsInfo.g_id, 1, new HttpForVolley.HttpTodo() {
                        @Override
                        public void httpTodo(Integer which, JSONObject response) {
                            holder.iv_add_cart.setEnabled(true);
                            int code = response.optInt("code");
                            if (code != 0) {
                                isAddCartCanClick = true;
                                CustomMiddleToast.getInstance(context).showToast(response.optString("msg"));
                                return;
                            }
                            CustomToast.getInstance(context).showToast(response.optString("msg"),
                                    R.mipmap.icon_add_cart_success);
                            cartManager.add(goodsInfo);
                            isAddCartCanClick = true;
                        }
                    });
                }

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
//                intent.putExtra("goodsInfo", goodsInfo);
                intent.putExtra("g_id", goodsInfo.g_id);
//                Log.e("gidgidgid", goodsInfo.g_id + "");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodsInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_goods;
        private TextView tv_name;
        private TextView tv_now_price;
        private TextView tv_market_price;
        private TextView tv_no_goods;
        private TextView tv_size;
        private LinearLayout ll_no_goods;
        private ImageView iv_add_cart;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_goods = (ImageView) itemView.findViewById(R.id.iv_goods);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_now_price = (TextView) itemView.findViewById(R.id.tv_now_price);
            tv_market_price = (TextView) itemView.findViewById(R.id.tv_market_price);
            tv_no_goods = (TextView) itemView.findViewById(R.id.tv_no_goods);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            ll_no_goods = (LinearLayout) itemView.findViewById(R.id.ll_no_goods);
            iv_add_cart = (ImageView) itemView.findViewById(R.id.iv_add_cart);
        }
    }
}
