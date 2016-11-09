package com.cqfrozen.jsh.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.common.http.HttpForVolley;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.appraise.AppraiseActivity;
import com.cqfrozen.jsh.entity.OrderResultInfo;
import com.cqfrozen.jsh.util.CustomSimpleDialog;
import com.cqfrozen.jsh.util.ToastUtil;
import com.cqfrozen.jsh.volleyhttp.MyHttp;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/8.
 */
public class OrderListAdapter2 extends RecyclerView.Adapter<OrderListAdapter2.MyViewHolder> implements View.OnClickListener {

    public interface STATE_EX{
        String EX = "ALL";//展开
        String UN_EX = "ONE";//收起
    }

    private Map<Integer, String> exmaps = new HashMap<>();//记录展开，收起状态
//    private Map<Integer, Integer> btnmaps = new HashMap<>();//记录按钮状态
    private Map<String, Integer> btnmapList = new HashMap<>();//记录按钮状态

    private Context context;
    private List<OrderResultInfo.OrderSearchInfo> orderSearchInfos;
    private final DisplayImageOptions defaultOptions;
    private final HttpForVolley http;
    private int index = -1;
    public OrderListAdapter2(Context context, List<OrderResultInfo.OrderSearchInfo> orderSearchInfos){
        this.context = context;
        this.orderSearchInfos = orderSearchInfos;
        this.http = new HttpForVolley(context);
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnLoading(R.color.transparency)
                .showImageForEmptyUri(R.mipmap.img_loading_empty)
                .showImageOnFail(R.mipmap.img_loading_failed)
                .build();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_orderlist1, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.d("OrderListposition", position + ":position");
        final OrderResultInfo.OrderSearchInfo orderSearchInfo = orderSearchInfos.get(position);
        holder.tv_time.setText("下单时间: " + orderSearchInfo.add_time);
        holder.tv_result_status.setText(orderSearchInfo.status_name);
        final List<OrderResultInfo.OrderGoodsInfo> exRcList = new ArrayList<>();
        exRcList.addAll(orderSearchInfo.orderinfo.subList(0, 1));
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        holder.rv_ordergoods.setLayoutManager(manager);
        final OrderListItemRCAdapter orderListItemRCAdapter = new OrderListItemRCAdapter(context,
                exRcList);
        holder.rv_ordergoods.setAdapter(orderListItemRCAdapter);

        holder.tv_order_count.setText(orderSearchInfo.count);
        holder.tv_order_sum.setText("¥" + orderSearchInfo.order_amount);

        if(orderSearchInfo.orderinfo.size() == 1){
            holder.tv_showdetail.setVisibility(View.GONE);
        }else {
            holder.tv_showdetail.setVisibility(View.VISIBLE);
        }

        String saveExState = exmaps.get(position);
        if(TextUtils.isEmpty(saveExState)){
            holder.tv_showdetail.setTag(STATE_EX.UN_EX);
        }else {
            holder.tv_showdetail.setTag(saveExState);
        }

        String curExState = (String) holder.tv_showdetail.getTag();
        if(STATE_EX.EX.equals(curExState)){
            exRcList.clear();
            holder.tv_showdetail.setText("收起商品清单∧");
            exRcList.addAll(orderSearchInfo.orderinfo);
            orderListItemRCAdapter.notifyDataSetChanged();
        }else if (STATE_EX.UN_EX.equals(curExState)){
            exRcList.clear();
            holder.tv_showdetail.setText("展开商品清单∨");
            exRcList.addAll(orderSearchInfo.orderinfo.subList(0, 1));
            orderListItemRCAdapter.notifyDataSetChanged();
        }

        holder.tv_showdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                exRcList.clear();
                if(STATE_EX.UN_EX.equals(tag)){
                    v.setTag(STATE_EX.EX);
                    exmaps.put(position, STATE_EX.EX);
                    holder.tv_showdetail.setText("收起商品清单∧");
                    exRcList.addAll(orderSearchInfo.orderinfo);
                    orderListItemRCAdapter.notifyDataSetChanged();
                }else if(STATE_EX.EX.equals(tag)){
                    v.setTag(STATE_EX.UN_EX);
                    exmaps.put(position, STATE_EX.UN_EX);
                    holder.tv_showdetail.setText("展开商品清单∨");
                    exRcList.addAll(orderSearchInfo.orderinfo.subList(0, 1));
                    orderListItemRCAdapter.notifyDataSetChanged();
                }

            }
        });


        Integer saveBtnType = btnmapList.get(orderSearchInfo.o_id);
        if(saveBtnType != null){
            initBtnType(saveBtnType, holder);
        }else {
            initBtnType(orderSearchInfo.btn_type, holder);
        }


//        // 0所有按钮都不显示，
//        // 1取消、去支付(未付款),
//        // 2取消（货到付款未出库），
//        // 3确认收货（已发货）,
//        // 4去评价(已收货、未评价)，
//        // 5删除（取消订单、已完成评价订单）
//        switch (orderSearchInfo.btn_type) {
//            case 0:
//                holder.btn_cancel_nopay.setVisibility(View.GONE);
//                holder.btn_go_pay.setVisibility(View.GONE);
//                holder.btn_cancel_noout.setVisibility(View.GONE);
//                holder.btn_confirm_get.setVisibility(View.GONE);
//                holder.btn_go_say.setVisibility(View.GONE);
//                holder.btn_delete.setVisibility(View.GONE);
//                break;
//            case 1:
//                holder.btn_cancel_nopay.setVisibility(View.GONE);
//                holder.btn_go_pay.setVisibility(View.VISIBLE);
//                holder.btn_cancel_noout.setVisibility(View.GONE);
//                holder.btn_confirm_get.setVisibility(View.GONE);
//                holder.btn_go_say.setVisibility(View.GONE);
//                holder.btn_delete.setVisibility(View.GONE);
//                //去支付(未付款),
//                holder.btn_go_pay.setOnClickListener(this);
//                break;
//            case 2:
//                holder.btn_cancel_nopay.setVisibility(View.GONE);
//                holder.btn_go_pay.setVisibility(View.GONE);
//                holder.btn_cancel_noout.setVisibility(View.VISIBLE);
//                holder.btn_confirm_get.setVisibility(View.GONE);
//                holder.btn_go_say.setVisibility(View.GONE);
//                holder.btn_delete.setVisibility(View.GONE);
//                // 取消 货到付款未出库 的订单
//                holder.btn_cancel_noout.setOnClickListener(this);
//                break;
//            case 3:
//                holder.btn_cancel_nopay.setVisibility(View.GONE);
//                holder.btn_go_pay.setVisibility(View.GONE);
//                holder.btn_cancel_noout.setVisibility(View.GONE);
//                holder.btn_confirm_get.setVisibility(View.VISIBLE);
//                holder.btn_go_say.setVisibility(View.GONE);
//                holder.btn_delete.setVisibility(View.GONE);
//                // 确认收货（已发货）
//                holder.btn_confirm_get.setOnClickListener(this);
//                break;
//            case 4:
//                holder.btn_cancel_nopay.setVisibility(View.GONE);
//                holder.btn_go_pay.setVisibility(View.GONE);
//                holder.btn_cancel_noout.setVisibility(View.GONE);
//                holder.btn_confirm_get.setVisibility(View.GONE);
//                holder.btn_go_say.setVisibility(View.VISIBLE);
//                holder.btn_delete.setVisibility(View.GONE);
//                //去评价
//                holder.btn_go_say.setOnClickListener(this);
//                break;
//            case 5:
//                holder.btn_cancel_nopay.setVisibility(View.GONE);
//                holder.btn_go_pay.setVisibility(View.GONE);
//                holder.btn_cancel_noout.setVisibility(View.GONE);
//                holder.btn_confirm_get.setVisibility(View.GONE);
//                holder.btn_go_say.setVisibility(View.GONE);
//                holder.btn_delete.setVisibility(View.VISIBLE);
//                //删除 取消的订单、已完成评价的订单
//                holder.btn_delete.setOnClickListener(this);
//                break;
//        }

        //取消未付款订单
        holder.btn_cancel_nopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNoPayDialog(position, holder, orderSearchInfo);
            }
        });

        //去支付
        holder.btn_go_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(context, "此功能暂未开放");
            }
        });

        //取消未发货订单
        holder.btn_cancel_noout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelNoOutDialog(position, holder, orderSearchInfo.o_id);
            }
        });

        //确认收货
        holder.btn_confirm_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmGetDialog(position, holder, orderSearchInfo.o_id);
            }
        });

        //去评价
        holder.btn_go_say.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AppraiseActivity.class);
                intent.putExtra("o_id", orderSearchInfo.o_id);
                context.startActivity(intent);
            }
        });

        //删除订单
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(position, holder, orderSearchInfo.o_id);
            }
        });

    }

    private void initBtnType(int btn_type, MyViewHolder holder) {
        // 0所有按钮都不显示，
        // 1取消、去支付(未付款),
        // 2取消（货到付款未出库），
        // 3确认收货（已发货）,
        // 4去评价(已收货、未评价)，
        // 5删除（取消订单、已完成评价订单）
        switch (btn_type) {
            case 0:
                holder.btn_cancel_nopay.setVisibility(View.GONE);
                holder.btn_go_pay.setVisibility(View.GONE);
                holder.btn_cancel_noout.setVisibility(View.GONE);
                holder.btn_confirm_get.setVisibility(View.GONE);
                holder.btn_go_say.setVisibility(View.GONE);
                holder.btn_delete.setVisibility(View.GONE);
                break;
            case 1:
                holder.btn_cancel_nopay.setVisibility(View.GONE);
                holder.btn_go_pay.setVisibility(View.VISIBLE);
                holder.btn_cancel_noout.setVisibility(View.GONE);
                holder.btn_confirm_get.setVisibility(View.GONE);
                holder.btn_go_say.setVisibility(View.GONE);
                holder.btn_delete.setVisibility(View.GONE);
                //去支付(未付款),
                holder.btn_go_pay.setOnClickListener(this);
                break;
            case 2:
                holder.btn_cancel_nopay.setVisibility(View.GONE);
                holder.btn_go_pay.setVisibility(View.GONE);
                holder.btn_cancel_noout.setVisibility(View.VISIBLE);
                holder.btn_confirm_get.setVisibility(View.GONE);
                holder.btn_go_say.setVisibility(View.GONE);
                holder.btn_delete.setVisibility(View.GONE);
                // 取消 货到付款未出库 的订单
                holder.btn_cancel_noout.setOnClickListener(this);
                break;
            case 3:
                holder.btn_cancel_nopay.setVisibility(View.GONE);
                holder.btn_go_pay.setVisibility(View.GONE);
                holder.btn_cancel_noout.setVisibility(View.GONE);
                holder.btn_confirm_get.setVisibility(View.VISIBLE);
                holder.btn_go_say.setVisibility(View.GONE);
                holder.btn_delete.setVisibility(View.GONE);
                // 确认收货（已发货）
                holder.btn_confirm_get.setOnClickListener(this);
                break;
            case 4:
                holder.btn_cancel_nopay.setVisibility(View.GONE);
                holder.btn_go_pay.setVisibility(View.GONE);
                holder.btn_cancel_noout.setVisibility(View.GONE);
                holder.btn_confirm_get.setVisibility(View.GONE);
                holder.btn_go_say.setVisibility(View.VISIBLE);
                holder.btn_delete.setVisibility(View.GONE);
                //去评价
                holder.btn_go_say.setOnClickListener(this);
                break;
            case 5:
                holder.btn_cancel_nopay.setVisibility(View.GONE);
                holder.btn_go_pay.setVisibility(View.GONE);
                holder.btn_cancel_noout.setVisibility(View.GONE);
                holder.btn_confirm_get.setVisibility(View.GONE);
                holder.btn_go_say.setVisibility(View.GONE);
                holder.btn_delete.setVisibility(View.VISIBLE);
                //删除 取消的订单、已完成评价的订单
                holder.btn_delete.setOnClickListener(this);
                break;
        }

    }

    private CustomSimpleDialog confirmGetDialog;
    private void showConfirmGetDialog(final int position, final MyViewHolder holder, final String o_id) {
        confirmGetDialog = new CustomSimpleDialog.Builder(context)
                .setMessage("确定已经收到该货了吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        MyHttp.orderConfirm(http, null, o_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                                int code = response.optInt("code");
                                if(code != 0){
                                    ToastUtil.showToast(context, response.optString("msg"));
                                    return;
                                }
                                holder.btn_confirm_get.setVisibility(View.GONE);
                                holder.btn_go_say.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(context, AppraiseActivity.class);
                                intent.putExtra("o_id", o_id);
                                context.startActivity(intent);
//                                notifyItemChanged(position);
//                                btnmaps.put(position, 4);
                                btnmapList.put(o_id, 4);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                })
                .create();
        confirmGetDialog.show();
    }

    private CustomSimpleDialog cancelNoPayDialog;
    private void cancelNoPayDialog(final int position, final MyViewHolder holder, final OrderResultInfo.OrderSearchInfo orderSearchInfo) {
        Log.d("OrderListposition", position + ":position cancelNoPayDialog");
        cancelNoPayDialog = new CustomSimpleDialog.Builder(context)
                .setMessage("确定要取消该订单吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        MyHttp.cancelOrder(http, null, orderSearchInfo.o_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                                dialog.cancel();
                                int code = response.optInt("code");
                                if(code != 0){
                                    ToastUtil.showToast(context, response.optString("msg"));
                                    return;
                                }
                                holder.btn_cancel_nopay.setVisibility(View.GONE);
                                holder.btn_delete.setVisibility(View.VISIBLE);
//                                orderSearchInfo.btn_type = 5;
//                                btnmaps.put(position, 5);
                                btnmapList.put(orderSearchInfo.o_id, 4);
//                                notifyItemChanged(position);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                })
                .create();
        cancelNoPayDialog.show();

    }



    private CustomSimpleDialog cancelNoOutDialog;
    private void showCancelNoOutDialog(final int position, final MyViewHolder holder, final String o_id) {

        cancelNoOutDialog = new CustomSimpleDialog.Builder(context)
                .setMessage("确定要取消该订单吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        MyHttp.cancelOrder(http, null, o_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                                dialog.cancel();
                                int code = response.optInt("code");
                                if(code != 0){
                                    ToastUtil.showToast(context, response.optString("msg"));
                                    return;
                                }
                                holder.btn_cancel_noout.setVisibility(View.GONE);
                                holder.btn_delete.setVisibility(View.VISIBLE);
//                                notifyItemChanged(position);
//                                btnmaps.put(position, 5);
                                btnmapList.put(o_id, 5);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                })
                .create();
        cancelNoOutDialog.show();

    }

    private CustomSimpleDialog deleteDialog;
    private void showDeleteDialog(final int position, final MyViewHolder holder, final String o_id) {
        deleteDialog = new CustomSimpleDialog.Builder(context)
                .setMessage("确定要删除该订单吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        MyHttp.orderDelete(http, null, o_id, new HttpForVolley.HttpTodo() {
                            @Override
                            public void httpTodo(Integer which, JSONObject response) {
                                dialog.cancel();
                                int code = response.optInt("code");
                                if(code != 0){
                                    ToastUtil.showToast(context, response.optString("msg"));
                                    return;
                                }
                                orderSearchInfos.remove(position);
                                notifyItemRemoved(position);
                                holder.btn_cancel_nopay.setVisibility(View.GONE);
                                holder.btn_go_pay.setVisibility(View.GONE);
                                holder.btn_cancel_noout.setVisibility(View.GONE);
                                holder.btn_confirm_get.setVisibility(View.GONE);
                                holder.btn_go_say.setVisibility(View.GONE);
                                holder.btn_delete.setVisibility(View.GONE);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                })
                .create();
        deleteDialog.show();

    }

    @Override
    public int getItemCount() {
        return orderSearchInfos.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_pay://去支付(未付款),
                break;
            case R.id.btn_cancel_noout://取消 货到付款未出库 的订单
                break;
            case R.id.btn_confirm_get://确认收货（已发货）,
                break;
            case R.id.btn_go_say://去评价
                break;
            case R.id.btn_delete://删除 取消的订单、已完成评价的订单）
                break;
            default:
                break;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_time;
        private TextView tv_result_status;
        private TextView tv_order_count;
        private TextView tv_order_sum;
        private Button btn_cancel_nopay;//未付款取消按钮
        private Button btn_go_pay;//去支付按钮
        private Button btn_cancel_noout;//未出库取消按钮
        private Button btn_confirm_get;//确认收货按钮
        private Button btn_go_say;//去评价按钮
        private RecyclerView rv_ordergoods;//取消订单 已完成评价订单 删除按钮
        private Button btn_delete;//取消订单 已完成评价订单 删除按钮
//        private ImageView iv_goods;
//        private TextView tv_name;
//        private TextView tv_brand;
//        private TextView tv_size;
//        private TextView tv_price;
//        private TextView tv_count;
//        private LinearLayout include_item_order_sumbit;
        private TextView tv_showdetail;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_result_status = (TextView) itemView.findViewById(R.id.tv_result_status);
            rv_ordergoods = (RecyclerView) itemView.findViewById(R.id.rv_ordergoods);
//            include_item_order_sumbit = (LinearLayout) itemView.findViewById(R.id.include_item_order_sumbit);
            tv_order_count = (TextView) itemView.findViewById(R.id.tv_order_count);
            tv_order_sum = (TextView) itemView.findViewById(R.id.tv_order_sum);
            btn_cancel_nopay = (Button) itemView.findViewById(R.id.btn_cancel_nopay);
            btn_go_pay = (Button) itemView.findViewById(R.id.btn_go_pay);
            btn_cancel_noout = (Button) itemView.findViewById(R.id.btn_cancel_noout);
            btn_confirm_get = (Button) itemView.findViewById(R.id.btn_confirm_get);
            btn_go_say = (Button) itemView.findViewById(R.id.btn_go_say);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
//            iv_goods = (ImageView) itemView.findViewById(R.id.iv_goods);
//            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
//            tv_brand = (TextView) itemView.findViewById(R.id.tv_brand);
//            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
//            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
//            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            tv_showdetail = (TextView) itemView.findViewById(R.id.tv_showdetail);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position, OrderResultInfo.OrderSearchInfo orderSearchInfo);
    }
}
