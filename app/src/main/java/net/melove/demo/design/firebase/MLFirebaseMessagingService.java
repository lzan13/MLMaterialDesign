package net.melove.demo.design.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import net.melove.demo.design.utils.MLLog;

/**
 * Created by lzan13 on 2016/11/29.
 * 集成实现 Firebase 消息处理服务
 */
public class MLFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * Firebase 推送消息回调
     *
     * @param remoteMessage 推送过来的消息
     */
    @Override public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        // 在这里处理 Firebase 推送过来的消息
        MLLog.d("firebase message from: %s", remoteMessage.getFrom());

        // 检查推送消息是否包含信息内容
        if (remoteMessage.getData().size() > 0) {
            MLLog.d("firebase message data: %s", remoteMessage.getData());
        }

        // 检查推送消息是否包含通知内容
        if (remoteMessage.getNotification() != null) {
            MLLog.d("firebase message notification: %s", remoteMessage.getNotification().getBody());
        }
    }
}
