package com.example.bibliotech.maplogic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculadorDistancia {

    public static double calcularDistanciaCuadratica(List<LecturaWiFi> lista1, List<LecturaWiFi> lista2) {
        // Crear mapas para acceder rápidamente a las lecturas por MAC
        Map<String, Integer> mapa1 = new HashMap<>();
        Map<String, Integer> mapa2 = new HashMap<>();

        for (LecturaWiFi lectura : lista1) {
            mapa1.put(lectura.getMac(), lectura.getNivelSenal());
        }

        for (LecturaWiFi lectura : lista2) {
            mapa2.put(lectura.getMac(), lectura.getNivelSenal());
        }

        double sumaCuadrados = 0.0;

        // Calcular la suma de los cuadrados de las diferencias
        for (String mac : mapa1.keySet()) {
            int nivel1 = mapa1.get(mac);
            int nivel2 = mapa2.getOrDefault(mac, 0); // Nivel de señal 0 si no está presente
            sumaCuadrados += Math.pow(nivel1 - nivel2, 2);
        }

        // Considerar las MACs que solo están en la lista 2
        for (String mac : mapa2.keySet()) {
            if (!mapa1.containsKey(mac)) {
                int nivel2 = mapa2.get(mac);
                sumaCuadrados += Math.pow(nivel2, 2); // Nivel en lista1 es 0
            }
        }

        return sumaCuadrados;
    }
}
