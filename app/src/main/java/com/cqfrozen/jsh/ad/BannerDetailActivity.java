package com.cqfrozen.jsh.ad;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.AdDetailResultInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.CustomMiddleToast;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.cqfrozen.jsh.widget.AdTitleView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */
public class BannerDetailActivity extends MyActivity implements View.OnClickListener {

    private Button btn_get_huibi;
    private PopupWindow popupWindow;
    private WebView webview_ad;
    private String url;
    private String ad_id;
    private TextView tv_area;
    private TextView tv_rule;
    private TextView tv_price;
    private TextView tv_num;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9;
    private TextView tv_input;
    private TextView tv_clear;
    private TextView tv_return;
    private String inputStr = "";
    private TextView tv_confirm;
    private TextView tv_skip;
    private LinearLayout ll_root;
    private LinearLayout ll_ad_root;
    private AdTitleView ad_title_view;
    private TextView[] tvs;
    private List<TextView> tvs_selected = new ArrayList<>();
    private String hanziStr = "";
    private String china_keyword;
    private int keywordLength;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_detail);
        getIntentData();
        initView();
        initWebView();
        getData();
    }

    private void getIntentData() {
        ad_id = getIntent().getStringExtra("ad_id");
    }

    private void initView() {
        setMyTitle("广告详情");
        btn_get_huibi = (Button) findViewById(R.id.btn_get_huibi);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_rule = (TextView) findViewById(R.id.tv_rule);
        webview_ad = (WebView) findViewById(R.id.webview_ad);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_num = (TextView) findViewById(R.id.tv_num);
        btn_get_huibi.setOnClickListener(this);
        createAdPop();
    }

    private void initWebView() {
        WebSettings settings = webview_ad.getSettings();
        settings.setJavaScriptEnabled(true);
//        webview_ad.loadData(url, "text/html;charset=utf-8", null);
    }

    private void createAdPop() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_ad_select, null);
        ll_root = (LinearLayout) popView.findViewById(R.id.ll_root);
        ad_title_view = (AdTitleView) popView.findViewById(R.id.ad_title_view);
        ll_ad_root = (LinearLayout) popView.findViewById(R.id.ll_ad_root);
        tv_input = (TextView) popView.findViewById(R.id.tv_input);
        tv_clear = (TextView) popView.findViewById(R.id.tv_clear);
        tv_return = (TextView) popView.findViewById(R.id.tv_return);
        tv_confirm = (TextView) popView.findViewById(R.id.tv_confirm);
        tv_skip = (TextView) popView.findViewById(R.id.tv_skip);
        tv1 = (TextView) popView.findViewById(R.id.tv1);
        tv2 = (TextView) popView.findViewById(R.id.tv2);
        tv3 = (TextView) popView.findViewById(R.id.tv3);
        tv4 = (TextView) popView.findViewById(R.id.tv4);
        tv5 = (TextView) popView.findViewById(R.id.tv5);
        tv6 = (TextView) popView.findViewById(R.id.tv6);
        tv7 = (TextView) popView.findViewById(R.id.tv7);
        tv8 = (TextView) popView.findViewById(R.id.tv8);
        tv9 = (TextView) popView.findViewById(R.id.tv9);
        tvs = new TextView[]{tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9};
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);
        tv9.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        tv_return.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        tv_skip.setOnClickListener(this);
        ll_root.setOnClickListener(this);
        ll_ad_root.setOnClickListener(this);
    }

    private void getData() {
        MyHttp.adInfo(http, null, ad_id, new MyHttp.MyHttpResult() {

            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    return;
                }
                AdDetailResultInfo detailResultInfo = (AdDetailResultInfo) bean;
                china_keyword = detailResultInfo.data1.china_keyword;
                initUI(detailResultInfo);
            }
        });
    }

    private void initUI(AdDetailResultInfo detailResultInfo) {
        AdDetailResultInfo.AdDetailInfo adDetailInfo = detailResultInfo.data1;
        webview_ad.loadUrl(adDetailInfo.content);
        tv_area.setText(adDetailInfo.area_name);
        tv_price.setText(adDetailInfo.hb_count + "粮票/次");
        tv_num.setText(adDetailInfo.ad_count + "次");
        setKeyTvs(adDetailInfo.choose_keyword);
        addPopTitleView(adDetailInfo);//添加pop窗口的标题组件
    }

    private void addPopTitleView(AdDetailResultInfo.AdDetailInfo adDetailInfo) {
        String[] hanziTitleAry = adDetailInfo.china_keyword.split(",");
        keywordLength = hanziTitleAry.length;
        for(int i = 0; i < hanziTitleAry.length; i++){
            hanziStr += hanziTitleAry[i] + "";
        }
        String[] pinyinTitleAry = adDetailInfo.py_keyword.split(",");
        ad_title_view.setData(hanziTitleAry, pinyinTitleAry);
    }

    private void setKeyTvs(String choose_keyword) {
        String[] chooseKeywordArray = choose_keyword.split(",");
        if(chooseKeywordArray.length != 9){
            return;
        }

        for(int i = 0; i < chooseKeywordArray.length; i++){
            tvs[i].setText(chooseKeywordArray[i]);
            tvs[i].setTag(chooseKeywordArray[i]);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_huibi:
                popupWindow.showAtLocation(btn_get_huibi, Gravity.CENTER, 0, 0);
                break;
            case R.id.tv1:
            case R.id.tv2:
            case R.id.tv3:
            case R.id.tv4:
            case R.id.tv5:
            case R.id.tv6:
            case R.id.tv7:
            case R.id.tv8:
            case R.id.tv9:
                if(tvs_selected.contains((TextView)view)){
                    return;
                }
                if(inputStr.length() >= keywordLength){
                    CustomMiddleToast.getInstance(this).showToast("核心广告词已选满");
                    return;
                }
                inputStr += (String)view.getTag();
                ((TextView)view).setTextColor(getResources().getColor(R.color.main));
                tv_input.setText(inputStr);
                tvs_selected.add((TextView)view);
                break;
            case R.id.tv_clear:
                for(int i = 0; i < tvs.length; i++){
                    tvs[i].setTextColor(getResources().getColor(R.color.six_nine));
                }
                if(inputStr.length() <= 0){
                    return;
                }
                inputStr="";
                tv_input.setText(inputStr);
                tvs_selected.clear();
                break;
            case R.id.tv_return:
                if(inputStr.length() <= 0 || tvs_selected.size() == 0){
                    return;
                }
                inputStr = inputStr.substring(0, inputStr.length() - 1);
                tv_input.setText(inputStr);
                clearTvColor();
                for(int i = 0; i < inputStr.length(); i++){
                    String zi = inputStr.charAt(i) + "";
                    for(int j = 0; j < tvs.length; j++){
                        if(zi.equals(tvs[j].getText())){
                            tvs[j].setTextColor(getResources().getColor(R.color.main));
                        }
                    }
                }
                tvs_selected.remove(tvs_selected.size() - 1);
                break;
            case R.id.tv_confirm://选好广告词，点击确定
                confirm();
                break;
            case R.id.tv_skip:
                finish();
                if(popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            case R.id.ll_root:
                if(popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            case R.id.ll_ad_root:
                break;
            default:
                break;
        }
    }

    private void clearTvColor(){
        for(int i = 0; i < tvs.length; i++){
            tvs[i].setTextColor(getResources().getColor(R.color.six_nine));
        }
    }

    private void confirm() {
        if(!hanziStr.equals(inputStr)){
            CustomMiddleToast.getInstance(this).showToast("核心广告词选取有误");
            return;
        }
        tv_confirm.setEnabled(false);
        MyHttp.adGetHB(http, null, ad_id, china_keyword, new HttpForVolley.HttpTodo() {
            @Override
            public void httpTodo(Integer which, JSONObject response) {
                tv_confirm.setEnabled(true);
                int code = response.optInt("code");
                showToast(response.optString("msg"));
                if(code != 0){
                    return;
                }
                if(popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        });
    }
}
