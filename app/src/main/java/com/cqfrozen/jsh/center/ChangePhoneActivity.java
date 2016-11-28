package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.util.ValidatorUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/7.
 */
public class ChangePhoneActivity extends MyActivity implements View.OnClickListener {

    private MyEditText et_new_phone;
    private MyEditText et_verify_code;
    private TextView tv_get_verify;
    private Button btn_change_phone;
    private String newPhoneStr;
    private String verifyCodeStr;
    private TextView tv_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changephone);
        initView();
    }

    private void initView() {
        setMyTitle("更换手机号码");
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        et_new_phone = (MyEditText) findViewById(R.id.et_new_phone);
        et_verify_code = (MyEditText) findViewById(R.id.et_verify_code);
        tv_get_verify = (TextView) findViewById(R.id.tv_get_verify);
        btn_change_phone = (Button) findViewById(R.id.btn_change_phone);
        btn_change_phone.setOnClickListener(this);
        tv_get_verify.setOnClickListener(this);

        if(getUserInfo() != null){
            tv_phone.setText(getUserInfo().c_phone_num);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_phone:
                changePhone();
                break;
            case R.id.tv_get_verify:
                getVerifyCode();
                break;
            default:
                break;
        }
    }

    private void getVerifyCode() {
        String newPhoneStr = et_new_phone.getText().toString().trim();

        if(!ValidatorUtil.isMobile(newPhoneStr)){
            showToast("请输入手机号");
            return;
        }
        MyApplication.downTimer.going();
        MyApplication.downTimer.setTextView(tv_get_verify);
        MyHttp.sendCode(http, null, 1, newPhoneStr, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                int code = response.optInt("code");
                showToast(response.optString("msg"));
                if(code != 0){
                    MyApplication.downTimer.setInit();
                }
            }
        });
    }

    private void changePhone() {
        newPhoneStr = et_new_phone.getText().toString().trim();
        verifyCodeStr = et_verify_code.getText().toString().trim();

        if(!ValidatorUtil.isMobile(newPhoneStr)){
            showToast("手机号码不正确");
            return;
        }

        if(TextUtils.isEmpty(verifyCodeStr) || verifyCodeStr.length() != 4){
            showToast("验证码不正确");
            return;
        }

        MyHttp.updateMobile(http, null, newPhoneStr, verifyCodeStr, new HttpForVolley.HttpTodo() {
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
