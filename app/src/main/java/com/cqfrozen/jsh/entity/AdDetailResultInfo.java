package com.cqfrozen.jsh.entity;

/**
 * Created by Administrator on 2016/12/28.
 */
public class AdDetailResultInfo extends BaseEntity {
    public int sy_count;
    public AdDetailInfo data1;

    public class AdDetailInfo{
        public int get_count;
        public String choose_keyword;
        public String china_keyword;
        public String py_keyword;
        public String content;
        public int content_type;
        public int ad_count;
        public String ad_id;
        public int is_stop;
        public String ad_num;
        public String title;
        public String introduction;
        public String pic_url;
        public float hb_count;
        public float use_hb_count;
        public float xf_hb_count;
        public String area_name;
        public int ad_status;

    }
}


