package com.cqfrozen.jsh.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.cqfrozen.jsh.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 微博分享
 */
public class ShareUtilsWB extends Activity implements IWeiboHandler.Response {
    public static String APP_KEY = "2753439185";
    private static final String PREFERENCES_NAME = "com_weibo_sdk_android";
    private static final String KEY_UID = "uid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    public IWeiboShareAPI mWeiboShareAPI;

    public static void wbShare(Activity context, String title, String content, String url, Bitmap
            bitmap) {
        Intent intent = new Intent(context, ShareUtilsWB.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("url", url);
        intent.putExtra("bitmap", bitmap);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, APP_KEY);
        mWeiboShareAPI.registerApp();
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
        try {
            Intent intent = getIntent();
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            String url = intent.getStringExtra("url");
            Bitmap bitmap = intent.getParcelableExtra("bitmap");
            wbShareInActivity(title, content, url, bitmap);
        } catch (Exception e) {
        }
    }

    public void wbShareInActivity(String title, String content, String url, Bitmap bitmap) {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_sinashare);
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        TextObject textObject = new TextObject();
        textObject.text = "【" + title + "】\n" + content + "\n" + url;
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = textObject;
        weiboMessage.imageObject = imageObject;
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
        AuthInfo authInfo = new AuthInfo(this, APP_KEY, "http://www.sina.com",
                SCOPE);
        Oauth2AccessToken accessToken = readAccessToken(getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(this, request, authInfo, token,
                new WeiboAuthListener() {
                    @Override
                    public void onWeiboException(WeiboException arg0) {
                        System.out.println("失败");
                    }

                    @Override
                    public void onComplete(Bundle bundle) {
                        System.out.println("成功");
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("取消");
                    }
                });
    }

    public Oauth2AccessToken readAccessToken(Context context) {
        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_APPEND);
        token.setUid(pref.getString(KEY_UID, ""));
        token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
        token.setRefreshToken(pref.getString(KEY_REFRESH_TOKEN, ""));
        token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
        return token;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    SharePop.getInstance().setResult(SharePop.ShareResult.SUCCESS);
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    SharePop.getInstance().setResult(SharePop.ShareResult.CANCEL);
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    SharePop.getInstance().setResult(SharePop.ShareResult.FAILED);
                    break;
            }
        }
        finish();
    }

}
