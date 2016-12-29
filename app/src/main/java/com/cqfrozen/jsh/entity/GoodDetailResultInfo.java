package com.cqfrozen.jsh.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class GoodDetailResultInfo extends BaseEntity {
    public GoodDetailInfo data1;
    public List<PicsInfo> data2;

    public class GoodDetailInfo{
        public int is_common;//是否添加为常用采购0否，1是，如果没有登陆返回为0
        public String weight;//重量
        public String unit;// "1件",（单位）
        public double now_price;//现价
        public double market_price;//市场价
        public String g_num;//商品编号
        public Long g_id;//商品id
        public String g_name;//"盛合鸡胗",（商品名称）
        public String g_introduction;//盛合鸡胗1kg*10包
        public String canshu_describe;//备注/说明
        public String brand_name;//"盛合",（品牌）
        public String shelf_life;//"12个月",（保质期）
        public String c_mode;//存储方式
        public String pj_count;//评价数
        public int is_oos;//是否缺货0否，1是
    }

    public class PicsInfo implements Serializable{
        public String pic_url;
    }
}
