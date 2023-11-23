package com.example.bibliotech;

import android.net.Uri;

public class User {

    public String email,password,name,surnames,username,id;

    public String completeName;
    public Uri photoUri;


    public User() {
        email = "";
        password = "";
        name = "";
        surnames = "";
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

    private void setCompleteName() {
        completeName = name + surnames;
    }


}
