package com.cqfrozen.jsh.center;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_EDIT = 2;

    private TextView tv_add;
    private RecyclerView rv_addresslist;
    private List<AddressInfo> addressInfos = new ArrayList<>();
    private AddressAdapter adapter;
    private AlertDialog deleteDialog;
    private AlertDialog defaultDialog;

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
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(8), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_addresslist.addItemDecoration(decoration);
        rv_addresslist.setLayoutManager(manager);
        rv_addresslist.setAdapter(adapter);
        //点击列表上每个item的删除按钮
        adapter.setOnDeleteClickListener(new AddressAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(int position, String a_id) {
                showDeleteDialog(position, a_id);
            }
        });

        //点击列表上每个item的编辑按钮
        adapter.setOnEditClickListener(new AddressAdapter.OnEditClickListener() {
            @Override
            public void onEdit(int position, AddressInfo addressInfo) {
                Intent intent = new Intent(AddressListActivity.this, AddressEditActivity.class);
                intent.putExtra("addressInfo", addressInfo);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });

        //点击列表上每个item的设置默认按钮
        adapter.setOnCheckClickListener(new AddressAdapter.OnCheckClickListener() {
            @Override
            public void onCheck(int position, String a_id, int is_default) {
                if(is_default != 1){
                    showDefaultDialog(position, a_id);
                }
            }
        });
    }

    private void showDefaultDialog(int position, final String a_id) {

        defaultDialog = new AlertDialog.Builder(this)
                .setMessage("确定设置此地址为默认地址吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyHttp.setDefault(http, null, a_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                                int code = response.optInt("code");
                                showToast(response.optString("msg"));
                                if(code != 0){
                                    return;
                                }
                                getData();
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        defaultDialog.show();
    }

    private void showDeleteDialog(final int position, final String a_id) {
        deleteDialog = new AlertDialog.Builder(this)
                .setMessage("确定要删除改地址吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        deleteDialog.show();
    }


    private void getData() {
        MyHttp.addressList(http, null, new MyHttp.MyHttpResult() {

            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
//                    showToast(msg);
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
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add://添加新收货地址
                Intent intent = new Intent(this, AddressAddActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD || requestCode == REQUEST_CODE_EDIT){
            if(resultCode == RESULT_OK){
                getData();
            }
        }
    }
}
