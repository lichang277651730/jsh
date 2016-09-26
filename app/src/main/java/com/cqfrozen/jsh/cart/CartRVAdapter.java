package com.cqfrozen.jsh.cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.util.ToastUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.cqfrozen.jsh.widget.NumberAddSubView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 *
 */
public class CartRVAdapter extends RecyclerView.Adapter<CartRVAdapter.MyViewHolder> {

    private List<CartGoodsInfo> cartGoodsInfos;
    private Context context;
    private TextView tv_total;
    private CheckBox cb_all;
    private final DisplayImageOptions defaultOptions;
    private final CartManager cartManager;
    private final HttpForVolley http;

    public CartRVAdapter(Context context, List<CartGoodsInfo> cartGoodsInfos, TextView tv_total,
                         final CheckBox cb_all){
        this.context = context;
        this.cartGoodsInfos = cartGoodsInfos;
        this.tv_total = tv_total;
        this.cb_all = cb_all;
        this.http = new HttpForVolley(context);
        cartManager = CartManager.getInstance(context);
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.solid_goods)
                .showImageOnFail(R.mipmap.solid_goods)
                .build();

        this.cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllNone(cb_all.isChecked());
            }
        });
        showTotalPrice();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart1, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CartGoodsInfo cartGoodsInfo = cartGoodsInfos.get(position);
        holder.checkbox.setChecked(cartGoodsInfo.isChecked);
        ImageLoader.getInstance().displayImage(cartGoodsInfo.pic_url, holder.iv_goods, defaultOptions);
        holder.tv_name.setText(cartGoodsInfo.g_name);
        holder.tv_price.setText("¥" + cartGoodsInfo.now_price);
        holder.add_sub_num.setCurValue(cartGoodsInfo.count);

        holder.tv_brand.setText("品牌: " + cartGoodsInfo.brand_name);
        holder.tv_size.setText("规格: " + cartGoodsInfo.weight + "/kg");

        holder.add_sub_num.setOnSubAddClickListener(new NumberAddSubView.OnSubAddClickListener() {
            @Override
            public void onSubAddClick(View view, final int curVal) {
                //TODO 改5为MyApplication.userInfo.area_id
                MyHttp.editCount(http, null, cartGoodsInfo.c_id, 5, curVal, new HttpForVolley.HttpTodo() {
                    @Override
                    public void httpTodo(Integer which, JSONObject response) {
                        ToastUtil.showToast(context, response.optString("msg"));
                        int code = response.optInt("code");
                        if(code != 0){
                            return;
                        }
                        holder.add_sub_num.setCurValue(curVal);
                        cartGoodsInfo.count = curVal;
                        cartManager.update(cartGoodsInfo);
                        showTotalPrice();
                    }
                });

//                holder.add_sub_num.setCurValue(curVal);
//                cartGoodsInfo.count = curVal;
//                cartManager.update(cartGoodsInfo);
//                showTotalPrice();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkbox.setChecked(!holder.checkbox.isChecked());
                cartGoodsInfo.isChecked = holder.checkbox.isChecked();
                cartManager.update(cartGoodsInfo);
                notifyItemChanged(position);
                allCheckedListen();
                showTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartGoodsInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private CheckBox checkbox;
        private ImageView iv_goods;
        private TextView tv_name;
        private TextView tv_price;
        private TextView tv_brand;
        private TextView tv_size;
        private NumberAddSubView add_sub_num;
        public MyViewHolder(View itemView) {
            super(itemView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            iv_goods = (ImageView)itemView.findViewById(R.id.iv_goods);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_brand = (TextView)itemView.findViewById(R.id.tv_brand);
            tv_size = (TextView)itemView.findViewById(R.id.tv_size);
            add_sub_num = (NumberAddSubView)itemView.findViewById(R.id.add_sub_num);
        }
    }

    /**
     * 得到购物车商品总价
     */
    public float getTotalPrice(){
        float sum = 0;
        if(isNull()){
            return  sum;
        }
        for (CartGoodsInfo goodsInfo : cartGoodsInfos){
            if(goodsInfo.isChecked){
                sum += goodsInfo.now_price*goodsInfo.count;
            }
        }
        return  sum;
    }

    /**
     * 删除购物车中的商品
     */
    public void delete(){
        if(isNull()){
            return;
        }

        for(Iterator<CartGoodsInfo> iterator = cartGoodsInfos.iterator();iterator.hasNext();){
            CartGoodsInfo goodsInfo = iterator.next();
            if(goodsInfo.isChecked){
                //TODO 请求网络删除对应数据库中购物车商品
                int positon = cartGoodsInfos.indexOf(goodsInfo);
                cartManager.delete(goodsInfo);
                iterator.remove();
                notifyItemRemoved(positon);
            }
        }
    }

    /**
     * 购物车，全选
     */
    public void chechAll(){
        if(isNull()){
            return;
        }
        for (CartGoodsInfo goodsInfo : cartGoodsInfos){
            goodsInfo.isChecked = true;
        }
        cb_all.setChecked(true);
        showTotalPrice();
        notifyDataSetChanged();
    }

    /**
     * 对是否全选的监听
     */
    public void allCheckedListen(){
        int count = 0;
        int checkNum = 0;
        if(!isNull()){
            count = cartGoodsInfos.size();
            for (CartGoodsInfo goodsInfo : cartGoodsInfos){
                if(!goodsInfo.isChecked){
                    cb_all.setChecked(false);
                    break;
                }else{
                    checkNum++;
                }
            }

            if(checkNum == count){
                cb_all.setChecked(true);
            }
        }
    }

    /**
     * 全不选
     */
    public void checkNone(){
        if(isNull()){
            return;
        }
        for (CartGoodsInfo goodsInfo : cartGoodsInfos){
            goodsInfo.isChecked = false;
        }
        cb_all.setChecked(false);
        showTotalPrice();
        notifyDataSetChanged();
    }

    /**
     * 设置tv_total控件的总价
     */
    public void showTotalPrice(){
        float totalPrice = getTotalPrice();
        tv_total.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + totalPrice + "</span>"), TextView.BufferType.SPANNABLE);
    }

    public void checkAllNone(boolean isChecked) {
        if(isNull()){
            return;
        }

        int i = 0;
        for (CartGoodsInfo goodInfo : cartGoodsInfos){
            goodInfo.isChecked = isChecked;
            cartManager.update(goodInfo);
            notifyItemChanged(i);
            i++;
        }
        cb_all.setChecked(isChecked);
        showTotalPrice();
    }

    /**
     * 判断购物车是否为空
     */
    private boolean isNull(){
        if(cartGoodsInfos == null || cartGoodsInfos.size() == 0){
            return true;
        }
        return false;
    }
}