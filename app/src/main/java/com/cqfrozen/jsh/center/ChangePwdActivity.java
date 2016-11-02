package com.cqfrozen.jsh.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.common.http.HttpForVolley;
import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.util.ShowHiddenPwdUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/13.
 * 修改密码页面
 */
public class ChangePwdActivity extends MyActivity implements View.OnClickListener {

    private MyEditText et_pwd_old;
    private ImageView iv_see_old_pwd;
    private MyEditText et_newpwd_once;
    private ImageView iv_see_once_newpwd;
    private MyEditText et_newpwd_again;
    private ImageView iv_see_again_newpwd;
    private Button btn_change;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);
        initView();
    }

    private void initView() {
        setMyTitle("修改密码");
        et_pwd_old = (MyEditText) findViewById(R.id.et_pwd_old);
        iv_see_old_pwd = (ImageView) findViewById(R.id.iv_see_old_pwd);
        et_newpwd_once = (MyEditText) findViewById(R.id.et_newpwd_once);
        iv_see_once_newpwd = (ImageView) findViewById(R.id.iv_see_once_newpwd);
        et_newpwd_again = (MyEditText) findViewById(R.id.et_newpwd_again);
        iv_see_again_newpwd = (ImageView) findViewById(R.id.iv_see_again_newpwd);
        btn_change = (Button) findViewById(R.id.btn_change);
        btn_change.setOnClickListener(this);
        ShowHiddenPwdUtil.initShowHiddenPwdView(iv_see_old_pwd, et_pwd_old);
        ShowHiddenPwdUtil.initShowHiddenPwdView(iv_see_once_newpwd, et_newpwd_once);
        ShowHiddenPwdUtil.initShowHiddenPwdView(iv_see_again_newpwd, et_newpwd_again);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change:
                change();
                break;
            default:
                break;
        }
    }

    private void change() {
        String oldPwd = et_pwd_old.getText().toString().trim();
        String newOldOnce = et_newpwd_once.getText().toString().trim();
        String newOldAgain = et_newpwd_again.getText().toString().trim();
        if(TextUtils.isEmpty(oldPwd) || oldPwd.length() < 6){
            showToast("请输入旧密码");
            return;
        }
        if(TextUtils.isEmpty(newOldOnce) || newOldOnce.length() < 6){
            showToast("请输入新密码");
            return;
        }
        if(!newOldOnce.equals(newOldAgain)){
            showToast("两次输入新密码不一致");
            return;
        }
        MyHttp.pd(http, null, oldPwd, newOldOnce, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                showToast(response.optString("msg"));
                int code = response.optInt("code");
                if(code != 0){
                    return;
                }
                MyApplication.userInfo = null;
                SPUtils.setToken("");
                MyApplication.token = "";
                finish();
                startActivity(new Intent(ChangePwdActivity.this, LoginActivity.class));
            }
        });

    }
}
