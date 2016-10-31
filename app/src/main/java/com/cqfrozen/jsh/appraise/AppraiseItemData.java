package com.cqfrozen.jsh.appraise;

import com.cqfrozen.jsh.entity.BaseEntity;

/**
 * Created by Administrator on 2016/10/31.
 */
public class AppraiseItemData extends BaseEntity {

    public String order_info_id;
    public String goods_id;
    public int star_count;
    public String content;

    public AppraiseItemData() {
    }

    public AppraiseItemData(String order_info_id, String goods_id, int star_count, String
            content) {
        this.order_info_id = order_info_id;
        this.goods_id = goods_id;
        this.star_count = star_count;
        this.content = content;
    }
}
