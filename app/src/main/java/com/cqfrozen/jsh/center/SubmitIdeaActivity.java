package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private Button btn_confirm;
    private String ideaStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitidea);
        initView();
    }

    private void initView() {
        setMyTitle("意见反馈");
        et_idea_content = (EditText) findViewById(R.id.et_idea_content);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                confirmIdea();
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
