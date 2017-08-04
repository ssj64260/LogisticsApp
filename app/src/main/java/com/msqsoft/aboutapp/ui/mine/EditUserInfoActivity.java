package com.msqsoft.aboutapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseAppCompatActivity;
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.utils.ToastMaster;
import com.msqsoft.aboutapp.widget.imageloader.GlideCircleTransform;
import com.msqsoft.aboutapp.widget.imageloader.ImageLoaderFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 修改个人资料
 */

public class EditUserInfoActivity extends BaseAppCompatActivity {

    private static final int REQUEST_CODE_NICKNAME = 1001;
    private static final int REQUEST_CODE_SIGN = 1002;

    private ImageView ivUserAvatar;
    private RelativeLayout rlNickname;
    private TextView tvNickname;
    private TextView tvUserId;
    private RelativeLayout rlUserSign;
    private TextView tvUserSign;
    private TextView tvSave;

    private GlideCircleTransform mTransform;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    onBackPressed();
                    break;
                case R.id.iv_user_avatar:

                    break;
                case R.id.rl_user_nickname:
                    toEditInformation(REQUEST_CODE_NICKNAME);
                    break;
                case R.id.rl_user_sign:
                    toEditInformation(REQUEST_CODE_SIGN);
                    break;
                case R.id.tv_save:
                    updateUserInfo();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        initData();
        initView();
        getUserInfoDetail();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            final String resultData = data.getStringExtra(EditInformationActivity.RESULT_DATA);
            if (requestCode == REQUEST_CODE_NICKNAME) {
                tvNickname.setText(resultData);
            } else if (requestCode == REQUEST_CODE_SIGN) {
                tvUserSign.setText(resultData);
            }
        }
    }

    private void initData() {
        mTransform = new GlideCircleTransform();
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(click);
        tvTitle.setText(getString(R.string.title_edit_user_information));
    }

    private void initView() {
        initToolbar();
        ivUserAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        rlNickname = (RelativeLayout) findViewById(R.id.rl_user_nickname);
        tvNickname = (TextView) findViewById(R.id.tv_user_nickname);
        tvUserId = (TextView) findViewById(R.id.tv_user_id);
        rlUserSign = (RelativeLayout) findViewById(R.id.rl_user_sign);
        tvUserSign = (TextView) findViewById(R.id.tv_user_sign);
        tvSave = (TextView) findViewById(R.id.tv_save);
    }

    private void setUserInfo(UserInfoDetailBean userInfo) {
        final String id = userInfo.getId();
        final String avatarURL = userInfo.getAvatar();
        final String name = userInfo.getUser_nicename();
        final String sign = userInfo.getSignature();

        ImageLoaderFactory
                .getLoader()
                .loadImageFitCenter(this, ivUserAvatar, avatarURL, mTransform);
        tvNickname.setText(name);
        tvUserId.setText(id);
        tvUserSign.setText(sign);

        ivUserAvatar.setOnClickListener(click);
        rlNickname.setOnClickListener(click);
        rlUserSign.setOnClickListener(click);
        tvSave.setOnClickListener(click);
    }

    private void toEditInformation(int requestCode) {
        final String defaultData;
        final String type;
        if (requestCode == REQUEST_CODE_NICKNAME) {
            defaultData = tvNickname.getText().toString();
            type = EditInformationActivity.ACTIVITY_TYPE_NICKNAME;
        } else {
            defaultData = tvUserSign.getText().toString();
            type = EditInformationActivity.ACTIVITY_TYPE_SIGN;
        }
        Intent intent = new Intent();
        intent.setClass(EditUserInfoActivity.this, EditInformationActivity.class);
        intent.putExtra(EditInformationActivity.ACTIVITY_TYPE, type);
        intent.putExtra(EditInformationActivity.DEFAULT_DATA, defaultData);
        startActivityForResult(intent, requestCode);
    }

    private void getUserInfoDetail() {
        final UserInfoDetailBean userInfo = getUserInfo();
        if (userInfo != null) {
            final String currentUserId = userInfo.getId();
            final String token = userInfo.getAccess_token();
            if (!TextUtils.isEmpty(currentUserId)) {
                showProgress(getString(R.string.text_progress_loading));
                ServiceClient.getService().getUserInfoDetail(token, currentUserId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Consumer<ServiceResult<UserInfoDetailBean>>() {
                                    @Override
                                    public void accept(@NonNull ServiceResult<UserInfoDetailBean> result) throws Exception {
                                        if ("100".equals(result.getResultCode())) {
                                            final UserInfoDetailBean newUserInfo = result.getResultData();
                                            if (newUserInfo != null) {
                                                setUserInfo(newUserInfo);
                                            }
                                        } else {
                                            ToastMaster.toast(result.getResultMsg());
                                        }
                                        hideProgress();
                                    }
                                },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {
                                        ToastMaster.toast(getString(R.string.toast_get_user_information_error));
                                        hideProgress();
                                    }
                                });
            }
        }
    }

    private void updateUserInfo() {
        final UserInfoDetailBean userInfo = getUserInfo();
        if (userInfo != null) {
            final String token = userInfo.getAccess_token();
            final String nickname = tvNickname.getText().toString();
            final String sign = tvUserSign.getText().toString();
            showProgress(getString(R.string.text_progress_committing));
            ServiceClient.getService().updateUserInfo(token, nickname, sign, "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Consumer<ServiceResult>() {
                                @Override
                                public void accept(@NonNull ServiceResult result) throws Exception {
                                    if ("100".equals(result.getResultCode())) {
                                        userInfo.setUser_nicename(nickname);
                                        userInfo.setSignature(sign);
                                        updateUserInfo(userInfo);
                                        ToastMaster.toast(getString(R.string.toast_upate_user_information_success));
                                        onBackPressed();
                                    } else {
                                        ToastMaster.toast(result.getResultMsg());
                                    }
                                    hideProgress();
                                }
                            },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    ToastMaster.toast(getString(R.string.toast_upate_user_information_error));
                                    hideProgress();
                                }
                            });
        }
    }
}
