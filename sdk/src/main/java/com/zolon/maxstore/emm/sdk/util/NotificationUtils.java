package com.zolon.maxstore.emm.sdk.util;

import static com.zolon.maxstore.emm.sdk.CommonConstants.SP_SMALL_LOGO_ICON;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;

import com.zolon.maxstore.emm.sdk.BuildConfig;
import com.zolon.maxstore.emm.sdk.R;

public class NotificationUtils {
    private static final int NOTIFICATION_ID = 1002;

    /**
     *
     * @param service
     * @param smallIcon
     * @param content
     */
    public static void showForeGround(Service service, int smallIcon, String content) {
        NotificationChannel mChannel = null;
        NotificationManager notificationManager = (NotificationManager) service.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(BuildConfig.LIBRARY_PACKAGE_NAME, service.getApplicationContext().getString(R.string.app_name), NotificationManager.IMPORTANCE_NONE);
            mChannel.setSound(null, null);
            mChannel.enableVibration(false);
            notificationManager.createNotificationChannel(mChannel);

            Notification.Builder builder = new Notification.Builder(service.getApplicationContext(), BuildConfig.LIBRARY_PACKAGE_NAME);
            builder.setContentText(content);
            builder.setSmallIcon(smallIcon);
            builder.setAutoCancel(true);
            builder.setShowWhen(true);

            // 这里两个通知使用同一个id且必须按照这个顺序后调用startForeground
            service.startForeground(NOTIFICATION_ID, builder.build());
        }
    }

    /**
     *
     * @param service
     * @param content
     */
    public static void showForeGround(Service service, String content) {
        NotificationChannel mChannel = null;
        NotificationManager notificationManager = (NotificationManager) service.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(BuildConfig.LIBRARY_PACKAGE_NAME, service.getApplicationContext().getString(R.string.app_name), NotificationManager.IMPORTANCE_NONE);
            mChannel.setSound(null, null);
            mChannel.enableVibration(false);
            notificationManager.createNotificationChannel(mChannel);

            Notification.Builder builder = new Notification.Builder(service.getApplicationContext(), BuildConfig.LIBRARY_PACKAGE_NAME);
            builder.setContentText(content);
            builder.setSmallIcon(PreferencesUtils.getInt(service, SP_SMALL_LOGO_ICON, R.drawable.ic_notificaiton));
            builder.setAutoCancel(true);
            builder.setShowWhen(true);

            // 这里两个通知使用同一个id且必须按照这个顺序后调用startForeground
            service.startForeground(NOTIFICATION_ID, builder.build());
        }
    }
}
