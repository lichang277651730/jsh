package com.cqfrozen.jsh.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by lichang on 2016/9/6.
 */
public class JSONUtil {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static Gson getGson(){
        return gson;
    }

    public static <T> T fromJson(String json, Class<T> clazz){
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type){
        return gson.fromJson(json, type);
    }

    public static String toJson(Object object){
        return gson.toJson(object);
    }
}
