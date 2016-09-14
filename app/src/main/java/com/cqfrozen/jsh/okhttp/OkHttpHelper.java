package com.cqfrozen.jsh.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lichang on 2016/9/1.
 * OkHttp 封装
 */
public class OkHttpHelper {

    private static final int READ_TIMEOUT = 1000;
    private static final int WRITE_TIMEOUT = 1000;
    private static final int CONNECT_TIMEOUT = 1000;

    public static final int TOKEN_MISSING=401;// token 丢失
    public static final int TOKEN_ERROR=402; // token 错误
    public static final int TOKEN_EXPIRE=403; // token 过期


    private OkHttpClient client;
    private Gson gson;
    private static OkHttpHelper instance;
    private Handler handler;

    private OkHttpHelper() {
        client = new OkHttpClient().newBuilder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        client.connectTimeoutMillis();
        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance(){
        if(instance == null){
            synchronized (OkHttpHelper.class){
                if (instance == null){
                    instance = new OkHttpHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 发GET请求 不带参数
     * @param url
     * @param callBack
     */
    public void doGet(String url, BaseCallback callBack){
        Request request = new Request.Builder().url(url).build();
        buildRequest(url, null, RequestMethod.GET);
        doRequest(request, callBack);
    }

    /**
     * 发GET请求 带参数
     * @param url
     * @param callBack
     */
    public void doGet(String url, Map<String, String> params, BaseCallback callBack){
        Request request = new Request.Builder().url(url).build();
        buildRequest(url, params, RequestMethod.GET);
        doRequest(request, callBack);
    }

    /**
     * 发POST请求
     * @param url
     * @param params
     * @param callBack
     */
    public void doPost(String url, Map<String, String> params, BaseCallback callBack){
        Request request = buildRequest(url, params, RequestMethod.POST);
        doRequest(request, callBack);
    }



    /**
     * 访问网络
     * @param request
     * @param callBack
     */
    public void doRequest(final Request request, final BaseCallback callBack){
        callBack.onPreRequest(request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onResponse(response);
                if(response.isSuccessful()){
                    String resultStr = response.body().string();
                    if(String.class == callBack.mType){
                        callBackSuccess(callBack, response, resultStr);
                    }else{
                        try{
                            Object object = gson.fromJson(resultStr, callBack.mType);
                            callBackSuccess(callBack, response, object);
                        }catch(JsonParseException e){
                            callBackError(callBack, response, response.code(), e);
                        }
                    }
                }else if(response.code() == TOKEN_ERROR
                        || response.code() == TOKEN_EXPIRE
                        ||response.code() == TOKEN_MISSING) {//返回失败，有可能是token失效
                    callBackTokenError(callBack, response);
                }else{
                    callBackError(callBack, response, response.code(), null);
                }
            }
        });
    }

    private void callBackTokenError(final BaseCallback callback, final Response response){
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response, response.code());
            }
        });
    }

    private void callBackError(final BaseCallback callback, final Response response, final int code, final Exception e){
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, code, e);
            }
        });
    }

    /**
     * 切换到主线程
     * @param callBack
     * @param response
     * @param object
     */
    private void callBackSuccess(final BaseCallback callBack, final Response response, final Object object){
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(response, object);
            }
        });
    }


    /**
     * 构建请求
     * @param url
     * @param params
     * @param method
     * @return
     */
    private Request buildRequest(String url, Map<String, String> params, RequestMethod method) {
        Request.Builder builder = new Request.Builder();
        if(method == RequestMethod.GET){
            if(params != null){
                url = buildGetUrl(url, params);
            }
            builder.url(url);
            builder.get();
        }else if (method == RequestMethod.POST){
            builder.url(url);
            builder.post(buildRequestBody(params));
        }

        return builder.build();
    }

    /**
     * 构建带参数的Get请求地址
     * @param params
     * @return
     */
    private String buildGetUrl(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()){
            builder.append(entry.getKey()).append("=")
                    .append(entry.getValue() == null ? "" : entry.getValue())
                    .append("&");
        }
        String s = builder.toString();
        if(s.endsWith("&")){
            s = s.substring(0, s.length() - 1);
        }
        if(url.indexOf("?") > 0){
            url = url + "&" + s;
        }else{
            url = url + "?" + s;
        }
        Log.d("getParamsUrl", url);
        return url;
    }

    /**
     * 构建POST请求参数
     * @param params
     * @return
     */
    private FormBody buildRequestBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> param : params.entrySet()){
            builder.add(param.getKey(), param.getValue());
        }
        return builder.build();
    }

    enum RequestMethod{
        GET,POST,
    }

}
