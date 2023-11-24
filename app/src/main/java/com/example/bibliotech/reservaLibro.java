package com.example.bibliotech;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class reservaLibro extends reserva{

    private String bookId;

    public reservaLibro(Date fechaIni, Date fechaFin, String userId, String bookId) {
        super(fechaIni, fechaFin, userId);
        this.bookId = bookId;
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

    public void anyadirAUser(String documentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get a reference to the document you want to update
        DocumentReference documentRef = db.collection("books").document(documentId);

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
}
