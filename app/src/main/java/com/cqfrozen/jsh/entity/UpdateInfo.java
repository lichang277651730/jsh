package com.cqfrozen.jsh.entity;

/**
 * Created by Administrator on 2016/11/4.
 */
public class UpdateInfo extends BaseEntity{

    public int version_num; //版本号
    public String http_url; //下载连接
    public int forcibly; //是否强制升级
    public String msg; //升级信息
    public String version_name; //版本名称

}
