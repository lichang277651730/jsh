package com.cqfrozen.jsh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.base.BaseFragment;
import com.cqfrozen.jsh.R;
import com.cqfrozen.jsh.entity.GoodDetailResultInfo;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2016/12/2.
 */
public class ShowImgsFragment extends BaseFragment {

    private GoodDetailResultInfo.PicsInfo picsInfo;
    private PhotoView photo_view;

    public static ShowImgsFragment getInstance(GoodDetailResultInfo.PicsInfo picsInfo) {
        ShowImgsFragment fragment = new ShowImgsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("picsInfo", picsInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        if(view == null){
            getBundleData(getArguments());
            view = inflater.inflate(R.layout.fragment_showimgs, container, false);
        }
        return view;
    }

    private void getBundleData(Bundle bundle) {
        this.picsInfo = (GoodDetailResultInfo.PicsInfo) bundle.getSerializable("picsInfo");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        photo_view = (PhotoView) view.findViewById(R.id.photo_view);
    }

    private void initData() {
        ViewCompat.setTransitionName(photo_view, picsInfo.pic_url);
        Picasso.with(mActivity).load(picsInfo.pic_url).into(photo_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(mActivity).load(picsInfo.pic_url).into(photo_view);
    }
}
