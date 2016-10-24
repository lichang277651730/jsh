package com.cqfrozen.jsh.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class OrderInfo extends BaseEntity {

    public String order_amount;//（未使用粮票 订单金额）
    public String weight_amount;//（未使用粮票 运费）
    public String goods_amount;//（未使用粮票 商品金额）
    public String use_hb_count;//（可使用粮票）
    public String order_amount_hb;//（使用粮票 订单金额）
    public String weight_amount_hb;//（使用粮票 运费金额）
    public String goods_amount_hb;//（使用粮票 商品金额）
    public List<OrderGoodsBean> goods;
    public List<OrderAddressBean> address;

    public class OrderGoodsBean extends BaseEntity{
        public String goods_amount;
        public String g_id;
        public String now_price;
        public String pic_url;
        public String g_name;
        public String brand_name;
        public String unit;
        public String weight;
        public String market_price;
        public String count;

    }

    public class OrderAddressBean extends BaseEntity{
        public String st_id;
        public String s_id;
        public String store_name;
        public int is_default;
        public String china_name;
        public String mobile_num;
        public String address;
        public String p_area_name;
        public String c_area_name;
        public String d_area_name;
    }



}
