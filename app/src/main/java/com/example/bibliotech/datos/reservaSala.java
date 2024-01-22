package com.example.bibliotech.datos;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class reservaSala extends reserva {

    private String roomId;
    private String objectId;

    public reservaSala(Date fechaIni, Date fechaFin, String userId, String roomId) {
        super(fechaIni, fechaFin, userId);
        this.roomId = roomId;
    }

    public reservaSala() {

    }

    public String getRoomId() {
        return roomId;
    }



    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "RoomReservation{" +
                "roomId='" + roomId + '\'' +
                "} " + super.toString();
    }


    public void anyadirAUser(String documentId, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtenir una referència al document que vols actualitzar
        DocumentReference documentRef = db.collection("users").document(documentId).collection("reservaSala").document();

        // Obtenir l'ID de la sala
        String salaId = this.getRoomId();

        // Assignar l'objecte actual com a valor al document amb un identificador generat automàticament
        documentRef.set(this)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Document updated successfully with the new object.");

                    // Crear una instància de NotificationHelper i mostrar la notificació directament
                    NotificationHelper notificationHelper = new NotificationHelper(context);
                    notificationHelper.createNotificationChannel();
                    String message = "La reserva para tu sala " + salaId + " se ha procesado con exito.";
                    notificationHelper.showNotification( message,  "Nova reserva processada");
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating the document:", e);
                });
    }



    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public static void getReservaSala(String userid, ReservasSalasCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reservaCollectionRef = db.collection("users").document(userid).collection("reservaSala");

        reservaCollectionRef.get()
                .addOnSuccessListener(task -> {
                    List<reservaSala> reservaList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task) {
                        reservaSala reserva = document.toObject(reservaSala.class);
                        reservaList.add(reserva);

                    }
                    callback.onReservasLoaded(reservaList);
                })
                .addOnFailureListener(e -> {
                    callback.onReservasError("Error: " + e.getMessage());
                });

    }

    public interface ReservasSalasCallback {
        void onReservasLoaded(List<reservaSala> reservaList);
        void onReservasError(String errorMessage);
    }


}

