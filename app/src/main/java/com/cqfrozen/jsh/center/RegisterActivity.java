package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/9/27.
 */
public class RegisterActivity extends MyActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        setMyTitle("注册");
    }
}