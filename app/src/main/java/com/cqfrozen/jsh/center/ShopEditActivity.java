package com.cqfrozen.jsh.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.AreaInfo;
import com.cqfrozen.jsh.entity.AreaStreetInfo;
import com.cqfrozen.jsh.entity.StreetInfo;
import com.cqfrozen.jsh.main.MyActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class ShopEditActivity extends MyActivity {

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
    }

    private void initView() {
        setMyTitle("新增店铺");
        et_name = (MyEditText) findViewById(R.id.et_name);
        et_phone = (MyEditText) findViewById(R.id.et_phone);
        tv_location = (TextView) findViewById(R.id.tv_location);
        et_shop = (MyEditText) findViewById(R.id.et_shop);
        et_address = (MyEditText) findViewById(R.id.et_address);
        btn_save = (Button) findViewById(R.id.btn_save);

//        tv_location.setOnClickListener(this);
//        btn_save.setOnClickListener(this);
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
}
