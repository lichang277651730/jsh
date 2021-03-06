package com.cqfrozen.jsh.center;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.MyFansPageInfo;
import com.cqfrozen.jsh.fragment.FansFragment;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.share.SharePop;
import com.cqfrozen.jsh.util.QrCodeUtil;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/10/24.
 */
public class FansListActity extends MyActivity implements View.OnClickListener{

    private TextView tv_desc;
    private TextView tv_huibi_total;
    private TextView tv_fans_count;
    private TextView tv_send_invite;
    private TextView tv_face_to_face;
    private TextView tv_invite_code;
    private TextView tv_one_fans;
    private TextView tv_two_fans;

    private int level = 1;//1一级兄弟伙  2兄弟伙的兄弟伙
    private int is_page = 0;//1有下一页 0没有下一页
    private int page = 1;

    private String invite_http_url;//邀请链接
    private Bitmap invite_qr_bitmap;
    private PopupWindow popupWindow;
    private ImageView iv_qr_code;
    private View v_one_fans;
    private View v_two_fans;
    private MyFansPageInfo myFansPageInfo;
    private TextView tv_invite_code1;
    private LinearLayout ll_root;
    private FansFragment fansFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanslist);
        initView();
        getViewData();
        setFragment();
    }

    private void getViewData() {
        MyHttp.myFans(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code != 0){
                    showToast(msg);
                    return;
                }
                myFansPageInfo = (MyFansPageInfo) bean;
                if(myFansPageInfo == null){
                    return;
                }
                SPUtils.setFansListContent(myFansPageInfo.content);
                SPUtils.setInviteUrl(myFansPageInfo.http_url);
                initViewData(myFansPageInfo);
            }
        });
    }

    private void setFragment() {
        fansFragment = FansFragment.getInstance(level);
        showFragment(fansFragment);
    }

    private void showFragment(FansFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fl_fans_container, fragment);
        transaction.commit();
    }

    private void initViewData(MyFansPageInfo myFansPageInfo) {
        invite_http_url = myFansPageInfo.http_url;
        invite_qr_bitmap = QrCodeUtil.createImage(invite_http_url, BaseValue.dp2px(150), BaseValue
                .dp2px(150), null);
        if(iv_qr_code != null){
            iv_qr_code.setImageBitmap(invite_qr_bitmap);
        }
        if(tv_invite_code1 != null){
            tv_invite_code1.setText("我的邀请码:" + myFansPageInfo.code);
        }
        tv_desc.setText(myFansPageInfo.content);
        tv_huibi_total.setText("￥" + myFansPageInfo.hb_count);
        tv_fans_count.setText(myFansPageInfo.intotal_fans_count + "");
        tv_invite_code.setText("我的邀请码:" + myFansPageInfo.code);
        tv_one_fans.setText("兄弟伙(" + myFansPageInfo.one_fans_count + ")");
        tv_two_fans.setText("二级兄弟伙(" + myFansPageInfo.two_fans_count + ")");
        tv_one_fans.setTextColor(getResources().getColor(R.color.main));
    }

    private void initView() {
        setMyTitle("我的兄弟伙");
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_huibi_total = (TextView) findViewById(R.id.tv_huibi_total);
        tv_fans_count = (TextView) findViewById(R.id.tv_fans_count);
        tv_send_invite = (TextView) findViewById(R.id.tv_send_invite);
        tv_face_to_face = (TextView) findViewById(R.id.tv_face_to_face);
        tv_invite_code = (TextView) findViewById(R.id.tv_invite_code);
        tv_one_fans = (TextView) findViewById(R.id.tv_one_fans);
        tv_two_fans = (TextView) findViewById(R.id.tv_two_fans);
        v_one_fans = (View) findViewById(R.id.v_one_fans);
        v_two_fans = (View) findViewById(R.id.v_two_fans);

        v_one_fans.setVisibility(View.VISIBLE);
        tv_one_fans.setOnClickListener(this);
        tv_two_fans.setOnClickListener(this);
        tv_send_invite.setOnClickListener(this);
        tv_face_to_face.setOnClickListener(this);

        String fansListContent = SPUtils.getFansListContent();
        if(!TextUtils.isEmpty(fansListContent)){
            tv_desc.setText(fansListContent);
        }

        createQrPop();
    }

    private void createQrPop() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_invite_code, null);
        iv_qr_code = (ImageView) popView.findViewById(R.id.iv_qr_code);
        ll_root = (LinearLayout) popView.findViewById(R.id.ll_root);
        ImageView iv_close = (ImageView)popView.findViewById(R.id.iv_close);
        tv_invite_code1 = (TextView)popView.findViewById(R.id.tv_invite_code);
        iv_close.setOnClickListener(this);
        ll_root.setOnClickListener(this);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_one_fans:
            case R.id.tv_two_fans:
                setTab(v.getId());
                fansFragment.setResultData(level);
                fansFragment.setSelection(0);
                break;
            case R.id.tv_send_invite:
//                String shareContent = "我在这里买冻品一起来捡耙活！注册就有送，有买就有送，不买也有";
//                Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
//                intent.setType("text/plain"); // 分享发送的数据类型
//                intent.putExtra(Intent.EXTRA_TEXT, shareContent); // 分享的内容
//                startActivity(Intent.createChooser(intent, "选择分享"));// 目标应用选择对话框的标题
                InputStream open = null;
                try {
                    open = getAssets().open("ic_launcher.png");
                    BitmapDrawable fromStream = (BitmapDrawable) Drawable.createFromStream(open,null);
                    Bitmap bitmap = fromStream.getBitmap();
                    SharePop.getInstance().showPop(this, tv_send_invite,
                            "我在这里买冻品一起来捡耙活！注册就有送，有买就有送，不买也有",
                            invite_http_url,
                            "我在这里买冻品一起来捡耙活！注册就有送，有买就有送，不买也有",
                            bitmap,
                            "http://p18.qhimg.com/t01024fff41e15d3348.png", null);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(open != null){
                        try {
                            open.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case R.id.tv_face_to_face:
                popupWindow.showAtLocation(tv_face_to_face, Gravity.CENTER, 0, 0);
                break;
            case R.id.iv_close:
            case R.id.ll_root:
                if(popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            default:
                break;
        }
    }

    private void setTab(int id) {
        tv_one_fans.setTextColor(getResources().getColor(R.color.myblack));
        tv_two_fans.setTextColor(getResources().getColor(R.color.myblack));
        v_one_fans.setVisibility(View.INVISIBLE);
        v_two_fans.setVisibility(View.INVISIBLE);
        switch (id) {
            case R.id.tv_one_fans:
                v_one_fans.setVisibility(View.VISIBLE);
                tv_one_fans.setTextColor(getResources().getColor(R.color.main));
                level = 1;
                break;
            case R.id.tv_two_fans:
                v_two_fans.setVisibility(View.VISIBLE);
                tv_two_fans.setTextColor(getResources().getColor(R.color.main));
                level = 2;
                break;
            default:
                break;
        }
    }


}
