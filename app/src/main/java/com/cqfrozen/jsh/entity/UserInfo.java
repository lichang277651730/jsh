package com.cqfrozen.jsh.entity;

/**
 * Created by Administrator on 2016/9/12.
 */
public class UserInfo extends BaseEntity {

    public String area_name;// "沙坪坝区"
    public String street_name;//"渝碚路街道"
    public String mobile_num;// 15736106292
    public String nick_name;// 15736106292
    public int sex;// 性别（1男，2女，0保密）
    public String head_url;//头像
    public String store_name;//店名
    public String area_id;//区域id
    public String st_id;//街道id
    public int verify_status;//0未审核，1已通过，2未通过
    public String verify_name;//审核名称
    public float hb_count;//总汇币数量
    public int one_fans_count;//一级兄弟伙数来那个
    public int two_fans_count;//二级兄弟伙数量
    public int inotal_fans_count;//总兄弟伙数量
    public int df_count;//订单代付款数量
    public int ds_count;//订单代收货数量
    public int dp_count;//订单代评价数量

    @Override
    public String toString() {
        return "UserInfo{" +
                "area_name='" + mobile_num + '\'' +
                '}';
    }
}
