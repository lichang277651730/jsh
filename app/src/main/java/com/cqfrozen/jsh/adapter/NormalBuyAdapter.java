package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.cart.CartManager;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.util.CustomSimpleDialog;
import com.cqfrozen.jsh.util.CustomToast;
import com.cqfrozen.jsh.util.ImageLoader;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/10/8.
 */
public class NormalBuyAdapter extends RecyclerView.Adapter<NormalBuyAdapter.MyViewHolder> {

    private Context context;
    private List<GoodsInfo> goodsInfos;
    private final HttpForVolley http;
    private CartManager cartManager;
    private CustomSimpleDialog goodNotDialog;

    public NormalBuyAdapter(Context context, List<GoodsInfo> goodsInfos){
        this.context = context;
        this.goodsInfos = goodsInfos;
        this.http = new HttpForVolley(context);
        this.cartManager =  CartManager.getInstance(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_normalbuy, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GoodsInfo goodsInfo = goodsInfos.get(position);
        ImageLoader.load(context, goodsInfo.pic_url, holder.iv_goods);
        holder.tv_name.setText(goodsInfo.g_name);
        holder.tv_brand.setText("品牌:" + goodsInfo.brand_name);
        holder.tv_size.setText("规格:" + goodsInfo.weight + "kg/" + goodsInfo.unit);
        holder.tv_price.setText("￥" + goodsInfo.now_price);

        if(goodsInfo.is_oos == 0){//不缺货
            holder.ll_no_goods.setVisibility(View.GONE);
            holder.iv_cart.setImageResource(R.mipmap.icon_cart);
        }else if(goodsInfo.is_oos == 1){//缺货
            holder.ll_no_goods.setVisibility(View.VISIBLE);
            holder.iv_cart.setImageResource(R.mipmap.icon_cart_grey);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(holder, position);
                }
            }
        });

        holder.iv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goodsInfo.is_oos == 1){
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
                holder.iv_cart.setEnabled(false);
                MyHttp.addcart(http, null, goodsInfo.g_id, 1, new HttpForVolley.HttpTodo() {
                    @Override
                    public void httpTodo(Integer which, JSONObject response) {
                        holder.iv_cart.setEnabled(true);
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

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(MyViewHolder holder, int position);
    }
}
