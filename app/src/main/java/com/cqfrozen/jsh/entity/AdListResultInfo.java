package com.cqfrozen.jsh.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */
public class AdListResultInfo extends BaseEntity {

    public List<AdListBeanInfo> data1;
    public int is_page;//是否有下一页0没有 1有

    public class AdListBeanInfo{
        public String title;
        public String ad_id;
        public int ad_status;
        public String start_time;
        public String pic_url;
        public String introduction;
        public int content_type;
        public String content;
    }
}
