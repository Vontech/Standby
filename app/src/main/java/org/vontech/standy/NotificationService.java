package org.vontech.standy;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * The service used to receive notification and speak stuff using the speech API.
 * @author Aaron Vontell
 */
public class NotificationService extends NotificationListenerService {

    private Context context;
    private Speaker speaker;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("NOTIFS", "Service created");
        context = getApplicationContext();
        speaker = Speaker.getInstance(context);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Log.e("NOTIFS", "Got notification " + sbn.getPackageName());

        String appName = getAppName(sbn.getPackageName());
        String title = sbn.getNotification().extras.getString("android.title");
        String content = sbn.getNotification().extras.getString("android.text");

        String text = appName != null ? "New message from " +  appName : "New message";
        title = title != null ? title + " " : " ";
        content = content != null ? content + " ": " ";

        speaker.bestEffortSpeak(text);
        speaker.bestEffortSpeak(title);
        speaker.bestEffortSpeak(content);

        super.onNotificationPosted(sbn);
    }

    private String getAppName(String packageName) {
        final PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : null);
    }

}
