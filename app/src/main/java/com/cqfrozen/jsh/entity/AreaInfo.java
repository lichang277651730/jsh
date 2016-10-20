package com.cqfrozen.jsh.entity;

/**
 * Created by Administrator on 2016/10/20.
 */
public class AreaInfo {
    private String name;
    private String area_id;


    public AreaInfo(String name, String area_id) {
        this.name = name;
        this.area_id = area_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
