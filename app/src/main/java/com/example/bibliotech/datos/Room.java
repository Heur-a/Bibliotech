package com.example.bibliotech.datos;

public class Room {
    private String nombreSala;

    private String numberpeople;
    private String[] accesories;
    private String disponibility = "No";

    public Room(String nombreSala, String numberpeople, String[] accesories, String disponibility) {
        this.nombreSala = nombreSala;
        this.numberpeople = numberpeople;
        this.accesories = accesories;
        this.disponibility = disponibility;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public String getDisponibility() {
        return disponibility;
    }

    public void setDisponibility(String disponibility) {
        this.disponibility = disponibility;
    }

    public String getNumberpeople() {

        return numberpeople;
    }

    public void setNumberpeople(String numberpeople) {

        this.numberpeople = numberpeople;
    }

    public String[] getAccesories() {
        return accesories;
    }

    public void setAccesories(String[] accesories) {
        this.accesories = accesories;
    }

    @Override
    public String toString() {
        return "Room{" +
                "numberpeople='" + numberpeople + '\'' +
                "accesories='" + accesories + '\'' +
                "disponibility='" + disponibility + '\'' +
                '}';
    }

}
