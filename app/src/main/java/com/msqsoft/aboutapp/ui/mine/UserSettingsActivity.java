package com.msqsoft.aboutapp.ui.mine;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseAppCompatActivity;
import com.msqsoft.aboutapp.model.FirImBean;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.utils.DataCleanManager;
import com.msqsoft.aboutapp.utils.FileUtil;
import com.msqsoft.aboutapp.utils.SDCardUtil;
import com.msqsoft.aboutapp.utils.ToastMaster;
import com.msqsoft.aboutapp.utils.UpdateAppUtils;
import com.msqsoft.aboutapp.utils.VersionUtil;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 用户设置
 */

public class UserSettingsActivity extends BaseAppCompatActivity {

    private LinearLayout llEditUserInfo;
    private LinearLayout llChangePassword;
    private LinearLayout llChangePhone;
    private LinearLayout llCheckUpdate;
    private TextView tvVersion;
    private LinearLayout llCleanCache;
    private TextView tvCache;
    private TextView tvLoginOut;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    onBackPressed();
                    break;
                case R.id.ll_edit:
                    startActivity(new Intent(UserSettingsActivity.this, EditUserInfoActivity.class));
                    break;
                case R.id.ll_change_password:
                    startActivity(new Intent(UserSettingsActivity.this, ChangePasswordActivity.class));
                    break;
                case R.id.ll_change_phone:
                    ToastMaster.toast(getString(R.string.text_change_bind_phone));
                    break;
                case R.id.ll_check_update:
                    checkUpdate();
                    break;
                case R.id.ll_clean_cache:
                    final String cacheDir = SDCardUtil.getCacheDir(UserSettingsActivity.this);
                    final String externalCacheDir = SDCardUtil.getExternalCacheDir(UserSettingsActivity.this);
                    DataCleanManager.deleteAllFiles(new File(cacheDir));
                    DataCleanManager.deleteAllFiles(new File(externalCacheDir));
                    getCacheSize();
                    ToastMaster.toast(getString(R.string.toast_clean_cache_finish));
                    break;
                case R.id.tv_login_out:
                    showAlertDialog();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        initView();
        setData();

    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(click);
        tvTitle.setText(getString(R.string.title_user_settings_activity));
    }

    private void initView() {
        initToolbar();
        llEditUserInfo = (LinearLayout) findViewById(R.id.ll_edit);
        llChangePassword = (LinearLayout) findViewById(R.id.ll_change_password);
        llChangePhone = (LinearLayout) findViewById(R.id.ll_change_phone);
        llCheckUpdate = (LinearLayout) findViewById(R.id.ll_check_update);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        llCleanCache = (LinearLayout) findViewById(R.id.ll_clean_cache);
        tvCache = (TextView) findViewById(R.id.tv_cache);
        tvLoginOut = (TextView) findViewById(R.id.tv_login_out);

        llEditUserInfo.setOnClickListener(click);
        llChangePassword.setOnClickListener(click);
        llChangePhone.setOnClickListener(click);
        llCheckUpdate.setOnClickListener(click);
        llCleanCache.setOnClickListener(click);
        tvLoginOut.setOnClickListener(click);
    }

    private void setData() {
        final String versionName = VersionUtil.getVersionName(this);
        tvVersion.setText(String.format(getString(R.string.text_current_version), versionName));

        getCacheSize();
    }

    private void getCacheSize() {
        long cacheSize = 0;
        try {
            final String cacheDir = SDCardUtil.getCacheDir(this);
            final String externalCacheDir = SDCardUtil.getExternalCacheDir(this);

            cacheSize += FileUtil.getDirSize(new File(cacheDir));
            cacheSize += FileUtil.getDirSize(new File(externalCacheDir));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String cache = FileUtil.formatFileSize(this, cacheSize);
        tvCache.setText(cache);
    }

    private void showAlertDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage(getString(R.string.text_dialog_message));
        dialog.setButton(Dialog.BUTTON_POSITIVE, getString(R.string.text_dialog_button_do_login_out), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doLoginOut();
            }
        });
        dialog.setButton(Dialog.BUTTON_NEGATIVE, getString(R.string.text_dialog_button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    private void checkUpdate() {
        showProgress(getString(R.string.text_progress_checking_update));
        ServiceClient.getService().checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<FirImBean>() {
                            @Override
                            public void accept(@NonNull FirImBean firImBean) throws Exception {
                                final int lastVersionCode = Integer.parseInt(firImBean.getVersion());
                                final int currentVersionCode = VersionUtil.getVersionCode(UserSettingsActivity.this);
                                if (lastVersionCode > currentVersionCode) {
                                    final UpdateAppUtils updateApp = new UpdateAppUtils(UserSettingsActivity.this, firImBean);
                                    updateApp.showUpdateDialog();
                                } else {
                                    ToastMaster.toast(getString(R.string.toast_is_last_version));
                                }
                                hideProgress();
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                ToastMaster.toast(getString(R.string.toast_get_version_error));
                                hideProgress();
                            }
                        });
    }

}
