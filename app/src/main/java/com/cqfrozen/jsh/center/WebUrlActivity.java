package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;

import com.common.widget.MyWebView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/11/1.
 *  intent.putExtra("title", "售后规则");
 *  intent.putExtra("url", after_sale_url);
 */
public class WebUrlActivity extends MyActivity {

    private String title;
    private String url;
    private MyWebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weburl);
        getIntentData();
        initView();
        initWebView();
    }

    private void getIntentData() {
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
    }

    private void initView() {
        setMyTitle(title);
        webview = (MyWebView) findViewById(R.id.webview);
    }

    private void initWebView() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.loadUrl(url);
    }
}
