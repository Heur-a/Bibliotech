package com.example.bibliotech;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bibliotech.presentacion.paginaInicialActivity;

public class paginacarga extends AppCompatActivity {

    ImageView fondoanimacion;
    Handler handler; // se encarga de hacer el retardo

    private final int TIEMPO = 5000; // tiempo en ms
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paginacarga);

        // Configurar notificación
        createNotificationChannel();

        // Construir y mostrar la notificación
        showNotification("¡Bienvenido a tu aplicación!");

        fondoanimacion = findViewById(R.id.fondoanimacion);
        lottieAnimationView = findViewById(R.id.animacion);
        fondoanimacion.animate().translationX(-1600).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationX(1400).setDuration(1000).setStartDelay(4000);

        handler = new Handler();
        handler.postDelayed(new Runnable() { // define el retardo con el que se hará la acción
            @Override
            public void run() { // inicia la acción
                cambiopag();
            }
        }, TIEMPO); // le pone el tiempo con el que tarda en hacer
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MiNotificacionChannel";
            String description = "Canal de notificación para mi aplicación";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MiNotificacionChannel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String message) {
        Notification.Builder builder = new Notification.Builder(this, "MiNotificacionChannel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Título de la notificación")
                .setContentText(message);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public void cambiopag() {
        Intent intent = new Intent(this, paginaInicialActivity.class);
        startActivity(intent);
        finish(); // se asegura de finalizar la actividad
    }
}
