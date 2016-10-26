package com.cqfrozen.jsh.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class CommentResultInfo {
    public int is_page;
    public List<CommentInfo> data1;

    public class CommentInfo{
        public String p_id;
        public String pj_content;
        public String order_info_id;
        public float star_count;
        public String add_time;
        public String unit;
        public String weight;
        public String is_anonymou;
        public String nick_name;
        public String head_url;

    }

}
