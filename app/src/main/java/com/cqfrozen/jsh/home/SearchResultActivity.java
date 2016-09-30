package com.cqfrozen.jsh.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/9/29.
 * intent.putExtra("keyword", keywordStr);
 * 输入搜索关键词，点击搜索跳转到的页面
 */
public class SearchResultActivity extends MyActivity implements View.OnClickListener {

    private String keyword;
    private MyEditText et_keyword;
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        keyword = getIntent().getStringExtra("keyword");
    }

    private void initView() {
        et_keyword = (MyEditText) findViewById(R.id.et_keyword);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_keyword.setText(keyword);
        iv_back.setOnClickListener(this);
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
