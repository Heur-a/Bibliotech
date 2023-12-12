package com.example.bibliotech;

import android.net.Uri;

public class User {

    public String email;
    public String password;
    public String name;
    public String surnames;
    public String username;
    public String id;
    public String categoria;

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
        email = "";
        password = "";
        name = "";
        surnames = "";
        role ="";
        setCompleteName();
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

    public User(String email, String username, Uri photoUri, String id){
        this.email = email;
        this.username =username;
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
