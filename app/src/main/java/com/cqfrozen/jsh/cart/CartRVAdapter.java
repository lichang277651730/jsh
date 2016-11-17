package com.cqfrozen.jsh.cart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsDetailActivity;
import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.cqfrozen.jsh.widget.NumberAddSubView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 *
 */
public class CartRVAdapter extends RecyclerView.Adapter<CartRVAdapter.MyViewHolder> {

    private static final int TYEP_ONE = 1;//删除单个
    private static final int TYEP_MORE = 3;//删除多个
    private static final int TYEP_ALL = 2;//删除全部

    private List<CartGoodsInfo> cartGoodsInfos;
    private Context context;
    private TextView tv_total;
    private CheckBox cb_all;
    private final DisplayImageOptions defaultOptions;
    private final CartManager cartManager;
    private final HttpForVolley http;
    private ArrayList<CartGoodsInfo> checkedList = new ArrayList<>();

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
                .showImageForEmptyUri(R.mipmap.img_loading_empty)
                .showImageOnFail(R.mipmap.img_loading_failed)
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
        holder.tv_size.setText("规格: " + cartGoodsInfo.weight + "kg/件");


        holder.add_sub_num.setOnSubAddClickListener(new NumberAddSubView.OnSubAddClickListener() {
            @Override
            public void onSubAddClick(View view, final int curVal) {
                holder.add_sub_num.setEnabled(false);

                MyHttp.editCount(http, null, cartGoodsInfo.c_id, curVal, new HttpForVolley.HttpTodo() {
                    @Override
                    public void httpTodo(Integer which, JSONObject response) {
                        holder.add_sub_num.setEnabled(true);
                        int code = response.optInt("code");
                        if(code != 0){
                            holder.add_sub_num.setCurValue(cartGoodsInfo.count);
                            return;
                        }
                        holder.add_sub_num.setCurValue(curVal);
                        cartGoodsInfo.count = curVal;
                        cartManager.update(cartGoodsInfo);
                        showTotalPrice();
                    }
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                GoodsInfo goodsInfo = cartManager.parseCartGoods(cartGoodsInfo);
                intent.putExtra("goodsInfo", goodsInfo);
                context.startActivity(intent);
            }
        });

        holder.ll_cart_cb.setOnClickListener(new View.OnClickListener() {
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
        private LinearLayout ll_cart_cb;
        public MyViewHolder(View itemView) {
            super(itemView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            iv_goods = (ImageView)itemView.findViewById(R.id.iv_goods);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_brand = (TextView)itemView.findViewById(R.id.tv_brand);
            tv_size = (TextView)itemView.findViewById(R.id.tv_size);
            add_sub_num = (NumberAddSubView)itemView.findViewById(R.id.add_sub_num);
            ll_cart_cb = (LinearLayout)itemView.findViewById(R.id.ll_cart_cb);
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
                sum += goodsInfo.now_price * goodsInfo.count;
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

        if(cartManager.isAllChecked()){//清空购物车
            MyHttp.deleteCart(http, null, TYEP_ALL, "", new HttpForVolley.HttpTodo() {
                    @Override
                    public void httpTodo(Integer which, JSONObject response) {
                        int code = response.optInt("code");
                        if(code != 0){
                            return;
                        }
                        cartGoodsInfos.clear();
                        cartManager.clear();
                        notifyDataSetChanged();
                        showTotalPrice();
                    }
                });
        }else {
            checkedList.clear();
            for(final Iterator<CartGoodsInfo> iterator = cartGoodsInfos.iterator(); iterator.hasNext();){
                final CartGoodsInfo goodsInfo = iterator.next();
                if(goodsInfo.isChecked){
                    checkedList.add(goodsInfo);
                }
            }
            if(checkedList.size() == 0){//一个都没选，就点删除按钮
                return;
            }

            if(checkedList.size() == 1){//删除一个
                final CartGoodsInfo checkedGoods = checkedList.get(0);
                MyHttp.deleteCart(http, null, TYEP_ONE, checkedList.get(0).c_id, new HttpForVolley.HttpTodo() {
                    @Override
                    public void httpTodo(Integer which, JSONObject response) {
                        int code = response.optInt("code");
                        if(code != 0){
                            return;
                        }
                        cartManager.delete(checkedGoods);
                        cartGoodsInfos.remove(checkedGoods);
                        notifyDataSetChanged();
                        showTotalPrice();
                    }
                });
            }

            if(checkedList.size() > 1){//删除多个
                String c_id_str = parseCheckedList(checkedList);
                MyHttp.deleteCart(http, null, TYEP_MORE, c_id_str, new HttpForVolley.HttpTodo() {
                    @Override
                    public void httpTodo(Integer which, JSONObject response) {
                        int code = response.optInt("code");
                        if(code != 0){
                            return;
                        }
                        cartManager.delete(checkedList);
                        cartGoodsInfos.removeAll(checkedList);
                        notifyDataSetChanged();
                        showTotalPrice();
                    }
                });
            }

        }
    }

    /**
     * 选择多个删除，将多个选中的购物车商品c_id构建成"1,2,3,4"
     */
    private String parseCheckedList(ArrayList<CartGoodsInfo> checkedList) {
        String c_id_str = "";
        for(int i = 0; i < checkedList.size(); i++){
            c_id_str = c_id_str + checkedList.get(i).c_id + ",";
        }
        if(c_id_str.endsWith(",")){
            c_id_str = c_id_str.substring(0, c_id_str.length() - 1);
        }
        return c_id_str;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return cartGoodsInfos.get(position).g_id;
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
        if(priceChangeListener != null){
            priceChangeListener.priceChange(totalPrice);
        }
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
    public boolean isNull(){
        if(cartGoodsInfos == null || cartGoodsInfos.size() == 0){
            return true;
        }
        return false;
    }

    private PriceChangeListener priceChangeListener;

    public void setPriceChangeListener(PriceChangeListener priceChangeListener) {
        this.priceChangeListener = priceChangeListener;
    }

    public interface PriceChangeListener{
        void priceChange(float price);
    }
}
