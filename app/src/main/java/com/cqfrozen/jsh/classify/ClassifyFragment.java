package com.cqfrozen.jsh.classify;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.common.base.BaseFragment;
import com.common.base.BaseValue;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.CategoryInfo;
import com.cqfrozen.jsh.home.SearchActivity;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.util.UIUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.google.gson.reflect.TypeToken;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 * 分类页面 fragment
 */
public class ClassifyFragment extends BaseFragment implements MyHttp.MyHttpResult, View
        .OnClickListener {

    private static ClassifyFragment fragment;
    private ViewPager vp_classify;
    private ScrollIndicatorView indicator_classify;
    private List<CategoryInfo> categoryInfos = new ArrayList<>();
    private ClassifyIndicatorAdapter adapter;
    private ImageView iv_search;
    private ImageView iv_pop;
    private ClassifyGvAdapter gvAdapter;
    private PopupWindow popupWindow;

    public static ClassifyFragment getInstance(){
        if(fragment == null){
            fragment = new ClassifyFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = this;
        if(view == null){
            view = inflater.inflate(R.layout.fragment_classify, null);
            initView();
            initVP();
            getData();
        }
        return view;
    }

    private void initView() {
        indicator_classify = (ScrollIndicatorView) view.findViewById(R.id.indicator_classify);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        iv_pop = (ImageView) view.findViewById(R.id.iv_pop);
        vp_classify = (ViewPager) view.findViewById(R.id.vp_classify);
        iv_search.setOnClickListener(this);
        iv_pop.setOnClickListener(this);
        createPopMore();
    }

    private void initVP() {
        vp_classify.setOverScrollMode(View.OVER_SCROLL_NEVER);
        vp_classify.setOffscreenPageLimit(1);
        indicator_classify.setScrollBar(new ColorBar(mActivity, UIUtils.getColor(R.color.main), BaseValue.dp2px(4)){

            @Override
            public int getWidth(int tabWidth) {
                return tabWidth - BaseValue.dp2px(14);
            }
        });
        // 设置滚动监听
        indicator_classify.setOnTransitionListener(new OnTransitionTextListener().setColor(UIUtils.getColor(R.color.main), Color.BLACK));

        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(indicator_classify, vp_classify);
        indicatorViewPager.setClickIndicatorAnim(false);
        adapter = new ClassifyIndicatorAdapter
                (mActivity.getSupportFragmentManager(), mActivity, categoryInfos);
        indicatorViewPager.setAdapter(adapter);
    }

    private void getData() {
        long nowTime = System.currentTimeMillis();
        Long saveTime = SPUtils.getClassifyTime();
        String classifyData = SPUtils.getClassifyData();
//        if(saveTime != 0 && nowTime < saveTime + 86400000L && !classifyData.isEmpty()){
//            setData(classifyData);
//        }else {
            MyHttp.goodsType(http, null, this);
//        }
    }

    @Override
    public void onShow() {
        super.onShow();
//        if(categoryInfos != null){
//            categoryInfos.clear();
//        }
//        getData();
        if(categoryInfos == null || categoryInfos.size() == 0){
            getData();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        if(code != 0){
//            showToast(msg);
            return;
        }
        categoryInfos.addAll((List<CategoryInfo>)bean);
        if(categoryInfos.size() == 0){
            return;
        }
        String beanJson = BaseValue.gson.toJson(categoryInfos);
        SPUtils.setClassifyData(beanJson);
        SPUtils.setClassifyTime(System.currentTimeMillis());
        setData(beanJson);
    }

    private void setData(String beanJson) {
        List<CategoryInfo> categoryList = BaseValue.gson.fromJson(beanJson, new TypeToken<List<CategoryInfo>>() {
        }.getType());
        categoryInfos.clear();
        categoryInfos.addAll(categoryList);
        adapter.notifyDataSetChanged();
        gvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.iv_pop:
                popupWindow.showAtLocation(iv_pop, Gravity.TOP, 0, 0);
                break;
            default:
                break;
        }
    }


    private void createPopMore() {
        View popView = LayoutInflater.from(mActivity).inflate(R.layout.pop_classify, null);
        GridView gv_classify = (GridView) popView.findViewById(R.id.gv_classify);
        ImageView pop_iv_search = (ImageView) popView.findViewById(R.id.iv_search);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);

        gvAdapter = new ClassifyGvAdapter(getActivity(), categoryInfos);
        pop_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        gvAdapter.setOnItemClickListener(new ClassifyGvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                vp_classify.setCurrentItem(position, false);
                if(popupWindow != null || popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        });
        gv_classify.setAdapter(gvAdapter);
    }

}
