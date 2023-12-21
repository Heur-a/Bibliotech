package com.example.bibliotech.datos;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bibliotech.datos.reserva;
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

    public reservaSala(Date startDate, Date endDate, String userId, String roomId) {
        super(startDate, endDate, userId);
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

    public void anyadirAUser(String documentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get a reference to the document you want to update
        DocumentReference documentRef = db.collection("salas").document(documentId);

        // Use the set method to replace all data in the document with the new object
        documentRef.set(this)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document updated successfully with the new object.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating the document:", e);
                    }
                });
    }

    public static void getReservaSala(String userid, ReservasSalasCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reservaCollectionRef = db.collection("users").document(userid).collection("reservaLibro");

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

