package com.cqfrozen.jsh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.center.LoginActivity;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.SPUtils;

/**
 * Created by Administrator on 2016/9/18.
 * 索引页
 */
public class IndexActivity extends MyActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
    }

    private void initView() {
        Button btn_go = (Button) findViewById(R.id.btn_go);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.setFirst(false);
                startActivity(new Intent(IndexActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
