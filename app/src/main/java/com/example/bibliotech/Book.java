package com.example.bibliotech;

import android.media.Image;

public class Book {

    private String ISBN;
    private String author;
    private boolean disponible;
    private String editorial;
    private String name;
    private Image BookPhoto;
    private boolean reserved;
    private String section;
    private String sinopsis;

    public Book(String ISBN, String author, String editorial, String name, Image bookPhoto, String section, String sinopsis) {
        this.ISBN = ISBN;
        this.author = author;
        this.disponible = true;
        this.editorial = editorial;
        this.name = name;
        BookPhoto = bookPhoto;
        this.reserved = true;
        this.section = section;
        this.sinopsis = sinopsis;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getBookPhoto() {
        return BookPhoto;
    }

    public void setBookPhoto(Image bookPhoto) {
        BookPhoto = bookPhoto;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + ISBN + '\'' +
                ", author='" + author + '\'' +
                ", disponible=" + disponible +
                ", editorial='" + editorial + '\'' +
                ", name='" + name + '\'' +
                ", BookPhoto=" + BookPhoto +
                ", reserved=" + reserved +
                ", section='" + section + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                '}';
    }
}


