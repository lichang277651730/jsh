package com.cqfrozen.jsh.classify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.CategoryInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/10/8.
 */
public class ClassifyGvAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryInfo> categoryInfos;
    private final DisplayImageOptions defaultOptions;

    public ClassifyGvAdapter(Context context, List<CategoryInfo> categoryInfos){
        this.context = context;
        this.categoryInfos = categoryInfos;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.icon_home_articleclassify)
                .showImageOnFail(R.mipmap.icon_home_articleclassify)
                .build();
    }

    @Override
    public int getCount() {
        return categoryInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if(convertView == null){
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_classifypop, parent, false);
            holder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        CategoryInfo categoryInfo = categoryInfos.get(position);
        holder.tv_name.setText(categoryInfo.t_type_name);
        ImageLoader.getInstance().displayImage(categoryInfo.pic_url,holder.iv_icon,
                defaultOptions);
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClick(position);
                }
            }
        });
        return convertView;
    }

    public class MyViewHolder{
        public LinearLayout ll_item;
        public ImageView iv_icon;
        public TextView tv_name;
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


}
