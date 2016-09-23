package com.cqfrozen.jsh.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.common.base.BaseFragment;
import com.cqfrozen.jsh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 * 购物车页面 fragment
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {

    private static final int TAG_EIDT = 1;
    private static final int TAG_FINISH = 2;

    private static CartFragment fragment;
    private Button btn_edit;
    private RecyclerView rv_cart;
    private CheckBox cb_all;
    private TextView tv_total;
    private Button btn_order;
    private Button btn_del;
    private List<CartGoodsInfo> cartGoodsInfos = new ArrayList<>();
    private CartRVAdapter cartAdapter;
    private CartManager cartManager;

    public static CartFragment getInstance(){
        if(fragment == null){
            fragment = new CartFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_cart, null);
            cartManager = CartManager.getInstance(mActivity);
            initView();
            initTitle();
            initRV();
            getData();
        }
        return view;
    }

    private void initTitle() {
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_edit.setTag(TAG_EIDT);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int)v.getTag();
                if(tag == TAG_EIDT){//当前是编辑，点击就变成完成
                    doFinish();
                }else if(tag == TAG_FINISH){//当前是完成，点击就变成编辑
                    doEdit();
                }
            }
        });
    }

    private void initView() {
        rv_cart = (RecyclerView) view.findViewById(R.id.rv_cart);
        cb_all = (CheckBox) view.findViewById(R.id.cb_all);
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        btn_order = (Button) view.findViewById(R.id.btn_order);
        btn_del = (Button) view.findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
//        cb_all.setChecked(true);
    }

    private void initRV() {
        rv_cart.setOverScrollMode(View.OVER_SCROLL_NEVER);
        cartAdapter = new CartRVAdapter(mActivity, cartGoodsInfos, tv_total, cb_all);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        rv_cart.setLayoutManager(manager);
        rv_cart.setAdapter(cartAdapter);
    }

    private void getData() {
        List<CartGoodsInfo> localData = cartManager.loadAllFromSp();
        cartGoodsInfos.clear();
        cartGoodsInfos.addAll(localData);
        cartAdapter.showTotalPrice();
        cartAdapter.allCheckedListen();
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onShow() {
        super.onShow();
        Log.d("FragmentShow", "CartFragment");
        getData();
    }

    private void doFinish() {
        btn_edit.setText(mActivity.getString(R.string.cart_finish));
        btn_order.setVisibility(View.GONE);
        btn_del.setVisibility(View.VISIBLE);
        tv_total.setVisibility(View.GONE);
        btn_edit.setTag(TAG_FINISH);
        cartAdapter.checkAllNone(false);
    }

    private void doEdit() {
        btn_edit.setText(mActivity.getString(R.string.cart_edit));
        btn_order.setVisibility(View.VISIBLE);
        btn_del.setVisibility(View.GONE);
        tv_total.setVisibility(View.VISIBLE);
        btn_edit.setTag(TAG_EIDT);
        cartAdapter.checkAllNone(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_del:
                cartAdapter.delete();
                break;
            default:
                break;
        }
    }
}
