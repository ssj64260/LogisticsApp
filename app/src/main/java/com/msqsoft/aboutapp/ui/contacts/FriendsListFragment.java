package com.msqsoft.aboutapp.ui.contacts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseFragment;
import com.msqsoft.aboutapp.model.FriendBean;
import com.msqsoft.aboutapp.model.ServiceResult;
import com.msqsoft.aboutapp.service.MyObserver;
import com.msqsoft.aboutapp.service.ServiceClient;
import com.msqsoft.aboutapp.ui.adapter.FriendsListAdapter;
import com.msqsoft.aboutapp.ui.adapter.OnListClickListener;
import com.msqsoft.aboutapp.utils.ToastMaster;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 好友列表
 */

public class FriendsListFragment extends BaseFragment {

    private static final String ARGUMENT = "argument";

    private View mRootView;
    private RecyclerView mRv_friends_list;
    private FriendsListAdapter mAdapter;
    private List<FriendBean> mList = new ArrayList<>();

    public FriendsListFragment() {

    }

    public static FriendsListFragment newInstance(String param) {
        FriendsListFragment fragment = new FriendsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, param);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mRootView == null){
            mRootView = inflater.inflate(R.layout.fragment_friends_list, null);
            initView();
        }
        return mRootView;
    }

    public void initView(){
        mRv_friends_list = (RecyclerView) mRootView.findViewById(R.id.rv_friends_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_friends_list.setLayoutManager(layoutManager);
        mAdapter = new FriendsListAdapter(getContext(), mList);
        mAdapter.setOnListClickListener(new OnListClickListener() {
            @Override
            public void onItemClick(int position) {
                ToastMaster.toast("点击第"+String.valueOf(position)+"个");
            }

            @Override
            public void onTagClick(@ItemView int tag, int position) {

            }
        });
        mRv_friends_list.setAdapter(mAdapter);

        getFriendsList();
    }

    private void getFriendsList() {
        showProgress(getString(R.string.text_progress_loading));
        final String token = getAboutAppToken();
        ServiceClient.getService().getFriendsList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new MyObserver<ServiceResult<List<FriendBean>>>() {
                            @Override
                            public void onSuccess(ServiceResult<List<FriendBean>> result) {
                                final List<FriendBean> list = result.getResultData();
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
}
