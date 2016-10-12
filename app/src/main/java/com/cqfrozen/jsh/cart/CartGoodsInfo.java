package com.cqfrozen.jsh.cart;

import com.cqfrozen.jsh.entity.BaseEntity;

/**
 * Created by Administrator on 2016/9/22.
 */
public class CartGoodsInfo extends BaseEntity {
    public Long g_id;//商品id
    public String c_id;//购物车id
    public String g_name;//商品名字
    public double market_price;//市场价
    public double now_price;//现价
    public String pic_url;
    public int is_edit;//商品状态：0未上架 1已上架 2已下架
    public int is_oos;//是否缺货：0否 1是
    public String sell_count;
    public String brand_name;//品牌名称
    public String weight;//规格： 10/kg

//    "is_edit":"0",(商品是否编辑，0否，1是)
//    "weight":"10",(重量)
//    "unit":"1件",（单位）

    public int count = 1;
    public boolean isChecked = true;
}
