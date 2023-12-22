package com.example.bibliotech.datos.firestore;

import android.util.Log;

import com.example.bibliotech.datos.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class RoomFireStore {

    private CollectionReference rooms;

    public interface RoomCallback {
        void onRoomLoaded(Room room);

        void onRoomError(Exception e);
    }

    public interface SetRoomCallback {
        void onRoomsLoaded(Set<Room> roomList);

        void onRoomsError(Exception e);
    }

    public RoomFireStore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        rooms = db.collection("rooms");
    }

    public void add(Room room) {
        rooms.document(room.getNombreSala()).set(room);
    }

    public void delete(String id) {
        rooms.document(id).delete();
    }

    public void getRoom(String id, RoomCallback callback) {
        rooms.document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Room room = documentSnapshot.toObject(Room.class);
                    callback.onRoomLoaded(room);
                })
                .addOnFailureListener(e -> {
                    Log.d("FireBase GET", "Error fetching room: " + e.getMessage());
                    callback.onRoomError(e);
                });
    }

    public void getRoomsSet(SetRoomCallback callback) {
        final Set<Room> roomList = new HashSet<>();
        final CountDownLatch latch = new CountDownLatch(1);

        rooms.get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Room room = document.toObject(Room.class);
                            roomList.add(room);
                        }
                        callback.onRoomsLoaded(roomList);
                    } else {
                        Log.w("FireBase GET", "Error getting documents.", task.getException());
                        callback.onRoomsError(task.getException());
                    }
                    latch.countDown(); // Indicamos que la tarea se ha completado
                });
    }
}
