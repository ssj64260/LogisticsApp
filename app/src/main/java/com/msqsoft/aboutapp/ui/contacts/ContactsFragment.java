package com.msqsoft.aboutapp.ui.contacts;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseFragment;
import com.msqsoft.aboutapp.ui.adapter.ContactsFragmentPageAdapter;
import com.msqsoft.aboutapp.utils.ToastMaster;

import java.util.ArrayList;

import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.model.Conversation;

/**
 * 通讯列表
 */

public class ContactsFragment extends BaseFragment {

    private static final String ARGUMENT = "argument";

    private View mRootView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private ArrayList<Fragment> fragments;
    private Conversation.ConversationType[] mConversationsTypes = null;

    public ContactsFragment() {

    }

    public static ContactsFragment newInstance(String param) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, param);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_contacts, null);
            initView();

            setData();
        }

        return mRootView;
    }

    private void initToolbar() {
        final ImageView ivBack = (ImageView) mRootView.findViewById(R.id.iv_toolbar_back);
        final TextView tvTitle = (TextView) mRootView.findViewById(R.id.tv_toolbar_title);
        final ImageView ivAction = (ImageView) mRootView.findViewById(R.id.iv_toolbar_action1);

        ivBack.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.title_contacts));
        ivAction.setImageResource(R.drawable.nav_add);
        ivAction.setVisibility(View.VISIBLE);
        ivAction.setOnClickListener(click);
    }

    private void initView() {
        initToolbar();
        mTabLayout = (TabLayout) mRootView.findViewById(R.id.tablayout);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.vp_contacts);
    }

    private void setData() {
        final String[] tabTitles = new String[]{
                getString(R.string.text_tab_chat),
                getString(R.string.text_tab_friends)
        };

        fragments = new ArrayList<>(2);
        fragments.add(initConversationList());
        fragments.add(FriendsListFragment.newInstance(""));

        mViewPager.setAdapter(new ContactsFragmentPageAdapter(getChildFragmentManager(), tabTitles, fragments));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private ConversationListFragment initConversationList() {
        ConversationListFragment listFragment = new ConversationListFragment();
        listFragment.setAdapter(new ConversationListAdapter(RongContext.getInstance()));
        Uri uri;
        uri = Uri.parse("rong://" + mActivity.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                .build();
        mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.PUBLIC_SERVICE,
                Conversation.ConversationType.APP_PUBLIC_SERVICE,
                Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.DISCUSSION
        };
        listFragment.setUri(uri);

        return listFragment;
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_action1:
                    ToastMaster.toast("加好友");
                    break;
            }
        }
    };

}
