package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/7.
 */
public class SubmitIdeaActivity extends MyActivity implements View.OnClickListener {

    private EditText et_idea_content;
    private TextView tv_submit;
    private String ideaStr;
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitidea);
        initView();
    }

    private void initView() {
        et_idea_content = (EditText) findViewById(R.id.et_idea_content);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_submit.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                confirmIdea();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void confirmIdea() {
        ideaStr = et_idea_content.getText().toString().trim();
        if(TextUtils.isEmpty(ideaStr)){
            showToast("亲,您还未填写您宝贵的意见哦!~");
            return;
        }

        MyHttp.feedBack(http, null, ideaStr, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                showToast(response.optString("msg"));
                int code = response.optInt("code");
                if(code != 0){
                    return;
                }
                finish();
            }
        });
    }
}
