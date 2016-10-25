package com.cqfrozen.jsh.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class OrderResultInfo extends BaseEntity {
    public List<OrderSearchInfo> data1;
    public int is_page;//是否有下一页0没有 1有

    public class OrderSearchInfo extends BaseEntity{
        public String store_name;//"锦晟汇 刘生印",
        public String o_id;//订单id
        public int is_pj;//是否评价，赞不使用
        public String o_num;//订单id
        public String add_time;//添加订单时间
        public String order_status;//订单状态
        public String goods_status;//商品状态
        public String pay_mode;//付款状态
        public String count;//商品数量
        public String g_amount;//商品总价
        public String use_hb_count;//使用汇币数量
        public String order_amount;//订单总价
        public String weight_amount;//运费
        public String status_name;
        public String is_fk_btn;
        public String is_qr_btn;
        public String is_pj_btn;
        public String is_del_btn;
        public List<OrderGoodsInfo> orderinfo;
    }

    public class OrderGoodsInfo extends BaseEntity{
        public String g_id;//"687"
        public String o_id;//944
        public String pic_url;//"http:\/\/img.cqfrozen.com\/upload\/20160815\/57b16a4f1c3d7.jpg"
        public String goods_name;//"君临猪长尾（猪尾）"
        public String brand_name;//君临
        public String goods_count;//1
        public String unit;//1件
        public String weight;//10
        public String now_price;//660
        public String market_price;//680
        public String g_amount;//660
    }

}
