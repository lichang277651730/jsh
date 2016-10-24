package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.HuibiAdapter;
import com.cqfrozen.jsh.entity.HuibiInfo;
import com.cqfrozen.jsh.entity.HuibiResultInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 * intent.putExtra("hb_count", getUserInfo().hb_count);
 */
public class HuibiListActivity extends MyActivity {

    private float hb_count;
    private TextView tv_huibi;
    private TextView tv_all;
    private TextView tv_right;
    private TextView tv_use;
    private ListView lv_huibi;
    private List<HuibiInfo> huibiInfos = new ArrayList<>();
    private HuibiAdapter adapter;
    private int type = 1;
    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huibilist);
        getIntentData();
        initView();
        initLV();
        getData();
    }

    private void getIntentData() {
        hb_count = getIntent().getFloatExtra("hb_count", 0f);
    }

    private void initView() {
        setMyTitle("我的汇币", "汇币规则");
        tv_huibi = (TextView) findViewById(R.id.tv_huibi);
        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_use = (TextView) findViewById(R.id.tv_use);
        lv_huibi = (ListView) findViewById(R.id.lv_huibi);
        tv_huibi.setText(hb_count + "");
    }

    private void initLV() {
        lv_huibi.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new HuibiAdapter(this, huibiInfos);
        lv_huibi.setAdapter(adapter);
    }

    private void getData() {
        MyHttp.searchHBinfo(http, null, type, page, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                HuibiResultInfo huibiResultInfo = (HuibiResultInfo) bean;
                huibiInfos.addAll(huibiResultInfo.data1);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
