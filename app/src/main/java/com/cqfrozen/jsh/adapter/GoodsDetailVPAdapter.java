package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.activity.GoodDetailResultInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class GoodsDetailVPAdapter extends PagerAdapter {
    private Context context;
    private List<GoodDetailResultInfo.PicsInfo> picsInfos;
    private final DisplayImageOptions defaultOptions;

    public GoodsDetailVPAdapter(Context context, List<GoodDetailResultInfo.PicsInfo> picsInfos){
        this.context = context;
        this.picsInfos = picsInfos;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.solid_banner)
                .showImageOnFail(R.mipmap.solid_banner)
                .build();
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
    public Object instantiateItem(ViewGroup container, int position) {
//        position = position % picsInfos.size();
        ImageView iv = newImageView(context);
        GoodDetailResultInfo.PicsInfo picsInfo = picsInfos.get(position);
        ImageLoader.getInstance().displayImage(picsInfo.pic_url, iv, defaultOptions);
        container.addView(iv);
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
