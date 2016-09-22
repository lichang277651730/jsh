package com.cqfrozen.jsh.cart;

import com.cqfrozen.jsh.entity.BaseEntity;

/**
 * Created by Administrator on 2016/9/22.
 */
public class CartGoodsInfo extends BaseEntity {
    public Long g_id;
    public String g_name;
    public double market_price;
    public double now_price;
    public String pic_url;
    public String is_oos;
    public String sell_count;

    public int count = 1;
    public boolean isChecked = true;
}
