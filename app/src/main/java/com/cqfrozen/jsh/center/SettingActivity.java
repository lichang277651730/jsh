package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.util.DataCleanManager;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SettingActivity extends MyActivity implements View.OnClickListener {

    private TextView tv_cache;
    private LinearLayout ll_cache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        setMyTitle("设置");
        tv_cache = (TextView) findViewById(R.id.tv_cache);
        ll_cache = (LinearLayout) findViewById(R.id.ll_cache);
        ll_cache.setOnClickListener(this);
        try {
            tv_cache.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cache:
                try {
                    DataCleanManager.clearAllCache(this);
                    tv_cache.setText(DataCleanManager.getTotalCacheSize(this));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
