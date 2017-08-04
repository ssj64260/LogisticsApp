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
import com.msqsoft.aboutapp.ui.MainActivity;
import com.msqsoft.aboutapp.utils.DataCleanManager;
import com.msqsoft.aboutapp.utils.FileUtil;
import com.msqsoft.aboutapp.utils.SDCardUtil;
import com.msqsoft.aboutapp.utils.ToastMaster;
import com.msqsoft.aboutapp.utils.VersionUtil;

import java.io.File;

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
                    ToastMaster.toast(getString(R.string.text_check_update));
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

        final String cache = FileUtil.FormatFileSize(this, cacheSize);
        tvCache.setText(cache);
    }

    private void showAlertDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage(getString(R.string.text_dialog_message));
        dialog.setButton(Dialog.BUTTON_POSITIVE, getString(R.string.text_dialog_button_do_login_out), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doLoginOut();
                Intent intent = new Intent();
                intent.setClass(UserSettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        dialog.setButton(Dialog.BUTTON_NEGATIVE, getString(R.string.text_dialog_button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

}
