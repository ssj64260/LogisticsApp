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
import com.msqsoft.aboutapp.utils.FastClick;
import com.msqsoft.aboutapp.utils.StringCheck;
import com.msqsoft.aboutapp.utils.ToastMaster;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 注册页面 or 忘记密码
 */

public class VerificationCodeActivity extends BaseAppCompatActivity {

    public static final String TYPE_IS_REGISTER = "type_is_register";//是否注册用，否则是忘记密码
    private static final int COUNT_DOWN_TIME = 60;//倒计时总时间

    private EditText etPhone;
    private EditText etCode;
    private TextView tvGetCode;
    private TextView tvNextStep;

    private boolean mIsRegister;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!FastClick.isFastClick()) {
                switch (v.getId()) {
                    case R.id.iv_toolbar_back:
                    case R.id.tv_toolbar_action:
                        onBackPressed();
                        break;
                    case R.id.tv_get_code:
                        getVerificationCode();
                        break;
                    case R.id.tv_next_step:
                        final String phoneNum = etPhone.getText().toString().trim();
                        final String code = etCode.getText().toString().trim();
                        Intent intent = new Intent();
                        intent.setClass(VerificationCodeActivity.this, SetPasswordActivity.class);
                        intent.putExtra(SetPasswordActivity.KEY_PHONE_NUMBER, phoneNum);
                        intent.putExtra(SetPasswordActivity.KEY_VERIFICATION_CODE, code);
                        startActivity(intent);
                        break;
                }
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String phoneNumber = etPhone.getText().toString();
            final String code = etCode.getText().toString();
            if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(code)) {
                tvNextStep.setBackgroundResource(R.drawable.shape_bg_button_yellow);
                tvNextStep.setTextColor(ContextCompat.getColor(VerificationCodeActivity.this, R.color.color_181818));
                tvNextStep.setClickable(true);
            } else {
                tvNextStep.setBackgroundResource(R.drawable.shape_bg_button_gray);
                tvNextStep.setTextColor(ContextCompat.getColor(VerificationCodeActivity.this, R.color.white));
                tvNextStep.setClickable(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        initData();
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etPhone.removeTextChangedListener(textWatcher);
        etCode.removeTextChangedListener(textWatcher);
    }

    private void initData() {
        mIsRegister = getIntent().getBooleanExtra(TYPE_IS_REGISTER, true);
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        final TextView tvAction = (TextView) findViewById(R.id.tv_toolbar_action);

        ivBack.setOnClickListener(click);
        if (mIsRegister) {
            tvTitle.setText(getString(R.string.title_register));
            tvAction.setText(getString(R.string.text_to_login));
            tvAction.setVisibility(View.VISIBLE);
            tvAction.setOnClickListener(click);
        } else {
            tvTitle.setText(getString(R.string.title_find_password));
        }
    }

    private void initView() {
        initToolbar();

        etPhone = (EditText) findViewById(R.id.et_phone_number);
        etCode = (EditText) findViewById(R.id.et_verification_code);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code);
        tvNextStep = (TextView) findViewById(R.id.tv_next_step);

        etPhone.addTextChangedListener(textWatcher);
        etCode.addTextChangedListener(textWatcher);
        tvGetCode.setOnClickListener(click);
        tvNextStep.setOnClickListener(click);
        tvNextStep.setClickable(false);
    }

    private void getVerificationCode() {
        final String mobile = etPhone.getText().toString().trim();
        if (!StringCheck.isMobileNO(mobile)) {
            ToastMaster.toast(getString(R.string.toast_phone_number_error));
        } else {
            showProgress("");
            ServiceClient.getService().getVerificationCode(mobile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Consumer<ServiceResult>() {
                                @Override
                                public void accept(@NonNull ServiceResult result) throws Exception {
                                    if ("100".equals(result.getResultCode())) {
                                        doCountDown();
                                        ToastMaster.toast(getString(R.string.toast_get_verification_code_success));
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
                                    ToastMaster.toast(getString(R.string.toast_get_verification_code_error));
                                }
                            });
        }
    }

    private void doCountDown() {
        tvGetCode.setClickable(false);
        tvGetCode.setTextColor(ContextCompat.getColor(this, R.color.color_999999));
        Observable.intervalRange(0, COUNT_DOWN_TIME, 0, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        final int second = (int) (COUNT_DOWN_TIME - aLong - 1);
                        tvGetCode.setText(String.format(getString(R.string.text_button_count_down), second));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        tvGetCode.setClickable(true);
                        tvGetCode.setTextColor(ContextCompat.getColor(VerificationCodeActivity.this, R.color.color_181818));
                        tvGetCode.setText(getString(R.string.text_button_reget_verification_code));
                    }
                });
    }
}
