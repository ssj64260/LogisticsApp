package com.msqsoft.aboutapp.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.msqsoft.aboutapp.config.Config;
import com.msqsoft.aboutapp.db.LiteOrmHelper;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;
import com.msqsoft.aboutapp.ui.MainActivity;
import com.msqsoft.aboutapp.utils.PreferencesUtil;
import com.msqsoft.aboutapp.widget.DefaultProgressDialog;
import com.orhanobut.logger.Logger;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 基类
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    private InputMethodManager manager;
    private DefaultProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.i(this.getLocalClassName());

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyboard();
        hideProgress();
    }

    protected boolean isLogin() {
        final LiteOrmHelper dbHelper = new LiteOrmHelper(this);
        final UserInfoDetailBean userInfo = dbHelper.queryFirst(UserInfoDetailBean.class);
        dbHelper.closeDB();
        return userInfo != null;
    }

    protected UserInfoDetailBean getUserInfo() {
        final LiteOrmHelper dbHelper = new LiteOrmHelper(this);
        final UserInfoDetailBean userInfo = dbHelper.queryFirst(UserInfoDetailBean.class);
        dbHelper.closeDB();
        return userInfo;
    }

    protected void updateCourierInfo(UserInfoDetailBean newUserInfo) {
        final LiteOrmHelper dbHelper = new LiteOrmHelper(this);
        final UserInfoDetailBean userInfo = dbHelper.queryFirst(UserInfoDetailBean.class);
        userInfo.setCourierInfo(newUserInfo);
        dbHelper.save(userInfo);
        dbHelper.closeDB();
    }

    protected void updateUserInfo(UserInfoDetailBean newUserInfo) {
        final LiteOrmHelper dbHelper = new LiteOrmHelper(this);
        final UserInfoDetailBean userInfo = dbHelper.queryFirst(UserInfoDetailBean.class);
        userInfo.setUserInfo(newUserInfo);
        dbHelper.save(userInfo);
        dbHelper.closeDB();
    }

    protected void doLoginOut() {
        PreferencesUtil.clearAll(Config.USER_INFO);
        final LiteOrmHelper dbHelper = new LiteOrmHelper(this);
        dbHelper.deleteAll(UserInfoDetailBean.class);
        dbHelper.closeDB();

        RongIM.getInstance().logout();

        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void doConnectRongIM(RongIMClient.ConnectCallback callback) {
        final String token = PreferencesUtil.getString(Config.USER_INFO, Config.KEY_RONGIM_TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            RongIM.getInstance().setMessageAttachedUserInfo(true);
            RongIMClient.connect(token, callback);
        }
    }

    protected void hideKeyboard() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showProgress(String message) {
        if (mProgress == null) {
            mProgress = new DefaultProgressDialog(this);
        }
        mProgress.setMessage(message);
        mProgress.showDialog();
    }

    protected void hideProgress() {
        if (mProgress != null) {
            mProgress.dismissDialog();
        }
    }
}
