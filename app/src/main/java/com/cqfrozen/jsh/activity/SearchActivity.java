package com.cqfrozen.jsh.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.widget.MyTagView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SearchActivity extends MyActivity implements View.OnClickListener {

    private MyTagView tag_hot;
    private String[] ary = new String[]{"周围毛肚", "周围毛肚", "周围毛肚",
                                            "九九鸭肠", "九九鸭肠", "九九鸭肠",
                                                "王味虾饺", "王味虾饺"};
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        tag_hot = (MyTagView) findViewById(R.id.tag_hot);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_back.setOnClickListener(this);
        tag_hot.setMyTag(ary);
        tag_hot.setOnTagClickListener(new MyTagView.OnTagClickListener() {
            @Override
            public void onTagClick(View v) {
                String s = ((TextView) v).getText().toString().trim();
                showToast(s);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
