package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.HomeTableInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HomeTableAdapter extends RecyclerView.Adapter<HomeTableAdapter.MyViewHolder> {


    private Context context;
    private List<HomeTableInfo> homeTableInfos;
    public HomeTableAdapter(Context context){
        this.context = context;
//        initData();
    }

    private void initData() {
//        homeTableInfos = new ArrayList<>();
//
//        homeTableInfos.add(new HomeTableInfo(R.mipmap.list_tejia_h, "特价推荐",
//                new GoodsInfo(R.mipmap.solid_goods, "老肉片", "¥129", "¥100"),
//                new GoodsInfo(R.mipmap.solid_goods, "牛肚", "¥129", "¥100"),
//                new GoodsInfo(R.mipmap.solid_goods, "凤爪", "¥129", "¥100")));
//        homeTableInfos.add(new HomeTableInfo(R.mipmap.list_remai_h, "热卖推荐",
//                new GoodsInfo(R.mipmap.solid_goods, "老肉片", "¥129", "¥100"),
//                new GoodsInfo(R.mipmap.solid_goods, "牛肚", "¥129", "¥100"),
//                new GoodsInfo(R.mipmap.solid_goods, "凤爪", "¥129", "¥100")));
//        homeTableInfos.add(new HomeTableInfo(R.mipmap.list_xinpin_h, "新品推荐",
//                new GoodsInfo(R.mipmap.solid_goods, "老肉片", "¥129", "¥100"),
//                new GoodsInfo(R.mipmap.solid_goods, "牛肚", "¥129", "¥100"),
//                new GoodsInfo(R.mipmap.solid_goods, "凤爪", "¥129", "¥100")));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hometable, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        HomeTableInfo homeTableInfo = homeTableInfos.get(position);
//        holder.iv_table.setImageResource(homeTableInfo.table_img);
//        holder.tv_title.setText(homeTableInfo.talbe_name);
//
//        holder.iv_table1.setImageResource(homeTableInfo.first_goods.url_Img);
//        holder.tv_table1.setText(homeTableInfo.first_goods.name);
//        holder.tv_cur_price_table1.setText(homeTableInfo.first_goods.cur_price);
//        holder.tv_pre_price_table1.setText(homeTableInfo.first_goods.pre_price);
//
//        holder.iv_table2.setImageResource(homeTableInfo.second_goods.url_Img);
//        holder.tv_table2.setText(homeTableInfo.second_goods.name);
//        holder.tv_cur_price_table2.setText(homeTableInfo.second_goods.cur_price);
//        holder.tv_pre_price_table2.setText(homeTableInfo.second_goods.pre_price);
//
//        holder.iv_table3.setImageResource(homeTableInfo.third_goods.url_Img);
//        holder.tv_table3.setText(homeTableInfo.third_goods.name);
//        holder.tv_cur_price_table3.setText(homeTableInfo.third_goods.cur_price);
//        holder.tv_pre_price_table3.setText(homeTableInfo.third_goods.pre_price);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
//        private ImageView iv_table;
//        private TextView tv_title;
//        private ImageView iv_table1;
//        private TextView tv_table1;
//        private TextView tv_cur_price_table1;
//        private TextView tv_pre_price_table1;
//
//        private ImageView iv_table2;
//        private TextView tv_table2;
//        private TextView tv_cur_price_table2;
//        private TextView tv_pre_price_table2;
//
//        private ImageView iv_table3;
//        private TextView tv_table3;
//        private TextView tv_cur_price_table3;
//        private TextView tv_pre_price_table3;
        public MyViewHolder(View itemView) {
            super(itemView);
//            iv_table = (ImageView) itemView.findViewById(R.id.iv_table);
//            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
//
//            iv_table1 = (ImageView) itemView.findViewById(R.id.iv_table1);
//            tv_table1 = (TextView) itemView.findViewById(R.id.tv_table1);
//            tv_cur_price_table1 = (TextView) itemView.findViewById(R.id.tv_cur_price_table1);
//            tv_pre_price_table1 = (TextView) itemView.findViewById(R.id.tv_pre_price_table1);
//
//            iv_table2 = (ImageView) itemView.findViewById(R.id.iv_table2);
//            tv_table2 = (TextView) itemView.findViewById(R.id.tv_table2);
//            tv_cur_price_table2 = (TextView) itemView.findViewById(R.id.tv_cur_price_table2);
//            tv_pre_price_table2 = (TextView) itemView.findViewById(R.id.tv_pre_price_table2);
//
//            iv_table3 = (ImageView) itemView.findViewById(R.id.iv_table3);
//            tv_table3 = (TextView) itemView.findViewById(R.id.tv_table3);
//            tv_cur_price_table3 = (TextView) itemView.findViewById(R.id.tv_cur_price_table3);
//            tv_pre_price_table3 = (TextView) itemView.findViewById(R.id.tv_pre_price_table3);
        }
    }
}
