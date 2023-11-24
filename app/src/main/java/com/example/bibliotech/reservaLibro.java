package com.example.bibliotech;

import android.util.Log;

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

public class reservaLibro extends reserva{

    private String bookId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public reservaLibro(Date fechaIni, Date fechaFin, String userId, String bookId) {
        super(fechaIni, fechaFin, userId);
        this.bookId = bookId;
    }
    public reservaLibro() {
        super(null,null,null);
        this.bookId = null;

    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "reservaLibro{" +
                "bookId='" + bookId + '\'' +
                "} " + super.toString();
    }

    public void anyadirAUser(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get a reference to the document you want to update
        DocumentReference documentRef = db.collection("users").document(userId).collection("reservaLibro").document();

        // Use the set method to replace all data in the document with the new object
        documentRef.set(this)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("HOLA", "Document updated successfully with the new object.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("HOLA", "Error updating the document:", e);
                    }
                });
    }

    static public List<reservaLibro> getReservasBook(String user) {
        List<reservaLibro> reservaList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reservaCollectionRef = db.collection("users").document(user).collection("reservaLibro");

        reservaCollectionRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            reservaLibro reserva = document.toObject(com.example.bibliotech.reservaLibro.class);
                            reservaList.add(reserva);
                        }
                    } else {
                        // Handle error
                    }
                });

        return reservaList;
    }
}
