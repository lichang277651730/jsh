package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.common.base.BaseValue;
import com.common.widget.GridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.ShopAdapter;
import com.cqfrozen.jsh.entity.ShopInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */
public class ShopListActivity extends MyActivity {

    private RecyclerView rv_shop;
    private List<ShopInfo> shopInfos = new ArrayList<>();
    private ShopAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);
        initView();
        initRC();
        getData();
    }

    private void initView() {
        setMyTitle("店铺管理");
        rv_shop = (RecyclerView) findViewById(R.id.rv_shop);
    }

    private void initRC() {
        rv_shop.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new ShopAdapter(this, shopInfos);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rv_shop.setLayoutManager(manager);
        GridDecoration decoration = new GridDecoration(0, BaseValue.dp2px(6),
                getResources().getColor(R.color.mybg), true);
        rv_shop.addItemDecoration(decoration);
        rv_shop.setAdapter(adapter);
    }

    private void getData() {
        MyHttp.storeList(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                shopInfos.clear();
                shopInfos.addAll((List<ShopInfo>)bean);
                if(shopInfos == null || shopInfos.size() == 0){
                    return;
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
