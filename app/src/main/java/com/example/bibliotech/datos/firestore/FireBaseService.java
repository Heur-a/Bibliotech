package com.example.bibliotech.datos.firestore;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.bibliotech.datos.NotificationHelper;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseService extends Service {

    private CollectionReference collectionReference;
    private NotificationHelper notificationHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicialitza la referència a la col·lecció de Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("users").document(FireBaseActions.getUserId()).collection("reservaSala");

        // Inicialitza la classe NotificationHelper
        notificationHelper = new NotificationHelper(this);
        notificationHelper.createNotificationChannel();

        // Estableix un listener per escoltar canvis a la col·lecció de Firestore
        collectionReference.addSnapshotListener((value, error) -> {
            if (value != null) {
                for (DocumentChange dc : value.getDocumentChanges()) {
                    // Verifica el tipus de canvi (added, modified, removed)
                    switch (dc.getType()) {
                        case ADDED:
                            mostrarNotificacio("Canvi detectat!", "S'ha afegit un nou document.");
                            break;
                        case MODIFIED:
                            mostrarNotificacio("Canvi detectat!", "Un document existent ha canviat.");
                            break;
                        // Pots gestionar altres casos com "REMOVED" si és necessari
                    }
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void mostrarNotificacio(String titol, String text) {
        // Utilitza la classe NotificationHelper per a mostrar una notificació
        notificationHelper.showNotification(text,titol);
    }
}
