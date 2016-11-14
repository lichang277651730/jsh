package com.cqfrozen.jsh.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.fragment.HuibiFragment;
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/10/24.
 * intent.putExtra("hb_count", getUserInfo().hb_count);
 * intent.putExtra("url", huibi_rule_url);
 */
public class HuibiListActivity extends MyActivity implements View.OnClickListener{

    private float hb_count;
    private TextView tv_huibi;
    private TextView tv_all;
    private TextView tv_right;
    private TextView tv_use;
    private int type = 1;//1收入  2支出
    private int page = 1;
    private int is_page = 0;//0没有下一页 1有下一页

    private View v_huibi_all;
    private View v_huibi_use;
    private String url;
    private HuibiFragment huibiFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huibilist);
        getIntentData();
        initView();
        setFragment();
    }

    private void getIntentData() {
        hb_count = getIntent().getFloatExtra("hb_count", 0f);
        url = getIntent().getStringExtra("url");
    }

    private void initView() {
        setMyTitle("我的粮票", "粮票规则");
        tv_huibi = (TextView) findViewById(R.id.tv_huibi);
        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_use = (TextView) findViewById(R.id.tv_use);

        v_huibi_all = findViewById(R.id.v_huibi_all);
        v_huibi_use = findViewById(R.id.v_huibi_use);
        tv_huibi.setText(hb_count + "");
        tv_all.setOnClickListener(this);
        tv_use.setOnClickListener(this);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HuibiListActivity.this, WebUrlActivity.class);
                intent.putExtra("title", "粮票规则");
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        tv_all.setTextColor(getResources().getColor(R.color.main));
        v_huibi_all.setVisibility(View.VISIBLE);
    }

    private void setFragment() {
        huibiFragment = HuibiFragment.getInstance(type);
        showFragment(huibiFragment);
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fl_huibi_container, fragment);
        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_all:
            case R.id.tv_use:
                setTab(v.getId());
                huibiFragment.setResultData(type);
                huibiFragment.setSelection(0);
                break;
            default:
                break;
        }
    }

    private void setTab(int id) {
        tv_all.setTextColor(getResources().getColor(R.color.myblack));
        tv_use.setTextColor(getResources().getColor(R.color.myblack));
        v_huibi_all.setVisibility(View.INVISIBLE);
        v_huibi_use.setVisibility(View.INVISIBLE);
        switch (id) {
            case R.id.tv_all:
                type = 1;
                v_huibi_all.setVisibility(View.VISIBLE);
                tv_all.setTextColor(getResources().getColor(R.color.main));
                break;
            case R.id.tv_use:
                type = 2;
                v_huibi_use.setVisibility(View.VISIBLE);
                tv_use.setTextColor(getResources().getColor(R.color.main));
                break;
            default:
                break;
        }
    }


}
