package com.cqfrozen.jsh.entity;

/**
 * Created by Administrator on 2016/11/4.
 */
public class UpdateInfo extends BaseEntity{

    public int version_num; //版本号
    public String http_url; //下载连接
    public int is_qsj; //是否强制升级 1强制升级 0不是强制
    public String remark; //升级信息
    public String version_name; //版本名称

}
