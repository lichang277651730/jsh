package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.common.http.HttpForVolley;
import com.common.widget.ClearEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.util.ValidatorUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/9/20.
 * intent.putExtra("s_id", s_id);
 */
public class AddressAddActivity extends MyActivity implements View.OnClickListener {

    private ClearEditText et_consignee;
    private ClearEditText et_phone;
    private EditText et_location;
    private ClearEditText et_address;
    private TextView tv_right;
    private TextView tv_location;
    private CheckBox cb_default;
    private String consigneeStr;
    private String phoneStr;
    private String addresStr;
    private String locationStr;
    private int is_default = 0;
    private String s_id;
    private String adCodeStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressadd);
        getIntentData();
        initView();
    }

    private void getIntentData() {
        s_id = getIntent().getStringExtra("s_id");
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
        et_location.setOnClickListener(this);

        //初始化定位
        initLocation();

    }

    private void initLocation() {
        et_location.setText("");
        MyApplication.locationClient.startLocation();
        AMapLocation mapLocation = MyApplication.locationClient.getLastKnownLocation();
        String locationStr = mapLocation.getDistrict();
        adCodeStr = mapLocation.getAdCode();
        et_location.setText(locationStr);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_location:
                location();
                break;
            case R.id.et_location://跳转至定位列表
                //TODO 跳转至定位列表
                break;
            case R.id.tv_right:
                saveAddress();
                break;
            default:
                break;
        }
    }

    private void location() {
        initLocation();
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
        if(!ValidatorUtil.isMobile(phoneStr)){
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
        if(TextUtils.isEmpty(s_id)){
            showToast("门店信息有误");
            return;
        }
        if(cb_default.isChecked()){
            is_default = 1;
        }else {
            is_default = 0;
        }

        MyHttp.addAddress(http, null, consigneeStr, phoneStr, addresStr, is_default, s_id, adCodeStr, new HttpForVolley.HttpTodo() {

            @Override
            public void httpTodo(Integer which, JSONObject response) {
                showToast(response.optString("msg"));
                int code = response.optInt("code");
                if(code != 0){
                    return;
                }
                finish();
            }
        });
    }
}
