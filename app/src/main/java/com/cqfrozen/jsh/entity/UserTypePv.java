package com.cqfrozen.jsh.entity;

/**
 * Created by Administrator on 2016/11/3.
 */
public class UserTypePv {

    private String u_t_id;
    private String user_type_name;

    public UserTypePv(String u_t_id, String user_type_name) {
        this.u_t_id = u_t_id;
        this.user_type_name = user_type_name;
    }

    public String getU_t_id() {
        return u_t_id;
    }

    public void setU_t_id(String u_t_id) {
        this.u_t_id = u_t_id;
    }

    public String getUser_type_name() {
        return user_type_name;
    }

    public void setUser_type_name(String user_type_name) {
        this.user_type_name = user_type_name;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return this.user_type_name;
    }

    @Override
    public String toString() {
        return this.user_type_name;
    }
}
