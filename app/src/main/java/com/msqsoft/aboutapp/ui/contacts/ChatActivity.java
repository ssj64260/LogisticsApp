package com.msqsoft.aboutapp.ui.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msqsoft.aboutapp.R;
import com.msqsoft.aboutapp.app.BaseAppCompatActivity;
import com.msqsoft.aboutapp.ui.MainActivity;
import com.msqsoft.aboutapp.utils.ToastMaster;

import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 聊天界面
 */

public class ChatActivity extends BaseAppCompatActivity {

    private TextView mTitle;

    private String mTargetId;//对方id
    private Conversation.ConversationType mConversationType;//会话类型
    private String title;
    private boolean isFromPush = false;//是否在讨论组内，如果不在讨论组内，则进入不到讨论组设置页面

    private ConversationFragment fragment;

    private RongIMClient.ConnectCallback mIMCallback = new RongIMClient.ConnectCallback() {
        @Override
        public void onTokenIncorrect() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgress();
                }
            });
        }

        @Override
        public void onSuccess(String s) {
            hideProgress();
            enterFragment(mConversationType, mTargetId);
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {
            hideProgress();
            enterFragment(mConversationType, mTargetId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();
        setData();

    }

    private void initToolbar() {
        mTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        final ImageView ivBack = (ImageView) findViewById(R.id.iv_toolbar_back);
        final ImageView ivAction = (ImageView) findViewById(R.id.iv_toolbar_action1);

        ivBack.setOnClickListener(click);
        ivAction.setOnClickListener(click);
        ivAction.setImageResource(R.drawable.nav_call);
        ivAction.setVisibility(View.VISIBLE);
    }

    private void initView() {
        initToolbar();
    }

    private void setData() {
        Intent intent = getIntent();

        if (intent == null || intent.getData() == null)
            return;

        mTargetId = intent.getData().getQueryParameter("targetId");
        title = intent.getData().getQueryParameter("title");
        mTitle.setText(title);

        if (mTargetId != null && mTargetId.equals("10000")) {
//            startActivity(new Intent(ConversationActivity.this, NewFriendListActivity.class));
            //TODO 跳转到加好友页面
            return;
        }
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.US));

        isPushMessage(intent);
    }

    /**
     * 加载会话页面 ConversationFragmentEx 继承自 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId         会话 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        fragment = new ConversationFragment();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //xxx 为你要加载的 id
        transaction.add(R.id.conversation, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 判断是否是 Push 消息，判断是否需要做 connect 操作
     */
    private void isPushMessage(Intent intent) {

        if (intent == null || intent.getData() == null)
            return;
        //push
        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {
            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
                //只有收到系统消息和不落地 push 消息的时候，pushId 不为 null。而且这两种消息只能通过 server 来发送，客户端发送不了。
                //RongIM.getInstance().getRongIMClient().recordNotificationEvent(id);
                showProgress("");
                isFromPush = true;
                enterActivity();
            } else if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                showProgress("");
                if (intent.getData().getPath().contains("conversation/system")) {
                    //TODO 跳到MainActivity
                    return;
                }
                enterActivity();
            } else {
                if (intent.getData().getPath().contains("conversation/system")) {
                    //TODO 跳到MainActivity
                    return;
                }
                enterFragment(mConversationType, mTargetId);
            }
        } else {
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                showProgress("");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enterActivity();
                    }
                }, 300);
            } else {
                enterFragment(mConversationType, mTargetId);
            }
        }
    }

    private void enterActivity() {
        if (isLogin()) {
            doConnectRongIM(mIMCallback);
        } else {
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
            onBackPressed();
        }
    }

    //点击监听
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_toolbar_back:
                    onBackPressed();
                    break;
                case R.id.iv_toolbar_action1:
                    ToastMaster.toast("电话");
                    break;
            }
        }
    };

}
