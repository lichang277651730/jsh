package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.HomeBannerInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class HomeBannerVPAdapter extends PagerAdapter {

    private Context context;
    private List<HomeBannerInfo> bannerInfos;
    private ImageView[] iv_imgs;
    private final DisplayImageOptions defaultOptions;

    public HomeBannerVPAdapter(Context context, List<HomeBannerInfo> bannerInfos){
        this.context = context;
        this.bannerInfos = bannerInfos;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.banner_default)
                .showImageOnFail(R.mipmap.banner_default)
                .build();
        setImgs();
    }

    private void setImgs() {
        iv_imgs = new ImageView[bannerInfos.size()];
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for(int i = 0; i < bannerInfos.size(); i++){
            iv_imgs[i] = new ImageView(context);
            iv_imgs[i].setLayoutParams(params);
            iv_imgs[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @Override
    public int getCount() {
        return bannerInfos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(iv_imgs[position]);
        ImageLoader.getInstance().displayImage(bannerInfos.get(position).pic_url,
                iv_imgs[position],
                defaultOptions);
        return iv_imgs[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
