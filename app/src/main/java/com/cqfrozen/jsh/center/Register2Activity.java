package com.cqfrozen.jsh.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.AreaInfo;
import com.cqfrozen.jsh.entity.AreaStreetInfo;
import com.cqfrozen.jsh.entity.StreetInfo;
import com.cqfrozen.jsh.entity.UserTypeInfo;
import com.cqfrozen.jsh.entity.UserTypePv;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.ShowHiddenPwdUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 *  intent.putExtra("phoneStr", phoneStr);
 * intent.putExtra("verifyCodeStr", verifyCodeStr);
 * intent.putExtra("pwdOnceStr", pwdOnceStr);
 * intent.putExtra("url", user_protocol_url);
 */
public class Register2Activity extends MyActivity implements View.OnClickListener {

    private static final int TAG_ALLOW_YES = 1;
    private static final int TAG_ALLOW_NO = 2;

    private String phoneStr;
    private String verifyCodeStr;
    private String pwdOnceStr;
    private MyEditText et_shop_name;
    private MyEditText et_shop_manager;
    private MyEditText et_address;
    private MyEditText et_invite;
    private TextView tv_location;
    private ImageView iv_allow;
    private TextView tv_allow;
    private Button btn_register;
    private String adCodeStr;
    private String shopNameStr;
    private String shopManStr;
    private String addressStr;
    private String inviteStr;
    private String locationStr;
    private OptionsPickerView streetOptionsPV;

    private String street_id;
    private String area_id;
    private String u_t_id;

    private List<AreaStreetInfo> locationInfos = new ArrayList<>();
    //  区域
    private ArrayList<AreaInfo> areaInfos = new ArrayList<>();
    //  街道
    private ArrayList<StreetInfo> streetInfos;
    private ArrayList<ArrayList<StreetInfo>> streestinfoList = new ArrayList<>();
    private TextView tv_user_type;
    private OptionsPickerView userTypeOptionsPV;

    //商户类型
    private ArrayList<UserTypeInfo> userTypeInfos = new ArrayList<>();
    private ArrayList<UserTypePv> userTypePvs = new ArrayList<>();
    private String userTypeStr;
    private String user_protocol_url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        getIntentData();
        initView();
        getLocationData();
    }

    private void getIntentData() {
        phoneStr = getIntent().getStringExtra("phoneStr");
        verifyCodeStr = getIntent().getStringExtra("verifyCodeStr");
        pwdOnceStr = getIntent().getStringExtra("pwdOnceStr");
        user_protocol_url = getIntent().getStringExtra("url");
    }

    private void initView() {
        setMyTitle("新用户注册");
        et_shop_name = (MyEditText) findViewById(R.id.et_shop_name);
        et_shop_manager = (MyEditText) findViewById(R.id.et_shop_manager);
        et_address = (MyEditText) findViewById(R.id.et_address);
        et_invite = (MyEditText) findViewById(R.id.et_invite);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_user_type = (TextView) findViewById(R.id.tv_user_type);
        iv_allow = (ImageView) findViewById(R.id.iv_allow);
        tv_allow = (TextView) findViewById(R.id.tv_allow);
        btn_register = (Button) findViewById(R.id.btn_register);

        streetOptionsPV = new OptionsPickerView(this);
        userTypeOptionsPV = new OptionsPickerView(this);

        tv_location.setOnClickListener(this);
        tv_user_type.setOnClickListener(this);
        tv_allow.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_allow.setOnClickListener(this);
        ShowHiddenPwdUtil.initAllow(iv_allow, tv_allow, btn_register);

        streetOptionsPV.setOnoptionsSelectListener(new OptionsPickerView
                .OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String address = "";
                if(streestinfoList.get(options1) == null || streestinfoList.get(options1).size() == 0){
                    streestinfoList.get(options1).add(new StreetInfo("", ""));
                    address = areaInfos.get(options1).getPickerViewText();
                }else {
                    address = areaInfos.get(options1).getPickerViewText()
                            + " " + streestinfoList.get(options1).get(option2).getPickerViewText();
                    street_id = streestinfoList.get(options1).get(option2).getSt_id();
                }
                area_id = areaInfos.get(options1).getArea_id();
                tv_location.setText(address);
            }
        });

        userTypeOptionsPV.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                u_t_id = userTypePvs.get(options1).getU_t_id();
                tv_user_type.setText(userTypePvs.get(options1).getUser_type_name());
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
                streetOptionsPV.show();
                break;
            case R.id.tv_user_type:
                userTypeOptionsPV.show();
                break;
            case R.id.tv_allow:
                Intent intent = new Intent(this, WebUrlActivity.class);
                intent.putExtra("title", "用户协议");
                intent.putExtra("url", user_protocol_url);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void register() {
        shopNameStr = et_shop_name.getText().toString().trim();
        shopManStr = et_shop_manager.getText().toString().trim();
        locationStr = tv_location.getText().toString().trim();
        userTypeStr = tv_user_type.getText().toString().trim();
        addressStr = et_address.getText().toString().trim();
        inviteStr = et_invite.getText().toString().trim();
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

        if(TextUtils.isEmpty(locationStr) || TextUtils.isEmpty(area_id) || TextUtils.isEmpty(street_id)){
            showToast("选择区域有误，请重新选择");
            return;
        }

        if(TextUtils.isEmpty(userTypeStr) || TextUtils.isEmpty(u_t_id)){
            showToast("请选择商户类型");
            return;
        }

        if(TextUtils.isEmpty(addressStr)){
            showToast("请填写收货地址");
            return;
        }

        MyHttp.register(http, null, phoneStr, pwdOnceStr, shopNameStr, shopManStr, area_id, street_id, u_t_id, addressStr,
                verifyCodeStr, TextUtils.isEmpty(inviteStr) ? "" : inviteStr, new HttpForVolley.HttpTodo() {
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

    /**
     * 从服务器获取区域街道信息
     */
    private void getLocationData() {
        MyHttp.searchStreet(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if (code != 0) {
                    return;
                }
                locationInfos.addAll((List<AreaStreetInfo>) bean);
                if(locationInfos.size() == 0){
                    return;
                }
                String location_json = BaseValue.gson.toJson(locationInfos);
                parseAreaStreet(location_json);

                streetOptionsPV.setPicker(areaInfos, streestinfoList, true);
                //  设置是否循环滚动
                streetOptionsPV.setCyclic(false);
                // 设置默认选中的三级项目
                streetOptionsPV.setSelectOptions(0, 0);
            }
        });

        MyHttp.userTypeList(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                userTypeInfos.addAll((List<UserTypeInfo>)bean);
                if(userTypeInfos.size() == 0){
                    return;
                }
                String user_type_json = BaseValue.gson.toJson(userTypeInfos);
                parseUserType(user_type_json);

                userTypeOptionsPV.setPicker(userTypePvs);
                //  设置是否循环滚动
                userTypeOptionsPV.setCyclic(false);
                // 设置默认选中的三级项目
                userTypeOptionsPV.setSelectOptions(0);
            }
        });
    }

    private void parseUserType(String user_type_json) {

        try {
            JSONArray ja = new JSONArray(user_type_json);
            for(int i = 0; i < ja.length(); i++){
                JSONObject jo = ja.optJSONObject(i);
                String u_t_id = jo.getString("u_t_id");
                String user_type_name = jo.getString("user_type_name");
                userTypePvs.add(new UserTypePv(u_t_id, user_type_name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseAreaStreet(String location_json) {
        try {
            //  获取json中的数组
            JSONArray jsonArray = new JSONArray(location_json);
            //  遍历数据组
            for (int i = 0; i < jsonArray.length(); i++) {
                //  获取省份的对象
                JSONObject areaObject = jsonArray.optJSONObject(i);
                //  获取省份名称放入集合
                String area_name = areaObject.getString("area_name");
                String area_id = areaObject.getString("area_id");
                areaInfos.add(new AreaInfo(area_name, area_id));
                //  获取城市数组
                JSONArray streetArray = areaObject.optJSONArray("street");
                streetInfos = new ArrayList<>();
                //  遍历城市数组
                for (int j = 0; j < streetArray.length(); j++) {

                    //  获取城市对象
                    JSONObject streetObject = streetArray.optJSONObject(j);
                    //  将城市放入集合
                    String street_name = streetObject.optString("street_name");
                    String st_id = streetObject.optString("st_id");
                    StreetInfo streetInfo = new StreetInfo(street_name, st_id);
                    streetInfos.add(streetInfo);

                }
                //  将存放城市的集合放入集合
                streestinfoList.add(streetInfos);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
