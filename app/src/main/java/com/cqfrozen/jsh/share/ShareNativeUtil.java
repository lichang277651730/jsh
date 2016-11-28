package com.cqfrozen.jsh.share;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;

import com.cqfrozen.jsh.util.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class ShareNativeUtil {

    public static boolean isNative(Activity activity, String pkgName){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        List<ResolveInfo> resolveInfos = activity.getPackageManager().queryIntentActivities
                (intent, 0);
        if(!resolveInfos.isEmpty()){
            for (ResolveInfo resInfo : resolveInfos){
                ActivityInfo activityInfo = resInfo.activityInfo;
                if(activityInfo.packageName.equals(pkgName)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void shareIntent(Activity activity, String pkgName, String content){
        if(isNative(activity, pkgName)){
            Intent targeted = new Intent(Intent.ACTION_SEND);
            targeted.setType("text/plain");
            targeted.setPackage(pkgName);
            targeted.putExtra(Intent.EXTRA_TEXT, content);
            activity.startActivity(targeted);
        }else {
            ToastUtil.showToast(activity, "没有安装分享应用");
        }
    }
}
