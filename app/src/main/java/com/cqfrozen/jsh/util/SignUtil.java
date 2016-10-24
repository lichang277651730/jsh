package com.cqfrozen.jsh.util;

import android.util.Log;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SignUtil {

    public static String getOrderSignInfo(String cartdata, long timestamp, String token){
        Log.d("addAddress_params", "cartdata" + cartdata + "timestamp" + timestamp + "token" + token);
        return MD5Util.encodeMD5("cartdata" + cartdata + "timestamp" + timestamp + "token" + token).toUpperCase();
    }

}
