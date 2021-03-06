package com.cqfrozen.jsh.center;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.base.BaseApplication;
import com.common.widget.MyEditText;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.HomeActivity;
import com.cqfrozen.jsh.entity.SigninInfo;
import com.cqfrozen.jsh.entity.UserInfo;
import com.cqfrozen.jsh.main.MyActivity;
import com.cqfrozen.jsh.main.MyApplication;
import com.cqfrozen.jsh.netstate.NetUtils;
import com.cqfrozen.jsh.util.CustomMiddleToast;
import com.cqfrozen.jsh.util.JSONUtil;
import com.cqfrozen.jsh.util.SPUtils;
import com.cqfrozen.jsh.util.UIUtils;
import com.cqfrozen.jsh.util.UMengUtils;
import com.cqfrozen.jsh.volleyhttp.MyHttp;

/**
 * Created by Administrator on 2016/9/12.
 */
public class LoginActivity extends MyActivity implements View.OnClickListener, MyEditText.TextChanged {

    private static final int TAG_PWD_SHOW = 1;
    private static final int TAG_PWD_HIDDEN = 2;

    private static final int REFRESHTOKEN = 1;
    private static final int GETUSERINFO = 2;

    private static LoginActivity instance;

    private MyEditText et_phone;
    private MyEditText et_password;
    private Button btn_login;
    private String phoneStr;
    private TextView tv_regist;
    private TextView tv_forget;
    private ImageView iv_see_pwd;

    protected int clickCount;
    protected long clickFirstTime;
    protected long clickSecondTime;
    private LinearLayout scrollview_login;
    private boolean isShow;
    private boolean isEdShow;
    private int height;
    private ImageView iv_log;
    private ImageView iv_pwd;
    private ImageView iv_username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparencyBar(true);
        setSwipeBackEnable(false);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollview_login.setFocusable(true);
        scrollview_login.setFocusableInTouchMode(true);
        scrollview_login.requestFocus();
    }

    private void initView() {
        scrollview_login = (LinearLayout) findViewById(R.id.scrollview_login);
        iv_log = (ImageView) findViewById(R.id.iv_log);
        et_phone = (MyEditText) findViewById(R.id.et_phone);
        et_password = (MyEditText) findViewById(R.id.et_password);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_regist = (TextView) findViewById(R.id.tv_regist);
        btn_login = (Button) findViewById(R.id.btn_login);
        iv_see_pwd = (ImageView) findViewById(R.id.iv_see_pwd);
        iv_pwd = (ImageView) findViewById(R.id.iv_pwd);
        iv_username = (ImageView) findViewById(R.id.iv_username);
        tv_forget.setOnClickListener(this);
        tv_regist.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        et_phone.addMyTextChangedListener(this);
        et_password.addMyTextChangedListener(this);

        String phoneNumCache = SPUtils.getPhoneNum();
        String pwdCache = SPUtils.getPwd();
        if(!TextUtils.isEmpty(phoneNumCache)){
            et_phone.setText(phoneNumCache);
            et_phone.setSelection(phoneNumCache.length());
        }
        if(!TextUtils.isEmpty(pwdCache)){
            et_password.setText(pwdCache);
            et_password.setSelection(pwdCache.length());
        }
        initShowHiddenPwdView();

        et_phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollview_login.setFocusable(false);
                scrollview_login.setFocusableInTouchMode(false);
                if (!isEdShow) {
                    if (!isShow) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(iv_log, "alpha", 1.0f, 0f);
                        anim.setDuration(500);// 动画持续时间
                        anim.start();
                        ObjectAnimator animator = ObjectAnimator.ofFloat(scrollview_login, "translationY", 0, -height);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isEdShow = true;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animator.setDuration(500);
                        animator.start();
                    }
                }
                return false;
            }
        });

        et_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollview_login.setFocusable(false);
                scrollview_login.setFocusableInTouchMode(false);
                if (!isEdShow) {
                    if (!isShow) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(iv_log, "alpha", 1.0f, 0f);
                        anim.setDuration(500);// 动画持续时间
                        anim.start();
                        ObjectAnimator animator = ObjectAnimator.ofFloat(scrollview_login, "translationY", 0, -height);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isEdShow = true;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animator.setDuration(500);
                        animator.start();
                    }
                }
                return false;
            }
        });

        scrollview_login.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (isShow) {
                    ObjectAnimator anim = ObjectAnimator.ofFloat(iv_log, "alpha", 0f, 1f);
                    anim.setDuration(500);// 动画持续时间
                    anim.start();
                    ObjectAnimator animator = ObjectAnimator.ofFloat(scrollview_login, "translationY", -height, 0);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isEdShow = false;
                            isShow = !isShow;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.setDuration(500);
                    animator.start();
//                    ObjectAnimator.ofFloat(login_logo, "translationY", -300, 0).setDuration(500).start();
                } else {
                    isShow = !isShow;
                }
            }
        });

        calcuHeight();
    }

    //计算高度
    private void calcuHeight() {
        ViewTreeObserver vto = iv_log.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_log.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = iv_log.getMeasuredHeight() + 100;
                int width = iv_log.getMeasuredWidth();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
            case R.id.tv_regist:
                startActivity(new Intent(this, Register1Activity.class));
                break;
            case R.id.btn_login:
                goLogin();
                break;
            default:
                break;
        }
    }

    private void goLogin() {
        phoneStr = et_phone.getText().toString().trim();
        String pwdStr = et_password.getText().toString().trim();
        if(TextUtils.isEmpty(phoneStr) || phoneStr.length() != 11){
            showToast("请输入正确手机号");
            return;
        }
        if(TextUtils.isEmpty(pwdStr)){
            showToast("请输入登陆密码");
            return;
        }
        SPUtils.setPhoneNum(phoneStr);
        SPUtils.setPwd(pwdStr);
        btn_login.setEnabled(false);//防止重复点击登陆
        MyHttp.userLogin(http, null, phoneStr, pwdStr, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                btn_login.setEnabled(true);//防止重复点击登陆
                if(code != 0){
                    CustomMiddleToast.getInstance(LoginActivity.this).showToast(msg);
                    return;
                }
                SigninInfo signinInfo = (SigninInfo) bean;
                MyApplication.signinInfo = signinInfo;
                MyApplication.token = signinInfo.getToken();
                String expire_time = signinInfo.getExpire_time();
                SPUtils.setToken(signinInfo.getToken());
                SPUtils.setExpireTime(Long.parseLong(expire_time));
                et_phone.setEnabled(false);
                getLoginUserInfo();
            }
        });
    }

    /**
     * 通过token去查找用户信息
     */
    private void getLoginUserInfo() {
        MyHttp.user(http, null, new MyHttp.MyHttpResult() {
            @Override
            public void httpResult(Integer which, int code, String msg, Object bean) {
                et_phone.setEnabled(true);
                if (code != 0) {
                    showToast(msg);
                    return;
                }
                UserInfo userInfo = (UserInfo) bean;
                MyApplication.userInfo = userInfo;
                String userInfoJson = JSONUtil.toJson(userInfo);
                SPUtils.setUserInfo(userInfoJson);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                UMengUtils.setSignIn();
                finish();
            }
        });
    }

    /**
     * 显示和隐藏第一次输入的密码
     */
    private void initShowHiddenPwdView() {

        iv_see_pwd.setTag(TAG_PWD_HIDDEN);
        iv_see_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPwdTad = (int) v.getTag();
                if(curPwdTad == TAG_PWD_HIDDEN){//当前是隐藏密码，点击显示密码
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_see_pwd.setTag(TAG_PWD_SHOW);
                }else if(curPwdTad == TAG_PWD_SHOW){//当前是显示密码，点击隐藏密码
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_see_pwd.setTag(TAG_PWD_HIDDEN);
                }
                setSlection(et_password);
            }
        });
    }

    private void setSlection(MyEditText etView) {
        if(etView.getText() != null && !etView.getText().equals("")){
            etView.setSelection(etView.getText().length());
        }
    }

    /**
     * 两秒内双击退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            clickCount++;
            if (clickCount == 1) {
                clickFirstTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出冻博汇", Toast.LENGTH_SHORT).show();
            } else if (clickCount == 2) {
                clickSecondTime = System.currentTimeMillis();
                if (clickSecondTime - clickFirstTime <= 2000) {
                    instance = null;
                    System.exit(0);
                    ((BaseApplication) UIUtils.getContext()).quitApp();
                } else {
                    clickCount = 1;
                    clickFirstTime = System.currentTimeMillis();
                    Toast.makeText(this, "再按一次退出冻博汇", Toast.LENGTH_SHORT).show();
                }
            } else {
                clickCount = 1;
                clickFirstTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出冻博汇", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void textChanged(View view, String s, int start, int before, int count) {
        switch (view.getId()) {
            case R.id.et_phone:
                if(s.length() == 0){
                    iv_username.setImageResource(R.mipmap.icon_register_user_empty);
                }else {
                    iv_username.setImageResource(R.mipmap.icon_register_user);
                }
                break;
            case R.id.et_password:
                if(s.length() == 0){
                    iv_pwd.setImageResource(R.mipmap.icon_register_pwd_empty);
                }else {
                    iv_pwd.setImageResource(R.mipmap.icon_register_pwd);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        if(!TextUtils.isEmpty(SPUtils.getToken())){
            String token = SPUtils.getToken();
            if (token.isEmpty() || token.length() < 2) {
                return;
            }
            MyHttp.refreshToken(http, REFRESHTOKEN, token, new MyHttp.MyHttpResult() {
                @Override
                public void httpResult(Integer which, int code, String msg, Object bean) {
                    if(code != 0){
                        return;
                    }
                    SigninInfo signinInfo = (SigninInfo) bean;
                    MyApplication.signinInfo = signinInfo;
                    MyApplication.token = signinInfo.getToken();
                    SPUtils.setToken(signinInfo.getToken());
                    MyHttp.user(http, GETUSERINFO, new MyHttp.MyHttpResult() {
                        @Override
                        public void httpResult(Integer which, int code, String msg, Object bean) {
                            if(code != 0){
                                return;
                            }
                            UMengUtils.setSignIn();
                            MyApplication.userInfo = (UserInfo) bean;
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });

        }
    }

}
