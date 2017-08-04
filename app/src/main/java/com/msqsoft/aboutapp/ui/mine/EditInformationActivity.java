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

/**
 * 修改信息
 */

public class EditInformationActivity extends BaseAppCompatActivity {

    public static final String DEFAULT_DATA = "default_data";
    public static final String RESULT_DATA = "result_data";
    public static final String ACTIVITY_TYPE = "activity_type";
    public static final String ACTIVITY_TYPE_NICKNAME = "activity_type_nickname";//昵称
    public static final String ACTIVITY_TYPE_SIGN = "activity_type_sign";//个人签名

    private EditText etInformation;
    private ImageView ivClear;
    private TextView tvSave;

    private String mTitle;
    private String mEditHint;
    private String mInformation;

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    hideKeyboard();
                    onBackPressed();
                    break;
                case R.id.iv_clear:
                    etInformation.setText("");
                    break;
                case R.id.tv_save:
                    hideKeyboard();
                    final String information = etInformation.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_DATA, information);
                    setResult(RESULT_OK, intent);
                    onBackPressed();
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
            final String information = etInformation.getText().toString();
            if (TextUtils.isEmpty(information)) {
                ivClear.setVisibility(View.GONE);
                tvSave.setEnabled(false);
            } else {
                ivClear.setVisibility(View.VISIBLE);
                tvSave.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);

        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etInformation.removeTextChangedListener(textWatcher);
    }

    private void initData() {
        final String tpye = getIntent().getStringExtra(ACTIVITY_TYPE);
        mInformation = getIntent().getStringExtra(DEFAULT_DATA);
        if (ACTIVITY_TYPE_NICKNAME.equals(tpye)) {
            mTitle = getString(R.string.title_edit_user_nickname);
            mEditHint = getString(R.string.text_hint_user_nickname);
        } else if (ACTIVITY_TYPE_SIGN.equals(tpye)) {
            mTitle = getString(R.string.title_edit_user_sign);
            mEditHint = getString(R.string.text_hint_user_sign);
        }
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(click);
        tvTitle.setText(mTitle);
    }

    private void initView() {
        initToolbar();

        etInformation = (EditText) findViewById(R.id.et_information);
        ivClear = (ImageView) findViewById(R.id.iv_clear);
        tvSave = (TextView) findViewById(R.id.tv_save);

        etInformation.addTextChangedListener(textWatcher);
        ivClear.setOnClickListener(click);
        tvSave.setOnClickListener(click);

        etInformation.setHint(mEditHint);
        if (!TextUtils.isEmpty(mInformation)) {
            etInformation.setText(mInformation);
        } else {
            tvSave.setEnabled(false);
        }
    }

}
