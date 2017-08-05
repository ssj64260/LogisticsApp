package com.msqsoft.aboutapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;
import com.msqsoft.aboutapp.service.MyObserver;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.utils.PreferencesUtil;
import com.msqsoft.aboutapp.utils.ToastMaster;
import com.msqsoft.aboutapp.widget.imageloader.GlideCircleTransform;
import com.msqsoft.aboutapp.widget.imageloader.ImageLoaderFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 我的消息
 */

public class MineFragment extends BaseFragment {

    private static final int REQUEST_CODE_USER_SETTINGS = 1001;
    private static final String ARGUMENT = "argument";

    private View mRootView;
    private TextView tvUserId;
    private ImageView ivUserAvatar;
    private ImageView ivCertification;
    private ImageView ivUserSettings;
    private TextView tvUserName;
    private TextView tvUserSign;
    private RelativeLayout rlCourier;
    private TextView tvCourierCompany;
    private TextView tvCourierNumber;
    private LinearLayout llBindPhone;
    private LinearLayout llQRCode;
    private LinearLayout llMyOrder;
    private LinearLayout llMyMessage;
    private LinearLayout llMyDraft;
    private LinearLayout llMyWallet;
    private LinearLayout llManageAddress;
    private LinearLayout llRegistered;
    private LinearLayout llCourierOrder;
    private LinearLayout llService;

    private GlideCircleTransform mTransform;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_user_settings:
                    startActivityForResult(new Intent(mActivity, UserSettingsActivity.class), REQUEST_CODE_USER_SETTINGS);
                    break;
                case R.id.ll_bind_phone:
                    ToastMaster.toast(getString(R.string.text_button_bind));
                    break;
                case R.id.ll_qrcode:
                    ToastMaster.toast(getString(R.string.text_button_qrcode));
                    break;
                case R.id.ll_order:
                    ToastMaster.toast(getString(R.string.text_button_my_ordre));
                    break;
                case R.id.ll_personal_message:
                    ToastMaster.toast(getString(R.string.text_button_my_message));
                    break;
                case R.id.ll_draft:
                    ToastMaster.toast(getString(R.string.text_button_my_draft_box));
                    break;
                case R.id.ll_wallet:
                    ToastMaster.toast(getString(R.string.text_button_my_wallet));
                    break;
                case R.id.ll_address:
                    ToastMaster.toast(getString(R.string.text_button_manage_address));
                    break;
                case R.id.ll_registered:
                    ToastMaster.toast(getString(R.string.text_button_register_courier));
                    break;
                case R.id.ll_courier_order:
                    ToastMaster.toast(getString(R.string.text_button_courier_order));
                    break;
                case R.id.ll_service:
                    ToastMaster.toast(getString(R.string.text_button_customer_service));
                    break;
            }
        }
    };

    public MineFragment() {

    }

    public static MineFragment newInstance(String param) {
        MineFragment fragment = new MineFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, param);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTransform = new GlideCircleTransform();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_mine, null);
            initView();
            setData();
            getUserInfoDetail();
        }

        return mRootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_USER_SETTINGS) {
            setData();
        }
    }

    private void initView() {
        tvUserId = (TextView) mRootView.findViewById(R.id.tv_user_id);
        ivUserAvatar = (ImageView) mRootView.findViewById(R.id.iv_user_avatar);
        ivCertification = (ImageView) mRootView.findViewById(R.id.iv_certification);
        ivUserSettings = (ImageView) mRootView.findViewById(R.id.iv_user_settings);
        tvUserName = (TextView) mRootView.findViewById(R.id.tv_user_name);
        tvUserSign = (TextView) mRootView.findViewById(R.id.tv_user_sign);
        rlCourier = (RelativeLayout) mRootView.findViewById(R.id.rl_courier);
        tvCourierCompany = (TextView) mRootView.findViewById(R.id.tv_courier_company);
        tvCourierNumber = (TextView) mRootView.findViewById(R.id.tv_courier_number);
        llBindPhone = (LinearLayout) mRootView.findViewById(R.id.ll_bind_phone);
        llQRCode = (LinearLayout) mRootView.findViewById(R.id.ll_qrcode);
        llMyOrder = (LinearLayout) mRootView.findViewById(R.id.ll_order);
        llMyMessage = (LinearLayout) mRootView.findViewById(R.id.ll_personal_message);
        llMyDraft = (LinearLayout) mRootView.findViewById(R.id.ll_draft);
        llMyWallet = (LinearLayout) mRootView.findViewById(R.id.ll_wallet);
        llManageAddress = (LinearLayout) mRootView.findViewById(R.id.ll_address);
        llRegistered = (LinearLayout) mRootView.findViewById(R.id.ll_registered);
        llCourierOrder = (LinearLayout) mRootView.findViewById(R.id.ll_courier_order);
        llService = (LinearLayout) mRootView.findViewById(R.id.ll_service);
    }

    private void setData() {
        final UserInfoDetailBean userInfo = getUserInfo();
        setUserInfo(userInfo);
    }

    private void setUserInfo(UserInfoDetailBean userInfo) {
        final String id = userInfo.getId();
        final String avatarURL = userInfo.getAvatar();
        final String name = userInfo.getUser_nicename();
        final String sign = userInfo.getSignature();
        final String userType = userInfo.getUser_type();

        final String expressCompanyName = userInfo.getExpress_company_name();
        final String employeeNum = userInfo.getEmployee_num();

        tvUserId.setText(String.format(getString(R.string.text_user_id), id));
        ImageLoaderFactory
                .getLoader()
                .loadImageFitCenter(mActivity, ivUserAvatar, avatarURL, mTransform);
        tvUserName.setText(name);
        tvUserSign.setText(sign);

        if ("3".equals(userType)) {
            ivCertification.setVisibility(View.VISIBLE);
            rlCourier.setVisibility(View.VISIBLE);
            tvCourierCompany.setText(expressCompanyName);
            tvCourierNumber.setText(employeeNum);
            llRegistered.setVisibility(View.GONE);
            llCourierOrder.setVisibility(View.VISIBLE);
        }

        ivUserSettings.setOnClickListener(click);
        llBindPhone.setOnClickListener(click);
        llQRCode.setOnClickListener(click);
        llMyOrder.setOnClickListener(click);
        llMyMessage.setOnClickListener(click);
        llMyDraft.setOnClickListener(click);
        llMyWallet.setOnClickListener(click);
        llManageAddress.setOnClickListener(click);
        llRegistered.setOnClickListener(click);
        llCourierOrder.setOnClickListener(click);
        llService.setOnClickListener(click);
    }

    private void getUserInfoDetail() {
        final String currentUserId = PreferencesUtil.getString(Config.USER_INFO, Config.KEY_ABOUTAPP_USER_ID, "");
        final String token = PreferencesUtil.getString(Config.USER_INFO, Config.KEY_ABOUTAPP_TOKEN, "");
        if (!TextUtils.isEmpty(currentUserId)) {
            showProgress(getString(R.string.text_progress_loading));
            ServiceClient.getService().getUserInfoDetail(token, currentUserId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new MyObserver<ServiceResult<UserInfoDetailBean>>() {
                                @Override
                                public void onSuccess(@NonNull ServiceResult<UserInfoDetailBean> result) {
                                    final UserInfoDetailBean newUserInfo = result.getResultData();
                                    if (newUserInfo != null) {
                                        updateUserInfo(newUserInfo);
                                        updateCourierInfo(newUserInfo);
                                        setUserInfo(newUserInfo);
                                    }
                                    hideProgress();
                                }

                                @Override
                                public void onError(String errorMsg) {
                                    super.onError(errorMsg);
                                    hideProgress();
                                }

                                @Override
                                public void onTokenIncorrect() {
                                    hideProgress();
                                    doLoginOut();
                                }
                            });
        }
    }

}
