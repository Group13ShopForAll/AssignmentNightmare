package my.edu.utar.assignmentnightmare;

import android.util.Log;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

// Done by Jiun Lin
public class MyPushService extends HmsMessageService {
    private static final String TAG = "MyPushService";
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i(TAG,"onNewToken: "+s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "onMessageReceived");
    }
}
