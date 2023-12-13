package com.example.bibliotech;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class UserFirestore {
    private CollectionReference users;

    public interface UserCallback {
        void onUserLoaded(User user);

        void onUserError(Exception e);
    }

    public UserFirestore() {
        // Inicializa Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        users = db.collection("users");
    }

    public void add(User user) {
        // Añade un usuario comprobando si ya existen datos sin sobrescribirlo todo
        users.document(user.id).set(user);
    }

    public void delete(String id) {
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // !!! CAUTION: CAUTION. WE SHOULD NEVER EVER DELETE USERS !!!
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        users.document(id).delete();
    }

    public void getUser(String id, UserCallback callback) {
        users.document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    callback.onUserLoaded(user);
                })
                .addOnFailureListener(e -> {
                    Log.d("FireBase GET", "Error fetching user: " + e.getMessage());
                    callback.onUserError(e);
                });

    }
}
