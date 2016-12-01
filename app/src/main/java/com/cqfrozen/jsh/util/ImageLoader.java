package com.cqfrozen.jsh.util;

import android.content.Context;
import android.widget.ImageView;

import com.cqfrozen.jsh.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Administrator on 2016/11/22.
 */
public class ImageLoader {

    public static void load(Context context, String url, int loadingImage, int errorImage, ImageView imageView){
        Picasso.with(context).load(url).placeholder(loadingImage).error(errorImage).into(imageView);
    }

    public static void load(Context context, File file, int loadingImage, int errorImage, ImageView imageView){
        Picasso.with(context).load(file).placeholder(loadingImage).error(errorImage).into(imageView);
    }

    public static void load(Context context, int resId, int loadingImage, int errorImage, ImageView imageView){
        Picasso.with(context).load(resId).placeholder(loadingImage).error(errorImage).into(imageView);
    }

    public static void load(Context context, String url, ImageView imageView){
        load(context, url, R.color.transparency, R.mipmap.img_loading_default, imageView);
    }

    public static void load(Context context, File file, ImageView imageView){
        load(context, file, R.color.transparency, R.mipmap.img_loading_default, imageView);
    }

    public static void load(Context context, int resId, ImageView imageView){
        load(context, resId, R.color.transparency, R.mipmap.img_loading_default, imageView);
    }


}
