package com.cqfrozen.jsh.center;

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
import com.cqfrozen.jsh.adapter.ShopAdapter;
import com.cqfrozen.jsh.entity.ShopInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.CustomSimpleDialog;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/9.
 */
public class ShopListActivity extends MyActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_EDIT = 2;

    private RecyclerView rv_shoplist;
    private List<ShopInfo> shopInfos = new ArrayList<>();
    private ShopAdapter adapter;

    private TextView tv_add;
    private CustomSimpleDialog deleteDialog;

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
        rv_shoplist = (RecyclerView) findViewById(R.id.rv_shoplist);
        tv_add = (TextView) findViewById(R.id.tv_add);

        tv_add.setOnClickListener(this);
    }

    private void initRC() {
        rv_shoplist.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new ShopAdapter(this, shopInfos);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(8), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_shoplist.addItemDecoration(decoration);
        rv_shoplist.setLayoutManager(manager);
        rv_shoplist.setAdapter(adapter);

        //点击列表上每个item的删除按钮
        adapter.setOnDeleteClickListener(new ShopAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(int position, String s_id) {
                showDeleteDialog(position, s_id);
            }
        });

        //点击列表上每个item的编辑按钮
        adapter.setOnEditClickListener(new ShopAdapter.OnEditClickListener() {
            @Override
            public void onEdit(int position, ShopInfo shopInfo) {
                Intent intent = new Intent(ShopListActivity.this, ShopEditActivity.class);
                intent.putExtra("shopInfo", shopInfo);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });
    }


    private void showDeleteDialog(final int position, final String s_id) {
        deleteDialog = new CustomSimpleDialog.Builder(this)
                .setMessage("确定要删除改地址吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        MyHttp.deleteStore(http, null, s_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                                dialog.cancel();
                                int code = response.optInt("code");
                                showToast(response.optString("msg"));
                                if (code != 0) {
                                    return;
                                }
                                shopInfos.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                })
                .create();
        deleteDialog.show();

    }

    private void getData() {
        MyHttp.shopList(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
//                    showToast(msg);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                Intent intent = new Intent(this, ShopAddActivity.class);
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
