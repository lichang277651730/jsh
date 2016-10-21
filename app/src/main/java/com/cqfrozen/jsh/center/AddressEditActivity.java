package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.AddressInfo;
import com.cqfrozen.jsh.entity.AreaInfo;
import com.cqfrozen.jsh.entity.AreaStreetInfo;
import com.cqfrozen.jsh.entity.ShopInfo;
import com.cqfrozen.jsh.entity.ShopPVInfo;
import com.cqfrozen.jsh.entity.StreetInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.ValidatorUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 * intent.putExtra("addressInfo", addressInfo);
 */
public class AddressEditActivity extends MyActivity implements View.OnClickListener {

    private MyEditText et_consignee;
    private MyEditText et_phone;
    private TextView tv_location;
    private MyEditText et_address;
    private CheckBox cb_default;
    private Button btn_save;
    private AddressInfo addressInfo;
    private List<AreaStreetInfo> locationInfos = new ArrayList<>();
    //  区域
    private ArrayList<AreaInfo> areaInfos = new ArrayList<>();
    //  街道
    private ArrayList<StreetInfo> streetInfos;
    private ArrayList<ArrayList<StreetInfo>> streestinfoList = new ArrayList<>();
    private OptionsPickerView streetOptionsPV;

    private String street_id;
    private String area_id;
    private int is_default = 0;
    private String s_id;
    private String a_id;//地址id

    private String consigneeStr;
    private String phoneStr;
    private String addresStr;
    private String locationStr;
    private OptionsPickerView shopOptionsPV;
    private TextView tv_shop;

    private List<ShopInfo> shopInfos = new ArrayList<>();
    private ArrayList<ShopPVInfo> shopPVInfos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressadd);
        getIntentData();
        initView();
        getLocationData();
        getShopData();
    }

    private void getIntentData() {
        addressInfo = (AddressInfo) getIntent().getSerializableExtra("addressInfo");
        s_id = addressInfo.s_id;
        street_id = addressInfo.st_id;
        area_id = addressInfo.area_id;
        a_id = addressInfo.a_id;
    }

    private void initView() {
        setMyTitle("修改地址");
        et_consignee = (MyEditText) findViewById(R.id.et_consignee);
        et_phone = (MyEditText) findViewById(R.id.et_phone);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_shop = (TextView) findViewById(R.id.tv_shop);
        et_address = (MyEditText) findViewById(R.id.et_address);
        cb_default = (CheckBox) findViewById(R.id.cb_default);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_location.setOnClickListener(this);
        tv_shop.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        initData();

        streetOptionsPV = new OptionsPickerView(this);
        shopOptionsPV = new OptionsPickerView(this);

        shopOptionsPV.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if(shopPVInfos.get(options1) != null){
                    String shop = shopPVInfos.get(options1).getPickerViewText();
                    s_id = shopPVInfos.get(options1).getS_id();
                    tv_shop.setText(shop);
                }
            }
        });

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


    }

    private void initData() {
        if(addressInfo != null){
            et_consignee.setText(TextUtils.isEmpty(addressInfo.china_name) ? "" : addressInfo.china_name);
            et_consignee.setSelection(et_consignee.getText().toString().length());
            et_phone.setText(TextUtils.isEmpty(addressInfo.mobile_num) ? "" : addressInfo.mobile_num);
            et_address.setText(TextUtils.isEmpty(addressInfo.address) ? "" : addressInfo.address);
            cb_default.setChecked(addressInfo.is_default == 1 ? true : false);
            String areaName = TextUtils.isEmpty(addressInfo.area_name) ? "" : addressInfo.area_name;
            String streetName = TextUtils.isEmpty(addressInfo.street_name) ? "" : addressInfo.street_name;
            tv_location.setText(areaName + " " + streetName);
            tv_shop.setText(TextUtils.isEmpty(addressInfo.store_name) ? "" : addressInfo.store_name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_location:
                streetOptionsPV.show();
                break;
            case R.id.tv_shop:
                shopOptionsPV.show();
                break;
            case R.id.btn_save:
                editAddress();
                break;
            default:
                break;
        }
    }
    private void editAddress() {
        consigneeStr = et_consignee.getText().toString().trim();
        phoneStr = et_phone.getText().toString().trim();
        locationStr = tv_location.getText().toString().trim();
        addresStr = et_address.getText().toString().trim();
        if (TextUtils.isEmpty(consigneeStr)) {
            showToast("收件人不能为空");
            return;
        }
        if (!ValidatorUtil.isMobile(phoneStr)) {
            showToast("请输入正确电话号码");
            return;
        }
        if (TextUtils.isEmpty(locationStr)) {
            showToast("请选择区域街道");
            return;
        }
        if (!TextUtils.isEmpty(locationStr) && TextUtils.isEmpty(street_id)) {
            showToast("此区域暂不支持，请重新选择");
            return;
        }
        if (TextUtils.isEmpty(s_id)) {
            showToast("门店信息有误");
            return;
        }
        if (TextUtils.isEmpty(addresStr)) {
            showToast("详细地址不能为空");
            return;
        }
        if (cb_default.isChecked()) {
            is_default = 1;
        } else {
            is_default = 0;
        }

        MyHttp.editrAddress(http, null, a_id, consigneeStr, phoneStr, addresStr, is_default, s_id,
                street_id, area_id, new HttpForVolley.HttpTodo() {

                    @Override
                    public void httpTodo(Integer which, JSONObject response) {

                        showToast(response.optString("msg"));
                        int code = response.optInt("code");
                        if (code != 0) {
                            return;
                        }
                        setResult(RESULT_OK);
                        finish();
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
                String location_json = BaseValue.gson.toJson(locationInfos);
                parseAreaStreet(location_json);

                streetOptionsPV.setPicker(areaInfos, streestinfoList, true);
                //  设置是否循环滚动
                streetOptionsPV.setCyclic(false);
                // 设置默认选中的三级项目
                streetOptionsPV.setSelectOptions(0, 0);
            }
        });
    }

    /**
     * 从服务器获取店铺信息
     */
    private void getShopData() {
        MyHttp.shopList(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                shopInfos.addAll((List<ShopInfo>)bean);
                if(shopInfos == null || shopInfos.size() == 0){
                    return;
                }
                String shop_json = BaseValue.gson.toJson(shopInfos);
                parseShop(shop_json);

                shopOptionsPV.setPicker(shopPVInfos);
                //  设置是否循环滚动
                shopOptionsPV.setCyclic(false);
                // 设置默认选中的三级项目
                shopOptionsPV.setSelectOptions(0);
            }
        });
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

    private void parseShop(String shop_json) {
        try {
            //  获取json中的数组
            JSONArray jsonArray = new JSONArray(shop_json);
            for(int i = 0; i < jsonArray.length(); i++){
                //  获取商铺的对象
                JSONObject shopObject = jsonArray.optJSONObject(i);

                String store_name = shopObject.getString("store_name");
                String s_id = shopObject.getString("s_id");
                shopPVInfos.add(new ShopPVInfo(store_name, s_id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
