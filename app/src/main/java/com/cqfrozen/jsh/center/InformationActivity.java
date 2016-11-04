package com.cqfrozen.jsh.center;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.common.util.PhotoPopupWindow;
import com.common.util.PhotoUtil;
import com.common.widget.MyHeadImageView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/9/29.
 */
public class InformationActivity extends MyActivity implements View.OnClickListener {

    private LinearLayout ll_head;
    private MyHeadImageView iv_head;
    private LinearLayout ll_name;
    private LinearLayout ll_nickname;
    private LinearLayout ll_sex;
    private TextView tv_sex;
    private TextView tv_nickname;
    private TextView tv_name;
    private AlertDialog dialog;
    private PhotoPopupWindow photoPopupWindow;
    private PhotoUtil photoUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initView();
    }

    private void initView() {
        setMyTitle("个人资料");
        ll_head = (LinearLayout) findViewById(R.id.ll_head);
        iv_head = (MyHeadImageView) findViewById(R.id.iv_head);
        ll_name = (LinearLayout) findViewById(R.id.ll_name);
        ll_nickname = (LinearLayout) findViewById(R.id.ll_nickname);
        ll_sex = (LinearLayout) findViewById(R.id.ll_sex);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_name = (TextView) findViewById(R.id.tv_name);

        ll_head.setOnClickListener(this);
        ll_sex.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_head:
                photoUtil = new PhotoUtil(this, 3, 3);
                photoPopupWindow = new PhotoPopupWindow(this, photoUtil);
                photoPopupWindow.showpop(v);
                break;
            case R.id.ll_sex:
                showSexDialog();
                break;
            case R.id.tv_man:
                setSex(1);
                break;
            case R.id.tv_woman:
                setSex(2);
                break;
            case R.id.tv_secret:
                setSex(0);
                break;
            default:
                break;
        }
    }

    private void setSex(int sex) {
        dialog.dismiss();
        //TODO 请求接口改变性别
        showToast("改变性别");
    }

    private void showSexDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_sex, null);
        TextView tv_man = (TextView) view.findViewById(R.id.tv_man);
        TextView tv_woman = (TextView) view.findViewById(R.id.tv_woman);
        TextView tv_secret = (TextView) view.findViewById(R.id.tv_secret);
        tv_man.setOnClickListener(this);
        tv_woman.setOnClickListener(this);
        tv_secret.setOnClickListener(this);
        dialog = new AlertDialog.Builder(this).create();
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtil.FromWhere.camera:
            case PhotoUtil.FromWhere.photo:
                photoUtil.onActivityResult(requestCode, resultCode, data);
            case PhotoUtil.FromWhere.forfex:
                if (resultCode == RESULT_OK) {
                    MyHttp.updateHead(http, null, photoUtil.getForfexPath(), new HttpForVolley
                            .HttpTodo() {
                        @Override
                        public void httpTodo(Integer which, JSONObject response) {
                            if (response.optInt("code",1)!=0){
//                                showToast("上传图片发生错误!");
                                return;
                            }
                            showToast("修改头像成功!");
                            String filename = response.optJSONObject("data").optString("head_url");
                            Log.d("headimgmsg", filename);
                            ImageLoader.getInstance().displayImage(filename, iv_head);
                            getUserInfo().head_url = filename;
                        }
                    });
                }
        }
    }
}
