package com.example.bibliotech;

public interface BookAsync {

    void add(Book Book);
    void delete(String ISBN);
    Book get(String ISBN);

    interface Getter {
        default void onResult(Book Book) {

        }
    }




}
