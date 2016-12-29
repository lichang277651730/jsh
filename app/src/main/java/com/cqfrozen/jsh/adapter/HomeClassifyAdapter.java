package com.cqfrozen.jsh.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.ad.AdListActivity;
import com.cqfrozen.jsh.center.HuibiListActivity;
import com.cqfrozen.jsh.center.NormalBuyActivity;
import com.cqfrozen.jsh.entity.HomeClassifyInfo;
import com.cqfrozen.jsh.order.OrderListActivity;
import com.cqfrozen.jsh.util.UIUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class HomeClassifyAdapter extends RecyclerView.Adapter<HomeClassifyAdapter.MyViewHolder> {

    private static final int PAGE_BUY_GOODS = 1;
    private static final int PAGE_NORMAL_BUY = 2;
    private static final int PAGE_HUIBI = 3;
    private static final int PAGE_ORDER_LIST = 4;

    private int[] titleIds = new int[]{R.string.home_classify_dh, R.string.home_classify_cg,
                                        R.string.home_classify_jf, R.string.home_classify_dd};
    private int[] imgs = new int[]{R.mipmap.list_btm_dinghuo_h, R.mipmap.list_btm_caigou_h, R.mipmap.list_btm_ifen_h, R.mipmap.list_btm_dingdan_h};
    private Context context;
    private List<HomeClassifyInfo> classifyInfos;
    public HomeClassifyAdapter(Context context, List<HomeClassifyInfo> classifyInfos){
        this.context = context;
        this.classifyInfos = classifyInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_homeclassify, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final String title = UIUtils.getString(titleIds[position - 1]);
        holder.tv_classify.setText(title);
        holder.iv_classify.setImageResource(imgs[position - 1]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case PAGE_BUY_GOODS://去赚粮票
//                        if(context instanceof HomeActivity){
//                            ((HomeActivity)context).setClassifyFragment();
//                        }
                        context.startActivity(new Intent(context, AdListActivity.class));
                        break;
                    case PAGE_NORMAL_BUY://常用采购
                        context.startActivity(new Intent(context, NormalBuyActivity.class));
                        break;
                    case PAGE_HUIBI://我的粮票
                        context.startActivity(new Intent(context, HuibiListActivity.class));
                        break;
                    case PAGE_ORDER_LIST://我的订单
                        context.startActivity(new Intent(context, OrderListActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_classify;
        private TextView tv_classify;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_classify = (ImageView) itemView.findViewById(R.id.iv_classify);
            tv_classify = (TextView) itemView.findViewById(R.id.tv_classify);
        }
    }
}
