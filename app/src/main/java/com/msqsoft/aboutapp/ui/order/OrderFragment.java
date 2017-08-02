package com.msqsoft.aboutapp.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseFragment;
import com.msqsoft.aboutapp.utils.ToastMaster;

/**
 * 周边订单
 */

public class OrderFragment extends BaseFragment {

    private static final String ARGUMENT = "argument";

    private View mRootView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_order, null);
            initView();

            setData();
        }

        return mRootView;
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
    }

    private void setData() {

    }

}
