package com.cqfrozen.jsh.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/9/27.
 */
public class GoodsDetailActivity extends MyActivity {

    private WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetail);
        initView();
    }

    private void initView() {
        webview = (WebView) findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.loadUrl("");
    }
}
