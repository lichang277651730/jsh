package com.cqfrozen.jsh.entity;

/**
 * Created by Administrator on 2016/10/20.
 */
public class StreetInfo {
    private String street_name;
    private String st_id;

    public StreetInfo(String street_name, String st_id) {
        this.street_name = street_name;
        this.st_id = st_id;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getSt_id() {
        return st_id;
    }

    public void setSt_id(String st_id) {
        this.st_id = st_id;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return this.street_name;
    }

    @Override
    public String toString() {
        return this.street_name;
    }
}
