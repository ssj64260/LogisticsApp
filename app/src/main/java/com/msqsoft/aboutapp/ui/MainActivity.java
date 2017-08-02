package com.msqsoft.aboutapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseAppCompatActivity;
import com.msqsoft.aboutapp.model.TabEntity;
import com.msqsoft.aboutapp.ui.contacts.ContactsFragment;
import com.msqsoft.aboutapp.ui.mine.LoginActivity;
import com.msqsoft.aboutapp.ui.mine.MineFragment;
import com.msqsoft.aboutapp.ui.order.OrderFragment;
import com.msqsoft.aboutapp.utils.FastClick;
import com.msqsoft.aboutapp.utils.ToastMaster;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends BaseAppCompatActivity implements RongIM.UserInfoProvider {

    private static final String CURRENT_TAB = "current_tab";

    private final int[] normalIcons = new int[]{
            R.drawable.tab_home_normal,
            R.drawable.tab_communication_normal,
            R.drawable.tab_my_normal
    };

    private final int[] selectedIcons = new int[]{
            R.drawable.tab_home_selected,
            R.drawable.tab_communication_selected,
            R.drawable.tab_my_selected
    };

    private CommonTabLayout mTabLayout;

    private Fragment currentFragment;

    private Fragment[] fragmentList;

    private ArrayList<CustomTabEntity> mTabEntities;

    private RongIMClient.ConnectCallback mIMCallback = new RongIMClient.ConnectCallback() {
        @Override
        public void onTokenIncorrect() {

        }

        @Override
        public void onSuccess(String s) {
            RongIM.setUserInfoProvider(MainActivity.this, true);
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setData();
        initFragment(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_TAB, mTabLayout.getCurrentTab());
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        mTabLayout = (CommonTabLayout) findViewById(R.id.com_tablayout);
    }

    private void setData() {
        mTabEntities = new ArrayList<>();
        final String[] titles = new String[]{
                getString(R.string.tab_order_text),
                getString(R.string.tab_contacts_text),
                getString(R.string.tab_mine_message_text)
        };

        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], selectedIcons[i], normalIcons[i]));
        }

        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showFragment(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        if (isLogin()) {
            doConnectRongIM(mIMCallback);
        }
    }

    private void initFragment(Bundle savedInstanceState) {
        int currentTab = 0;
        fragmentList = new Fragment[mTabEntities.size()];
        if (savedInstanceState != null) {
            currentTab = savedInstanceState.getInt(CURRENT_TAB, 0);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            List<Fragment> list = getSupportFragmentManager().getFragments();
            if (list != null) {
                Logger.d(list.size() + "");
                for (Fragment fragment : list) {
                    if (fragment instanceof OrderFragment) {
                        fragmentList[0] = fragment;
                    } else if (fragment instanceof ContactsFragment) {
                        fragmentList[1] = fragment;
                    } else if (fragment instanceof MineFragment) {
                        fragmentList[2] = fragment;
                    }
                }
            }
            transaction.commit();
        }

        if (fragmentList[0] == null) {
            fragmentList[0] = OrderFragment.newInstance("");
        }
        if (fragmentList[1] == null) {
            fragmentList[1] = ContactsFragment.newInstance("");
        }
        if (fragmentList[2] == null) {
            fragmentList[2] = MineFragment.newInstance("");
        }

        showFragment(currentTab);
    }

    private void showFragment(int position) {

        if (position > 0) {
            if (!isLogin()) {
                mTabLayout.setCurrentTab(0);
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        if (position < fragmentList.length) {
            currentFragment = fragmentList[position];

            if (!currentFragment.isAdded()) {
                transaction.add(R.id.fragment, currentFragment);
            }
            transaction.show(currentFragment);
        }

        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().disconnect();
    }

    @Override
    public void onBackPressed() {
        if (FastClick.isExitClick()) {
            finish();
        } else {
            ToastMaster.toast(getString(R.string.toast_exit));
        }
    }

    @Override
    public UserInfo getUserInfo(String id) {
        return null;
    }
}
