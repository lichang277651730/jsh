package com.cqfrozen.jsh.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/11/28.
 */
public class AppUtil {

    //去往应用市场下载
    public static void marketDownload(Activity activity, String pkgName){
        try{
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("market://details?id=" + pkgName));
            activity.startActivity(intent);
        }catch(Exception e){
            Toast.makeText(activity, "未找到安卓市场", Toast.LENGTH_SHORT).show();
        }
    }
}
