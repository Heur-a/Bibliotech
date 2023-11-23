package com.example.bibliotech;

public interface ObjectAsync {

    void add(Book Object);
    void delete(String id);
    Book get(String id);

    interface Getter {
        default void onResult(Book Object) {

        }
    }




}
