package com.msqsoft.aboutapp.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseFragment;
import com.msqsoft.aboutapp.config.Config;
import com.msqsoft.aboutapp.model.BannerBean;
import com.msqsoft.aboutapp.model.OrderDetailBean;
import com.msqsoft.aboutapp.model.OrderListBean;
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;
import com.msqsoft.aboutapp.service.MyObserver;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.ui.adapter.OrderListAdapter;
import com.msqsoft.aboutapp.utils.ToastMaster;
import com.msqsoft.aboutapp.widget.imageloader.GlideCircleTransform;
import com.msqsoft.aboutapp.widget.imageloader.ImageLoaderFactory;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * 周边订单
 */

public class OrderFragment extends BaseFragment {

    private static final String ARGUMENT = "argument";

    private View mRootView;
    private SwipeRefreshLayout srlRefresh;
    private AppBarLayout mAppbarLayout;
    private Banner mBanner;
    private LinearLayout llMyOrder;
    private RelativeLayout rlOrderMore;
    private TextView tvTipsOrder;
    private ImageView ivOrderMore;
    private TextView tvOrderMore;
    private View mMyOrderView;
    private RelativeLayout rlAroundOrder;
    private TextView tvCourierMore;
    private ImageView ivCourierMore;
    private RelativeLayout rlTabType;
    private TextView tvTabType;
    private RelativeLayout rlTabStatus;
    private TextView tvTabStatus;
    private RelativeLayout rlTabCompany;
    private TextView tvTabCompany;
    private RecyclerView rvList;

    private OrderDetailBean mMyOrder;
    private int mPage = 1;

    private List<OrderDetailBean> mOrderList;
    private OrderListAdapter mOrderAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mNoMoreData = false;
    private int mLastVisibleItemPosition = 0;

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_action1:
                    ToastMaster.toast("扫一扫");
                    break;
                case R.id.iv_toolbar_action2:
                    final Intent i = new Intent(getActivity(), ReleaseOrderActivity.class);
                    startActivity(i);
                    break;
                case R.id.rl_order_more:
                    ToastMaster.toast(getString(R.string.text_tips_more_my_order));
                    break;
                case R.id.rl_around_order:
                    ToastMaster.toast(getString(R.string.text_tips_near_courier));
                    break;
                case R.id.rl_tab_type:
                    mAppbarLayout.setExpanded(false);
                    rlTabType.setSelected(true);
                    rlTabStatus.setSelected(false);
                    rlTabCompany.setSelected(false);
                    break;
                case R.id.rl_tab_status:
                    mAppbarLayout.setExpanded(false);
                    rlTabType.setSelected(false);
                    rlTabStatus.setSelected(true);
                    rlTabCompany.setSelected(false);
                    break;
                case R.id.rl_tab_company:
                    mAppbarLayout.setExpanded(false);
                    rlTabType.setSelected(false);
                    rlTabStatus.setSelected(false);
                    rlTabCompany.setSelected(true);
                    break;
            }
        }
    };

    private SwipeRefreshLayout.OnRefreshListener mRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };

    private final RecyclerView.OnScrollListener mListScroll = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            final int itemCount = mOrderAdapter == null ? 0 : mOrderAdapter.getItemCount();
            if (!srlRefresh.isRefreshing() && newState == RecyclerView.SCROLL_STATE_IDLE
                    && mLastVisibleItemPosition + 1 == itemCount) {
                if (!mNoMoreData) {
                    getAroundOrderList();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mLastVisibleItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
        }
    };

    private AppBarLayout.OnOffsetChangedListener offsetChange = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (verticalOffset >= 0) {
                srlRefresh.setEnabled(true);
            } else {
                srlRefresh.setEnabled(false);
            }
        }
    };

    private MyObserver<ServiceResult<List<OrderListBean>>> mMyObserver = new MyObserver<ServiceResult<List<OrderListBean>>>() {
        @Override
        public void onSuccess(@NonNull ServiceResult<List<OrderListBean>> result) {
            final List<OrderListBean> orderList = result.getResultData();
            if (orderList != null && orderList.size() > 0) {
                final List<OrderDetailBean> orderDetail = orderList.get(0).getOrder();
                if (orderDetail != null && orderDetail.size() > 0) {
                    final OrderDetailBean order = orderDetail.get(0);
                    if (order != null) {
                        mMyOrder = order;
                        initMyOrderView();
                        llMyOrder.setVisibility(View.VISIBLE);
                        return;
                    }
                }
            }
            llMyOrder.setVisibility(View.GONE);
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
            refreshData();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAppbarLayout.removeOnOffsetChangedListener(offsetChange);
        rvList.removeOnScrollListener(mListScroll);
    }

    private void initData() {
        mOrderList = new ArrayList<>();
        mOrderAdapter = new OrderListAdapter(mActivity, mOrderList);
        mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) mRootView.findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) mRootView.findViewById(R.id.tv_toolbar_title);
        final ImageView ivAction1 = (ImageView) mRootView.findViewById(R.id.iv_toolbar_action1);
        final ImageView ivAction2 = (ImageView) mRootView.findViewById(R.id.iv_toolbar_action2);

        ivBack.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.title_order));
        ivAction1.setVisibility(View.VISIBLE);
        ivAction1.setOnClickListener(mClick);
        ivAction2.setVisibility(View.VISIBLE);
        ivAction2.setOnClickListener(mClick);
    }

    private void initView() {
        initToolbar();

        srlRefresh = (SwipeRefreshLayout) mRootView.findViewById(R.id.srl_refresh);
        mAppbarLayout = (AppBarLayout) mRootView.findViewById(R.id.appbar);
        mBanner = (Banner) mRootView.findViewById(R.id.banner);
        llMyOrder = (LinearLayout) mRootView.findViewById(R.id.ll_my_order);
        rlOrderMore = (RelativeLayout) mRootView.findViewById(R.id.rl_order_more);
        tvTipsOrder = (TextView) mRootView.findViewById(R.id.tv_tips_order);
        ivOrderMore = (ImageView) mRootView.findViewById(R.id.iv_order_more);
        tvOrderMore = (TextView) mRootView.findViewById(R.id.tv_order_more);
        rlAroundOrder = (RelativeLayout) mRootView.findViewById(R.id.rl_around_order);
        tvCourierMore = (TextView) mRootView.findViewById(R.id.tv_courier_more);
        ivCourierMore = (ImageView) mRootView.findViewById(R.id.iv_courier_more);
        rlTabType = (RelativeLayout) mRootView.findViewById(R.id.rl_tab_type);
        tvTabType = (TextView) mRootView.findViewById(R.id.tv_tab_type);
        rlTabStatus = (RelativeLayout) mRootView.findViewById(R.id.rl_tab_status);
        tvTabStatus = (TextView) mRootView.findViewById(R.id.tv_tab_status);
        rlTabCompany = (RelativeLayout) mRootView.findViewById(R.id.rl_tab_company);
        tvTabCompany = (TextView) mRootView.findViewById(R.id.tv_tab_company);
        rvList = (RecyclerView) mRootView.findViewById(R.id.rv_list);

        srlRefresh.setOnRefreshListener(mRefresh);
        mAppbarLayout.addOnOffsetChangedListener(offsetChange);
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

        rlAroundOrder.setOnClickListener(mClick);
        rlTabType.setOnClickListener(mClick);
        rlTabStatus.setOnClickListener(mClick);
        rlTabCompany.setOnClickListener(mClick);

        DividerItemDecoration decoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL);
        rvList.addItemDecoration(decoration);
        rvList.setLayoutManager(mLayoutManager);
        rvList.setAdapter(mOrderAdapter);
        rvList.addOnScrollListener(mListScroll);
    }

    public void refreshData() {
        getMyOrder();
        mPage = 1;
        mNoMoreData = false;
        getAroundOrderList();
    }

    private void getMyOrder() {
        if (isLogin()) {
            final UserInfoDetailBean userInfo = getUserInfo();
            final String userType = userInfo.getUser_type();
            tvCourierMore.setVisibility(View.VISIBLE);
            ivCourierMore.setVisibility(View.VISIBLE);
            if (mMyOrderView == null) {
                mMyOrderView = LayoutInflater.from(mActivity).inflate(R.layout.item_main_my_order_list, llMyOrder);
            }

            if ("3".equals(userType)) {
                initCourierView();
                getReceiveOrderList();
            } else {
                initNormalUserView();
                getMyOrderList();
            }
        }
    }

    private void initCourierView() {
        rlOrderMore.setOnClickListener(mClick);
        tvTipsOrder.setVisibility(View.VISIBLE);
        tvTipsOrder.setText(getString(R.string.text_tips_receive_order));
        tvOrderMore.setVisibility(View.VISIBLE);
        tvOrderMore.setText(getString(R.string.text_tips_more_my_order));
        ivOrderMore.setVisibility(View.VISIBLE);

    }

    private void initNormalUserView() {
        tvTipsOrder.setVisibility(View.GONE);
        tvOrderMore.setVisibility(View.GONE);
        ivOrderMore.setVisibility(View.GONE);

    }

    private void initMyOrderView() {
        final ImageView ivImage = (ImageView) mMyOrderView.findViewById(R.id.iv_image);
        final TextView tvTitle = (TextView) mMyOrderView.findViewById(R.id.tv_title);
        final LinearLayout llPlace = (LinearLayout) mMyOrderView.findViewById(R.id.ll_place);
        final TextView tvPlaceFrom = (TextView) mMyOrderView.findViewById(R.id.tv_place_from);
        final TextView tvPlaceTo = (TextView) mMyOrderView.findViewById(R.id.tv_place_to);
        final TextView tvSubTitle = (TextView) mMyOrderView.findViewById(R.id.tv_sub_title);
        final TextView tvStatus = (TextView) mMyOrderView.findViewById(R.id.tv_status);
        final TextView tvTips = (TextView) mMyOrderView.findViewById(R.id.tv_tips);

        final UserInfoDetailBean userInfo = getUserInfo();
        final String userType = userInfo.getUser_type();
        final String imageUrl = mMyOrder.getAvatar();
        final String title;
        final String subTitle;
        final String status;
        final String tips;
        if ("3".equals(userType)) {
            title = mMyOrder.getSender_area_name();
            subTitle = mMyOrder.getSender_area_detail();
            status = "";
            tips = mMyOrder.getOrderStatus();

            llPlace.setVisibility(View.GONE);
        } else {
            title = String.format(getString(R.string.text_item_order_number), mMyOrder.getCode());
            subTitle = mMyOrder.getSender_area_detail();
            status = mMyOrder.getOrderStatus();
            tips = mMyOrder.getCreate_time();

            llPlace.setVisibility(View.VISIBLE);
            tvPlaceFrom.setText(mMyOrder.getSender_area_name());
            tvPlaceTo.setText(mMyOrder.getRecipient_area_name());
        }
        ImageLoaderFactory.getLoader().loadImageCenterCrop(mActivity, ivImage, imageUrl, new GlideCircleTransform());
        tvTitle.setText(title);
        tvSubTitle.setText(subTitle);
        tvStatus.setText(status);
        tvTips.setText(tips);
    }

    private void getBannerList() {
        ServiceClient.getService().getBannerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new MyObserver<ServiceResult<List<BannerBean>>>() {
                            @Override
                            public void onSuccess(ServiceResult<List<BannerBean>> result) {
                                final List<BannerBean> bannerList = result.getResultData();
                                if (bannerList != null && bannerList.size() > 0) {
                                    mBanner.setImages(bannerList).start();
                                }
                            }
                        });
    }

    private void getMyOrderList() {
        if (mMyOrderView != null) {
            final String token = getAboutAppToken();
            ServiceClient.getService().getMyOrderList(token, "1", "1", "", "", "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mMyObserver);
        }
    }

    private void getReceiveOrderList() {
        if (mMyOrderView != null) {
            final String token = getAboutAppToken();
            ServiceClient.getService().getReceiveOrderList(token, "1", "1", "", "", "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mMyObserver);
        }
    }

    private void getAroundOrderList() {
        if (isLogin()) {
            srlRefresh.setRefreshing(true);
            final String token = getAboutAppToken();
            final String page = String.valueOf(mPage);
            final String pageSize = String.valueOf(Config.MAX_PAGE_SIZE);
            final String type = "";
            final String status = "1";
            final String companyId = "";
            final String publishWay = "";
            ServiceClient.getService().getAroundOrderList(token, page, pageSize, status, publishWay, companyId, "282")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<ServiceResult<OrderListBean>>() {
                        @Override
                        public void onSuccess(@NonNull ServiceResult<OrderListBean> result) {
                            if (mPage == 1) {
                                mOrderList.clear();
                            }
                            final OrderListBean orderList = result.getResultData();
                            if (orderList != null) {
                                final List<OrderDetailBean> orderDetailList = orderList.getOrders();
                                if (orderDetailList != null && orderDetailList.size() > 0) {
                                    mOrderList.addAll(orderDetailList);

                                    if (PAGE_SIZE == mOrderList.size()) {
                                        mPage++;
                                    } else {
                                        mNoMoreData = true;
                                    }
                                } else {
                                    ToastMaster.toast(getString(R.string.toast_no_more_data));
                                }
                            }
                            mOrderAdapter.notifyDataSetChanged();
                            srlRefresh.setRefreshing(false);
                        }

                        @Override
                        public void onError(String errorMsg) {
                            super.onError(errorMsg);
                            ToastMaster.toast(errorMsg);
                            srlRefresh.setRefreshing(false);
                        }
                    });
        } else {
            srlRefresh.setRefreshing(false);
        }
    }
}
