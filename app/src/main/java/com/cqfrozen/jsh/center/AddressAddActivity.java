package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.common.widget.ClearEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/9/20.
 */
public class AddressAddActivity extends MyActivity implements View.OnClickListener {

    private ClearEditText et_consignee;
    private ClearEditText et_phone;
    private EditText et_location;
    private ClearEditText et_address;
    private TextView tv_right;
    private TextView tv_location;
    private CheckBox cb_default;
    private int is_default = 0;
    private String consigneeStr;
    private String phoneStr;
    private String addresStr;
    private String locationStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressadd);
        initView();
    }

    private void initView() {
        setMyTitle("添加新地址", "保存");
        et_consignee = (ClearEditText) findViewById(R.id.et_consignee);
        et_phone = (ClearEditText) findViewById(R.id.et_phone);
        et_location = (EditText) findViewById(R.id.et_location);
        tv_location = (TextView) findViewById(R.id.tv_location);
        cb_default = (CheckBox) findViewById(R.id.cb_default);
        et_address = (ClearEditText) findViewById(R.id.et_address);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_location.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_location:
                location();
                break;
            case R.id.tv_right:
                saveAddress();
                break;
            default:
                break;
        }
    }

    private void location() {

    }

    private void saveAddress() {
        consigneeStr = et_consignee.getText().toString().trim();
        phoneStr = et_phone.getText().toString().trim();
        locationStr = et_location.getText().toString().trim();
        addresStr = et_address.getText().toString().trim();
        if(TextUtils.isEmpty(consigneeStr)){
            showToast("收件人不能为空");
            return;
        }
        if(TextUtils.isEmpty(phoneStr)){
            showToast("电话不能为空");
            return;
        }
        if(TextUtils.isEmpty(locationStr)){
            showToast("地区不能为空");
            return;
        }
        if(TextUtils.isEmpty(addresStr)){
            showToast("详细地址不能为空");
            return;
        }
        if(cb_default.isChecked()){
            is_default = 1;
        }else {
            is_default = 0;
        }
//        MyHttp.addrAddress(http, null, );

    }
}
