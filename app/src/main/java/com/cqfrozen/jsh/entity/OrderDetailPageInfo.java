package com.cqfrozen.jsh.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class OrderDetailPageInfo extends BaseEntity {

    public String use_hb_count;//使用粮票数量
    public String o_num;//订单号
    public String pay_time;//付款时间
    public String msg_content;//用户留言
    public String is_pj;//是否评价
    public String o_id;//订单id
    public String add_time;//订单添加时间
    public String order_status;//暂不使用
    public String goods_status;//暂不使用
    public String pay_mode;//暂不使用
    public String pay_mode_str;//支付方式名称
    public String count;//商品总数量
    public String weight_amount;//运费
    public String g_amount;//商品总金额
    public String order_amount;//订单总金额
    public String china_name;//收货人
    public String mobile_num;//电话
    public String address;//收货地址
    public String status_name;//订单状态名称
    public int btn_type;
    // 0所有按钮都不显示，
    // 1取消、去支付(未付款),
    // 2取消（货到付款未出库），
    // 3确认收货（已发货）,
    // 4去评价(已收货、未评价)，
    // 5删除（取消订单、已完成评价订单）

    public List<OrderDetailPageBean> oinfo;

    public class OrderDetailPageBean extends BaseEntity{
        public String g_id;//商品id
        public String pic_url;//商品图片
        public String g_name;//商品名称
        public String count;//数量
        public String unit;//单位
        public String brand_name;//品牌名
        public String weight;//重量
        public String now_price;//商品单价
        public String amount;//商品金额
    }

}
