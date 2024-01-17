package com.example.bibliotech.datos.firestore;

import android.util.Log;

import com.example.bibliotech.datos.Room;
import com.example.bibliotech.datos.reservaSala;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoomFireStore {

    private final FirebaseFirestore db;
    private final CollectionReference rooms;

    public RoomFireStore() {
        db = FirebaseFirestore.getInstance();
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

        rooms.get()
                .addOnCompleteListener(task -> {
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
                });
    }

    public void addReserva(reservaSala reserva,String salaID){
        rooms.document(salaID).collection("reservaSala").document().set(reserva);
    }

    public void getReservaRooms(RoomReserveMap callback) {
        final Map<Room, List<reservaSala>> roomListMap = new HashMap<>();
        Map<Room, List<reservaSala>> RoomReserva = new HashMap<>();
        getRoomsSet(new RoomFireStore.SetRoomCallback() {
            @Override
            public void onRoomsLoaded(Set<Room> roomList) {
                final boolean[] SUCCES_FLAG = {false};
                final Exception[] exceptions = new Exception[0];
                for (Room room : roomList) {
                    List<reservaSala> reservaSalaList = new ArrayList<>();
                    rooms.document(room.getNombreSala()).collection("reservaSala").get()
                            .addOnSuccessListener(task -> {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task) {
                                    reservaSala reservaSala = queryDocumentSnapshot.toObject(com.example.bibliotech.datos.reservaSala.class);
                                    reservaSalaList.add(reservaSala);
                                }


                            }).addOnFailureListener(task -> {
                                SUCCES_FLAG[0] = false;
                                exceptions[0] = task;
                            });


                    if (SUCCES_FLAG[0] == false) {
                    callback.onRoomsReserveError(exceptions[0]);
                    break;
                    }
                    roomListMap.put(room, reservaSalaList);
                }

                callback.onRoomReserveMapLoaded(roomListMap);
            }

            @Override
            public void onRoomsError(Exception e) {
                callback.onRoomsReserveError(e);
            }
        });
    }

    public interface RoomCallback {
        void onRoomLoaded(Room room);

        void onRoomError(Exception e);
    }

    public interface SetRoomCallback {
        void onRoomsLoaded(Set<Room> roomList);

        void onRoomsError(Exception e);
    }


    public interface RoomReserveMap {
        void onRoomReserveMapLoaded(Map<Room, List<reservaSala>> roomsRsereva);

        void onRoomsReserveError(Exception e);
    }


}
