package com.example.bibliotech.datos;

import java.util.List;

public class Room {
    private String nombreSala;
    private String numberpeople;
    private List<String> accesories; // Cambiado de String[] a List<String>
    private String disponibility = "No";

    // Constructor sin argumentos
    public Room() {}

    public Room(String nombreSala, String numberpeople, List<String> accesories, String disponibility) {
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

    public List<String> getAccesories() {
        return accesories;
    }

    public void setAccesories(List<String> accesories) {
        this.accesories = accesories;
    }

    @Override
    public String toString() {
        StringBuilder accessoriesStr = new StringBuilder("[");
        if (accesories != null && !accesories.isEmpty()) {
            for (String accessory : accesories) {
                accessoriesStr.append(accessory).append(", ");
            }
            // Eliminar la coma y el espacio extra al final
            accessoriesStr.setLength(accessoriesStr.length() - 2);
        }
        accessoriesStr.append("]");

        return "Room{" +
                "nombreSala='" + nombreSala + '\'' +
                ", numberpeople='" + numberpeople + '\'' +
                ", accesories=" + accessoriesStr +
                ", disponibility='" + disponibility + '\'' +
                '}';
    }
}
