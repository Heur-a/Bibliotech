package com.example.bibliotech;

public interface BookAsync {

    void add(Book Object);

    void delete(String id);
    Book get(String id);

    interface Getter {
        default void onResult(Book Object) {

        }
    }




}
