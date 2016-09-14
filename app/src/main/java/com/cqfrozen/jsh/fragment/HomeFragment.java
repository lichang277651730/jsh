package com.cqfrozen.jsh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.HomeAdapter;
import com.cqfrozen.jsh.base.BaseFragment;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
import com.cqfrozen.jsh.http.MyHttp;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 * 主页面 fragment
 */
public class HomeFragment extends BaseFragment implements MyHttp.MyHttpResult, ViewPagerEx
        .OnPageChangeListener, BaseSliderView.OnSliderClickListener {

    private static HomeFragment fragment;
    private SliderLayout slider;
    private PagerIndicator custom_indicator;

    private List<HomeBannerInfo> bannerInfos = new ArrayList<>();
//    private MyToolbar toolbar;
//    private RefreshLayout refresh_home;
//    private RecyclerView rv_home;
//    private HomeAdapter homeAdapter;

    public static HomeFragment getInstance(){
        if(fragment == null){
            fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_home2, null);
            initView();
            initToolbar();
            initRV();
            getData();
        }
        return view;
    }

    //初始化控件
    private void initView() {
//        toolbar = (MyToolbar) view.findViewById(R.id.toolbar);
//        refresh_home = (RefreshLayout) view.findViewById(R.id.refresh_home);
//        rv_home = (RecyclerView) view.findViewById(R.id.rv_home);
        slider = (SliderLayout) view.findViewById(R.id.slider);
        custom_indicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);
    }

    //初始化Toolbar
    private void initToolbar() {

    }


    //初始化RecycleView
    private void initRV() {
//        HomeBannerAdapter homeBannerAdapter = new HomeBannerAdapter(mActivity, bannerInfos);
//        rv_home.setOverScrollMode(View.OVER_SCROLL_NEVER);
//        GridLayoutManager manager = new GridLayoutManager(mActivity, 10);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return 10;
//            }
//        });
//        rv_home.setLayoutManager(manager);
//        homeAdapter = new HomeAdapter(homeBannerAdapter);
//        rv_home.setAdapter(homeAdapter);
    }

    //获取数据
    private void getData() {
        MyHttp.homeBanner(http, HomeAdapter.TYPE_BANNER, this);
        slider.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        slider.setCustomIndicator(custom_indicator);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        slider.addOnPageChangeListener(this);
    }

    @Override
    public void httpResult(Integer which, int code, String msg, Object bean) {
        if(code != 0){
            showToast(msg);
            return;
        }

        switch (which) {
            case HomeAdapter.TYPE_BANNER:
                bannerInfos.clear();
                bannerInfos.addAll((List<HomeBannerInfo>) bean);
                if (bannerInfos.size() == 0) {
                    return;
                }
                setSlider();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        if(slider != null){
            slider.stopAutoCycle();
        }
        super.onStop();
    }

    @Override
    public void onShow() {
        super.onShow();
        if(slider != null){
            slider.startAutoCycle();
        }
    }

    private void setSlider() {
        if(bannerInfos != null){
            for(HomeBannerInfo bannerInfo : bannerInfos){
                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView
                        .description(bannerInfo.g_name)
                        .image(bannerInfo.pic_url)
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", bannerInfo.g_name);
                slider.addSlider(textSliderView);
            }
//            slider.setCustomAnimation(new DescriptionAnimation());
//            slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        showToast(slider.getBundle().get("extra") + "");
    }
}
