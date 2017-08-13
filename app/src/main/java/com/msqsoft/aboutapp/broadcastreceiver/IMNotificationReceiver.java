package com.msqsoft.aboutapp.broadcastreceiver;

import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 融云推送广播接收
 */

public class IMNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        //TODO 通知点击跳转
//        final String targetId = pushNotificationMessage.getTargetId();
//        final String targetName = pushNotificationMessage.getTargetUserName();
//        final String pushId = pushNotificationMessage.getPushId();
//        final String extra = pushNotificationMessage.getExtra();
//        final String packageName = "com.msqsoft.aboutapp";
//
//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        Uri.Builder uriBuilder = Uri.parse("rong://" + packageName).buildUpon();
//        uriBuilder.appendPath("push_message")
//                .appendQueryParameter("targetId", targetId)
//                .appendQueryParameter("title", targetName)
//                .appendQueryParameter("pushId", pushId)
//                .appendQueryParameter("extra", extra);
//
//        context.startActivity(intent);
        return true;
    }
}
