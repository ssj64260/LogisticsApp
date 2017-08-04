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
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.utils.ToastMaster;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.msqsoft.aboutapp.config.Config.COUNT_DOWN_TIME;

/**
 * 修改密码
 */

public class ChangePasswordActivity extends BaseAppCompatActivity {

    private TextView tvTips;
    private EditText etCode;
    private ImageView ivClearCode;
    private TextView tvGetCode;
    private TextView tvNextStep;

    private String mMobile;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    hideKeyboard();
                    onBackPressed();
                    break;
                case R.id.iv_clear_code:
                    etCode.setText("");
                    break;
                case R.id.tv_get_code:
                    getVerificationCode();
                    break;
                case R.id.tv_next_step:
                    final String code = etCode.getText().toString().trim();
                    Intent intent = new Intent();
                    intent.setClass(ChangePasswordActivity.this, SetPasswordActivity.class);
                    intent.putExtra(SetPasswordActivity.TYPE_SET_PASSWORD, SetPasswordActivity.TYPE_CHANGE_PASSWORD);
                    intent.putExtra(SetPasswordActivity.KEY_PHONE_NUMBER, mMobile);
                    intent.putExtra(SetPasswordActivity.KEY_VERIFICATION_CODE, code);
                    startActivity(intent);
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
            final String code = etCode.getText().toString();
            if (!TextUtils.isEmpty(code)) {
                tvNextStep.setEnabled(true);
                ivClearCode.setVisibility(View.VISIBLE);
            } else {
                tvNextStep.setEnabled(false);
                ivClearCode.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initData();
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etCode.removeTextChangedListener(textWatcher);
    }

    private void initData() {
        final UserInfoDetailBean userInfo = getUserInfo();
        if (userInfo != null) {
            mMobile = userInfo.getMobile();
        }
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(click);
        tvTitle.setText(getString(R.string.title_change_password));
    }

    private void initView() {
        initToolbar();

        tvTips = (TextView) findViewById(R.id.tv_tips);
        etCode = (EditText) findViewById(R.id.et_code);
        ivClearCode = (ImageView) findViewById(R.id.iv_clear_code);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code);
        tvNextStep = (TextView) findViewById(R.id.tv_next_step);

        if (!TextUtils.isEmpty(mMobile)) {
            final String mobile = mMobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
            tvTips.setText(String.format(getString(R.string.text_tips_input_phone_code), mobile));
            etCode.addTextChangedListener(textWatcher);
            ivClearCode.setOnClickListener(click);
            tvGetCode.setOnClickListener(click);
            tvNextStep.setOnClickListener(click);
        }
        tvNextStep.setEnabled(false);
    }

    private void getVerificationCode() {
        showProgress(getString(R.string.text_progress_requesting));
        ServiceClient.getService().getVerificationCode(mMobile)
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

    private void doCountDown() {
        tvGetCode.setEnabled(false);
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
                        tvGetCode.setEnabled(true);
                        tvGetCode.setText(getString(R.string.text_button_reget_verification_code));
                    }
                });
    }
}

