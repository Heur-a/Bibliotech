package com.example.bibliotech.maplogic;

public class LecturaWiFi {
    private String mac; // Dirección MAC de la red WiFi
    private int nivelSenal; // Nivel de señal de la red WiFi
    private double posX; // Coordenada X
    private double posY; // Coordenada Y

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

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    @Override
    public String toString() {
        return "LecturaWiFi{" +
                "mac='" + mac + '\'' +
                ", nivelSenal=" + Math.abs(nivelSenal) +
                ", posX=" + posX +
                ", posY=" + posY +
                '}';
    }
}
