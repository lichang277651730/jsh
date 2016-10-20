package com.cqfrozen.jsh.entity;

/**
 * Created by Administrator on 2016/10/20.
 */
public class ShopPVInfo {
    private String store_name;
    private String s_id;

    public ShopPVInfo(String store_name, String s_id) {
        this.store_name = store_name;
        this.s_id = s_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return this.store_name;
    }

    @Override
    public String toString() {
        return this.store_name;
    }
}
