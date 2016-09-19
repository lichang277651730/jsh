package com.cqfrozen.jsh.entity;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HomeTableInfo extends BaseEntity {
    public int table_img;
    public String talbe_name;
    public GoodsInfo first_goods;
    public GoodsInfo second_goods;
    public GoodsInfo third_goods;

    public HomeTableInfo(int table_img, String talbe_name, GoodsInfo first_goods, GoodsInfo
            second_goods, GoodsInfo third_goods) {
        this.table_img = table_img;
        this.talbe_name = talbe_name;
        this.first_goods = first_goods;
        this.second_goods = second_goods;
        this.third_goods = third_goods;
    }
}
