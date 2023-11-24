package com.example.bibliotech;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public User get(String id) {
        try {
            Task<DocumentSnapshot> task = users.document(id).get();
            Tasks.await(task); // Espera fins que la tasca estigui completada

            if (task.isSuccessful()) {
                return task.getResult().toObject(User.class);
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


}

