package com.cqfrozen.jsh.util;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SignUtil {

    public static String getOrderSignInfo(String cartdata, long timestamp, String token){
//        Log.d("addAddress_params", "cartdata" + cartdata + "timestamp" + timestamp + "token" + token);
        return MD5Util.encodeMD5("cartdata" + cartdata + "timestamp" + timestamp + "token" + token).toUpperCase();
    }

    public static String getOrderBuySignInfo(String a_id, String cartdata, int is_use_hb, String
            msg_content, int pay_mode, int ptype, long timestamp, String token) {
//        Log.d("addAddress_params", "a_id" + a_id +
//                        "cartdata" + cartdata +
//                        "is_use_hb" + is_use_hb +
//                        "msg_content" + msg_content +
//                        "pay_mode" + pay_mode +
//                        "ptype" + ptype +
//                        "timestamp" + timestamp +
//                        "token" + token);
        return MD5Util.encodeMD5("a_id" + a_id +
                "cartdata" + cartdata +
                "is_use_hb" + is_use_hb +
                "msg_content" + msg_content +
                "pay_mode" + pay_mode +
                "ptype" + ptype +
                "timestamp" + timestamp +
                "token" + token)
                .toUpperCase();
    }
}
