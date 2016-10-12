package com.cqfrozen.jsh.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.LocationAdapter;
import com.cqfrozen.jsh.entity.LocationInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class LocationActivity extends MyActivity {

    private TextView tv_cur_address;
    private AMapLocationClient locationClient;
    private ListView lv_location;
    private List<LocationInfo> locationInfos = new ArrayList<>();
    private LocationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initView();
        initLV();
        getData();
    }

    private void initView() {
        setMyTitle("重庆");
        tv_cur_address = (TextView) findViewById(R.id.tv_cur_address);
        lv_location = (ListView) findViewById(R.id.lv_location);
        MyApplication.locationClient.startLocation();
        AMapLocation mapLocation = MyApplication.locationClient.getLastKnownLocation();
        String locationStr = mapLocation.getDistrict();
        tv_cur_address.setText(locationStr);
    }


    private void initLV() {
        lv_location.setOverScrollMode(View.OVER_SCROLL_NEVER);
//        LocationAdapter adapter = new LocationAdapter(this);
        adapter = new LocationAdapter(this, locationInfos);
        lv_location.setAdapter(adapter);
    }

    private void getData() {
        MyHttp.areaList(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                locationInfos.clear();
                locationInfos.addAll((List<LocationInfo>)bean);
                if(locationInfos == null || locationInfos.size() == 0){
                    return;
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationClient != null){
            locationClient.onDestroy();
            locationClient = null;
        }
    }
}
