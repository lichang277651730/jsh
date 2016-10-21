package com.cqfrozen.jsh.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.fragment.GoodsFragment;
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/9/29.
 * 搜索结果页面
 * intent.putExtra("keyword", keywordStr);
 * 输入搜索关键词，点击搜索跳转到的页面
 */
public class SearchResultActivity extends MyActivity implements View.OnClickListener {

    private GoodsFragment goodsFragment;

    public interface SortType{
        //0默认，1价格，2销量 默认是默认排序
        int DEFAULT = 0;
        int PRICE = 1;
        int SALES = 2;
    }

    public interface OrderType{
        //0升序，1降序 默认升序
        int ASC = 0;
        int DESC = 1;
    }

    private MyEditText et_keyword;
    private ImageView iv_back;
    private TextView tv_grid2list;
    private TextView tv_all;
    private TextView tv_price;
    private ImageView iv_price_sort;
    private LinearLayout ll_price;
    private TextView tv_sales;

    private String keyword = "";
    private int sort = SortType.DEFAULT;
    private int order = OrderType.ASC;
    private View v_all;
    private View v_price;
    private View v_sales;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        getDataFromIntent();
        initView();
        setFragment();
    }

    private void getDataFromIntent() {
        keyword = getIntent().getStringExtra("keyword");
    }

    private void initView() {
        et_keyword = (MyEditText) findViewById(R.id.et_keyword);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_grid2list = (TextView) findViewById(R.id.tv_grid2list);
        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_price = (TextView) findViewById(R.id.tv_price);
        iv_price_sort = (ImageView) findViewById(R.id.iv_price_sort);
        ll_price = (LinearLayout) findViewById(R.id.ll_price);
        tv_sales = (TextView) findViewById(R.id.tv_sales);
        v_all = (View) findViewById(R.id.v_all);
        v_price = (View) findViewById(R.id.v_price);
        v_sales = (View) findViewById(R.id.v_sales);
        et_keyword.setText(keyword);

        iv_back.setOnClickListener(this);
        tv_grid2list.setOnClickListener(this);
        tv_all.setOnClickListener(this);
        ll_price.setOnClickListener(this);
        tv_sales.setOnClickListener(this);
        et_keyword.setOnClickListener(this);
        v_all.setVisibility(View.VISIBLE);
    }

    private void setFragment() {
        goodsFragment = GoodsFragment.getInstanceForSearch(keyword, sort, order);
        showFragment(goodsFragment);
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fl_container, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.et_keyword:
                finish();
                break;
            case R.id.tv_grid2list:
                //TODO 切换视图
//                search();
                break;
            case R.id.tv_all:
            case R.id.ll_price:
            case R.id.tv_sales:
                setTab(v.getId());
                goodsFragment.setSearchValue(sort, order);
                goodsFragment.setSelection(0);
                break;
            default:
                break;
        }
    }

    private void search() {

    }

    //tab随着选择不同要同步更新
    private void setTab(int id) {
        tv_all.setTextColor(getResources().getColor(R.color.black));
        tv_price.setTextColor(getResources().getColor(R.color.black));
        tv_sales.setTextColor(getResources().getColor(R.color.black));
        iv_price_sort.setImageResource(R.mipmap.icon_search_price_default);

        v_all.setVisibility(View.INVISIBLE);
        v_price.setVisibility(View.INVISIBLE);
        v_sales.setVisibility(View.INVISIBLE);
        switch (id) {
            case R.id.tv_all:
                tv_all.setTextColor(getResources().getColor(R.color.main));
                v_all.setVisibility(View.VISIBLE);
                order = OrderType.ASC;
                sort = SortType.DEFAULT;
                break;
            case R.id.ll_price:
                tv_price.setTextColor(getResources().getColor(R.color.main));
                v_price.setVisibility(View.VISIBLE);
                sort = SortType.PRICE;
                if(OrderType.DESC == order){
                    order = OrderType.ASC;
                    iv_price_sort.setImageResource(R.mipmap.icon_search_price_asc);
                }else if(OrderType.ASC == order){
                    order = OrderType.DESC;
                    iv_price_sort.setImageResource(R.mipmap.icon_search_price_desc);
                }
                break;
            case R.id.tv_sales:
                tv_sales.setTextColor(getResources().getColor(R.color.main));
                v_sales.setVisibility(View.VISIBLE);
                order = OrderType.ASC;
                sort = SortType.SALES;
                break;
            default:
                break;
        }
    }
}
