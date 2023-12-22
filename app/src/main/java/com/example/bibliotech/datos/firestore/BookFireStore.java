package com.example.bibliotech.datos.firestore;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.bibliotech.datos.Book;
import com.example.bibliotech.datos.reservaLibro;
import com.example.bibliotech.datos.reservaSala;
import com.example.bibliotech.presentacion.perfilActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class BookFireStore{

    private CollectionReference books;
    public interface SetbookCallback {
        void onBookLoaded(Set<Book> books);

        void onBookError(Exception e);
    }
    public interface bookCallback {
        void onBookLoaded(Book book);

        void onBookError(Exception e);

        void onBookDetailsLoaded(Book book);
    }

    public BookFireStore () {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        books = db.collection("book");
    }

    public void add(Book Object) {
        books.document(Object.getISBN()).set(Object);
    }
    public void getBookDetails(String id, bookCallback callback) {
        books.document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Book book = documentSnapshot.toObject(Book.class);
                    callback.onBookLoaded(book);
                    // Llamamos al nuevo método para manejar la carga de detalles
                    callback.onBookDetailsLoaded(book);
                })
                .addOnFailureListener(e -> {
                    Log.d("FireBase GET", "Error fetching book details: " + e.getMessage());
                    callback.onBookError(e);
                });
    }



    public void delete(String id) {
        books.document(id).delete();
    }

    public void getBook(String id, bookCallback callback) {
        books.document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Book book = documentSnapshot.toObject(Book.class);
                    callback.onBookLoaded(book);
                })
                .addOnFailureListener(e -> {
                    Log.d("FireBase GET", "Error fetching book: " + e.getMessage());
                    callback.onBookError(e);
                });

    }

    public  void  getBooksSet(BookFireStoreSetCallback callback) {
         Set<Book> bookList = new HashSet<>();;

        books.get()
                .addOnSuccessListener(task -> {
            for (QueryDocumentSnapshot document : task) {
                Book book = document.toObject(Book.class);
                FirebaseStorage.getInstance()
                        .getReference().child("images/portada/" + book.getISBN() + ".jpg").getDownloadUrl().addOnSuccessListener(task2 -> {
                            book.setImageUri(task2);
                        }).addOnFailureListener(e -> {
                            Log.d("BookFireStoreImageDownload", e.getMessage());
                        });
                bookList.add(book);
            }
            callback.onBookSetLoaded(bookList);
        })
                .addOnFailureListener(e -> {
                    callback.onBookSetError("Error: " + e.getMessage());
                });
    }


    public interface BookFireStoreSetCallback {
        void onBookSetLoaded(Set<Book> books);
        void onBookSetError(String errorMessage);
    }



}
