package com.example.bibliotech.maplogic;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WifiScanner {

    public static List<LecturaWiFi> obtenerLecturasWifi(Context context, int minLevelThreshold) {
        List<LecturaWiFi> lecturasWifi = new ArrayList<>();

        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            if (wifiManager == null) {
                Log.e("WifiScanner", "El servicio de Wi-Fi no está disponible.");
                return lecturasWifi; // Retorna la lista vacía en caso de error
            }

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String connectedBSSID = wifiInfo.getBSSID();

            List<ScanResult> resultados = wifiManager.getScanResults();

            // Filtrar por nivel de señal mínimo
            for (ScanResult resultado : resultados) {
                String mac = resultado.BSSID; // Dirección MAC de la red WiFi
                int nivelSenal = resultado.level; // Nivel de señal de la red WiFi

                // Excluir la red a la que está conectado actualmente
                if (connectedBSSID == null || !connectedBSSID.equals(mac)) {
                    // Filtrar por nivel de señal mínimo
                    if (nivelSenal >= minLevelThreshold) {
                        lecturasWifi.add(new LecturaWiFi(mac, nivelSenal));
                    }
                }
            }

            // Ordenar las lecturas por nivel de señal descendente
            Collections.sort(lecturasWifi, new Comparator<LecturaWiFi>() {
                @Override
                public int compare(LecturaWiFi lectura1, LecturaWiFi lectura2) {
                    return Integer.compare(lectura2.getNivelSenal(), lectura1.getNivelSenal());
                }
            });
        } catch (SecurityException e) {
            Log.e("WifiScanner", "Se requiere el permiso ACCESS_FINE_LOCATION para escanear Wi-Fi.", e);
        } catch (Exception e) {
            Log.e("WifiScanner", "Error al escanear Wi-Fi.", e);
        }

        return lecturasWifi;
    }

    public static void iniciarEscaneoWifi(Context context, int minLevelThreshold) {
        // Crea un ScheduledExecutorService con un solo hilo de ejecución
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Programa una tarea para ejecutarse cada 10 segundos
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Llama a tu método para obtener las lecturas de Wi-Fi aquí
                List<LecturaWiFi> lecturasWifi = obtenerLecturasWifi(context, minLevelThreshold);

                // Aquí puedes usar las lecturas de Wi-Fi para la trilateración
                // double[] posicion = Trilateracion.trilaterarPosicion(coordenadas, distancias);
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
}
