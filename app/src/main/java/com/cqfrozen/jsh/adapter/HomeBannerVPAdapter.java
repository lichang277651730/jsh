package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodsDetailActivity;
import com.cqfrozen.jsh.ad.BannerDetailActivity;
import com.cqfrozen.jsh.entity.HomeBannerAdResultInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class HomeBannerVPAdapter extends PagerAdapter{

    private Context context;
    private List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos;
    private final DisplayImageOptions defaultOptions;

    public HomeBannerVPAdapter(Context context, List<HomeBannerAdResultInfo.HomeBannerAdInfo> bannerAdInfos){
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
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        position = position % bannerAdInfos.size();
        final HomeBannerAdResultInfo.HomeBannerAdInfo bannerAdInfo = bannerAdInfos.get(position);
        final int content_type = bannerAdInfo.content_type;
        ImageView iv_img = newImageView(context);
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                1图文，2外链接，3不看详情，4粮票（图文），5粮票（外链），6购买[返回商品id]
                switch (content_type) {
                    case 1:
                    case 2:
                        Intent intent2 = new Intent(context, BannerDetailActivity.class);
                        intent2.putExtra("url", bannerAdInfo.content);
                        intent2.putExtra("title", bannerAdInfo.title);
                        context.startActivity(intent2);
                        break;
                    case 3:
                        break;
                    case 4:
                    case 5:
                        Intent intent = new Intent(context, BannerDetailActivity.class);
                        intent.putExtra("ad_id", bannerAdInfo.ad_id);
                        context.startActivity(intent);
                        break;
                    case 6:
                        Intent intent1 = new Intent(context, GoodsDetailActivity.class);
                        intent1.putExtra("g_id", Long.parseLong(bannerAdInfo.content));
                        context.startActivity(intent1);
                        break;
                }

            }
        });
        ImageLoader.getInstance().displayImage(bannerAdInfos.get(position).pic_url,
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
