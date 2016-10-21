package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.common.base.BaseValue;
import com.common.http.HttpForVolley;
import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.AreaInfo;
import com.cqfrozen.jsh.entity.AreaStreetInfo;
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
 * Created by Administrator on 2016/10/21.
 */
public class ShopAddActivity extends MyActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopadd);
        initView();
        getLocationData();
    }

    private void initView() {
        setMyTitle("新增店铺");
        et_name = (MyEditText) findViewById(R.id.et_name);
        et_phone = (MyEditText) findViewById(R.id.et_phone);
        tv_location = (TextView) findViewById(R.id.tv_location);
        et_shop = (MyEditText) findViewById(R.id.et_shop);
        et_address = (MyEditText) findViewById(R.id.et_address);
        btn_save = (Button) findViewById(R.id.btn_save);

        tv_location.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        streetOptionsPV = new OptionsPickerView(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_location://弹出区域选择列表
                streetOptionsPV.show();
                break;
            case R.id.btn_save:
                saveShop();
                break;
            default:
                break;
        }
    }

    private void saveShop() {
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

        MyHttp.addStore(http, null, nameStr, phoneStr, street_id, area_id, shopStr, addressStr, new HttpForVolley.HttpTodo() {

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
