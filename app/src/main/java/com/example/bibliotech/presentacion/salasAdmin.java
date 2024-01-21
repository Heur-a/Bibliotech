package com.example.bibliotech.presentacion;

public class salasAdmin {
private String NumeroPersonas;
private String NumeroSala;
private String accesorios;

    public salasAdmin(String numeroPersonas, String numeroSala, String accesorios) {
        NumeroPersonas = numeroPersonas;
        NumeroSala = numeroSala;
        this.accesorios = accesorios;
    }

    public String getNumeroPersonas() {
        return NumeroPersonas;
    }

    public void setNumeroPersonas(String numeroPersonas) {
        NumeroPersonas = numeroPersonas;
    }

    public String getNumeroSala() {
        return NumeroSala;
    }

    public void setNumeroSala(String numeroSala) {
        NumeroSala = numeroSala;
    }

    public String getAccesorios() {
        return accesorios;
    }

    public void setAccesorios(String accesorios) {
        this.accesorios = accesorios;
    }

    @Override
    public String toString() {
        return "salasAdmin{" +
                "NumeroPersonas='" + NumeroPersonas + '\'' +
                ", NumeroSala='" + NumeroSala + '\'' +
                ", accesorios='" + accesorios + '\'' +
                '}';
    }
}
