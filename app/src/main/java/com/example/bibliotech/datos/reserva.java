package com.example.bibliotech.datos;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class reserva {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Date fechaIni;
    private Date fechaFin;
    private String userId;

    public reserva(Date fechaIni, Date fechaFin, String userId) {
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.userId = userId;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void addToBook(Book book) {
        DocumentReference doc = db.collection("book").document(book.getISBN()).collection("reserva").document();
        doc.set(this);
    }

    public void addToRoom(Room room) {
        DocumentReference doc = db.collection("rooms").document(room.getNombreSala()).collection("reserva").document();
        doc.set(this);
    }

    static public List<reserva> getReservasFromBook(Book book) {
        List<reserva> reservaList = new ArrayList<>();
         FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reservaCollectionRef = db.collection("books").document(book.getISBN()).collection("reserva");

        reservaCollectionRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            reserva reserva = document.toObject(com.example.bibliotech.datos.reserva.class);
                            reservaList.add(reserva);
                        }
                    } else {
                        // Handle error
                    }
                });

        return reservaList;
    }

    static public List<reserva> getReservasFromRoom(Room room) {
        List<reserva> reservaList = new ArrayList<>();
         FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference reservaCollectionRef = db.collection("rooms").document(room.getNombreSala()).collection("reserva");

        reservaCollectionRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            reserva reserva = document.toObject(com.example.bibliotech.datos.reserva.class);
                            reservaList.add(reserva);
                        }
                    } else {
                        // Handle error
                    }
                });

        return reservaList;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "fechaIni=" + fechaIni +
                ", fechaFin=" + fechaFin +
                ", userId='" + userId + '\'' +
                '}';
    }
}
