package com.cqfrozen.jsh.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.widget.MyEditText;
import com.common.widget.MyTagView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SearchActivity extends MyActivity implements View.OnClickListener, MyTagView.OnTagClickListener {

    private MyTagView tag_hot;
    private String[] ary = new String[]{"周围毛肚", "周围毛肚", "周围毛肚",
                                            "九九鸭肠", "九九鸭肠", "九九鸭肠",
                                                "王味虾饺", "王味虾饺"};
    private ImageView iv_back;
    private MyEditText et_keyword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        tag_hot = (MyTagView) findViewById(R.id.tag_hot);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_keyword = (MyEditText) findViewById(R.id.et_keyword);
        iv_back.setOnClickListener(this);
        tag_hot.setMyTag(ary);
        tag_hot.setOnTagClickListener(this);
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

    @Override
    public void onTagClick(View v) {
        String tagStr = ((TextView) v).getText().toString().trim();
        et_keyword.setText(tagStr);
        et_keyword.setSelection(tagStr.length());
        search();
    }

    private void search() {
        String keywordStr = et_keyword.getText().toString().trim();
        if(TextUtils.isEmpty(keywordStr)){
            return;
        }
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("keyword", keywordStr);
        startActivity(intent);
    }
}
