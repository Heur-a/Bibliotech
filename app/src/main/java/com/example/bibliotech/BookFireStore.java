package com.example.bibliotech;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicReference;

public class BookFireStore implements BookAsync{

    private CollectionReference books;


    public BookFireStore () {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        books = db.collection("book");
    }
    @Override
    public void add(Book Book) {
        books.document(Book.getISBN()).set(Book);
    }


    @Override
    public void delete(String ISBN) {
        books.document(ISBN).delete();
    }

    @Override
    public Book get(String ISBN) {
        try {
            Task<DocumentSnapshot> task = books.document(ISBN).get();
            Tasks.await(task); // Espera fins que la tasca estigui completada

            if (task.isSuccessful()) {
                return task.getResult().toObject(Book.class);
            } else {
                // Potser voldràs gestionar l'error d'alguna manera aquí
                Log.d("FireBase GET", "Error usuari null");
                return null;
            }
        } catch (Exception e) {
            // Gestionar excepcions, com per exemple TimeoutException si la tasca pren massa temps
            Log.d("FireBase GET", "Error unkown");
            return null;
        }
    }

}
