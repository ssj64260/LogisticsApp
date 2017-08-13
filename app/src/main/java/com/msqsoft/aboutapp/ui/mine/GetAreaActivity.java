package com.msqsoft.aboutapp.ui.mine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseAppCompatActivity;
import com.msqsoft.aboutapp.config.Config;
import com.msqsoft.aboutapp.model.AreaBean;
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.service.MyObserver;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.ui.adapter.AreaListAdapter;
import com.msqsoft.aboutapp.ui.adapter.OnListClickListener;
import com.msqsoft.aboutapp.utils.LocationUtil;
import com.msqsoft.aboutapp.utils.PreferencesUtil;
import com.msqsoft.aboutapp.utils.ToastMaster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GetAreaActivity extends BaseAppCompatActivity {

    private LinearLayout mLl_location_now;
    private TextView mTv_location_now, mTv_choose_area;
    private RecyclerView mRv_area;

    private AreaListAdapter mAdapter;
    private List<AreaBean> mList = new ArrayList<>();
    private String areaName, areaId;
    private StringBuilder area = new StringBuilder();
    private double longitude, latitude;
    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private Address address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_area);

        initView();
        initData();
    }

    private void initView() {

        initToolbar();
        mLl_location_now = (LinearLayout) findViewById(R.id.ll_location_now);
        mTv_location_now = (TextView) findViewById(R.id.tv_location_now);
        mTv_choose_area = (TextView) findViewById(R.id.tv_choose_area);
        mRv_area = (RecyclerView) findViewById(R.id.rv_area);

        mLl_location_now.setOnClickListener(click);
        LinearLayoutManager layoutManager = new LinearLayoutManager(GetAreaActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_area.setLayoutManager(layoutManager);
        mAdapter = new AreaListAdapter(GetAreaActivity.this, mList);
        mAdapter.setOnListClickListener(new OnListClickListener() {
            @Override
            public void onItemClick(int position) {
                final AreaBean bean = mList.get(position);
                areaId = bean.getId();
                areaName = bean.getName();
                area.append(areaName);
                mTv_choose_area.setVisibility(View.VISIBLE);
                mTv_choose_area.setText(areaName);
                getArea(areaId);
            }

            @Override
            public void onTagClick(@ItemView int tag, int position) {

            }
        });
        mRv_area.setAdapter(mAdapter);
    }

    public void initData(){

        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,1);
        }else {
            requestLocation();
        }

        getArea("");
    }

    private void initToolbar() {

        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        ivBack.setOnClickListener(click);
        tvTitle.setText(getString(R.string.title_choose_address));
    }

    private void requestLocation(){
        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if(providers.contains(LocationManager.NETWORK_PROVIDER)){
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else if(providers.contains(LocationManager.GPS_PROVIDER)){
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        }else{
            ToastMaster.toast(getString(R.string.title_open_location));
            return ;
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.
                permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //获取Location
            location = locationManager.getLastKnownLocation(locationProvider);
            //不为空,显示定位城市，保存经纬度
            if (location != null) {
                showLocation(location);
            }else {
                locationManager.requestLocationUpdates(locationProvider, 500, 1, locationListener);
            }
        }
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */
    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }
    };

    private void showLocation(Location location){
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        try {
            address = LocationUtil.getCity(this, longitude, latitude);
            mTv_location_now.setText(address.getAdminArea() + address.getLocality() + address.getSubLocality());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(locationManager != null){
            //移除监听器
            locationManager.removeUpdates(locationListener);
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
                case R.id.ll_location_now:
                    if(address == null) {
                        requestLocation();
                    }else {
                        if (!TextUtils.isEmpty(address.getSubLocality())) {
                            getPosition(address.getSubLocality());
                        } else if (!TextUtils.isEmpty(address.getLocality())) {
                            getPosition(address.getLocality());
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            //移除监听器
            locationManager.removeUpdates(locationListener);
        }
    }

    /**
     * 获取省市区接口
     */
    private void getArea(String pid) {
        showProgress(getString(R.string.text_progress_requesting));
        ServiceClient.getService().getArea(pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new MyObserver<ServiceResult<List<AreaBean>>>() {
                            @Override
                            public void onSuccess(ServiceResult<List<AreaBean>> result) {
                                final List<AreaBean> list = result.getResultData();
                                if(list != null && list.size() > 0){
                                    mList.clear();
                                    mList.addAll(list);
                                    mAdapter.notifyDataSetChanged();
                                }else {
                                    Intent i = new Intent();
                                    i.putExtra("AREA", area.toString());
                                    i.putExtra("AREAID", areaId);
                                    setResult(RESULT_OK, i);
                                    finish();
                                }
                                hideProgress();
                            }

                            @Override
                            public void onError(String errorMsg) {
                                super.onError(errorMsg);
                                hideProgress();
                                ToastMaster.toast(errorMsg);
                            }
                        });
    }

    /**
     * 提交定位接口
     */
    private void getPosition(String area) {
        showProgress(getString(R.string.text_progress_committing));
        ServiceClient.getService().getPosition(area)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new MyObserver<ServiceResult<Map<String, String>>>() {
                            @Override
                            public void onSuccess(ServiceResult<Map<String, String>> result) {
                                final Map<String, String> map = result.getResultData();
                                Intent i = new Intent();
                                i.putExtra("AREA", address.getAdminArea() + address.getLocality() + address.getSubLocality());
                                i.putExtra("AREAID", map.get("area_id"));
                                setResult(RESULT_OK, i);
                                finish();
                                hideProgress();
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
