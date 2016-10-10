package com.cqfrozen.jsh.entity;

/**
 * Created by Administrator on 2016/9/13.
 */
public class GoodsInfo extends BaseEntity{
    public Long g_id;
    public String g_name;
    public double market_price;
    public double now_price;
    public String pic_url;
    public int is_oos;
    public String sell_count;
    public String weight;//规格：10kg
//    public String unit;//单位：1件
    public String brand_name;//品牌：金锣
}
