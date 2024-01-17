package com.example.bibliotech.maplogic;

import java.util.ArrayList;
import java.util.List;

public class TriangulacionPosicion {

    public static LecturaPos triangularPosicion(List<LecturaPos> lecturasPos, List<LecturaWiFi> lecturasPersona) {
        // Crear una lista para almacenar la distancia cuadrática entre las lecturas de la persona y cada posición conocida
        List<Double> distancias = new ArrayList<>();

        // Calcular la distancia cuadrática para cada posición conocida
        for (LecturaPos posicionConocida : lecturasPos) {
            double distancia = CalculadorDistancia.calcularDistanciaCuadratica(posicionConocida.getLecturasWifi(), lecturasPersona);
            distancias.add(distancia);
        }

        // Encontrar la posición con la distancia mínima (triangulación)
        int indicePosicionMinima = encontrarIndiceDistanciaMinima(distancias);
        return lecturasPos.get(indicePosicionMinima);
    }

    private static int encontrarIndiceDistanciaMinima(List<Double> distancias) {
        double distanciaMinima = Double.MAX_VALUE;
        int indiceMinimo = -1;

        for (int i = 0; i < distancias.size(); i++) {
            double distanciaActual = distancias.get(i);
            if (distanciaActual < distanciaMinima) {
                distanciaMinima = distanciaActual;
                indiceMinimo = i;
            }
        }

        return indiceMinimo;
    }
}
