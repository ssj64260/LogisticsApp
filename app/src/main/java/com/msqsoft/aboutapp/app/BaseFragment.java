package com.msqsoft.aboutapp.app;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.msqsoft.aboutapp.config.Config;
import com.msqsoft.aboutapp.db.LiteOrmHelper;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;
import com.msqsoft.aboutapp.utils.PreferencesUtil;
import com.msqsoft.aboutapp.widget.DefaultProgressDialog;

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
        final LiteOrmHelper dbHelper = new LiteOrmHelper(APP.getInstance());
        final UserInfoDetailBean userInfo = dbHelper.queryFirst(UserInfoDetailBean.class);
        dbHelper.closeDB();
        return userInfo != null;
    }

    protected UserInfoDetailBean getUserInfo() {
        final LiteOrmHelper dbHelper = new LiteOrmHelper(APP.getInstance());
        final UserInfoDetailBean userInfo = dbHelper.queryFirst(UserInfoDetailBean.class);
        dbHelper.closeDB();
        return userInfo;
    }

    protected void updateUserInfo(UserInfoDetailBean newUserInfo) {
        final LiteOrmHelper dbHelper = new LiteOrmHelper(APP.getInstance());
        final UserInfoDetailBean userInfo = dbHelper.queryFirst(UserInfoDetailBean.class);
        userInfo.setCourierInfo(newUserInfo);
        dbHelper.save(userInfo);
        dbHelper.closeDB();
    }

    protected void doLoginOut() {
        PreferencesUtil.clearAll(Config.USER_INFO);
        final LiteOrmHelper dbHelper = new LiteOrmHelper(APP.getInstance());
        dbHelper.deleteAll(UserInfoDetailBean.class);
        dbHelper.closeDB();

        //TODO 返回MainActivity
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
}
