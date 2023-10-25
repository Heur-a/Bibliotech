package com.example.bibliotech;

import android.net.Uri;

public class UserCredentials{

    public String email,password,name,surnames,username,id;

    public String completeName;
    public Uri photoUri;


    public UserCredentials() {
        email = "";
        password = "";
        name = "";
        surnames = "";
        setCompleteName();
    }

    public UserCredentials(String email, String password, String name, String surnames) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surnames = surnames;
        setCompleteName();
    }

    public UserCredentials(String email, String password, String name, String surnames, Uri photoUri){
      new UserCredentials(email,password,name,surnames);
      this.photoUri = photoUri;
    }

    public UserCredentials(String email,String username,Uri photoUri,String id){
        this.email = email;
        this.username =username;
        this.photoUri = photoUri;
        this.id = id;
    }

    private void setCompleteName() {
        completeName = name + surnames;
    }


}
