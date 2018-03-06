package com.bilgetech.guvercin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


public class NotificationHelper {

    public static int SERVICE_NOTIF_ID = 1001;

    private static Notification notification = NotificationHelper
            .getBuilder(App.getInstance())
            .setContentTitle("Pigeon is active!")
            .setContentText("Your sms messages are being relayed to web.")
            .setStyle(new NotificationCompat.BigTextStyle().bigText("Your sms messages are being relayed to web."))
            .build();

    public static NotificationCompat.Builder getBuilder(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = NotificationChannel.DEFAULT_CHANNEL_ID;
            String name = "Local notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            notificationManager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(context, channelId);
        } else {
            //noinspection deprecation
            builder = new NotificationCompat.Builder(context);
        }

        return builder
                .setContentIntent(NotificationActivity.getPendingIntent())
                .setSmallIcon(android.R.drawable.ic_menu_compass);
    }

    public static Notification getNotification() {
        return notification;
    }

    public static void updateServiceNotification(String text) {
        Context ctx = App.getInstance();
        notification = getBuilder(ctx)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .build();

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(SERVICE_NOTIF_ID, notification);
    }

}
