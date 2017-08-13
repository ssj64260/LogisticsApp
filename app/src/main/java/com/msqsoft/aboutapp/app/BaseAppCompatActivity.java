package com.msqsoft.aboutapp.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import io.rong.imlib.model.UserInfo;

/**
 * 基类
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    private InputMethodManager manager;
    private DefaultProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.i(this.getPackageName());
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
        final String rongTOken = PreferencesUtil.getString(Config.USER_INFO, Config.KEY_RONGIM_TOKEN, "");
        return !TextUtils.isEmpty(rongTOken);
    }

    protected String getUserId(){
        return PreferencesUtil.getString(Config.USER_INFO, Config.KEY_ABOUTAPP_USER_ID, "");
    }

    protected String getAboutAppToken(){
        return PreferencesUtil.getString(Config.USER_INFO, Config.KEY_ABOUTAPP_TOKEN, "");
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
        final String newNickname = newUserInfo.getUser_nicename();
        final String newAvatar = newUserInfo.getAvatar();
        final String newSignature = newUserInfo.getSignature();
        final String newMobile = newUserInfo.getMobile();
        final String userId = newUserInfo.getId();

        updateRongUserInfo(userId, newNickname, newAvatar);
        userInfo.setUserInfo(newNickname, newAvatar, newSignature, newMobile);

        dbHelper.save(userInfo);
        dbHelper.closeDB();
    }

    protected void updateRongUserInfo(String userId, String nickname, String avatar) {
        final UserInfo userInfo = new UserInfo(userId, nickname, Uri.parse(avatar));
        RongIM.getInstance().setCurrentUserInfo(userInfo);
    }

    protected void doLoginOut() {
        PreferencesUtil.clearAll(Config.USER_INFO);
        final LiteOrmHelper dbHelper = new LiteOrmHelper(this);
        dbHelper.deleteAll(UserInfoDetailBean.class);
        dbHelper.closeDB();

        RongIM.getInstance().logout();

        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void doConnectRongIM(RongIMClient.ConnectCallback callback) {
        final String token = PreferencesUtil.getString(Config.USER_INFO, Config.KEY_RONGIM_TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            final UserInfoDetailBean cacheInfo = getUserInfo();
            final UserInfo userInfo = new UserInfo(cacheInfo.getId(), cacheInfo.getUser_nicename(), Uri.parse(cacheInfo.getAvatar()));
            RongIM.getInstance().setCurrentUserInfo(userInfo);
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
