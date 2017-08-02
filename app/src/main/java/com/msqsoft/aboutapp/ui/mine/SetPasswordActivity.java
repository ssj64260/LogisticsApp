package com.msqsoft.aboutapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
    private TextView tvDoCommit;

    private String mPhoneNum;
    private String mCode;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    onBackPressed();
                    break;
                case R.id.tv_do_commit:
                    commitPassword();
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
                tvDoCommit.setBackgroundResource(R.drawable.shape_bg_button_yellow);
                tvDoCommit.setTextColor(ContextCompat.getColor(SetPasswordActivity.this, R.color.color_181818));
                tvDoCommit.setClickable(true);
            } else {
                tvDoCommit.setBackgroundResource(R.drawable.shape_bg_button_gray);
                tvDoCommit.setTextColor(ContextCompat.getColor(SetPasswordActivity.this, R.color.white));
                tvDoCommit.setClickable(false);
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
        mPhoneNum = getIntent().getStringExtra(KEY_PHONE_NUMBER);
        mCode = getIntent().getStringExtra(KEY_VERIFICATION_CODE);
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(click);
        tvTitle.setText(getString(R.string.title_set_password_activity));
    }

    private void initView() {
        initToolbar();

        etPassword = (EditText) findViewById(R.id.et_password);
        tvDoCommit = (TextView) findViewById(R.id.tv_do_commit);

        etPassword.addTextChangedListener(textWatcher);
        tvDoCommit.setOnClickListener(click);
    }

    private void commitPassword() {
        final String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastMaster.toast(getString(R.string.toast_passowrd_is_null));
        } else {
            showProgress("");
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
                                        Intent intent = new Intent();
                                        intent.setClass(SetPasswordActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
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
}
