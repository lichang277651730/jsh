package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.HomeBannerAdResultInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */
public class SplashVPAdapter  extends PagerAdapter {
    private Context context;
    private List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos;
    private final DisplayImageOptions defaultOptions;

    public SplashVPAdapter(Context context, List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos){
        this.context = context;
        this.bannerAdInfos = bannerAdInfos;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.img_banner_loading)
                .showImageOnFail(R.mipmap.img_banner_loading)
                .build();

    }
    @Override
    public int getCount() {
        return bannerAdInfos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final HomeBannerAdResultInfo.HomeBannerAdInfo bannerAdInfo = bannerAdInfos.get(position);
        ImageView iv_img = newImageView(context);
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        ImageLoader.getInstance().displayImage(bannerAdInfo.pic_url,
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

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
