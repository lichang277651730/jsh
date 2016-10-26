package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.common.widget.MyHeadImageView;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.CommentResultInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class CommentAdapter extends BaseAdapter {

    private Context context;
    private List<CommentResultInfo.CommentInfo> commentInfos;
    private final DisplayImageOptions defaultOptions;
    public CommentAdapter(Context context, List<CommentResultInfo.CommentInfo> commentInfos){
        this.context = context;
        this.commentInfos = commentInfos;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.icon_mine_head_default)
                .showImageOnFail(R.mipmap.icon_mine_head_default)
                .build();
    }

    @Override
    public int getCount() {
        return commentInfos.size();
    }

    @Override
    public CommentResultInfo.CommentInfo getItem(int position) {
        return commentInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if(convertView == null){
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
            holder.iv_head = (MyHeadImageView) convertView.findViewById(R.id.iv_head);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.rb_love = (RatingBar) convertView.findViewById(R.id.rb_love);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        CommentResultInfo.CommentInfo commentInfo = commentInfos.get(position);
        ImageLoader.getInstance().displayImage(commentInfo.head_url, holder.iv_head, defaultOptions);
        holder.tv_phone.setText(commentInfo.nick_name);
        if(TextUtils.isEmpty(commentInfo.pj_content)){
            holder.tv_comment.setText("好，很好，非常好，赞一个！");
        }else {
            holder.tv_comment.setText(commentInfo.pj_content);
        }
        holder.tv_time.setText(commentInfo.add_time);
        holder.rb_love.setRating(commentInfo.star_count);
        return convertView;
    }

    public class MyViewHolder{
        public MyHeadImageView iv_head;
        public TextView tv_phone;
        public RatingBar rb_love;
        public TextView tv_comment;
        public TextView tv_time;
    }
}
