package com.example.bibliotech;

import java.util.List;

// Clase para representar una posición con una lista de lecturas WiFi.
public class LecturaPos {
    private double x; // Coordenada X
    private double y; // Coordenada Y
    private List<LecturaWiFi> lecturasWifi; // Lista de lecturas WiFi en esta posición

    // Constructor
    public LecturaPos(double x, double y, List<LecturaWiFi> lecturasWifi) {
        this.x = x;
        this.y = y;
        this.lecturasWifi = lecturasWifi;
    }

    // Getters y setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public List<LecturaWiFi> getLecturasWifi() {
        return lecturasWifi;
    }

    public void setLecturasWifi(List<LecturaWiFi> lecturasWifi) {
        this.lecturasWifi = lecturasWifi;
    }
}


