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
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.utils.ToastMaster;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 设置密码
 */

public class SetPasswordActivity extends BaseAppCompatActivity {

    public static final String KEY_PHONE_NUMBER = "key_phone_number";
    public static final String KEY_VERIFICATION_CODE = "key_verification_code";

    private EditText etPassword;
    private ImageView ivClearPassword;
    private TextView tvDoCommit;

    private String mPhoneNum;
    private String mCode;

    private boolean mIsRegister;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    onBackPressed();
                    break;
                case R.id.iv_clear_password:
                    etPassword.setText("");
                    break;
                case R.id.tv_do_commit:
                    if (mIsRegister) {
                        doRegister();
                    } else {
                        resetPassword();
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
        mIsRegister = getIntent().getBooleanExtra(Config.ACTIVITY_TYPE_IS_REGISTER, true);
        mPhoneNum = getIntent().getStringExtra(KEY_PHONE_NUMBER);
        mCode = getIntent().getStringExtra(KEY_VERIFICATION_CODE);
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(click);
        if (mIsRegister) {
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
                            new Consumer<ServiceResult>() {
                                @Override
                                public void accept(@NonNull ServiceResult result) throws Exception {
                                    final String code = result.getResultCode();
                                    if ("100".equals(code)) {
                                        ToastMaster.toast(getString(R.string.toast_register_success));
                                        toLogin();
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
                                    ToastMaster.toast(getString(R.string.toast_register_error));
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
            ServiceClient.getService().doSetPassword(mPhoneNum, mCode, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Consumer<ServiceResult>() {
                                @Override
                                public void accept(@NonNull ServiceResult result) throws Exception {
                                    final String code = result.getResultCode();
                                    if ("100".equals(code)) {
                                        ToastMaster.toast(getString(R.string.toast_set_password_success));
                                        toLogin();
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
                                    ToastMaster.toast(getString(R.string.toast_set_password_error));
                                }
                            });
        }
    }

    private void toLogin() {
        Intent intent = new Intent();
        intent.setClass(SetPasswordActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
