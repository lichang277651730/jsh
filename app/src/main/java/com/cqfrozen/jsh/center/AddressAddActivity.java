package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.ValidatorUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/9/20.
 * intent.putExtra("s_id", s_id);
 */
public class AddressAddActivity extends MyActivity implements View.OnClickListener {

    private MyEditText et_consignee;
    private MyEditText et_phone;
    private EditText et_location;
    private MyEditText et_address;
    private TextView tv_right;
    private CheckBox cb_default;
    private String consigneeStr;
    private String phoneStr;
    private String addresStr;
    private String locationStr;
    private int is_default = 0;
    private String s_id;
    private String adCodeStr;
    private Button btn_save;

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
        setMyTitle("新增地址");
        et_consignee = (MyEditText) findViewById(R.id.et_consignee);
        et_phone = (MyEditText) findViewById(R.id.et_phone);
        et_location = (EditText) findViewById(R.id.et_location);
        cb_default = (CheckBox) findViewById(R.id.cb_default);
        et_address = (MyEditText) findViewById(R.id.et_address);
        tv_right = (TextView) findViewById(R.id.tv_right);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_right.setOnClickListener(this);
        et_location.setOnClickListener(this);

    }

    private void initLocation() {
        et_location.setText("");
        et_location.setText(locationStr);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_location://跳转至定位列表
                //TODO 跳转至定位列表
                break;
            case R.id.btn_save:
                saveAddress();
                break;
            default:
                break;
        }
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
