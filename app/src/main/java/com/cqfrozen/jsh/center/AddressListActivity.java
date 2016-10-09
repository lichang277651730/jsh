package com.cqfrozen.jsh.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.base.BaseValue;
import com.common.widget.GridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.AddressAdapter;
import com.cqfrozen.jsh.entity.AddressInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class AddressListActivity extends MyActivity implements View.OnClickListener {

    private RelativeLayout rl_add;
    private RecyclerView rv_addresslist;
    private List<AddressInfo> addressInfos = new ArrayList<>();
    private AddressAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresslist);
        initView();
        initRV();
        getData();
    }

    private void initView() {
        setMyTitle("地址管理");
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        rv_addresslist = (RecyclerView) findViewById(R.id.rv_addresslist);
        rl_add.setOnClickListener(this);
    }

    private void initRV() {
        rv_addresslist.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new AddressAdapter(this, addressInfos);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        GridDecoration decoration = new GridDecoration(0, BaseValue.dp2px(6),
                getResources().getColor(R.color.mybg), true);
        rv_addresslist.addItemDecoration(decoration);
        rv_addresslist.setLayoutManager(manager);
        rv_addresslist.setAdapter(adapter);

    }

    private void getData() {
        MyHttp.addressList(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                addressInfos.clear();
                addressInfos.addAll((List<AddressInfo>)bean);
                if(addressInfos == null || addressInfos.size() == 0){
                    return;
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add:
                startActivity(new Intent(this, AddressAddActivity.class));
                break;
            default:
                break;
        }
    }
}
