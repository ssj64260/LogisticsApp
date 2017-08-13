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
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.model.UserInfoDetailBean;
import com.msqsoft.aboutapp.service.MyObserver;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.utils.ToastMaster;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 设置密码
 */

public class SetPasswordActivity extends BaseAppCompatActivity {

    public static final String TYPE_SET_PASSWORD = "type_set_password";//忘记密码，修改密码，注册设置密码
    public static final int TYPE_FORGET_PASSWORD = 1001;
    public static final int TYPE_CHANGE_PASSWORD = 1002;
    public static final int TYPE_REGISTER_PASSWORD = 1003;
    public static final String KEY_PHONE_NUMBER = "key_phone_number";
    public static final String KEY_VERIFICATION_CODE = "key_verification_code";

    private EditText etPassword;
    private ImageView ivClearPassword;
    private TextView tvDoCommit;

    private String mPhoneNum;
    private String mCode;

    private int mSetPasswordType;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    hideKeyboard();
                    onBackPressed();
                    break;
                case R.id.iv_clear_password:
                    etPassword.setText("");
                    break;
                case R.id.tv_do_commit:
                    if (mSetPasswordType == TYPE_REGISTER_PASSWORD) {
                        doRegister();
                    } else if (mSetPasswordType == TYPE_FORGET_PASSWORD) {
                        resetPassword();
                    } else if (mSetPasswordType == TYPE_CHANGE_PASSWORD) {
                        changePassword();
                    }
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
            final String password = etPassword.getText().toString();
            if (!TextUtils.isEmpty(password)) {
                tvDoCommit.setEnabled(true);
                ivClearPassword.setVisibility(View.VISIBLE);
            } else {
                tvDoCommit.setEnabled(false);
                ivClearPassword.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        initData();
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etPassword.removeTextChangedListener(textWatcher);
    }

    private void initData() {
        mSetPasswordType = getIntent().getIntExtra(TYPE_SET_PASSWORD, SetPasswordActivity.TYPE_REGISTER_PASSWORD);
        mPhoneNum = getIntent().getStringExtra(KEY_PHONE_NUMBER);
        mCode = getIntent().getStringExtra(KEY_VERIFICATION_CODE);
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(click);
        if (mSetPasswordType == TYPE_REGISTER_PASSWORD) {
            tvTitle.setText(getString(R.string.title_set_password));
        } else {
            tvTitle.setText(getString(R.string.title_reset_password));
        }
    }

    private void initView() {
        initToolbar();

        etPassword = (EditText) findViewById(R.id.et_password);
        ivClearPassword = (ImageView) findViewById(R.id.iv_clear_password);
        tvDoCommit = (TextView) findViewById(R.id.tv_do_commit);

        etPassword.addTextChangedListener(textWatcher);
        tvDoCommit.setOnClickListener(click);
        ivClearPassword.setOnClickListener(click);
        tvDoCommit.setEnabled(false);
    }

    private void doRegister() {
        final String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastMaster.toast(getString(R.string.toast_passowrd_is_null));
        } else {
            showProgress(getString(R.string.text_progress_committing));
            ServiceClient.getService().doRegister(mPhoneNum, mCode, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new MyObserver<ServiceResult<UserInfoDetailBean>>() {
                                @Override
                                public void onSuccess(@NonNull ServiceResult<UserInfoDetailBean> result) {
                                    hideProgress();
                                    ToastMaster.toast(getString(R.string.toast_register_success));
                                    toActivity(LoginActivity.class);
                                }

                                @Override
                                public void onError(String errorMsg) {
                                    super.onError(errorMsg);
                                    hideProgress();
                                    ToastMaster.toast(errorMsg);
                                }
                            });
        }
    }

    private void resetPassword() {
        final String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastMaster.toast(getString(R.string.toast_passowrd_is_null));
        } else {
            showProgress(getString(R.string.text_progress_committing));
            ServiceClient.getService().doResetPassword(mPhoneNum, mCode, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new MyObserver<ServiceResult>() {
                                @Override
                                public void onSuccess(@NonNull ServiceResult result) {
                                    hideProgress();
                                    ToastMaster.toast(getString(R.string.toast_set_password_success));
                                    toActivity(LoginActivity.class);
                                }

                                @Override
                                public void onError(String errorMsg) {
                                    super.onError(errorMsg);
                                    hideProgress();
                                    ToastMaster.toast(errorMsg);
                                }
                            });
        }
    }

    private void changePassword() {
        final String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastMaster.toast(getString(R.string.toast_passowrd_is_null));
        } else {
            final String token = getAboutAppToken();
            showProgress(getString(R.string.text_progress_committing));
            ServiceClient.getService().doChangePassword(token, mCode, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new MyObserver<ServiceResult>() {
                                @Override
                                public void onSuccess(@NonNull ServiceResult result) {
                                    hideProgress();
                                    ToastMaster.toast(getString(R.string.toast_change_password_success));
                                    toActivity(UserSettingsActivity.class);
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
    }

    private void toActivity(Class clazz) {
        Intent intent = new Intent();
        intent.setClass(SetPasswordActivity.this, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
