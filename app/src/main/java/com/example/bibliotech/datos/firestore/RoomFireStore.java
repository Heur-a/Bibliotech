package com.example.bibliotech.datos.firestore;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.bibliotech.datos.Room;
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

public class RoomFireStore {

    private CollectionReference rooms;


    public RoomFireStore () {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        rooms = db.collection("users");
    }

    public void add(Room Object) {
        rooms.document(Object.getNombreSala()).set(Object);
    }



    public void delete(String id) {
        rooms.document(id).delete();
    }

    public Room get(String id) {
        try {
            Task<DocumentSnapshot> task = rooms.document(id).get();
            Tasks.await(task); // Espera fins que la tasca estigui completada

            if (task.isSuccessful()) {
                return task.getResult().toObject(Room.class);
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

    public List<Room> getBooksSynchronously() {
        final List<Room> UserList = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);

        rooms.get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Room room = document.toObject(Room.class);
                            UserList.add(room);
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
