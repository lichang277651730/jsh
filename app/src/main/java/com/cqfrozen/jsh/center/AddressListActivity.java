package com.cqfrozen.jsh.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.common.widget.MyGridDecoration;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.AddressAdapter;
import com.cqfrozen.jsh.entity.AddressInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class AddressListActivity extends MyActivity implements View.OnClickListener {

    private TextView tv_add;
    private RecyclerView rv_addresslist;
    private List<AddressInfo> addressInfos = new ArrayList<>();
    private AddressAdapter adapter;
    private String s_id;

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
        tv_add = (TextView) findViewById(R.id.tv_add);
        rv_addresslist = (RecyclerView) findViewById(R.id.rv_addresslist);
        tv_add.setOnClickListener(this);
    }

    private void initRV() {
        rv_addresslist.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new AddressAdapter(this, addressInfos);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(4), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_addresslist.addItemDecoration(decoration);
        rv_addresslist.setLayoutManager(manager);
        rv_addresslist.setAdapter(adapter);
        adapter.setOnDeleteClickListener(new AddressAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(final int position, String a_id) {
                MyHttp.deleterAddress(http, null, a_id, new HttpForVolley.HttpTodo() {
                    @Override
                    public void httpTodo(Integer which, JSONObject response) {
                        int code = response.optInt("code");
                        showToast(response.optString("msg"));
                        if(code != 0){
                            return;
                        }
                        addressInfos.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
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
                //处理默认地址显示在第一个
                AddressInfo pauseInfo = new AddressInfo();
                for(int i = 0; i < addressInfos.size(); i++){
                    if(addressInfos.get(i).is_default == 1){
                        pauseInfo = addressInfos.get(i);
                        addressInfos.remove(i);
                        addressInfos.add(0, pauseInfo);
                        break;
                    }
                }
                s_id = addressInfos.get(0).s_id;//为添加新地址需要参数：s_id
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add://添加新收货地址
                Intent intent = new Intent(this, AddressAddActivity.class);
                intent.putExtra("s_id", s_id);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
