package com.cqfrozen.jsh.center;

import android.content.DialogInterface;
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
import com.cqfrozen.jsh.util.AppUtil;
import com.cqfrozen.jsh.util.CustomSimpleDialog;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.util.UMengUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SettingActivity extends MyActivity implements View.OnClickListener {

    private TextView tv_cache;
    private TextView tv_version;
    private LinearLayout ll_cache;
    private LinearLayout ll_change_pwd;
    private Button btn_exit;
    private LinearLayout ll_help;
    private LinearLayout ll_change_phone;
    private LinearLayout ll_good;
    private CustomSimpleDialog exitDialog;

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
        tv_version = (TextView) findViewById(R.id.tv_version);
        ll_help = (LinearLayout) findViewById(R.id.ll_help);
        ll_change_phone = (LinearLayout) findViewById(R.id.ll_change_phone);
        ll_good = (LinearLayout) findViewById(R.id.ll_good);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        ll_change_pwd.setOnClickListener(this);
        ll_cache.setOnClickListener(this);
        ll_change_phone.setOnClickListener(this);
        ll_good.setOnClickListener(this);
        ll_help.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        try {
            tv_cache.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_version.setText("V" + AppUtil.getVersionName(this));
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
            case R.id.ll_change_phone://更换手机号码
                startActivity(new Intent(this, ChangePhoneActivity.class));
                break;
            case R.id.ll_good://去应用市场给好评
                AppUtil.marketDownload(this, "com.cqfrozen.jsh");
                break;
            case R.id.ll_help://意见反馈
                startActivity(new Intent(this, SubmitIdeaActivity.class));
                break;
            case R.id.btn_exit://退出
                showExitDialog();
//                loginout();
                break;
            default:
                break;
        }
    }

    private void showExitDialog() {
        exitDialog = new CustomSimpleDialog.Builder(this)
                .setMessage("确定要退出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        loginout();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                })
                .create();
        exitDialog.show();

    }

    private void loginout() {

        MyHttp.loginout(http, null, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                MyApplication.userInfo = null;
                MyApplication.token = "";
                SPUtils.setToken("");
                finish();
                UMengUtils.setSignOff(); //友盟退出
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            }
        });

    }
}
