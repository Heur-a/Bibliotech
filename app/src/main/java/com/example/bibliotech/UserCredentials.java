package com.example.bibliotech;

public class UserCredentials{

    public String email,password,name,surnames;

    public String completeName;

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

    private void setCompleteName() {
        completeName = name + surnames;
    }


}
