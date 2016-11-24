package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.AreaInfo;
import com.cqfrozen.jsh.entity.AreaStreetInfo;
import com.cqfrozen.jsh.entity.ShopInfo;
import com.cqfrozen.jsh.entity.StreetInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.util.HiddenInputUtil;
import com.cqfrozen.jsh.util.ValidatorUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 *  intent.putExtra("addressInfo", shopInfo);
 */
public class ShopEditActivity extends MyActivity implements View.OnClickListener {

    private MyEditText et_name;
    private MyEditText et_phone;
    private TextView tv_location;
    private MyEditText et_shop;
    private MyEditText et_address;
    private Button btn_save;

    private List<AreaStreetInfo> locationInfos = new ArrayList<>();
    //  区域
    private ArrayList<AreaInfo> areaInfos = new ArrayList<>();
    //  街道
    private ArrayList<StreetInfo> streetInfos;
    private ArrayList<ArrayList<StreetInfo>> streestinfoList = new ArrayList<>();
    private OptionsPickerView streetOptionsPV;


    private String street_id;
    private String area_id;
    private ShopInfo shopInfo;
    private String s_id;
    private LinearLayout ll_shop_edit_root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopadd);
        getIntentData();
        initView();
        getLocationData();
    }

    private void getIntentData() {
        shopInfo = (ShopInfo) getIntent().getSerializableExtra("shopInfo");
        s_id = shopInfo.s_id;
        street_id = shopInfo.st_id;
        area_id = shopInfo.area_id;
    }

    private void initView() {
        setMyTitle("修改店铺");
        ll_shop_edit_root = (LinearLayout) findViewById(R.id.ll_shop_edit_root);
        et_name = (MyEditText) findViewById(R.id.et_name);
        et_phone = (MyEditText) findViewById(R.id.et_phone);
        tv_location = (TextView) findViewById(R.id.tv_location);
        et_shop = (MyEditText) findViewById(R.id.et_shop);
        et_address = (MyEditText) findViewById(R.id.et_address);
        btn_save = (Button) findViewById(R.id.btn_save);

        tv_location.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        ll_shop_edit_root.setOnClickListener(this);

        streetOptionsPV = new OptionsPickerView(this);
        streetOptionsPV.setCancelable(true);

        initData();
        streetOptionsPV.setOnoptionsSelectListener(new OptionsPickerView
                .OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String address = "";
                if(streestinfoList.get(options1) == null || streestinfoList.get(options1).size() == 0){
                    streestinfoList.get(options1).add(new StreetInfo("", ""));
                    address = areaInfos.get(options1).getPickerViewText();
                    street_id = "";
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

    /**
     * 初始化修改店铺控件内容
     */
    private void initData() {
        if(shopInfo != null){
            et_name.setText(TextUtils.isEmpty(shopInfo.china_name) ? "" : shopInfo.china_name);
            et_name.setSelection(et_name.getText().toString().length());
            et_phone.setText(TextUtils.isEmpty(shopInfo.mobile_num) ? "" : shopInfo.mobile_num);
            et_address.setText(TextUtils.isEmpty(shopInfo.address) ? "" : shopInfo.address);
            et_shop.setText(TextUtils.isEmpty(shopInfo.store_name) ? "" : shopInfo.store_name);
            String areaName = TextUtils.isEmpty(shopInfo.area_name) ? "" : shopInfo.area_name;
            String streetName = TextUtils.isEmpty(shopInfo.street_name) ? "" : shopInfo.street_name;
            tv_location.setText(areaName + " " + streetName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_location://弹出区域选择列表
                streetOptionsPV.show();
                hideSoftInput(v);
                break;
            case R.id.btn_save:
                editShop();
                break;
            case R.id.ll_shop_edit_root:
                hideSoftInput(v);
                break;
            default:
                break;
        }
    }

    private void hideSoftInput(View v) {
        HiddenInputUtil.hiddenSoftInput(this, v);
    }

    private void editShop() {
        String nameStr = et_name.getText().toString().trim();
        String phoneStr = et_phone.getText().toString().trim();
        String locationStr = tv_location.getText().toString().trim();
        String shopStr = et_shop.getText().toString().trim();
        String addressStr = et_address.getText().toString().trim();
        if (TextUtils.isEmpty(nameStr)) {
            showToast("店长名不能为空");
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
        if(!TextUtils.isEmpty(locationStr) && TextUtils.isEmpty(street_id)){
            showToast("此区域暂不支持，请重新选择");
            return;
        }
        if (TextUtils.isEmpty(shopStr)) {
            showToast("请输入店铺名");
            return;
        }
        if (TextUtils.isEmpty(addressStr)) {
            showToast("店铺地址不能为空");
            return;
        }

        if(TextUtils.isEmpty(s_id)){
            showToast("请重新选择要编辑店铺");
            return;
        }

        MyHttp.updateStore(http, null, s_id, nameStr, phoneStr, street_id, area_id, shopStr, addressStr, new HttpForVolley.HttpTodo() {

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
