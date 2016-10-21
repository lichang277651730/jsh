package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.util.ShowHiddenPwdUtil;
import com.cqfrozen.jsh.util.ValidatorUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/21.
 */
public class ForgetPwdActivity extends MyActivity implements View.OnClickListener {

    private MyEditText et_phone;
    private MyEditText et_verify_code;
    private TextView tv_get_verify;
    private MyEditText et_pwd_once;
    private ImageView iv_see_once_pwd;
    private MyEditText et_pwd_again;
    private ImageView iv_see_again_pwd;
    private Button btn_confirm;
    private String phoneStr;
    private String verifyCodeStr;
    private String pwdOnceStr;
    private String pwdAgainStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
        initView();
    }

    private void initView() {
        setMyTitle("忘记密码");
        et_phone = (MyEditText) findViewById(R.id.et_phone);
        et_verify_code = (MyEditText) findViewById(R.id.et_verify_code);
        tv_get_verify = (TextView) findViewById(R.id.tv_get_verify);
        et_pwd_once = (MyEditText) findViewById(R.id.et_pwd_once);
        iv_see_once_pwd = (ImageView) findViewById(R.id.iv_see_once_pwd);
        et_pwd_again = (MyEditText) findViewById(R.id.et_pwd_again);
        iv_see_again_pwd = (ImageView) findViewById(R.id.iv_see_again_pwd);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tv_get_verify.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        iv_see_again_pwd.setOnClickListener(this);
        ShowHiddenPwdUtil.initShowHiddenPwdView(iv_see_once_pwd, et_pwd_once);
        ShowHiddenPwdUtil.initShowHiddenPwdView(iv_see_again_pwd, et_pwd_again);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_verify://获取验证码
                getVerifyCode();
                break;
            case R.id.btn_confirm://确认修改
                confirm();
                break;
            default:
                break;
        }
    }

    private void confirm() {
        phoneStr = et_phone.getText().toString().trim();
        verifyCodeStr = et_verify_code.getText().toString().trim();
        pwdOnceStr = et_pwd_once.getText().toString().trim();
        pwdAgainStr = et_pwd_again.getText().toString().trim();
        if(!ValidatorUtil.isMobile(phoneStr)){
            showToast("手机号码不正确");
            return;
        }

        if(TextUtils.isEmpty(verifyCodeStr) || verifyCodeStr.length() != 4){
            showToast("验证码不正确");
            return;
        }
        if(pwdOnceStr.length() < 6 || pwdAgainStr.length() > 12){
            showToast("请输入6-12位密码");
            return;
        }
        if(!pwdOnceStr.equals(pwdAgainStr)){
            showToast("两次输入的密码不一致");
            return;
        }

        //TODO 修改密码
        MyHttp.editPwd(http, null, phoneStr, verifyCodeStr, pwdOnceStr, new HttpForVolley.HttpTodo() {

            @Override
            public void httpTodo(Integer which, JSONObject response) {
                int code = response.optInt("code");
                showToast(response.optString("msg"));
                if(code != 0){
                    return;
                }
                finish();
            }
        });
    }

    private void getVerifyCode() {
        phoneStr = et_phone.getText().toString().trim();

        if (!ValidatorUtil.isMobile(phoneStr)) {
            showToast("请输入手机号");
            return;
        }
        MyApplication.downTimer.going();
        MyApplication.downTimer.setTextView(tv_get_verify);
        //TODO 打开发送验证码接口调用
//        MyHttp.sendCode(http, null, 1, phoneStr, new HttpForVolley.HttpTodo() {
//            @Override
//            public void httpTodo(Integer which, JSONObject response) {
//                int code = response.optInt("code");
//                showToast(response.optString("msg"));
//                if(code != 0){
//                    MyApplication.downTimer.setInit();
//                }
//            }
//        });
    }
}
