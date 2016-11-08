package com.cqfrozen.jsh.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Administrator on 2016/11/8.
 */
public class Uri2PathUtil {

    public static String getImgFilePath(Uri data, Context context){

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(data,
                proj, null, null, null);

        int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String img_path = cursor.getString(actual_image_column_index);
        cursor.close();
        return img_path;

    }
}
