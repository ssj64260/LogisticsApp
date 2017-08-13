package com.msqsoft.aboutapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseAppCompatActivity;
import com.msqsoft.aboutapp.model.AddressBean;
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.service.MyObserver;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.utils.StringCheck;
import com.msqsoft.aboutapp.utils.ToastMaster;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EditAddressActivity extends BaseAppCompatActivity {

    //添加地址为1，编辑地址为2
    private String is_add = "1", id, name, mobile, area, areaId, detail, status = "1";
    private AddressBean bean;
    private final int GETAREA = 1;

    private EditText mEt_edit_address_name, mEt_edit_address_mobile, mEt_edit_address_detail;
    private LinearLayout mLl_edit_address_address, mLl_edit_address_set_default;
    private TextView mTv_edit_address_address, mTv_edit_address_save;
    private ImageView mIv_edit_address_set_default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        is_add = getIntent().getStringExtra("isAdd");
        initView();
        initData();
        setEditStatus();
    }

    public void initView(){

        initToolbar();
        mEt_edit_address_name = (EditText) findViewById(R.id.et_edit_address_name);
        mEt_edit_address_mobile = (EditText) findViewById(R.id.et_edit_address_mobile);
        mLl_edit_address_address = (LinearLayout) findViewById(R.id.ll_edit_address_address);
        mTv_edit_address_address = (TextView) findViewById(R.id.tv_edit_address_address);
        mEt_edit_address_detail = (EditText) findViewById(R.id.et_edit_address_detail);
        mLl_edit_address_set_default = (LinearLayout) findViewById(R.id.ll_edit_address_set_default);
        mIv_edit_address_set_default = (ImageView) findViewById(R.id.iv_edit_address_set_default);
        mTv_edit_address_save = (TextView) findViewById(R.id.tv_edit_address_save);

        mEt_edit_address_name.addTextChangedListener(textWatcher);
        mEt_edit_address_mobile.addTextChangedListener(textWatcher);
        mLl_edit_address_address.setOnClickListener(click);
        mTv_edit_address_address.addTextChangedListener(textWatcher);
        mEt_edit_address_detail.addTextChangedListener(textWatcher);
        mLl_edit_address_set_default.setOnClickListener(click);
        mTv_edit_address_save.setOnClickListener(click);
    }

    public void initData(){

        if(is_add.equals("2")){
            bean = (AddressBean) getIntent().getSerializableExtra("bean");
            id = bean.getId();
            name = bean.getName();
            mobile = bean.getMobile();
            area = bean.getArea();
            areaId = bean.getArea_id();
            detail = bean.getDetail();
            status = bean.getStatus();

            mEt_edit_address_name.setText(name);
            mEt_edit_address_mobile.setText(mobile);
            mTv_edit_address_address.setText(area);
            mEt_edit_address_detail.setText(detail);
            if(status.equals("1")){
                mIv_edit_address_set_default.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_nomal));
            }else if(status.equals("2")){
                mIv_edit_address_set_default.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_selected));
            }
        }
    }

    private void initToolbar() {

        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(click);
        if(is_add.equals("1")) {
            tvTitle.setText(getString(R.string.title_add_address));
        }else if(is_add.equals("2")){
            tvTitle.setText(getString(R.string.title_edit_address));
        }
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    hideKeyboard();
                    onBackPressed();
                    break;
                case R.id.ll_edit_address_address:
                    Intent i = new Intent(EditAddressActivity.this, GetAreaActivity.class);
                    startActivityForResult(i,GETAREA);
                    break;
                case R.id.ll_edit_address_set_default:
                    if(status.equals("1")){
                        mIv_edit_address_set_default.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_selected));
                        status = "2";
                    }else if(status.equals("2")){
                        mIv_edit_address_set_default.setImageDrawable(getResources().getDrawable(R.drawable.ic_radio_nomal));
                        status = "1";
                    }
                    break;
                case R.id.tv_edit_address_save:
                    final String edit_mobile = mEt_edit_address_mobile.getText().toString();
                    if(!StringCheck.isMobileNO(edit_mobile)){
                        ToastMaster.toast(getString(R.string.toast_phone_number_error));
                        return;
                    }
                    if(is_add.equals("1")){
                        addAddress(mEt_edit_address_name.getText().toString(), edit_mobile, areaId,
                                mEt_edit_address_detail.getText().toString(), status);
                    }else if (is_add.equals("2")){
                        editAddress(mEt_edit_address_name.getText().toString(), edit_mobile, areaId,
                                mEt_edit_address_detail.getText().toString(), status, id);
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
            setEditStatus();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setEditStatus() {
        final String edit_name = mEt_edit_address_name.getText().toString();
        final String edit_mobile = mEt_edit_address_mobile.getText().toString();
        final String edit_address = mTv_edit_address_address.getText().toString();
        final String edit_detail = mEt_edit_address_detail.getText().toString();
        final boolean nameIsEmpty = TextUtils.isEmpty(edit_name);
        final boolean mobileIsEmpty = TextUtils.isEmpty(edit_mobile);
        final boolean addressIsEmpty = TextUtils.isEmpty(edit_address);
        final boolean detailIsEmpty = TextUtils.isEmpty(edit_detail);
        final boolean enabled = mTv_edit_address_save.isEnabled();
        if (!nameIsEmpty && !mobileIsEmpty && !addressIsEmpty && !detailIsEmpty && !enabled) {
            mTv_edit_address_save.setEnabled(true);
        } else if ((nameIsEmpty || mobileIsEmpty || addressIsEmpty || detailIsEmpty) && enabled) {
            mTv_edit_address_save.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case GETAREA:
                    final String area = data.getStringExtra("AREA");
                    areaId = data.getStringExtra("AREAID");
                    mTv_edit_address_address.setText(area);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEt_edit_address_name.removeTextChangedListener(textWatcher);
        mEt_edit_address_mobile.removeTextChangedListener(textWatcher);
        mTv_edit_address_address.removeTextChangedListener(textWatcher);
        mEt_edit_address_detail.removeTextChangedListener(textWatcher);
    }

    /**
     * 添加地址接口
     */
    private void addAddress(String name, String mobile, String areaId, String detail, String status) {
        showProgress(getString(R.string.text_progress_committing));
        final String token = getAboutAppToken();
        ServiceClient.getService().addAddress(token, name, mobile, areaId, detail, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new MyObserver<ServiceResult>() {
                            @Override
                            public void onSuccess(ServiceResult result) {
                                hideProgress();
                                finish();
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

    /**
     * 编辑地址接口
     */
    private void editAddress(String name, String mobile, String areaId, String detail, String status, String aid) {
        showProgress(getString(R.string.text_progress_committing));
        final String token = getAboutAppToken();
        ServiceClient.getService().editAddress(token, name, mobile, areaId, detail, status, aid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new MyObserver<ServiceResult>() {
                            @Override
                            public void onSuccess(ServiceResult result) {
                                hideProgress();
                                finish();
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
