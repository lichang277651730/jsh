package com.cqfrozen.jsh.share;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cqfrozen.jsh.R;
import com.tencent.tauth.Tencent;
/**
 * 分享POP
 */
public class SharePop implements View.OnClickListener {
    public final static int TAG_SETTING = 1, TAG_APPRENTICE = 2, TAG_ARTICLE = 3;
    private Activity context;
    private String url;
    private String title = "";
    private String text;
    private String image_url;
    private ShareResult shareResult;
    private PopupWindow window;
    private static SharePop instance;
    private Bitmap image;
    private Tencent tencent;
    private int clickId = 0;
    private LinearLayout ll_qqhy;
    private LinearLayout ll_qqkj;

    public static SharePop getInstance() {
        if (instance == null) {
            synchronized (SharePop.class) {
                if (instance == null) {
                    instance = new SharePop();
                }
            }
        }
        return instance;
    }

    public SharePop showPop(Activity context, View view, String title, String url, String text,
                            Bitmap image, String image_url,
                            ShareResult shareResult) {
        this.image = image;
        this.image_url = image_url;
        this.shareResult = shareResult;
        this.title = title;
        this.context = context;
        this.url = url;
        this.text = text;
        tencent = Tencent.createInstance("1105787638", context.getApplicationContext());
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_share, null);
        LinearLayout ll_wxhy =  (LinearLayout) popView.findViewById(R.id.ll_wxhy);
        View v_mask =   popView.findViewById(R.id.v_mask);
        LinearLayout ll_pyq = (LinearLayout) popView.findViewById(R.id.ll_pyq);
        LinearLayout ll_xlwb = (LinearLayout) popView.findViewById(R.id.ll_xlwb);
        ll_qqhy = (LinearLayout) popView.findViewById(R.id.ll_qqhy);
        ll_qqkj = (LinearLayout) popView.findViewById(R.id.ll_qqkj);
        LinearLayout ll_link = (LinearLayout) popView.findViewById(R.id.ll_link);
        TextView tv_cancel = (TextView) popView.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(this);
        ll_wxhy.setOnClickListener(this);
        ll_pyq.setOnClickListener(this);
        ll_xlwb.setOnClickListener(this);
        ll_qqhy.setOnClickListener(this);
        ll_qqkj.setOnClickListener(this);
        ll_link.setOnClickListener(this);
        v_mask.setOnClickListener(this);

        window = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setTouchable(true);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.showAtLocation(view, Gravity.CENTER, 0, 0);
        return instance;
    }

    @Override
    public void onClick(View v) {
        clickId = v.getId();
        switch (v.getId()) {
            case R.id.ll_wxhy: //微信分享
                ShareUtilsWX.wxShare(context, 1, title, url, text, image);
                break;
            case R.id.ll_pyq: //朋友圈分享
                ShareUtilsWX.wxShare(context, 2, title, url, text, image);
                break;
            case R.id.ll_xlwb: //新浪微博
                ShareUtilsWX.wxShare(context, 2, title, url, text, image);
//                ShareUtilsWB.wbShare(context,title, text, url, image);
                break;
            case R.id.ll_qqhy: //QQ分享
                ShareUtilsQQ.ShareQQ(tencent, context, title, text, url, image_url);
                break;
            case R.id.ll_qqkj: //QQ空间分享
                ShareUtilsQQ.ShareQQZone(tencent, context, title, text, url, image_url);
                break;
            case R.id.ll_link: //复制到剪切板
                ClipboardManager cmb = (ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(url);
                Toast.makeText(context, "已经复制到粘贴板!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        window.dismiss();
    }

    public void setResult(int result) {
        switch (result) {
            case ShareResult.SUCCESS:
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                break;
            case ShareResult.CANCEL:
                Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
                break;
            case ShareResult.FAILED:
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
                break;
        }

        if (shareResult != null) {
            shareResult.shareResult(result);
        }
    }

    /**
     * 分享结果接口,在支付的Activity实现方法
     */
    public interface ShareResult {
        public static int SUCCESS = -1;
        public static int DOING = -2;
        public static int CANCEL = -3;
        public static int FAILED = -4;

        /**
         * @param result (int) <br>
         *               SUCCESS -1 支付成功<br>
         *               DOING -2 支付处理中<br>
         *               CANCEL -3 支付取消<br>
         *               FAILED -4 支付失败<br>
         */
        public void shareResult(int result);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (null != window && (clickId == ll_qqhy.getId() || clickId ==
                    ll_qqkj.getId())) {
                tencent.onActivityResultData(requestCode, resultCode, data, ShareUtilsQQ.listener);
                clickId = 0;
            }
        } catch (Exception e) {
        }
    }
}
