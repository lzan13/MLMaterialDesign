package net.melove.demo.design.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import net.melove.demo.design.utils.MLLog;

/**
 * Created by lzan13 on 2016/11/29.
 * 集成实现 Firebase 处理 token 相关服务
 */
public class MLFirebaseInstanceIdService extends FirebaseInstanceIdService {

    /**
     * Firebase 刷新 token 回调
     */
    @Override public void onTokenRefresh() {
        //super.onTokenRefresh();
        // 获取 Firebase Token
        String token = FirebaseInstanceId.getInstance().getToken();
        MLLog.d("firebase token: %s", token);

        // 如果自己的服务器需要调用推送服务推送内容，则在这里将 token 发送到自己的服务器
        //sendTokenToService();
    }
}
