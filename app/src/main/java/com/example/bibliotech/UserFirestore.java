package com.example.bibliotech;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class UserFirestore {
    private CollectionReference users;


    public UserFirestore () {
        // Inicializes firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        users = db.collection("users");
    }

    public void add(User Object) {
        // Adds user checking if exists data without overwriting all the data
        users.document(Object.id).set(Object, SetOptions.merge());
    }



    public void delete(String id) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!CAUTION: CAUTION. WE SHOLD NEVER EVER DELETE USERS!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        users.document(id).delete();
    }

    public User get(String id) {
        try {
            //DOWNLOADS DOCUMENT
            Task<DocumentSnapshot> task = users.document(id).get();
            Tasks.await(task); // waits...

            if (task.isSuccessful()) {
                //if works returns a user
                return task.getResult().toObject(User.class);
            } else {
                //if not user doesn't exist
                Log.d("FireBase GET", "Error user null");
                return null;
            }
        } catch (Exception e) {
            // internal error
            Log.d("FireBase GET", "Error: " + e);
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

