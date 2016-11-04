package com.cqfrozen.jsh.appraise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.HomeActivity;
import com.cqfrozen.jsh.entity.AppraiseInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 * intent.putExtra("o_id", o_id);
 */
public class AppraiseActivity extends MyActivity implements View.OnClickListener {

    private String o_id;
    private ListView lv_appraise;
    private CheckBox cb_anony;
    private TextView tv_submit_appraise;
    private List<AppraiseInfo> appraiseInfos = new ArrayList<>();
    private List<AppraiseItemData> appraiseItemDatas;
    private AppraiseAdapter appraiseAdapter;
    private String star_count_list = "";
    private String order_info_id_list = "";
    private String goods_id_list = "";
    private String content_list = "";
    private int is_anonymou = 1;//是否匿名评价  1.匿名 0不匿名

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise);
        getIntentData();
        initView();
        initLv();
        getData();
    }

    private void getIntentData() {
        o_id = getIntent().getStringExtra("o_id");
    }

    private void initView() {
        setMyTitle("商品评价");
        lv_appraise = (ListView) findViewById(R.id.lv_appraise);
        cb_anony = (CheckBox) findViewById(R.id.cb_anony);
        tv_submit_appraise = (TextView) findViewById(R.id.tv_submit_appraise);
        tv_submit_appraise.setOnClickListener(this);
    }

    private void initLv() {
        lv_appraise.setOverScrollMode(View.OVER_SCROLL_NEVER);
        appraiseAdapter = new AppraiseAdapter(this, appraiseInfos);
        lv_appraise.setAdapter(appraiseAdapter);
        appraiseAdapter.setOnItemEditChangeListener(new AppraiseAdapter.OnItemEditChangeListener() {
            @Override
            public void editChange(Editable s, int position, AppraiseInfo appraiseInfo) {
                appraiseItemDatas.get(position).content = s.toString();
            }
        });

        appraiseAdapter.setOnItemRatingBarChangeListener(new AppraiseAdapter
                .OnItemRatingBarChangeListener() {

            @Override
            public void ratingBarChange(RatingBar ratingBar, float rating, boolean fromUser, int
                    position, AppraiseInfo appraiseInfo) {
                appraiseItemDatas.get(position).star_count = (int)(rating + 0.5);
            }
        });
    }

    private void getData() {
        MyHttp.pj(http, null, o_id, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
//                    showToast(msg);
                    return;
                }
                appraiseInfos.addAll((List<AppraiseInfo>) bean);
                appraiseItemDatas = new ArrayList<AppraiseItemData>(appraiseInfos.size());
                for (int i = 0; i < appraiseInfos.size(); i++) {
                    AppraiseItemData appraiseItemData = new AppraiseItemData();
                    appraiseItemData.goods_id = appraiseInfos.get(i).g_id;
                    appraiseItemData.order_info_id = appraiseInfos.get(i).order_info_id;
                    appraiseItemData.star_count = 5;
                    appraiseItemData.content = "";
                    appraiseItemDatas.add(appraiseItemData);
                }
                appraiseAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit_appraise://提交评价
                submitAppraise();
                break;
            default:
                break;
        }
    }

    private void submitAppraise() {
        //先构建服务器需要的数据
        parseData();
        is_anonymou = cb_anony.isChecked() ? 1 : 0;
        tv_submit_appraise.setEnabled(false);
        MyHttp.addPJ(http, null, o_id, star_count_list, order_info_id_list,
                goods_id_list, content_list, is_anonymou, new HttpForVolley.HttpTodo() {
                    @Override
                    public void httpTodo(Integer which, JSONObject response) {
                        tv_submit_appraise.setEnabled(true);
                        showToast(response.optString("msg"));
                        int code = response.optInt("code");
                        if(code != 0){
                            return;
                        }
//                        finish();
                        HomeActivity.startActivity(AppraiseActivity.this, HomeActivity.PAGE_CLASSIFY);
                    }
                });
    }

    private void parseData() {
        star_count_list = "";
        order_info_id_list = "";
        goods_id_list = "";
        content_list = "";
        for (int i = 0; i < appraiseItemDatas.size(); i++) {
            AppraiseItemData appraiseItemData = appraiseItemDatas.get(i);
            star_count_list += appraiseItemData.star_count + ",";
            order_info_id_list += appraiseItemData.order_info_id + ",";
            goods_id_list += appraiseItemData.goods_id + ",";
            content_list += appraiseItemData.content + ",";
        }

        if(star_count_list.endsWith(",")){
            star_count_list = star_count_list.substring(0, star_count_list.length() - 1);
        }
        if(order_info_id_list.endsWith(",")){
            order_info_id_list = order_info_id_list.substring(0, order_info_id_list.length() - 1);
        }
        if(goods_id_list.endsWith(",")){
            goods_id_list = goods_id_list.substring(0, goods_id_list.length() - 1);
        }
        if(content_list.endsWith(",")){
            content_list = content_list.substring(0, content_list.length() - 1);
        }

        Log.d("appraiseItemDatas", "star_count_list:" + star_count_list + ":" +
                "order_info_id_list:" + order_info_id_list + ":" +
                "goods_id_list:" + goods_id_list + ":" +
                "content_list:" + content_list);
    }
}
