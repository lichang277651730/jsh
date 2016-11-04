package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cqfrozen.jsh.R;

/**
 * Created by Administrator on 2016/11/4.
 */
public class IndexAdapter extends PagerAdapter {

    private ImageView[] imageViews =new ImageView[3];

    public IndexAdapter(Context context){
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < 3; i++) {
            imageViews[i] = new ImageView(context);
            imageViews[i].setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews[i].setLayoutParams(params);
            imageViews[i].setBackgroundResource(R.color.white);
        }
        imageViews[0] .setImageResource(R.mipmap.img_guide1);
        imageViews[1] .setImageResource(R.mipmap.img_guide2);
        imageViews[2] .setImageResource(R.mipmap.img_guide3);
    }
    @Override
    public int getCount() {
        return imageViews.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews[position]);
        return imageViews[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
