package com.cqfrozen.jsh.appraise;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.AppraiseInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/31.
 */
public class AppraiseAdapter extends BaseAdapter {

    private Context context;
    private List<AppraiseInfo> appraiseInfos;
    private final DisplayImageOptions defaultOptions;
    private Map<Integer, String> saveContent = new HashMap<>();
    private Map<Integer, Float> saveStar = new HashMap<>();
    private int index = -1;
    public AppraiseAdapter(Context context, List<AppraiseInfo> appraiseInfos){
        this.context = context;
        this.appraiseInfos = appraiseInfos;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.solid_goods)
                .showImageOnFail(R.mipmap.solid_goods)
                .build();
    }

    @Override
    public int getCount() {
        return appraiseInfos.size();
    }

    @Override
    public AppraiseInfo getItem(int position) {
        return appraiseInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_appraise, parent, false);
        ImageView iv_appraise = (ImageView) convertView.findViewById(R.id.iv_appraise);
        RatingBar rb_appraise = (RatingBar) convertView.findViewById(R.id.rb_appraise);
        EditText et_appraise = (EditText) convertView.findViewById(R.id.et_appraise);

        final AppraiseInfo appraiseInfo = appraiseInfos.get(position);
        ImageLoader.getInstance().displayImage(appraiseInfo.pic_url, iv_appraise, defaultOptions);
        rb_appraise.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                saveStar.put(position, rating);
                if(onItemRatingBarChangeListener != null){
                    onItemRatingBarChangeListener.ratingBarChange(ratingBar, rating, fromUser, position, appraiseInfo);
                }
            }
        });
        et_appraise.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    index = position;
                }
                return false;
            }
        });

        et_appraise.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveContent.put(position, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(onItemEditChangeListener != null){
                    onItemEditChangeListener.editChange(s, position, appraiseInfo);
                }
            }
        });

        if(saveContent.get(position) != null){
            et_appraise.setText(saveContent.get(position));
        }

        if(saveStar.get(position) != null){
            rb_appraise.setRating(saveStar.get(position));
        }

        return convertView;

    }

    private OnItemEditChangeListener onItemEditChangeListener;

    public void setOnItemEditChangeListener(OnItemEditChangeListener onItemEditChangeListener) {
        this.onItemEditChangeListener = onItemEditChangeListener;
    }

    public interface OnItemEditChangeListener{
        void editChange(Editable s, int position, AppraiseInfo appraiseInfo);
    }

    private OnItemRatingBarChangeListener onItemRatingBarChangeListener;

    public void setOnItemRatingBarChangeListener(OnItemRatingBarChangeListener
                                                         onItemRatingBarChangeListener) {
        this.onItemRatingBarChangeListener = onItemRatingBarChangeListener;
    }

    public interface OnItemRatingBarChangeListener{
        void ratingBarChange(RatingBar ratingBar, float rating, boolean fromUser, int position,
                             AppraiseInfo appraiseInfo);
    }

}
