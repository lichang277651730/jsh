package com.cqfrozen.jsh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsVPActivity;
import com.cqfrozen.jsh.entity.GoodDetailResultInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class GoodsDetailVPAdapter extends PagerAdapter {
    private Context context;
    private List<GoodDetailResultInfo.PicsInfo> picsInfos;
    private final DisplayImageOptions defaultOptions;
    private String goodsName;

    public GoodsDetailVPAdapter(Context context, List<GoodDetailResultInfo.PicsInfo> picsInfos){
        this.context = context;
        this.picsInfos = picsInfos;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.img_loading_empty)
                .showImageOnFail(R.mipmap.img_loading_failed)
                .build();
    }

    public void setGoodsName(String goodsName){
        this.goodsName = goodsName;
    }

    @Override
    public int getCount() {
        return picsInfos.size();
//        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        position = position % picsInfos.size();
        final ImageView iv = newImageView(context);
        final GoodDetailResultInfo.PicsInfo picsInfo = picsInfos.get(position);
        ImageLoader.getInstance().displayImage(picsInfo.pic_url, iv, defaultOptions);
        container.addView(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, GoodsVPActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("title", goodsName);
                intent.putExtra("pics", (Serializable) picsInfos);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation((Activity) context, v, picsInfo.pic_url);
                    ActivityCompat.startActivity((Activity)context, intent, options.toBundle());
                }else {
                    context.startActivity(intent);
                }
            }
        });
        return iv;
    }

    /**
     * 新建一张图片
     */
    private ImageView newImageView(Context context) {
        ImageView iv_img = new ImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv_img.setLayoutParams(params);
        iv_img.setScaleType(ImageView.ScaleType.FIT_XY);
        return iv_img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }


}
