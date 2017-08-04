package com.msqsoft.aboutapp.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseFragment;
import com.msqsoft.aboutapp.model.BannerBean;
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.utils.ToastMaster;
import com.msqsoft.aboutapp.widget.imageloader.ImageLoaderFactory;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 周边订单
 */

public class OrderFragment extends BaseFragment {

    private static final String ARGUMENT = "argument";

    private View mRootView;
    private Banner mBanner;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_action1:
                    ToastMaster.toast("扫一扫");
                    break;
                case R.id.iv_toolbar_action2:
                    ToastMaster.toast("发布订单");
                    break;
            }
        }
    };

    public OrderFragment() {

    }

    public static OrderFragment newInstance(String param) {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, param);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_order, null);

            initView();
            getBannerList();
        }

        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    private void initData() {

    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) mRootView.findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) mRootView.findViewById(R.id.tv_toolbar_title);
        final ImageView ivAction1 = (ImageView) mRootView.findViewById(R.id.iv_toolbar_action1);
        final ImageView ivAction2 = (ImageView) mRootView.findViewById(R.id.iv_toolbar_action2);

        ivBack.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.title_order));
        ivAction1.setVisibility(View.VISIBLE);
        ivAction1.setOnClickListener(click);
        ivAction2.setVisibility(View.VISIBLE);
        ivAction2.setOnClickListener(click);
    }

    private void initView() {
        initToolbar();

        mBanner = (Banner) mRootView.findViewById(R.id.banner);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                if (path instanceof BannerBean) {
                    final BannerBean banner = (BannerBean) path;
                    ImageLoaderFactory.getLoader().loadImageCenterCrop(mActivity, imageView, banner.getSlide_pic());
                }
            }
        });
    }

    private void getBannerList() {
        //TODO 封装
        ServiceClient.getService().getBannerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<ServiceResult<List<BannerBean>>>() {
                            @Override
                            public void accept(@NonNull ServiceResult<List<BannerBean>> result) throws Exception {
                                if ("100".equals(result.getResultCode())) {
                                    final List<BannerBean> bannerList = result.getResultData();
                                    if (bannerList != null && bannerList.size() > 0) {
                                        mBanner.setImages(bannerList).start();
                                    }
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        });
    }

}
