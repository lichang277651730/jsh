package com.cqfrozen.jsh.util;

import android.content.Context;

import com.bigkoo.pickerview.OptionsPickerView;
import com.cqfrozen.jsh.entity.AreaInfo;
import com.cqfrozen.jsh.entity.StreetInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/20.
 */
public class PickView {

    private static PickView instance;
    //  区域
    private ArrayList<AreaInfo> areaInfos = new ArrayList<>();
    //  街道
    private ArrayList<StreetInfo> streetInfos;
    private ArrayList<ArrayList<StreetInfo>> streestinfoList = new ArrayList<>();
    private OptionsPickerView optionsPV;

    private String street_id;
    private String area_id;

    private PickView(Context context){
        optionsPV = new OptionsPickerView(context);
    }

    public static PickView getInstance(Context context){
        if(instance == null){
            synchronized (PickView.class){
                instance = new PickView(context);
            }
        }
        return instance;
    }

    public void initOptionPV(){
        optionsPV.setPicker(areaInfos, streestinfoList, true);
        //  设置是否循环滚动
        optionsPV.setCyclic(false);
        // 设置默认选中的三级项目
        optionsPV.setSelectOptions(0, 0);
        optionsPV.setOnoptionsSelectListener(new OptionsPickerView
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
