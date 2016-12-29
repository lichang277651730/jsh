package com.cqfrozen.jsh.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.graphics.Palette;
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

    /**
     * 提取颜色
     */
    public static int getPaletteColor(Bitmap bitmap) {
        int color = -12417291;
        Palette palette = Palette.from(bitmap).generate();
        Palette.Swatch vibrant = palette.getVibrantSwatch();
        Palette.Swatch vibrantDark = palette.getDarkVibrantSwatch();
        final Palette.Swatch vibrantLight = palette.getLightVibrantSwatch();
        Palette.Swatch muted = palette.getMutedSwatch();
        Palette.Swatch mutedDark = palette.getDarkMutedSwatch();
        Palette.Swatch mutedLight = palette.getLightMutedSwatch();

        if(vibrant != null){
            color = vibrant.getRgb();
        }else if(vibrantDark != null){
            color = vibrantDark.getRgb();
        }else if(vibrantLight != null){
            color = vibrantLight.getRgb();
        }else if(muted != null){
            color = muted.getRgb();
        }else if(mutedDark != null){
            color = mutedDark.getRgb();
        }else if(mutedLight != null){
            color = mutedLight.getRgb();
        }
        return color;
    }

    //获取版本名称
    public static String getVersionName(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
