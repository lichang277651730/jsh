package com.cqfrozen.jsh.takephoto;

import com.jph.takephoto.app.TakePhoto;

/**
 * Created by Administrator on 2016/11/14.
 */
public class TakePhotoHelper {

    public static TakePhotoHelper instance;
    private TakePhoto takePhoto;

    private TakePhotoHelper(TakePhoto takePhoto){
        this.takePhoto = takePhoto;
    }

    public static TakePhotoHelper getInstance(TakePhoto takePhoto){
        if(instance == null){
            synchronized (TakePhotoHelper.class){
                if(instance == null){
                    instance = new TakePhotoHelper(takePhoto);
                }
            }
        }
        return instance;
    }


}
