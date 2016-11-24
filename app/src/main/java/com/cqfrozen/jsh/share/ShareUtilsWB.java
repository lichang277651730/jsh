package com.cqfrozen.jsh.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.cqfrozen.jsh.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseRequest;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
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
public class ShareUtilsWB extends Activity implements IWeiboHandler.Response, IWeiboHandler.Request {
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

    public static final String KEY_SHARE_TYPE = "key_share_type";
    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;
    private int mShareType = SHARE_CLIENT;

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
        mShareType = getIntent().getIntExtra(KEY_SHARE_TYPE, SHARE_CLIENT);
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
        if(mShareType == SHARE_CLIENT){
            if(mWeiboShareAPI.isWeiboAppSupportAPI()){
                int supportAPI = mWeiboShareAPI.getWeiboAppSupportAPI();
                if(supportAPI >= 10351){
                    sendMultiMessage(true, true);
                }else {
                    sendSingleMessage(true, true);
                }
            }else{
                Toast.makeText(this, "不支持的微博API", Toast.LENGTH_SHORT).show();
            }
        }else if(mShareType == SHARE_ALL_IN_ONE){
            sendMultiMessage(true, true);
        }
//        if (bitmap == null) {
//            bitmap = BitmapFactory.decodeResource(getResources(),
//                    R.mipmap.ic_sinashare);
//        }
//        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
//        ImageObject imageObject = new ImageObject();
//        imageObject.setImageObject(bitmap);
//        TextObject textObject = new TextObject();
//        textObject.text = "【" + title + "】\n" + content + "\n" + url;
//        // 1. 初始化微博的分享消息
//        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        weiboMessage.textObject = textObject;
//        weiboMessage.imageObject = imageObject;
//         //2. 初始化从第三方到微博的消息请求
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        // 用transaction唯一标识一个请求
//        request.transaction = String.valueOf(System.currentTimeMillis());
//        request.multiMessage = weiboMessage;

//        ProvideMultiMessageForWeiboResponse response = new ProvideMultiMessageForWeiboResponse();
//        // 2. 初始化从微博到第三方的消息请求
//        response.transaction = mBaseRequest.transaction;
//        response.reqPackageName = mBaseRequest.packageName;
//        response.multiMessage = weiboMessage;
//
//        // 3. 发送响应消息到微博
//        mWeiboShareAPI.sendResponse(response);
        // 3. 发送请求消息到微博，唤起微博分享界面
//        AuthInfo authInfo = new AuthInfo(this, APP_KEY, "http://www.sina.com",
//                SCOPE);
//        Oauth2AccessToken accessToken = readAccessToken(getApplicationContext());
//        String token = "";
//        if (accessToken != null) {
//            token = accessToken.getToken();
//        }
//        mWeiboShareAPI.sendRequest(this, request, authInfo, token,
//                new WeiboAuthListener() {
//                    @Override
//                    public void onWeiboException(WeiboException arg0) {
//                        System.out.println("失败");
//                    }
//
//                    @Override
//                    public void onComplete(Bundle bundle) {
//                        System.out.println("成功");
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        System.out.println("取消");
//                    }
//                });
    }

    private void sendSingleMessage(boolean hasText, boolean hasImage) {
        WeiboMessage weiboMessage = new WeiboMessage();
        if (hasText) {
            weiboMessage.mediaObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.mediaObject = getImageObj();
        }
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(ShareUtilsWB.this, request);
    }

    private void sendMultiMessage(boolean hasText, boolean hasImage) {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.imageObject = getImageObj();
        }
        SendMultiMessageToWeiboRequest request = new
                SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        if(mShareType == SHARE_CLIENT){
            mWeiboShareAPI.sendRequest(ShareUtilsWB.this, request);
        }else if(mShareType == SHARE_ALL_IN_ONE){
            AuthInfo authInfo = new AuthInfo(this, APP_KEY, "http://www.sina.com", SCOPE);
            Oauth2AccessToken accessToken = readAccessToken(getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiboShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {
                @Override
                public void onComplete(Bundle bundle) {
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    writeAccessToken(getApplicationContext(), newToken);
                    Toast.makeText(getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onWeiboException(WeiboException e) {

                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "冻博汇";
        return textObject;
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

    /**
     * 保存 Token 对象到 SharedPreferences。
     *
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     */
    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        if (null == context || null == token) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_UID, token.getUid());
        editor.putString(KEY_ACCESS_TOKEN, token.getToken());
        editor.putString(KEY_REFRESH_TOKEN, token.getRefreshToken());
        editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
        editor.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
//        setIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
//                    SharePop.getInstance().setResult(SharePop.ShareResult.SUCCESS);
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
//                    SharePop.getInstance().setResult(SharePop.ShareResult.CANCEL);
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, "分享失败", Toast.LENGTH_LONG).show();
//                    SharePop.getInstance().setResult(SharePop.ShareResult.FAILED);
                    break;
            }
        }
        finish();
    }

    /** 从微博客户端唤起第三方应用时，客户端发送过来的请求数据对象 */
    private BaseRequest mBaseRequest = null;
    @Override
    public void onRequest(BaseRequest baseRequest) {
        mBaseRequest = baseRequest;
        String resId = (mBaseRequest != null) ?
                "收到微博发起的请求成功" :
                "收到客户端请求消息失败";
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }
}
