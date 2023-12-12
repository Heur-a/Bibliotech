package com.example.bibliotech;

import java.util.List;

// Clase para representar una lectura WiFi individual.
public class LecturaWiFi {
    private String mac; // Dirección MAC de la red WiFi
    private int nivelSenal; // Nivel de señal de la red WiFi

    // Constructor
    public LecturaWiFi(String mac, int nivelSenal) {
        this.mac = mac;
        this.nivelSenal = nivelSenal;
    }

    // Getters y setters
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getNivelSenal() {
        return nivelSenal;
    }

    public void setNivelSenal(int nivelSenal) {
        this.nivelSenal = nivelSenal;
    }
    @Override
    public String toString() {
        return "LecturaWiFi{" +
                "mac='" + mac + '\'' +
                ", nivelSenal=" + nivelSenal +
                '}';
    }
}
