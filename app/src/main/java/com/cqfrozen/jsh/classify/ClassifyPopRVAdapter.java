package com.cqfrozen.jsh.classify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.CategoryInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ClassifyPopRVAdapter extends RecyclerView.Adapter<ClassifyPopRVAdapter.MyViewHolder> {

    private Context context;
    private List<CategoryInfo> categoryInfos;
    public ClassifyPopRVAdapter(Context context, List<CategoryInfo> categoryInfos){
        this.context = context;
        this.categoryInfos = categoryInfos;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_homepop, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryInfo categoryInfo = categoryInfos.get(position);
        holder.tv_name.setText(categoryInfo.t_type_name);
    }

    @Override
    public int getItemCount() {
        return categoryInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
