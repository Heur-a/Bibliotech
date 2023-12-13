package com.example.bibliotech;

import android.net.Uri;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class User implements Serializable {

    public String email;
    public String password;
    public String name;
    public String surnames;
    public String username;
    public String id;
    public String categoria;
    @PropertyName("photoUri")
    private String photoUriString; // Nuevo campo para almacenar la URI como String

    public User(String email, String password, String name, String surnames, String id, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surnames = surnames;
        this.id = id;
        this.role = role;
    }

    public String role;

    public String completeName;
    public Uri photoUri;

    public User() {
        // Constructor predeterminado necesario para Firestore
    }


    public User(String email, String password, String name, String surnames) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surnames = surnames;
        setCompleteName();
    }

    public User(String email, String password, String name, String surnames, Uri photoUri){
      new User(email,password,name,surnames);
      this.photoUri = photoUri;
    }

    public User(String email, String name, Uri photoUri, String id){
        this.email = email;
        this.name =name;
        this.photoUri = photoUri;
        this.id = id;
    }

    public User(String email, String password, String name, String surnames, String username, String id, String completeName, String role, Uri photoUri) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surnames = surnames;
        this.username = username;
        this.id = id;
        this.completeName = completeName;
        this.role = role;
        this.photoUri = photoUri;
    }


    // Constructor modificado para realizar la conversión
    public User(String email, String password, String name, String surnames, String id, String role, String photoUri) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surnames = surnames;
        this.id = id;
        this.role = role;
        this.photoUriString = photoUri;
        setCompleteName();
    }

    // Método getter para obtener la URI
    @PropertyName("photoUri")
    public Uri getPhotoUri() {
        return (photoUriString != null) ? Uri.parse(photoUriString) : null;
    }

    // Método setter para establecer la URI
    @PropertyName("photoUri")
    public void setPhotoUri(Uri photoUri) {
        this.photoUriString = (photoUri != null) ? photoUri.toString() : null;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }



    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surnames='" + surnames + '\'' +
                ", username='" + username + '\'' +
                ", id='" + id + '\'' +
                ", completeName='" + completeName + '\'' +
                ", role='" + role + '\'' +
                ", photoUri=" + photoUri +
                '}';
    }

    private void setCompleteName() {
        completeName = name + surnames;
    }


}
