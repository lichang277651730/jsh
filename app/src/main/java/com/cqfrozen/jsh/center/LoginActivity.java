package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.SigninInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

/**
 * Created by Administrator on 2016/9/12.
 */
public class LoginActivity extends MyActivity implements View.OnClickListener {

    private MyEditText et_phone;
    private MyEditText et_password;
    private Button btn_login;
    private String phoneStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparencyBar(true);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_phone = (MyEditText) findViewById(R.id.et_phone);
        et_password = (MyEditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                goLogin();
                break;
            default:
                break;
        }
    }

    private void goLogin() {
        phoneStr = et_phone.getText().toString().trim();
        String pwdStr = et_password.getText().toString().trim();
        if(TextUtils.isEmpty(phoneStr) || phoneStr.length() != 11){
            showToast("请输入正确手机号");
            return;
        }
        if(TextUtils.isEmpty(pwdStr) || pwdStr.length() < 6){
            showToast("请输入登陆密码");
            return;
        }
        MyHttp.userLogin(http, null, phoneStr, pwdStr, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    SPUtils.setToken("");
                    showToast(msg);
                    return;
                }
                showToast("登陆成功");
                SigninInfo signinInfo = (SigninInfo) bean;
                MyApplication.signinInfo = signinInfo;
                MyApplication.token = signinInfo.getToken();
                et_phone.setEnabled(false);
                getLoginUserInfo(signinInfo.getUser_id());
            }
        });
    }

    /**
     * 通过user_id去查找用户信息
     */
    private void getLoginUserInfo(String user_id) {
        finish();
    }

}
