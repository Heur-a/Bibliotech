package com.example.bibliotech.datos;

// Clase NotificationHelper.java

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.example.bibliotech.R;

public class NotificationHelper {

    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MiNotificacionChannel";
            String description = "Canal de notificación para mi aplicación";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MiNotificacionChannel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(String message) {
        Notification.Builder builder = new Notification.Builder(context, "MiNotificacionChannel")
                .setSmallIcon(R.drawable.logoappsintexto)
                .setContentTitle("BiblioTech")
                .setContentText(message);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
