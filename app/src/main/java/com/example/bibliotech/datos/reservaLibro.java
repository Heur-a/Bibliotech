package com.example.bibliotech.datos;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class reservaLibro extends reserva {

    private String bookId;
    private FirebaseFirestore db;

    public reservaLibro(Date fechaIni, Date fechaFin, String userId, String bookId) {
        super(fechaIni, fechaFin, userId);
        db = FirebaseFirestore.getInstance();
        this.bookId = bookId;
    }
    public reservaLibro() {
        super();
        db = FirebaseFirestore.getInstance();
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

    public boolean anyadirALibro(String bookId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final boolean[] ret = {false};

        if(db.collection("books").document(bookId).collection("reserva").whereGreaterThan("fechaFin", new Date() ).get() != null){

            db.collection("books").document(bookId).collection("reserva").document()
                    .set(this, SetOptions.merge())
                    .addOnSuccessListener(success -> {
                        Log.d("reservaBook","Successfully added reservaLibro to Libro");
                        ret[0] = true;
                    })
                    .addOnFailureListener(e -> {
                        Log.d("reservaBook", "Error setting book" + e.getMessage());

                    });
        }

        return ret[0];


    }

    public interface ReservasLibrosCallback {
        void onReservasLoaded(List<reservaLibro> reservaList);
        void onReservasError(String errorMessage);
    }

    public static void getReservasBook(String userid, ReservasLibrosCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reservaCollectionRef = db.collection("users").document(userid).collection("reservaLibro");

        reservaCollectionRef.get()
                .addOnSuccessListener(task -> {
                    List<reservaLibro> reservaList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task) {
                        reservaLibro reserva = document.toObject(reservaLibro.class);
                        reservaList.add(reserva);
                    }
                    callback.onReservasLoaded(reservaList);
                })
                .addOnFailureListener(e -> {
                    callback.onReservasError("Error: " + e.getMessage());
                });
    }
}
