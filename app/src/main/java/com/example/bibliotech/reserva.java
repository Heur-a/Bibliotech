package com.example.bibliotech;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class reserva {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Date FechaIni;
    private Date FechaFin;
    private String userId;

    public reserva(Date fechaIni, Date fechaFin, String userId) {
        FechaIni = fechaIni;
        FechaFin = fechaFin;
        this.userId = userId;
    }


   final public void addToBook(Book book) {
        DocumentReference doc = db.collection("books").document(book.getISBN());
        doc.update("reserva", this);
    }

   final public void addToRoom (Room room) {
        DocumentReference doc = db.collection("books").document(room.getNombreSala());
        doc.update("reserva",this);
    }

    @Override
    public String toString() {
        return "reserva{" +
                "FechaIni=" + FechaIni +
                ", FechaFin=" + FechaFin +
                ", userId='" + userId + '\'' +
                '}';
    }

    public Date getFechaIni() {
        return FechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        FechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        FechaFin = fechaFin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
