package com.msqsoft.aboutapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseAppCompatActivity;
import com.msqsoft.aboutapp.config.Config;
import com.msqsoft.aboutapp.db.LiteOrmHelper;
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.utils.PreferencesUtil;
import com.msqsoft.aboutapp.utils.ToastMaster;
import com.orhanobut.logger.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;

/**
 * 登录页面
 */

public class LoginActivity extends BaseAppCompatActivity {

    private EditText etPhoneNumber;
    private ImageView ivClearPhone;
    private EditText etPassword;
    private ImageView ivClearPassword;
    private TextView tvDoLogin;
    private TextView tvForgetPwd;

    private UserInfoDetailBean mUserInfo;

    private RongIMClient.ConnectCallback mIMCallback = new RongIMClient.ConnectCallback() {
        @Override
        public void onTokenIncorrect() {
            ToastMaster.toast(getString(R.string.toast_token_error));
        }

        @Override
        public void onSuccess(String s) {
            PreferencesUtil.setData(Config.APP_SETTING, Config.KEY_LAST_LOGIN_USER, mUserInfo.getMobile());
            final LiteOrmHelper dbHelper = new LiteOrmHelper(LoginActivity.this);
            dbHelper.save(mUserInfo);
            dbHelper.closeDB();
            hideProgress();
            ToastMaster.toast(getString(R.string.toast_login_success));
            onBackPressed();
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {
            Logger.d("ErrorCode：" + errorCode.getValue() + "\nErrorMessage：" + errorCode.getMessage());
            ToastMaster.toast(getString(R.string.toast_login_error));
        }
    };

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    hideKeyboard();
                    onBackPressed();
                    break;
                case R.id.tv_toolbar_action:
                    hideKeyboard();
                    startActivity(new Intent(LoginActivity.this, VerificationCodeActivity.class));
                    break;
                case R.id.iv_clear_phone:
                    etPhoneNumber.setText("");
                    break;
                case R.id.iv_clear_password:
                    etPassword.setText("");
                    break;
                case R.id.tv_do_login:
                    hideKeyboard();
                    doLogin();
                    break;
                case R.id.tv_forget_password:
                    hideKeyboard();
                    startActivity(
                            new Intent(LoginActivity.this, VerificationCodeActivity.class)
                                    .putExtra(Config.ACTIVITY_TYPE_IS_REGISTER, false));
                    break;
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setEditStatus();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private View.OnFocusChangeListener focusChange = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            setEditStatus();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etPhoneNumber.removeTextChangedListener(textWatcher);
        etPassword.removeTextChangedListener(textWatcher);
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        final TextView tvAction = (TextView) findViewById(R.id.tv_toolbar_action);

        ivBack.setOnClickListener(click);
        tvTitle.setText(getString(R.string.title_login_activity));
        tvAction.setText(getString(R.string.text_to_register));
        tvAction.setVisibility(View.VISIBLE);
        tvAction.setOnClickListener(click);
    }

    private void initView() {
        initToolbar();
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        ivClearPhone = (ImageView) findViewById(R.id.iv_clear_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        ivClearPassword = (ImageView) findViewById(R.id.iv_clear_password);
        tvDoLogin = (TextView) findViewById(R.id.tv_do_login);
        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_password);

        etPhoneNumber.addTextChangedListener(textWatcher);
        etPhoneNumber.setOnFocusChangeListener(focusChange);
        etPassword.addTextChangedListener(textWatcher);
        etPassword.setOnFocusChangeListener(focusChange);
        ivClearPhone.setOnClickListener(click);
        ivClearPassword.setOnClickListener(click);
        tvDoLogin.setOnClickListener(click);
        tvForgetPwd.setOnClickListener(click);

        final String phoneNumber = PreferencesUtil.getString(Config.APP_SETTING, Config.KEY_LAST_LOGIN_USER, "");
        etPhoneNumber.setText(phoneNumber);
        tvDoLogin.setEnabled(false);
    }

    private void setEditStatus() {
        final String phoneNumber = etPhoneNumber.getText().toString();
        final String password = etPassword.getText().toString();
        final boolean phoneIsEmpty = TextUtils.isEmpty(phoneNumber);
        final boolean passwordIsEmpty = TextUtils.isEmpty(password);
        final boolean enabled = tvDoLogin.isEnabled();
        if (!phoneIsEmpty && !passwordIsEmpty && !enabled) {
            tvDoLogin.setEnabled(true);
        } else if ((phoneIsEmpty || passwordIsEmpty) && enabled) {
            tvDoLogin.setEnabled(false);
        }
        ivClearPhone.setVisibility((etPhoneNumber.hasFocus() && !phoneIsEmpty) ? View.VISIBLE : View.GONE);
        ivClearPassword.setVisibility((etPassword.hasFocus() && !passwordIsEmpty) ? View.VISIBLE : View.GONE);
    }

    private void doLogin() {
        final String phoneNumber = etPhoneNumber.getText().toString();
        final String password = etPassword.getText().toString();

        showProgress(getString(R.string.text_progress_logining));
        ServiceClient.getService().doLogin(phoneNumber, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<ServiceResult<UserInfoDetailBean>>() {
                            @Override
                            public void accept(@NonNull ServiceResult<UserInfoDetailBean> result) throws Exception {
                                if ("100".equals(result.getResultCode())) {
                                    mUserInfo = result.getResultData();
                                    if (mUserInfo != null) {
                                        mUserInfo.getAccess_token();
                                        final String token = Config.TEST_TOKEN;

                                        PreferencesUtil.setData(Config.USER_INFO, Config.KEY_RONGIM_TOKEN, token);
                                        doConnectRongIM(mIMCallback);
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
                                hideProgress();
                                ToastMaster.toast(getString(R.string.toast_login_error));
                            }
                        });
    }

}
