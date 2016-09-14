package com.cqfrozen.jsh.okhttp;

import android.content.Context;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lichang on 2016/9/1.
 */
public abstract class SimpleCallBack<T> extends BaseCallback<T> {

    private SpotsDialog dialog;
    private Context context;


    public SimpleCallBack(Context context){
        this.context = context;
        dialog = new SpotsDialog(context);
    }

    private void showDialog(){
        if(dialog != null){
            dialog.show();
        }
    }

    private void dismissDialog(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public void setDialogContent(String content){
        if(dialog != null  && dialog.isShowing()){
            dialog.setTitle(content);
        }
    }

    @Override
    public void onPreRequest(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }

    @Override
    public void onTokenError(Response response, int code) {
//        ToastUtil.showToast(context, context.getString(R.string.token_error));
//        Intent intent = new Intent(context, LoginActivity.class);
//        context.startActivity(intent);
//        MyApplication.getInstance().clearUser();
    }
}
