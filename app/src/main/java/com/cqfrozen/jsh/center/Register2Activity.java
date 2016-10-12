package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.common.http.HttpForVolley;
import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/12.
 *  intent.putExtra("phoneStr", phoneStr);
 * intent.putExtra("verifyCodeStr", verifyCodeStr);
 * intent.putExtra("pwdOnceStr", pwdOnceStr);
 */
public class Register2Activity extends MyActivity implements View.OnClickListener {

    private static final int TAG_ALLOW_YES = 1;
    private static final int TAG_ALLOW_NO = 2;

    private String phoneStr;
    private String verifyCodeStr;
    private String pwdOnceStr;
    private MyEditText et_shop_name;
    private MyEditText et_shop_manager;
    private MyEditText et_location;
    private MyEditText et_address;
    private TextView tv_location;
    private ImageView iv_allow;
    private TextView tv_allow;
    private Button btn_register;
    private String adCodeStr;
    private String shopNameStr;
    private String shopManStr;
    private String addressStr;
    private String locationStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        getIntentData();
        initView();
    }

    private void getIntentData() {
        phoneStr = getIntent().getStringExtra("phoneStr");
        verifyCodeStr = getIntent().getStringExtra("verifyCodeStr");
        pwdOnceStr = getIntent().getStringExtra("pwdOnceStr");
    }

    private void initView() {
        setMyTitle("新用户注册");
        et_shop_name = (MyEditText) findViewById(R.id.et_shop_name);
        et_shop_manager = (MyEditText) findViewById(R.id.et_shop_manager);
        et_location = (MyEditText) findViewById(R.id.et_location);
        et_address = (MyEditText) findViewById(R.id.et_address);
        tv_location = (TextView) findViewById(R.id.tv_location);
        iv_allow = (ImageView) findViewById(R.id.iv_allow);
        tv_allow = (TextView) findViewById(R.id.tv_allow);
        btn_register = (Button) findViewById(R.id.btn_register);


        //初始化定位
        MyApplication.locationClient.startLocation();
        AMapLocation mapLocation = MyApplication.locationClient.getLastKnownLocation();
        String locationStr = mapLocation.getDistrict();
        adCodeStr = mapLocation.getAdCode();
        et_location.setText(locationStr);

        tv_location.setOnClickListener(this);
        tv_allow.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        initAllow();
    }

    private void initAllow() {
        iv_allow.setTag(TAG_ALLOW_YES);
        iv_allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curTag = (int) v.getTag();
                if(curTag == TAG_ALLOW_YES){//当前是同意，点击变不同意
                    iv_allow.setImageResource(R.mipmap.icon_register_allow_not);
                    tv_allow.setTextColor(getResources().getColor(R.color.mygray));
                    btn_register.setEnabled(false);
                    iv_allow.setTag(TAG_ALLOW_NO);
                    btn_register.setBackgroundResource(R.drawable.shape_no_press_btn_bg);
                }else if(curTag == TAG_ALLOW_NO){//当前是不同意，点击变同意
                    iv_allow.setImageResource(R.mipmap.icon_register_allow_yes);
                    tv_allow.setTextColor(getResources().getColor(R.color.myblack));
                    btn_register.setEnabled(true);
                    iv_allow.setTag(TAG_ALLOW_YES);
                    btn_register.setBackgroundResource(R.drawable.sl_blue2gray_btn_bg);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                register();
                break;
            case R.id.tv_location:
                break;
            case R.id.tv_allow:
                break;
            default:
                break;
        }
    }

    private void register() {
        shopNameStr = et_shop_name.getText().toString().trim();
        shopManStr = et_shop_manager.getText().toString().trim();
        locationStr = et_location.getText().toString().trim();
        addressStr = et_address.getText().toString().trim();
        if(TextUtils.isEmpty(phoneStr) || TextUtils.isEmpty(verifyCodeStr) || TextUtils.isEmpty(pwdOnceStr)){
            showToast("上一步注册数据出错");
            return;
        }

        if(TextUtils.isEmpty(shopNameStr) || shopNameStr.length() > 30){
            showToast("店名不正确");
            return;
        }

        if(TextUtils.isEmpty(shopManStr) || shopManStr.length() > 10){
            showToast("姓名不正确");
            return;
        }

        if(TextUtils.isEmpty(adCodeStr) || TextUtils.isEmpty(locationStr)){
            showToast("定位信息有误，请手动定位");
            return;
        }

        if(TextUtils.isEmpty(addressStr)){
            showToast("请填写收货地址");
            return;
        }

        MyHttp.register(http, null, phoneStr, pwdOnceStr, shopNameStr, shopNameStr, adCodeStr, addressStr,
                verifyCodeStr, new HttpForVolley.HttpTodo() {
                    @Override
                    public void httpTodo(Integer which, JSONObject response) {
                        showToast(response.optString("msg"));
                        int code = response.optInt("code");
                        if(code != 0){
                            return;
                        }
                    }
                });
    }
}
