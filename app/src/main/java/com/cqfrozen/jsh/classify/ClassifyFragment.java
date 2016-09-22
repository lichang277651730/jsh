package com.cqfrozen.jsh.classify;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.common.base.BaseFragment;
import com.common.base.BaseValue;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.SearchActivity;
import com.cqfrozen.jsh.entity.CategoryInfo;
import com.cqfrozen.jsh.util.UIUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
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
        if(view == null){
            view = inflater.inflate(R.layout.fragment_classify, null);
            initView();
            initVP();
            geData();
        }
        return view;
    }

    private void initView() {
        indicator_classify = (ScrollIndicatorView) view.findViewById(R.id.indicator_classify);
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        vp_classify = (ViewPager) view.findViewById(R.id.vp_classify);
        iv_search.setOnClickListener(this);
    }

    private void initVP() {
        vp_classify.setOverScrollMode(View.OVER_SCROLL_NEVER);
        indicator_classify.setScrollBar(new ColorBar(mActivity, UIUtils.getColor(R.color.main), BaseValue.dp2px(4)));
        // 设置滚动监听
        indicator_classify.setOnTransitionListener(new OnTransitionTextListener().setColor(UIUtils.getColor(R.color.main), Color.BLACK));
        vp_classify.setOffscreenPageLimit(2);
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(indicator_classify, vp_classify);
        indicatorViewPager.setClickIndicatorAnim(false);
        adapter = new ClassifyIndicatorAdapter
                (mActivity.getSupportFragmentManager(), mActivity, categoryInfos);
        indicatorViewPager.setAdapter(adapter);
    }

    private void geData() {
        MyHttp.goodsType(http, null, this);
    }

    @Override
    public void onShow() {
        super.onShow();
//        if(categoryInfos == null || categoryInfos.size() == 0){
//            geData();
//        }
        Log.d("FragmentShow", "ClassifyFragment" + BaseValue.density);
        Log.d("miduvalue", BaseValue.density + "");
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        if(code != 0){
            showToast(msg);
            return;
        }
        categoryInfos.addAll((List<CategoryInfo>)bean);
        if(categoryInfos.size() == 0){
            return;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            default:
                break;
        }
    }
}
