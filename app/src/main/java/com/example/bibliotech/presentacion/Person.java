package com.example.bibliotech.presentacion;

public class Person {
    private String name;
    private String apellidos;

    private String correo;

    public Person(String name, String apellidos, String correo) {
        this.name = name;
        this.apellidos = apellidos;
        this.correo = correo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }
}
