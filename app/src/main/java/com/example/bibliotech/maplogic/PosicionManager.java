package com.example.bibliotech.maplogic;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PosicionManager {

    private final FirebaseFirestore db;
    private final Map<String, List<LecturaWiFi>> datosCache; // Almacena los datos recuperados
    private final Context context;
    private double coordenadaAnteriorX = Double.NaN;
    private double coordenadaAnteriorY = Double.NaN;

    public PosicionManager(Context context) {
        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();
        datosCache = new HashMap<>();
        this.context = context; // Inicializa el contexto
    }
    public void obtenerDatosParaTodasCoordenadas() {
        // Definir las coordenadas
        double[][] coordenadas = {
                {1007.47266, 187.6864},
                {1007.47266, 67.6864},
                {1017.47266, 317.6864},
                {1017.47266, 507.6864},
                {1017.47266, 617.6864},
                {1057.4727, 477.6864},
                {1057.4727, 547.6864},
                {1057.4727, 617.6864},
                {1067.4727, 317.6864},
                {1077.4727, 187.6864},
                {1077.4727, 67.6864},
                {1097.4727, 487.6864},
                {1097.4727, 547.6864},
                {1097.4727, 617.6864},
                {1127.4727, 187.6864},
                {1127.4727, 317.6864},
                {1127.4727, 477.6864},
                {1127.4727, 617.6864},
                {1137.4727, 67.6864},
                {1167.4727, 477.6864},
                {1167.4727, 547.6864},
                {1167.4727, 617.6864},
                {1187.4727, 187.6864},
                {1187.4727, 317.6864},
                {1187.4727, 67.6864},
                {1207.4727, 497.6864},
                {1207.4727, 557.6864},
                {1207.4727, 617.6864},
                {1247.4727, 187.6864},
                {1247.4727, 317.6864},
                {1247.4727, 507.6864},
                {1247.4727, 617.6864},
                {1257.4727, 67.6864},
                {1277.4727, 507.6864},
                {1277.4727, 617.6864},
                {1317.4727, 507.6864},
                {1317.4727, 607.6864},
                {1327.4727, 427.6864},
                {1327.4727, 67.6864},
                {1347.4727, 517.6864},
                {1347.4727, 627.6864},
                {1407.4727, 437.6864},
                {1417.4727, 537.6864},
                {1467.4727, 537.6864},
                {1696.6014, 442.69165},
                {1697.4727, 537.6864},
                {533.0, 580.0},
                {623.0, 580.0},
                {663.0, 580.0},
                {717.47266, 447.6864},
                {717.47266, 607.6864},
                {733.0, 780.0},
                {797.47266, 537.6864},
                {847.47266, 537.6864},
                {877.47266, 537.6864},
                {897.47266, 427.6864},
                {917.47266, 497.6864},
                {917.47266, 617.6864},
                {927.47266, 67.6864},
                {947.47266, 397.6864},
                {947.47266, 507.6864},
                {947.47266, 617.6864},
                {987.47266, 507.6864},
                {987.47266, 627.6864},
                {140.0, 1133.0},
                {118.01938, 1141.4086},
                {790.3495, 968.8672}
        };

        // Obtener datos para cada coordenada
        for (double[] coord : coordenadas) {
            double latitud = coord[0];
            double longitud = coord[1];
            String coordenadasStr = obtenerCoordenadasStr(latitud, longitud);

            if (!datosCache.containsKey(coordenadasStr)) {
                obtenerDatosFirestore(latitud, longitud);
            }
        }
    }
    public void actualizarLecturasWifiEnTiempoReal() {
        List<LecturaWiFi> lecturasEnTiempoReal = WifiScanner.obtenerLecturasWifi(context,  -70);


        if (lecturasEnTiempoReal != null && !lecturasEnTiempoReal.isEmpty()) {
            triangularPosicionEnTiempoReal(lecturasEnTiempoReal);
        } else {
            Log.e("PosicionManager", "No hay lecturas Wi-Fi en tiempo real para triangular la posición.");
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> actualizarLecturasWifiEnTiempoReal(), 5000);
    }

    private void obtenerDatosFirestore(double latitud, double longitud) {
        String coordenadasStr = obtenerCoordenadasStr(latitud, longitud);
        CollectionReference collectionRef = db.collection("/mapp1/" + coordenadasStr + "/lecturaWifi");

        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<LecturaWiFi> lecturas = new ArrayList<>();

                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    LecturaWiFi lectura = document.toObject(LecturaWiFi.class);
                    lecturas.add(lectura);
                }

                datosCache.put(coordenadasStr, lecturas);
                triangularPosicion(coordenadasStr);
            } else {
                Log.e("PosicionManager", "Error al obtener la colección", task.getException());
            }
        });
    }

    private String obtenerCoordenadasStr(double latitud, double longitud) {
        return String.format("(%s,%s)", latitud, longitud);
    }

    public void triangularPosicionEnTiempoReal(List<LecturaWiFi> lecturasEnTiempoReal) {
        if (!lecturasEnTiempoReal.isEmpty()) {
            List<String> macsEncontradas = new ArrayList<>();
            List<String> coordenadasCoincidentes = new ArrayList<>();

            for (LecturaWiFi lecturaEnTiempoReal : lecturasEnTiempoReal) {
                String macEnTiempoReal = lecturaEnTiempoReal.getMac();

                datosCache.forEach((coordenadas, lecturasAlmacenadas) -> {
                    if (contieneMac(lecturasAlmacenadas, macEnTiempoReal)) {
                        macsEncontradas.add(macEnTiempoReal);
                        coordenadasCoincidentes.add(coordenadas);
                    }
                });
            }

            int cantidadCoincidencias = macsEncontradas.size();

            if (cantidadCoincidencias >= 3) {
                triangularPosicionBasadoEnMacs(macsEncontradas, coordenadasCoincidentes);
            } else {
                Log.e("PosicionManager", "No se encontraron suficientes coincidencias (" + cantidadCoincidencias + ") para realizar la triangulación en tiempo real.");
            }
        } else {
            Log.e("PosicionManager", "No hay lecturas Wi-Fi en tiempo real para triangular la posición.");
        }
    }




    public void triangularPosicionActual() {
        // Obtener lecturas WiFi en tiempo real
        List<LecturaWiFi> lecturasEnTiempoReal = WifiScanner.obtenerLecturasWifi(context,  -70);

        if (lecturasEnTiempoReal != null && !lecturasEnTiempoReal.isEmpty()) {
            // Filtrar las lecturas en tiempo real para coordinar con las almacenadas en la base de datos
            List<Integer> senalesEncontradas = new ArrayList<>();
            List<String> coordenadasCoincidentes = new ArrayList<>();

            for (LecturaWiFi lecturaEnTiempoReal : lecturasEnTiempoReal) {
                String macEnTiempoReal = lecturaEnTiempoReal.getMac();

                datosCache.forEach((coordenadas, lecturasAlmacenadas) -> {
                    if (contieneMac(lecturasAlmacenadas, macEnTiempoReal)) {
                        senalesEncontradas.add(lecturaEnTiempoReal.getNivelSenal());
                        coordenadasCoincidentes.add(coordenadas);
                    }
                });
            }

            if (senalesEncontradas.size() >= 3) {
                double sumaPonderadaX = 0.0;
                double sumaPonderadaY = 0.0;
                double sumaPonderaciones = 0.0;

                for (int i = 0; i < senalesEncontradas.size(); i++) {
                    double posX = obtenerPosXDesdeCoordenadas(coordenadasCoincidentes.get(i));
                    double posY = obtenerPosYDesdeCoordenadas(coordenadasCoincidentes.get(i));
                    int nivelSenal = senalesEncontradas.get(i);

                    double ponderacion = Math.pow(10.0, nivelSenal / -20.0);
                    sumaPonderadaX += posX * ponderacion;
                    sumaPonderadaY += posY * ponderacion;
                    sumaPonderaciones += ponderacion;
                }

                double posicionX = sumaPonderadaX / sumaPonderaciones;
                double posicionY = sumaPonderadaY / sumaPonderaciones;

                posicionX = Math.round(posicionX * 100.0) / 100.0;
                posicionY = Math.round(posicionY * 100.0) / 100.0;

                if (!Double.isNaN(posicionX) && !Double.isNaN(posicionY)) {
                    if (huboCambioUbicacion(posicionX, posicionY)) {
                        Log.d("PosicionManager", "Ubicación ponderada actual: (" + posicionX + "," + posicionY + ")");
                        mostrarToast("Ubicación ponderada actual: (" + posicionX + "," + posicionY + ")");
                    } else {
                        Log.d("PosicionManager", "Ubicación no ha cambiado.");
                    }
                } else {
                    Log.e("PosicionManager", "Error al calcular la ubicación ponderada actual.");
                }
            } else {
                Log.e("PosicionManager", "No se encontraron suficientes coincidencias para triangular la posición actual.");
            }
        } else {
            Log.e("PosicionManager", "No hay lecturas Wi-Fi en tiempo real para triangular la posición actual.");
        }
    }

    private void triangularPosicion(String coordenadasStr) {
        List<LecturaWiFi> lecturas = datosCache.get(coordenadasStr);

        if (lecturas != null && lecturas.size() >= 3) {
            lecturas.sort((l1, l2) -> Integer.compare(l2.getNivelSenal(), l1.getNivelSenal()));

            double sumaPonderadaX = 0.0;
            double sumaPonderadaY = 0.0;
            double sumaPonderaciones = 0.0;

            for (int i = 0; i < Math.min(3, lecturas.size()); i++) {
                LecturaWiFi lectura = lecturas.get(i);
                double posX = lectura.getPosX();
                double posY = lectura.getPosY();
                int nivelSenal = lectura.getNivelSenal();

                double ponderacion = Math.pow(10.0, nivelSenal / -20.0);
                sumaPonderadaX += posX * ponderacion;
                sumaPonderadaY += posY * ponderacion;
                sumaPonderaciones += ponderacion;
            }

            double posicionX = sumaPonderadaX / sumaPonderaciones;
            double posicionY = sumaPonderadaY / sumaPonderaciones;

            posicionX = Math.round(posicionX * 100.0) / 100.0;
            posicionY = Math.round(posicionY * 100.0) / 100.0;

            if (!Double.isNaN(posicionX) && !Double.isNaN(posicionY)) {
                if (huboCambioUbicacion(posicionX, posicionY)) {
                    Log.d("PosicionManager", "Ubicación aproximada para " + coordenadasStr + ": (" + posicionX + "," + posicionY + ")");
                    mostrarToast(coordenadasStr + ": (" + posicionX + "," + posicionY + ")");
                } else {
                    Log.d("PosicionManager", "Ubicación no ha cambiado para " + coordenadasStr);
                }
            } else {
                Log.e("PosicionManager", "Error al calcular la ubicación aproximada para " + coordenadasStr);
            }
        } else {
            // Llama a la trilateración si tienes suficientes lecturas
            if (lecturas != null && lecturas.size() >= 3) {
                trilaterarPosicion(coordenadasStr, lecturas);
            } else {
                Log.e("PosicionManager", "No hay suficientes lecturas para la triangulación.");
            }
        }
    }
    private void trilaterarPosicion(String coordenadasStr, List<LecturaWiFi> lecturas) {
        // Extraer distancias y coordenadas para la trilateración
        double[][] coordenadas = new double[lecturas.size() - 1][2];
        double[] distancias = new double[lecturas.size() - 1];

        for (int i = 0; i < lecturas.size() - 1; i++) {
            coordenadas[i][0] = lecturas.get(i).getPosX();
            coordenadas[i][1] = lecturas.get(i).getPosY();
            distancias[i] = calcularDistanciaEntreDosPuntos(
                    coordenadas[i][0], coordenadas[i][1],
                    lecturas.get(lecturas.size() - 1).getPosX(), lecturas.get(lecturas.size() - 1).getPosY()
            );
        }

        triangularPosicionTrilateracion(coordenadas, distancias);
    }
    private double calcularDistanciaEntreDosPuntos(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }



    private void triangularPosicionBasadoEnMacs(List<String> macs, List<String> coordenadas) {
        double sumaPonderadaX = 0.0;
        double sumaPonderadaY = 0.0;
        double sumaPonderaciones = 0.0;

        for (int i = 0; i < macs.size(); i++) {
            String coordenada = coordenadas.get(i);
            int nivelSenal = obtenerNivelSenalDesdeMac(macs.get(i), datosCache.get(coordenada));

            double posX = obtenerPosXDesdeCoordenadas(coordenada);
            double posY = obtenerPosYDesdeCoordenadas(coordenada);

            double ponderacion = Math.pow(10.0, nivelSenal / -20.0);
            sumaPonderadaX += posX * ponderacion;
            sumaPonderadaY += posY * ponderacion;
            sumaPonderaciones += ponderacion;
        }

        double posicionX = sumaPonderadaX / sumaPonderaciones;
        double posicionY = sumaPonderadaY / sumaPonderaciones;

        posicionX = Math.round(posicionX * 100.0) / 100.0;
        posicionY = Math.round(posicionY * 100.0) / 100.0;
        Log.d("PosicionManager", "Posición X calculada: " + posicionX);
        Log.d("PosicionManager", "Posición Y calculada: " + posicionY);


        if (!Double.isNaN(posicionX) && !Double.isNaN(posicionY)) {
            Log.d("PosicionManager", "Ubicación aproximada basada en MACs: (" + posicionX + "," + posicionY + ")");
            mostrarToast("MACs: (" + posicionX + "," + posicionY + ")");
            if (huboCambioUbicacion(posicionX, posicionY)) {
                // Actualizar la posición almacenada
                coordenadaAnteriorX = posicionX;
                coordenadaAnteriorY = posicionY;
            }
        } else {
            Log.e("PosicionManager", "Error al calcular la ubicación aproximada basada en MACs.");
        }
    }

    private int obtenerNivelSenalDesdeMac(String mac, List<LecturaWiFi> lecturas) {
        for (LecturaWiFi lectura : lecturas) {
            if (lectura.getMac().equalsIgnoreCase(mac)) {
                return lectura.getNivelSenal();
            }
        }
        return 0; // Valor predeterminado si no se encuentra la MAC en las lecturas
    }





    private void calcularPosicionPonderada(List<Integer> senales, List<String> coordenadas) {
        double sumaPonderadaX = 0.0;
        double sumaPonderadaY = 0.0;
        double sumaPonderaciones = 0.0;

        for (int i = 0; i < senales.size(); i++) {
            double posX = obtenerPosXDesdeCoordenadas(coordenadas.get(i));
            double posY = obtenerPosYDesdeCoordenadas(coordenadas.get(i));
            int nivelSenal = senales.get(i);

            double ponderacion = Math.pow(10.0, nivelSenal / -20.0);
            sumaPonderadaX += posX * ponderacion;
            sumaPonderadaY += posY * ponderacion;
            sumaPonderaciones += ponderacion;
        }

        double posicionX = sumaPonderadaX / sumaPonderaciones;
        double posicionY = sumaPonderadaY / sumaPonderaciones;

        posicionX = Math.round(posicionX * 100.0) / 100.0;
        posicionY = Math.round(posicionY * 100.0) / 100.0;

        if (!Double.isNaN(posicionX) && !Double.isNaN(posicionY)) {
            if (huboCambioUbicacion(posicionX, posicionY)) {
                Log.d("PosicionManager", "Ubicación ponderada actual: (" + posicionX + "," + posicionY + ")");
                mostrarToast("Ubicación ponderada actual: (" + posicionX + "," + posicionY + ")");
            } else {
                Log.d("PosicionManager", "Ubicación no ha cambiado.");
            }
        } else {
            Log.e("PosicionManager", "Error al calcular la ubicación ponderada actual.");
        }
    }

    private boolean contieneMac(List<LecturaWiFi> lecturas, String mac) {
        return lecturas.stream().anyMatch(lectura -> lectura.getMac().equalsIgnoreCase(mac));
    }

    private double obtenerPosXDesdeCoordenadas(String coordenadas) {
        String[] partes = coordenadas.split(",");
        return Double.parseDouble(partes[0].replace("(", "").trim());
    }

    private double obtenerPosYDesdeCoordenadas(String coordenadas) {
        String[] partes = coordenadas.split(",");
        return Double.parseDouble(partes[1].replace(")", "").trim());
    }

    private void mostrarToast(final String mensaje) {
        //new Handler(Looper.getMainLooper()).post(() ->
                //Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show());
    }

    private boolean huboCambioUbicacion(double nuevaPosX, double nuevaPosY) {
        boolean huboCambio = Double.isNaN(coordenadaAnteriorX) || Double.isNaN(coordenadaAnteriorY) ||
                coordenadaAnteriorX != nuevaPosX || coordenadaAnteriorY != nuevaPosY;

        if (huboCambio) {
            coordenadaAnteriorX = nuevaPosX;
            coordenadaAnteriorY = nuevaPosY;
        }

        return huboCambio;
    }

    // Método para triangular la posición utilizando trilateración
    private void triangularPosicionTrilateracion(double[][] coordenadas, double[] distancias) {
        try {
            if (coordenadas.length < 3 || distancias.length < 3) {
                throw new IllegalArgumentException("Se requieren al menos tres puntos de referencia.");
            }

            double[] posicion = Trilateracion.trilaterarPosicion(coordenadas, distancias);

            // Puedes utilizar la posición estimada según tus necesidades
            double posicionX = Math.round(posicion[0] * 100.0) / 100.0;
            double posicionY = Math.round(posicion[1] * 100.0) / 100.0;

            if (!Double.isNaN(posicionX) && !Double.isNaN(posicionY)) {
                if (huboCambioUbicacion(posicionX, posicionY)) {
                    Log.d("PosicionManager", "Ubicación trilaterada: (" + posicionX + "," + posicionY + ")");
                    mostrarToast("Ubicación trilaterada: (" + posicionX + "," + posicionY + ")");
                } else {
                    Log.d("PosicionManager", "Ubicación no ha cambiado.");
                }
            } else {
                Log.e("PosicionManager", "Error al calcular la ubicación trilaterada.");
            }
        } catch (Exception e) {
            Log.e("PosicionManager", "Error en la trilateración: " + e.getMessage());
        }
    }
}