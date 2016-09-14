package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
import com.cqfrozen.jsh.util.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class HomeBannerVPAdapter extends PagerAdapter{

    private Context context;
    private List<HomeBannerInfo> bannerInfos;
    private final DisplayImageOptions defaultOptions;

    public HomeBannerVPAdapter(Context context, List<HomeBannerInfo> bannerInfos){
        this.context = context;
        this.bannerInfos = bannerInfos;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.solid_banner)
                .showImageOnFail(R.mipmap.solid_banner)
                .build();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % bannerInfos.size();
        final HomeBannerInfo bannerInfo = bannerInfos.get(position);
        ImageView iv_img = newImageView(context);
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 首页轮播条的点击事件
                ToastUtil.showToast(context, bannerInfo.g_name);
            }
        });
        ImageLoader.getInstance().displayImage(bannerInfos.get(position).pic_url,
                iv_img,
                defaultOptions);
        container.addView(iv_img);
        return iv_img;
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
