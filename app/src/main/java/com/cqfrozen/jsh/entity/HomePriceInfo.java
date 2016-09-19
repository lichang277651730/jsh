package com.cqfrozen.jsh.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HomePriceInfo extends BaseEntity {
    public String id;
    public int imgId;
    public String name;
    public List<GoodsInfo> priceGoods;

    public HomePriceInfo(String id, int imgId, String name, List<GoodsInfo> priceGoods) {
        this.id = id;
        this.imgId = imgId;
        this.name = name;
        this.priceGoods = priceGoods;
    }
}
