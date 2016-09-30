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
import com.cqfrozen.jsh.main.MyActivity;

/**
 * Created by Administrator on 2016/9/27.
 */
public class LocationActivity extends MyActivity {

    private TextView tv_cur_address;
    private AMapLocationClient locationClient;
    private ListView lv_location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        locationClient = new AMapLocationClient(getApplicationContext());
        initView();
        initLV();
    }

    private void initView() {
        setMyTitle("重庆");
        tv_cur_address = (TextView) findViewById(R.id.tv_cur_address);
        lv_location = (ListView) findViewById(R.id.lv_location);
        locationClient.startLocation();
        AMapLocation mapLocation = locationClient.getLastKnownLocation();
        String locationStr = mapLocation.getDistrict() + ":" + mapLocation.getAdCode();
        tv_cur_address.setText(locationStr);
    }


    private void initLV() {
        lv_location.setOverScrollMode(View.OVER_SCROLL_NEVER);
        LocationAdapter locationAdapter = new LocationAdapter(this);
        lv_location.setAdapter(locationAdapter);
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
