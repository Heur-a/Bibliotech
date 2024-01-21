package com.example.bibliotech.datos;

import java.util.List;

public class Room {
    private String nombreSala;
    private String numberPeople;
    private List<String> accesories; // Cambiado de String[] a List<String>
    private String disponibility = "No";
    private int floor;

    // Constructor sin argumentos
    public Room() {}

    public Room(String nombreSala, String numberPeople, List<String> accesories, String disponibility, int floor) {
        this.nombreSala = nombreSala;
        this.numberPeople = numberPeople;
        this.accesories = accesories;
        this.disponibility = disponibility;
        this.floor = floor;
    }

    public Room(String numeroPersonas, String numeroSala, List<String> listaAccesorios) {
        this.numberPeople = numeroPersonas;
        this.nombreSala = numeroSala;
        this.accesories = listaAccesorios;
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

    public String getNumberPeople() {
        return numberPeople;
    }

    public void setNumberPeople(String numberPeople) {
        this.numberPeople = numberPeople;
    }

    public List<String> getAccesories() {
        return accesories;
    }

    public void setAccesories(List<String> accesories) {
        this.accesories = accesories;
    }

    public int getFloor() {
        return floor;
    }

    @Override
    public String toString() {
        return "Room{" +
                "nombreSala='" + nombreSala + '\'' +
                ", numberpeople='" + numberPeople + '\'' +
                ", accesories=" + accesories +
                ", disponibility='" + disponibility + '\'' +
                ", floor=" + floor +
                '}';
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }


}
