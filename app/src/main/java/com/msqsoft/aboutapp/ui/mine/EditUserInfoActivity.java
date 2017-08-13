package com.msqsoft.aboutapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseAppCompatActivity;
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;
import com.msqsoft.aboutapp.service.MyObserver;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.ui.adapter.OnListClickListener;
import com.msqsoft.aboutapp.utils.ToastMaster;
import com.msqsoft.aboutapp.widget.ListSelectDialog;
import com.msqsoft.aboutapp.widget.imageloader.GlideCircleTransform;
import com.msqsoft.aboutapp.widget.imageloader.ImageLoaderFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    private ListSelectDialog mListDialog;
    private List<String> mButtonNames;
    private GlideCircleTransform mTransform;
    private String mAvatarUrl;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    onBackPressed();
                    break;
                case R.id.iv_user_avatar:
                    showPhotoDialog();
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
        setData();
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
            } else if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null) {
                    String avatarPath;
                    for (LocalMedia media : selectList) {
                        if (media.isCompressed()) {
                            avatarPath = media.getCompressPath();
                        } else if (media.isCut()) {
                            avatarPath = media.getCutPath();
                        } else {
                            avatarPath = media.getPath();
                        }
                        if (!TextUtils.isEmpty(avatarPath)) {
                            uploadUserAvatar(avatarPath);
                            break;
                        }
                    }
                }
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

    private void setData() {
        final UserInfoDetailBean userInfo = getUserInfo();
        setUserInfo(userInfo);
    }

    private void setUserInfo(UserInfoDetailBean userInfo) {
        final String id = userInfo.getId();
        mAvatarUrl = userInfo.getAvatar();
        final String name = userInfo.getUser_nicename();
        final String sign = userInfo.getSignature();

        ImageLoaderFactory
                .getLoader()
                .loadImageFitCenter(this, ivUserAvatar, mAvatarUrl, mTransform);
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

    private void showPhotoDialog() {
        if (mListDialog == null) {
            mListDialog = new ListSelectDialog(this);
            mListDialog.setListClick(new OnListClickListener() {
                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 0:
                            startActivity(new Intent(EditUserInfoActivity.this, ShowImageActivity.class)
                                    .putExtra(ShowImageActivity.URL_IMAGE, mAvatarUrl));
                            break;
                        case 1:
                            takePhoto();
                            break;
                        case 2:
                            selectPhoto();
                            break;
                    }
                    mListDialog.dismiss();
                }

                @Override
                public void onTagClick(@ItemView int tag, int position) {

                }
            });

            mButtonNames = new ArrayList<>();
            mButtonNames.add(getString(R.string.text_list_dialog_show_avatar));
            mButtonNames.add(getString(R.string.text_list_dialog_take_photo));
            mButtonNames.add(getString(R.string.text_list_dialog_select_photo));
            mButtonNames.add(getString(R.string.text_list_dialog_cancel));
        }
        mListDialog.show();

        mListDialog.setListData(mButtonNames);
    }

    private void takePhoto() {
        PictureSelector.create(EditUserInfoActivity.this)
                .openCamera(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .compressGrade(Luban.THIRD_GEAR)
                .enableCrop(true)
                .compress(true)
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)
                .withAspectRatio(1, 1)
                .freeStyleCropEnabled(false)
                .showCropFrame(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    private void selectPhoto() {
        PictureSelector.create(EditUserInfoActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(1)
                .minSelectNum(1)
                .imageSpanCount(3)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(false)
                .compressGrade(Luban.THIRD_GEAR)
                .isCamera(false)
                .isZoomAnim(false)
                .enableCrop(true)
                .compress(true)
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)
                .glideOverride(160, 160)
                .withAspectRatio(1, 1)
                .freeStyleCropEnabled(false)
                .showCropFrame(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    private void getUserInfoDetail() {
        final String currentUserId = getUserId();
        final String token = getAboutAppToken();
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

    private void updateUserInfo() {
        final String token = getAboutAppToken();
        final String nickname = tvNickname.getText().toString();
        final String sign = tvUserSign.getText().toString();
        showProgress(getString(R.string.text_progress_committing));
        ServiceClient.getService().updateUserInfo(token, nickname, sign, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new MyObserver<ServiceResult>() {
                            @Override
                            public void onSuccess(@NonNull ServiceResult result) {
                                final UserInfoDetailBean userInfo = getUserInfo();
                                userInfo.setUser_nicename(nickname);
                                userInfo.setSignature(sign);
                                updateUserInfo(userInfo);

                                hideProgress();
                                ToastMaster.toast(getString(R.string.toast_upate_user_information_success));
                                onBackPressed();
                            }

                            @Override
                            public void onError(String errorMsg) {
                                super.onError(errorMsg);
                                hideProgress();
                                ToastMaster.toast(errorMsg);
                            }

                            @Override
                            public void onTokenIncorrect() {
                                hideProgress();
                                doLoginOut();
                            }
                        });
    }

    private void uploadUserAvatar(String avatarPath) {
        final File file = new File(avatarPath);
        if (file.exists()) {
            showProgress(getString(R.string.text_progress_uploading));
            final String token = getAboutAppToken();
            RequestBody requestFile = RequestBody.create(MultipartBody.FORM, file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            ServiceClient.getService().uploadUserAvatar(token, part)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new MyObserver<ServiceResult>() {
                                @Override
                                public void onSuccess(@NonNull ServiceResult result) {
                                    hideProgress();
                                    ToastMaster.toast(result.getResultMsg());
                                    getUserInfoDetail();
                                }

                                @Override
                                public void onError(String errorMsg) {
                                    super.onError(errorMsg);
                                    hideProgress();
                                    ToastMaster.toast(errorMsg);
                                }

                                @Override
                                public void onTokenIncorrect() {
                                    hideProgress();
                                    doLoginOut();
                                }
                            });

        } else {
            ToastMaster.toast(getString(R.string.toast_file_is_not_exists));
        }
    }
}
