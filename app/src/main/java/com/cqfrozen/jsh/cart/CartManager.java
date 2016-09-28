package com.cqfrozen.jsh.cart;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.cqfrozen.jsh.entity.GoodsInfo;
import com.cqfrozen.jsh.util.JSONUtil;
import com.cqfrozen.jsh.util.SPUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class CartManager {

    private static CartManager instance;
    private Context context;
    private SparseArray<CartGoodsInfo> cartGoods;
    private int goodsTotalNum = 0;
    private OnNumChangeListener listener;

    private CartManager(Context context) {
        this.context = context;
        cartGoods = new SparseArray<>();
//        initCart();
    }

    public static CartManager getInstance(Context context) {
        if (instance == null) {
            synchronized (CartManager.class) {
                if (instance == null) {
                    instance = new CartManager(context);
                }
            }
        }
        return instance;
    }


    /**
     * 初始化购物车
     */
    private void initCart() {
        List<CartGoodsInfo> list = loadAllFromSp();
        if (list != null && list.size() > 0) {
            for (CartGoodsInfo goodInfo : list) {
                cartGoods.put(goodInfo.g_id.intValue(), goodInfo);
            }
        }
    }

    /**
     * 清空购物车
     */
    public void clear() {
        cartGoods.clear();
        commit();
    }

    /**
     * 修改购物车商品
     */
    public void update(CartGoodsInfo cartGoodsInfo) {
        cartGoods.put(cartGoodsInfo.g_id.intValue(), cartGoodsInfo);
        commit();
    }

    /**
     * 删除购物车数据
     */
    public void delete(CartGoodsInfo cartGoodsInfo) {
        cartGoods.delete(cartGoodsInfo.g_id.intValue());
        commit();
    }

    /**
     * 添加单个商品购物车
     */
    public void add(CartGoodsInfo cartGoodsInfo) {
        Long goodsId = cartGoodsInfo.g_id;
        CartGoodsInfo temp = cartGoods.get(goodsId.intValue());
        if (temp != null) {//购物车已存在此商品，数量加1
            temp.count = temp.count + 1;
        } else {//第一次添加的商品
            temp = cartGoodsInfo;
            temp.count = 1;
        }
        cartGoods.put(goodsId.intValue(), temp);
        commit();
    }

    /**
     * 添加单个商品购物车
     */
    public void add(GoodsInfo goodsInfo) {
        CartGoodsInfo cartGoodsInfo = parseCartGoods(goodsInfo);
        add(cartGoodsInfo);
    }

    /**
     * 添加一个集合的商品到购物车
     */
    public void add(List<CartGoodsInfo> cartGoodsInfos) {
        for (CartGoodsInfo goodsInfo : cartGoodsInfos) {
            add(goodsInfo);
        }
    }


    private CartGoodsInfo parseCartGoods(GoodsInfo goodsInfo) {
        CartGoodsInfo cartGoodsInfo = new CartGoodsInfo();
        cartGoodsInfo.g_id = goodsInfo.g_id;
        cartGoodsInfo.g_name = goodsInfo.g_name;
        cartGoodsInfo.market_price = goodsInfo.market_price;
        cartGoodsInfo.now_price = goodsInfo.now_price;
        cartGoodsInfo.pic_url = goodsInfo.pic_url;
        cartGoodsInfo.is_oos = goodsInfo.is_oos;
        cartGoodsInfo.sell_count = goodsInfo.sell_count;
        return cartGoodsInfo;
    }

    /**
     * 购物车有变动，就需commit
     */
    private void commit() {
        List<CartGoodsInfo> list = new ArrayList<>();
        for (int i = 0; i < cartGoods.size(); i++) {
            list.add(cartGoods.valueAt(i));
        }
//        SPUtils.setCartData(JSONUtil.toJson(list));
        goodsTotalNum = 0;
//        for(int i = 0; i < cartGoods.size(); i++){
//            goodsTotalNum += cartGoods.valueAt(i).count;
//        }
        for (CartGoodsInfo goodsInfo : list) {
            goodsTotalNum += goodsInfo.count;
        }
        if (listener != null) {
            listener.onNumChangeListener(goodsTotalNum);
        }
    }


    /**
     * 从SP中读取购物车数据
     */
    public List<CartGoodsInfo> loadAllFromSp() {
        String cart_json = SPUtils.getCartData();
        List<CartGoodsInfo> cartGoodsList = null;
        if (!TextUtils.isEmpty(cart_json)) {
            cartGoodsList = JSONUtil.fromJson(cart_json, new TypeToken<List<CartGoodsInfo>>() {
            }.getType());
        }
        return cartGoodsList;
    }

    /**
     * 获取购物车数量
     */
    public int getCartGoodsNum() {
        if (isNull()) {
            return 0;
        }
//        return cartGoods.size();
        return this.goodsTotalNum;
    }

    /**
     * 判断购物车商品是否为空
     */
    public boolean isNull() {
        if (cartGoods == null || cartGoods.size() == 0) {
            return true;
        }
        return false;
    }

    public interface OnNumChangeListener {
        void onNumChangeListener(int curNum);
    }

    public void setOnNumChangeListener(OnNumChangeListener listener) {
        this.listener = listener;
    }
}
