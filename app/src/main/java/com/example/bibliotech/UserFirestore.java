package com.example.bibliotech;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class UserFirestore {
    private CollectionReference users;


    public UserFirestore () {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        users = db.collection("users");
    }

    public void add(User Object) {
        users.document(Object.id).set(Object);
    }



    public void delete(String id) {
        users.document(id).delete();
    }

    public Book get(String id) {
        try {
            Task<DocumentSnapshot> task = users.document(id).get();
            Tasks.await(task); // Espera fins que la tasca estigui completada

            if (task.isSuccessful()) {
                return task.getResult().toObject(Book.class);
            } else {
                // Potser voldràs gestionar l'error d'alguna manera aquí
                Log.d("FireBase GET", "Error user null");
                return null;
            }
        } catch (Exception e) {
            // Gestionar excepcions, com per exemple TimeoutException si la tasca pren massa temps
            Log.d("FireBase GET", "Error unkown");
            return null;
        }
    }

    public List<User> getBooksSynchronously() {
        final List<User> UserList = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);

        users.get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            UserList.add(user);
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }

                    latch.countDown(); // Indiquem que la tasca s'ha completat
                });

        try {
            latch.await(); // Esperem fins que la tasca s'hagi completat
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return UserList;
    }


}

