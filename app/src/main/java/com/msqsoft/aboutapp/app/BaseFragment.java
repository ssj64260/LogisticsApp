package com.msqsoft.aboutapp.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msqsoft.aboutapp.config.Config;
import com.msqsoft.aboutapp.db.LiteOrmHelper;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;
import com.msqsoft.aboutapp.ui.MainActivity;
import com.msqsoft.aboutapp.utils.PreferencesUtil;
import com.msqsoft.aboutapp.widget.DefaultProgressDialog;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 基类
 */

public class BaseFragment extends Fragment {

    private DefaultProgressDialog mProgress;

    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
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
        final LiteOrmHelper dbHelper = new LiteOrmHelper(APP.getInstance());
        final UserInfoDetailBean userInfo = dbHelper.queryFirst(UserInfoDetailBean.class);
        dbHelper.closeDB();
        return userInfo;
    }

    protected void updateCourierInfo(UserInfoDetailBean newUserInfo) {
        final LiteOrmHelper dbHelper = new LiteOrmHelper(APP.getInstance());
        final UserInfoDetailBean userInfo = dbHelper.queryFirst(UserInfoDetailBean.class);
        userInfo.setCourierInfo(newUserInfo);
        dbHelper.save(userInfo);
        dbHelper.closeDB();
    }

    protected void updateUserInfo(UserInfoDetailBean newUserInfo) {
        final LiteOrmHelper dbHelper = new LiteOrmHelper(APP.getInstance());
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
        RongIM.getInstance().refreshUserInfoCache(userInfo);
    }

    protected void doLoginOut() {
        PreferencesUtil.clearAll(Config.USER_INFO);
        final LiteOrmHelper dbHelper = new LiteOrmHelper(APP.getInstance());
        dbHelper.deleteAll(UserInfoDetailBean.class);
        dbHelper.closeDB();

        RongIM.getInstance().logout();

        Intent intent = new Intent();
        intent.setClass(mActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void showProgress(String message) {
        if (mProgress == null) {
            mProgress = new DefaultProgressDialog(getActivity());
        }
        mProgress.setMessage(message);
        mProgress.showDialog();
    }

    protected void hideProgress() {
        if (mProgress != null) {
            mProgress.dismissDialog();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
    }

    @Override
    public void onAttach(Context context) {
        Log.e(this.getClass().getSimpleName(), "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(this.getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(this.getClass().getSimpleName(), "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e(this.getClass().getSimpleName(), "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e(this.getClass().getSimpleName(), "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e(this.getClass().getSimpleName(), "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e(this.getClass().getSimpleName(), "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e(this.getClass().getSimpleName(), "onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.e(this.getClass().getSimpleName(), "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.e(this.getClass().getSimpleName(), "onDetach");
        super.onDetach();
    }

    @Override
    public boolean getUserVisibleHint() {
        Log.e(this.getClass().getSimpleName(), "getUserVisibleHint");
        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(this.getClass().getSimpleName(), "setUserVisibleHint:" + isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(this.getClass().getSimpleName(), "onHiddenChanged:" + hidden);
    }
}
