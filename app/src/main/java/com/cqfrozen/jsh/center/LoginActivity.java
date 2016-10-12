package com.cqfrozen.jsh.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    private static final int TAG_PWD_SHOW = 1;
    private static final int TAG_PWD_HIDDEN = 2;

    private MyEditText et_phone;
    private MyEditText et_password;
    private Button btn_login;
    private String phoneStr;
    private TextView tv_regist;
    private ImageView iv_see_pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparencyBar(true);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        setMyTitle("");
        et_phone = (MyEditText) findViewById(R.id.et_phone);
        et_password = (MyEditText) findViewById(R.id.et_password);
        tv_regist = (TextView) findViewById(R.id.tv_regist);
        btn_login = (Button) findViewById(R.id.btn_login);
        iv_see_pwd = (ImageView) findViewById(R.id.iv_see_pwd);
        btn_login.setOnClickListener(this);
        tv_regist.setOnClickListener(this);
        initShowHiddenPwdView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                goLogin();
                break;
            case R.id.tv_regist:
                startActivity(new Intent(this, Register1Activity.class));
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

    /**
     * 显示和隐藏第一次输入的密码
     */
    private void initShowHiddenPwdView() {

        iv_see_pwd.setTag(TAG_PWD_HIDDEN);
        iv_see_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPwdTad = (int) v.getTag();
                if(curPwdTad == TAG_PWD_HIDDEN){//当前是隐藏密码，点击显示密码
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_see_pwd.setTag(TAG_PWD_SHOW);
                }else if(curPwdTad == TAG_PWD_SHOW){//当前是显示密码，点击隐藏密码
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_see_pwd.setTag(TAG_PWD_HIDDEN);
                }
                setSlection(et_password);
            }
        });
    }

    private void setSlection(MyEditText etView) {
        if(etView.getText() != null && !etView.getText().equals("")){
            etView.setSelection(etView.getText().length());
        }
    }
}
