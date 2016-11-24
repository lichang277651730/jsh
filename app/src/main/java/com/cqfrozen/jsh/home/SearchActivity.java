package com.cqfrozen.jsh.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.widget.MyEditText;
import com.common.widget.MyTagView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.SearchKwdInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.CustomSimpleDialog;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 * 搜索页面
 */
public class SearchActivity extends MyActivity implements View.OnClickListener, MyTagView.OnTagClickListener, TextView.OnEditorActionListener {

    private MyTagView tag_hot;
    private MyTagView tag_history;
    private ImageView iv_back;
    private MyEditText et_keyword;
    private TextView tv_gosearch;
    private TextView tv_clear;
    private String[] historyAry;
    private List<SearchKwdInfo> searchKwdInfos = new ArrayList<>();
    private CustomSimpleDialog clearDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getHistorySearchKwd();
        initView();
        getHotKeyData();
    }

    private void getHistorySearchKwd() {
        historyAry = SPUtils.getSearchKwd();
    }

    private void initView() {
        tag_hot = (MyTagView) findViewById(R.id.tag_hot);
        tag_history = (MyTagView) findViewById(R.id.tag_history);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_keyword = (MyEditText) findViewById(R.id.et_keyword);
        tv_gosearch = (TextView) findViewById(R.id.tv_gosearch);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        iv_back.setOnClickListener(this);
        tv_gosearch.setOnClickListener(this);
        if(historyAry != null){
            tag_history.setMyTag(historyAry, R.drawable.shape_mytagview_fill_bg);
            tv_clear.setVisibility(View.VISIBLE);
        }else {
            tv_clear.setVisibility(View.GONE);
        }
        tag_hot.setOnTagClickListener(this);
        tag_history.setOnTagClickListener(this);
        tv_clear.setOnClickListener(this);
        et_keyword.setOnEditorActionListener(this);
        String serverSearchKwd = SPUtils.getServerSearchKwd();
        if(!TextUtils.isEmpty(serverSearchKwd)){
            List<SearchKwdInfo> kwds = BaseValue.gson.fromJson(serverSearchKwd, new
                    TypeToken<List<SearchKwdInfo>>() {
            }.getType());
            tag_hot.setMyTag(parseTags(kwds));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_gosearch://执行搜索
                search();
                break;
            case R.id.tv_clear://清楚搜索历史
                showClearDialog();
                break;
            default:
                break;
        }
    }

    private void getHotKeyData() {
        MyHttp.hotkw(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
//                    showToast(msg);
                    return;
                }
                searchKwdInfos.addAll((List<SearchKwdInfo>)bean);
                if(searchKwdInfos.size() == 0){
                    return;
                }
                String kwdJson = BaseValue.gson.toJson(searchKwdInfos);
                SPUtils.setServerSearchKwd(kwdJson);
                tag_hot.clearTag();
                tag_hot.setMyTag(parseTags(searchKwdInfos));

            }
        });
    }

    private List<String> parseTags(List<SearchKwdInfo> searchKwdInfos) {
        List<String> tagList = new ArrayList<>();
        for(int i = 0; i < searchKwdInfos.size(); i++){
            tagList.add(searchKwdInfos.get(i).key_words);
        }
        return tagList;
    }

    private void showClearDialog() {
        clearDialog = new CustomSimpleDialog.Builder(this)
                .setMessage("确定要清除搜索记录吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        SPUtils.clearSearchKwd();
                        tag_history.clearTag();
                        tv_clear.setVisibility(View.GONE);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                })
                .create();
        clearDialog.show();
    }

    @Override
    public void onTagClick(View v) {
        String tagStr = ((TextView) v).getText().toString().trim();
        et_keyword.setText(tagStr);
        et_keyword.setSelection(tagStr.length());
        search();
    }

    private void search() {
        String keywordStr = et_keyword.getText().toString().trim();
        if(TextUtils.isEmpty(keywordStr)){
            showToast("请输入要搜索内容");
            return;
        }
        if(tv_clear.getVisibility() == View.GONE){
            tv_clear.setVisibility(View.VISIBLE);
        }
        //跳至搜索结果页面
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("keyword", keywordStr);
        startActivity(intent);

        //存历史搜索缓存
        SPUtils.setSearchKwd(keywordStr);
        tag_history.setMyTag(SPUtils.getSearchKwd(), R.drawable.shape_mytagview_fill_bg);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            search();
        }
        return false;
    }
}
