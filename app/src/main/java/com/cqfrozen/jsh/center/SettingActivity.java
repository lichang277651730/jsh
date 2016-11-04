package com.cqfrozen.jsh.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.common.util.DataCleanManager;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SettingActivity extends MyActivity implements View.OnClickListener {

    private TextView tv_cache;
    private LinearLayout ll_cache;
    private LinearLayout ll_change_pwd;
    private Button btn_exit;
    private LinearLayout ll_help;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        setMyTitle("设置");
        ll_change_pwd = (LinearLayout) findViewById(R.id.ll_change_pwd);
        ll_cache = (LinearLayout) findViewById(R.id.ll_cache);
        tv_cache = (TextView) findViewById(R.id.tv_cache);
        ll_help = (LinearLayout) findViewById(R.id.ll_help);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        ll_change_pwd.setOnClickListener(this);
        ll_cache.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        try {
            tv_cache.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_change_pwd://修改密码
                startActivity(new Intent(this, ChangePwdActivity.class));
                break;
            case R.id.ll_cache://清除缓存
                try {
                    DataCleanManager.clearAllCache(this);
                    tv_cache.setText(DataCleanManager.getTotalCacheSize(this));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.btn_exit://退出
                loginout();
                break;
            default:
                break;
        }
    }

    private void loginout() {
        MyHttp.loginout(http, null, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                MyApplication.userInfo = null;
                MyApplication.token = "";
                SPUtils.setToken("");
                finish();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
//                showToast(response.optString("msg"));
//                int code = response.optInt("code");
//                if(code != 0){
//                    return;
//                }
            }
        });

    }
}
