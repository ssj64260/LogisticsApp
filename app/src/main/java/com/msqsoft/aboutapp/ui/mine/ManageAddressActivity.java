package com.msqsoft.aboutapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseAppCompatActivity;
import com.msqsoft.aboutapp.model.AddressBean;
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.service.MyObserver;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.ui.adapter.AddressListAdapter;
import com.msqsoft.aboutapp.ui.adapter.OnListClickListener;
import com.msqsoft.aboutapp.utils.ToastMaster;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 管理地址
 */

public class ManageAddressActivity extends BaseAppCompatActivity {

    private RecyclerView mRv_manage_address;
    private AddressListAdapter mAdapter;
    private List<AddressBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address);

        initView();
        getAddressList();
    }

    private void initView() {

        initToolbar();
        mRv_manage_address = (RecyclerView) findViewById(R.id.rv_manage_address);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ManageAddressActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_manage_address.setLayoutManager(layoutManager);
        mAdapter = new AddressListAdapter(ManageAddressActivity.this, mList);
        mAdapter.setOnListClickListener(new OnListClickListener() {
            @Override
            public void onItemClick(int position) {
                final AddressBean bean = mList.get(position);
                Intent i = new Intent(ManageAddressActivity.this, EditAddressActivity.class);
                i.putExtra("isAdd", "2");
                i.putExtra("bean", bean);
                startActivity(i);
            }

            @Override
            public void onTagClick(@ItemView int tag, int position) {
                final AddressBean bean = mList.get(position);
                final String id = bean.getId();
                final String name = bean.getName();
                final String mobile = bean.getMobile();
                final String area_id = bean.getArea_id();
                final String detail = bean.getDetail();
                final String status = bean.getStatus();
                switch (tag) {
                    case BUTTON:
                        if (status.equals("1")) {
                            editAddress(name, mobile, area_id, detail, "2", id);
                        } else if (status.equals("2")) {
                            editAddress(name, mobile, area_id, detail, "1", id);
                        }
                        break;
                    case TEXTVIEW:
                        Intent i = new Intent(ManageAddressActivity.this, EditAddressActivity.class);
                        i.putExtra("isAdd", "2");
                        i.putExtra("bean", bean);
                        startActivity(i);
                        break;
                    case LINEARLAYOUT:
                        deleteAddress(id);
                        break;
                }
            }
        });
        mRv_manage_address.setAdapter(mAdapter);
    }

    private void initToolbar() {

        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        final ImageView ivAdd = (ImageView) findViewById(R.id.iv_toolbar_action3);

        ivBack.setOnClickListener(click);
        tvTitle.setText(getString(R.string.text_button_manage_address));
        ivAdd.setVisibility(View.VISIBLE);
        ivAdd.setOnClickListener(click);
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    hideKeyboard();
                    onBackPressed();
                    break;
                case R.id.iv_toolbar_action3:
                    Intent i = new Intent(ManageAddressActivity.this, EditAddressActivity.class);
                    i.putExtra("isAdd", "1");
                    startActivity(i);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getAddressList();
    }

    /**
     * 获取地址列表接口
     */
    private void getAddressList() {
        showProgress(getString(R.string.text_progress_loading));
        final String token = getAboutAppToken();
        ServiceClient.getService().getAddressList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new MyObserver<ServiceResult<List<AddressBean>>>() {
                            @Override
                            public void onSuccess(ServiceResult<List<AddressBean>> result) {
                                final List<AddressBean> list = result.getResultData();
                                if (list != null && list.size() > 0) {
                                    mList.clear();
                                    mList.addAll(list);
                                    mAdapter.notifyDataSetChanged();
                                }
                                hideProgress();
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
                                getAddressList();
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
     * 删除地址
     */
    private void deleteAddress(String aid) {
        showProgress(getString(R.string.text_progress_committing));
        final String token = getAboutAppToken();
        ServiceClient.getService().deleteAddress(token, aid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new MyObserver<ServiceResult>() {
                            @Override
                            public void onSuccess(ServiceResult result) {
                                getAddressList();
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
