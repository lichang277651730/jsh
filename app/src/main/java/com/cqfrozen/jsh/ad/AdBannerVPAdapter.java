package com.cqfrozen.jsh.ad;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cqfrozen.jsh.R;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Administrator on 2016/12/29.
 */
public class AdBannerVPAdapter implements BGABanner.Adapter {

    private Context context;
    public AdBannerVPAdapter(Context context){
        this.context = context;
    }

    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
        Glide.with(context)
                .load(model)
                .crossFade()
                .placeholder(R.mipmap.img_loading_default)
                .into((ImageView)view);
    }
}
