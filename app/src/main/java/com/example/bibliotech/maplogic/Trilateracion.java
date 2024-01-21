package com.example.bibliotech.maplogic;

import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;

public class Trilateracion {

    public static double[] trilaterarPosicion(double[][] coordenadas, double[] distancias) {
        int numberOfPoints = coordenadas.length;

        if (numberOfPoints < 3 || distancias.length < 3 || numberOfPoints != distancias.length) {
            throw new IllegalArgumentException("Se requieren al menos tres puntos de referencia con distancias correspondientes.");
        }

        double[] posicion = new double[2];

        DMatrixRMaj coefficientsMatrix = new DMatrixRMaj(numberOfPoints - 1, 2);
        DMatrixRMaj constantsVector = new DMatrixRMaj(numberOfPoints - 1, 1);

        for (int i = 0; i < numberOfPoints - 1; i++) {
            coefficientsMatrix.set(i, 0, 2 * (coordenadas[i][0] - coordenadas[numberOfPoints - 1][0]));
            coefficientsMatrix.set(i, 1, 2 * (coordenadas[i][1] - coordenadas[numberOfPoints - 1][1]));
            constantsVector.set(i, 0, Math.pow(coordenadas[i][0], 2) - Math.pow(coordenadas[numberOfPoints - 1][0], 2) +
                    Math.pow(coordenadas[i][1], 2) - Math.pow(coordenadas[numberOfPoints - 1][1], 2) +
                    Math.pow(distancias[numberOfPoints - 1], 2) - Math.pow(distancias[i], 2));
        }

        // Resuelve el sistema de ecuaciones lineales utilizando EJML
        SimpleMatrix coefficientsSimpleMatrix = SimpleMatrix.wrap(coefficientsMatrix);
        SimpleMatrix constantsSimpleMatrix = SimpleMatrix.wrap(constantsVector);
        SimpleMatrix result = coefficientsSimpleMatrix.solve(constantsSimpleMatrix);

        // Establece las coordenadas X e Y de la posición
        posicion[0] = result.get(0, 0);
        posicion[1] = result.get(1, 0);

        return posicion;
    }
}
