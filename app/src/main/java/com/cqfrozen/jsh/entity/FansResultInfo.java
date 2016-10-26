package com.cqfrozen.jsh.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class FansResultInfo extends BaseEntity {

    public int is_page;
    public List<FansInfo> data1;

    public class FansInfo{
        public String hb_count;
        public String user_id;
        public String store_name;
        public String r_id;
    }

}
