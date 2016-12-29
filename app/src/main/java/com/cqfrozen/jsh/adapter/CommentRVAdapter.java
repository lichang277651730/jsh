package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.MyViewHolder> {

    private Context context;
    private List<CommentResultInfo.CommentInfo> commentInfos;
    private final DisplayImageOptions defaultOptions;
    public CommentRVAdapter(Context context, List<CommentResultInfo.CommentInfo> commentInfos){
        this.context = context;
        this.commentInfos = commentInfos;
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.icon_mine_head_default)
                .showImageOnFail(R.mipmap.icon_mine_head_default)
                .build();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_commentrv, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommentResultInfo.CommentInfo commentInfo = commentInfos.get(position);
        ImageLoader.getInstance().displayImage(commentInfo.head_url, holder.iv_head, defaultOptions);

        holder.tv_phone.setText(commentInfo.nick_name.substring(0, 3) + "****" + commentInfo.nick_name.substring(7));
        if(TextUtils.isEmpty(commentInfo.pj_content)){
            holder.tv_comment.setText("默认好评！");
        }else {
            holder.tv_comment.setText(commentInfo.pj_content);
        }
        holder.tv_time.setText(commentInfo.add_time);
        holder.rb_love.setRating(commentInfo.star_count);
    }

    @Override
    public int getItemCount() {
        return commentInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private MyHeadImageView iv_head;
        private TextView tv_phone;
        private RatingBar rb_love;
        private TextView tv_comment;
        private TextView tv_time;
        private MyViewHolder(View itemView) {
            super(itemView);
            iv_head = (MyHeadImageView) itemView.findViewById(R.id.iv_head);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            rb_love = (RatingBar) itemView.findViewById(R.id.rb_love);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
