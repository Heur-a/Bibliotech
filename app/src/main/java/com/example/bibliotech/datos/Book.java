package com.example.bibliotech.datos;

import android.net.Uri;

import java.io.Serializable;

public class Book {

    //book
    private String ISBN;
    private String author;
    private String name;
    private String pagenumber;
    private String sinopsis;
    private String genre;
    private String editorial;

    private boolean disponible;



    private boolean reserved;
    private String section;

    private Uri ImageUri;


    public Book(String ISBN, String author, String editorial, String name, String pageNumber, String section, String sinopsis) {
        this.ISBN = ISBN;
        this.author = author;
        this.pagenumber = pageNumber;
        this.disponible = true;
        this.editorial = editorial;
        this.name = name;
        this.reserved = true;
        this.section = section;
        this.sinopsis = sinopsis;
    }

    public Book() {

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
    public String getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(String pagenumber) {
        this.pagenumber = pagenumber;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Uri getImageUri() {
        return ImageUri;
    }

    public void setImageUri(Uri imageUri) {
        ImageUri = imageUri;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + ISBN + '\'' +
                ", author='" + author + '\'' +
                ", pageNumber='" + pagenumber + '\'' +
                ", disponible=" + disponible +
                ", editorial='" + editorial + '\'' +
                ", name='" + name + '\'' +
                ", reserved=" + reserved +
                ", section='" + section + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                '}';
    }
}


