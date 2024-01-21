package com.example.bibliotech.datos.firestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bibliotech.datos.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashSet;
import java.util.Set;
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
        users.document(user.id).set(user, SetOptions.merge());
    }

    public void addAutoId(User user) {

        users.add(user);
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

    public void getUser(getUserSet callback){
        Set<User> userSet = new HashSet<>();
        users.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                User user = queryDocumentSnapshot.toObject(User.class);
                userSet.add(user);
            }
            callback.onUserLoaded(userSet);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onUserFail(e);
            }
        });
    }


    public interface getUserSet {
        void onUserLoaded(Set<User> userSet);
        void onUserFail(Exception e);
    }
}


