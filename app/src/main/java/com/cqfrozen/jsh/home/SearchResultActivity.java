package com.cqfrozen.jsh.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/9/29.
 * intent.putExtra("keyword", keywordStr);
 * 输入搜索关键词，点击搜索跳转到的页面
 */
public class SearchResultActivity extends MyActivity implements View.OnClickListener {

    private MyEditText et_keyword;
    private ImageView iv_back;
    private TextView tv_all;
    private TextView tv_price;
    private ImageView iv_price_sort;
    private LinearLayout ll_price;
    private TextView tv_sales;

    private String keyword = "";
    private String sort = "add_time";
    private String order = "DESC";
    private View v_all;
    private View v_price;
    private View v_sales;

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
        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_price = (TextView) findViewById(R.id.tv_price);
        iv_price_sort = (ImageView) findViewById(R.id.iv_price_sort);
        ll_price = (LinearLayout) findViewById(R.id.ll_price);
        tv_sales = (TextView) findViewById(R.id.tv_sales);
        v_all = (View) findViewById(R.id.v_all);
        v_price = (View) findViewById(R.id.v_price);
        v_sales = (View) findViewById(R.id.v_sales);
        et_keyword.setText(keyword);
        et_keyword.setSelection(keyword.length());

        iv_back.setOnClickListener(this);
        tv_all.setOnClickListener(this);
        ll_price.setOnClickListener(this);
        tv_sales.setOnClickListener(this);
        v_all.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_all:
            case R.id.ll_price:
            case R.id.tv_sales:
                setTab(v.getId());

                //TODO 更新点击不同tab得到的结果
                break;
            default:
                break;
        }
    }

    //tab随着选择不同要同步更新
    private void setTab(int id) {
        tv_all.setTextColor(getResources().getColor(R.color.black));
        tv_price.setTextColor(getResources().getColor(R.color.black));
        tv_sales.setTextColor(getResources().getColor(R.color.black));
        iv_price_sort.setImageResource(R.mipmap.sort_default);

        v_all.setVisibility(View.INVISIBLE);
        v_price.setVisibility(View.INVISIBLE);
        v_sales.setVisibility(View.INVISIBLE);
        switch (id) {
            case R.id.tv_all:
                tv_all.setTextColor(getResources().getColor(R.color.main));
                v_all.setVisibility(View.VISIBLE);
                //TODO 更改搜索参数
                order = "DESC";
                sort = "add_time";
                break;
            case R.id.ll_price:
                tv_price.setTextColor(getResources().getColor(R.color.main));
                v_price.setVisibility(View.VISIBLE);
                sort = "shop_price";
                if("DESC".equals(order)){
                    order = "ASC";
                    iv_price_sort.setImageResource(R.mipmap.sort_asc);
                }else if("ASC".equals(order)){
                    order = "DESC";
                    iv_price_sort.setImageResource(R.mipmap.sort_desc);
                }
                break;
            case R.id.tv_sales:
                tv_sales.setTextColor(getResources().getColor(R.color.main));
                v_sales.setVisibility(View.VISIBLE);
                //TODO 更改搜索参数
                order = "DESC";
                sort = "volume";
                break;
            default:
                break;
        }
    }
}
