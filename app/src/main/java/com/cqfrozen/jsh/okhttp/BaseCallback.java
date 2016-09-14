package com.cqfrozen.jsh.okhttp;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lichang on 2016/9/1.
 */
public abstract class BaseCallback<T> {
    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }


    /**
     * 请求开始前
     * @param request
     */
    public abstract void onPreRequest(Request request);

    /**
     * 请求失败
     * @param request
     * @param e
     */
    public abstract void onFailure(Request request, IOException e);

    /**
     * 请求成功时
     * @param response
     */
    public abstract void onResponse(Response response);

    /**
     * 请求成功，处理请求结果成功，状态码>200,<300,是调用此方法
     * @param response
     * @param t
     */
    public abstract void onSuccess(Response response, T t);

    /**
     * 请求成功，但是返回状态码：400，404,403,500时调用此方法
     * @param response
     * @param code
     * @param e
     */
    public abstract void onError(Response response, int code, Exception e);

    /**
     * 请求成功，但是返回状态码：401，402,403时调用此方法
     * @param response
     * @param code
     */
    public abstract void onTokenError(Response response, int code);
}
