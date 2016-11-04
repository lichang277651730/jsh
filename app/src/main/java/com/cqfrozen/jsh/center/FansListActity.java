package com.cqfrozen.jsh.center;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.common.base.BaseValue;
import com.common.widget.MyGridDecoration;
import com.common.widget.RefreshLayout;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.adapter.FansRVAdapter;
import com.cqfrozen.jsh.entity.FansResultInfo;
import com.cqfrozen.jsh.entity.MyFansPageInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.share.SharePop;
import com.cqfrozen.jsh.util.QrCodeUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class FansListActity extends MyActivity implements View.OnClickListener, RefreshLayout.OnRefreshListener, RefreshLayout.TopOrBottom {

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
    private RefreshLayout refresh_fans;
    private RecyclerView rv_fans;
    private List<FansResultInfo.FansInfo> fansInfos = new ArrayList<>();
    private FansRVAdapter fansRVAdapter;
    private String invite_http_url;//邀请链接
    private Bitmap invite_qr_bitmap;
    private PopupWindow popupWindow;
    private ImageView iv_qr_code;
    private View v_one_fans;
    private View v_two_fans;
    private MyFansPageInfo myFansPageInfo;
    private TextView tv_invite_code1;
    private LinearLayout ll_root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanslist);
        initView();
        getViewData();
        initRV();
        getRVData();
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
                initViewData(myFansPageInfo);
            }
        });
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
        tv_fans_count.setText("邀请好友总计：" + myFansPageInfo.intotal_fans_count +"人");
        tv_invite_code.setText("我的邀请码:" + myFansPageInfo.code);
        tv_one_fans.setText("我的兄弟伙(" + myFansPageInfo.one_fans_count + ")");
        tv_two_fans.setText("兄弟伙的兄弟伙(" + myFansPageInfo.two_fans_count + ")");
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
        refresh_fans = (RefreshLayout) findViewById(R.id.refresh_fans);
        rv_fans = (RecyclerView) findViewById(R.id.rv_fans);
        v_one_fans = (View) findViewById(R.id.v_one_fans);
        v_two_fans = (View) findViewById(R.id.v_two_fans);
        v_one_fans.setVisibility(View.VISIBLE);
        tv_one_fans.setOnClickListener(this);
        tv_two_fans.setOnClickListener(this);
        tv_send_invite.setOnClickListener(this);
        tv_face_to_face.setOnClickListener(this);
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

    private void initRV() {
        refresh_fans.setRefreshble(true);
        refresh_fans.setOnRefreshListener(this);
        rv_fans.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        rv_fans.setLayoutManager(manager);
        MyGridDecoration decoration = new MyGridDecoration(BaseValue.dp2px(1), BaseValue
                .dp2px(0), getResources().getColor(R.color.mybg), false);
        rv_fans.addItemDecoration(decoration);
        fansRVAdapter = new FansRVAdapter(this, fansInfos);
        rv_fans.setAdapter(fansRVAdapter);
        refresh_fans.setRC(rv_fans, this);
    }

    private void getRVData() {
        MyHttp.searchFans(http, null, page, level, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                if(code == 404){
//                    showToast(msg);
                    refresh_fans.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                if(code != 0){
//                    showToast(msg);
                    refresh_fans.setResultState(RefreshLayout.ResultState.failed);
                    return;
                }
                refresh_fans.setResultState(RefreshLayout.ResultState.success);
                FansResultInfo fansResultInfo = (FansResultInfo) bean;
                is_page = fansResultInfo.is_page;
                fansInfos.addAll(fansResultInfo.data1);
                if(fansInfos.size() == 0){
                    return;
                }
                fansRVAdapter.notifyDataSetChanged();
                page++;
            }
        });
    }

    @Override
    public void onClick(View v) {
        tv_one_fans.setTextColor(getResources().getColor(R.color.myblack));
        tv_two_fans.setTextColor(getResources().getColor(R.color.myblack));
        v_one_fans.setVisibility(View.INVISIBLE);
        v_two_fans.setVisibility(View.INVISIBLE);
        switch (v.getId()) {
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
            case R.id.tv_send_invite:
                InputStream open = null;
                try {
                    open = getAssets().open("ic_launcher.png");
                    BitmapDrawable fromStream = (BitmapDrawable) Drawable.createFromStream(open,null);
                    Bitmap bitmap = fromStream.getBitmap();
                    SharePop.getInstance().showPop(this, tv_send_invite,
                            "我在这里买冻品一起来捡耙活！注册就有送，有买就有送，不买也有",
                            invite_http_url,
                            "分享内容", bitmap,
                            "https://img.alicdn.com/tfscom/i3/78539403/TB2qIMubpHzQeBjSZFOXXcM9FXa_!!78539403.jpg_300x300.jpg", null);
                } catch (IOException e) {
                    e.printStackTrace();
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
        page = 1;
        is_page = 0;
        fansInfos.clear();
        getRVData();

    }

    @Override
    public void onRefresh() {
        page = 1;
        is_page = 0;
        fansInfos.clear();
        getRVData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(refresh_fans != null && refresh_fans.isRefreshing){
            refresh_fans.setResultState(RefreshLayout.ResultState.close);
        }
    }

    @Override
    public void gotoTop() {

    }

    @Override
    public void gotoBottom() {
        if(is_page == 1){
            getRVData();
        }else if(is_page == 0){

        }
    }

    @Override
    public void move() {

    }

    @Override
    public void stop() {

    }
}
