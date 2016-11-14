package com.cqfrozen.jsh.takephoto;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.cqfrozen.jsh.R;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/14.
 */
public class TakePhotoWindow {

    Activity context;
    LayoutInflater inflater;
    PopupWindow popupWindow;
    public static Uri imageUri;
    public static String tempfile;
    private TakePhoto takePhoto;
    private View view;
    private int which;

    public TakePhotoWindow(Activity context, TakePhoto takePhoto) {
        this.context = context;
        this.takePhoto = takePhoto;
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_pop_photo, null);
        View v_half_al = view.findViewById(R.id.v_half_al);
        Button pop_btn_camera = (Button) view.findViewById(R.id.pop_btn_camera);
        Button pop_btn_photo = (Button) view.findViewById(R.id.pop_btn_photo);
        Button pop_btn_no = (Button) view.findViewById(R.id.pop_btn_no);

        popupWindow = new PopupWindow(context);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        pop_btn_camera.setOnClickListener(click);
        pop_btn_photo.setOnClickListener(click);
        pop_btn_no.setOnClickListener(click);
        v_half_al.setOnClickListener(click);
    }

    public void showpop(View view) {
        this.view = view;
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
    public void showpop(View view, int which) {
        this.view = view;
        this.which = which;
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public ImageView getImageView() {
        return (ImageView) view;
    }
    public int getInt() {
        return which;
    }

    View.OnClickListener click = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
            if (!file.getParentFile().exists()){file.getParentFile().mkdirs();}

            Uri imageUri = Uri.fromFile(file);

            configCompress();
            configTakePhotoOpthion();
            switch (v.getId()) {
                case R.id.pop_btn_camera:
                    takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions());
                    popupWindow.dismiss();
                    break;
                case R.id.pop_btn_photo:
                    takePhoto.onPickFromDocumentsWithCrop(imageUri,getCropOptions());
                    popupWindow.dismiss();
                    break;
                case R.id.pop_btn_no:
                case R.id.v_half_al:
                    popupWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private void configTakePhotoOpthion() {
        takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setWithOwnGallery(true).create());
    }

    private void configCompress() {
        int maxSize= Integer.parseInt("102400");
        int width= 200;
        int height= 200;
        CompressConfig config;
        config=new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width>=height? width:height)
                .create();
        takePhoto.onEnableCompress(config,false);
    }

    private CropOptions getCropOptions() {
        int height= 200;
        int width= 200;

        CropOptions.Builder builder=new CropOptions.Builder();

        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(true);
        return builder.create();
    }

    public static String getPATH() {
        return tempfile;
    }

    public static Uri getImgUri() {
        return imageUri;
    }

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
}
