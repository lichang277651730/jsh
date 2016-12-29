package com.cqfrozen.jsh.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */
public class HomeBannerAdResultInfo extends BaseEntity {

    public List<HomeBannerAdInfo> data1;

    public class HomeBannerAdInfo extends BaseEntity{
        //        "title": "冻博汇APP上线啦",（广告名称）
//                "ad_id": "19",（广告id）
//                "pic_url": "http://img.cqfrozen.com/upload/20161129/583d220d2ac6c.png",
//                "content_type": "2",（内容类型（1图文，2外链接，3不看详情，4粮票（图文），5粮票（外链），6购买[返回商品id]））
//                "content": "http://a.app.qq.com/o/simple.jsp?pkgname=com.cqfrozen.jsh"（内容，1图文，2外链接，3不看详情，4粮票（图文），5粮票（外链），6购买[返回商品id]））

        public String title;
        public String ad_id;
        public String pic_url;
        public int content_type;
        public String content;
    }

}
