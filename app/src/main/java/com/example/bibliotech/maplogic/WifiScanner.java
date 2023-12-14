package com.example.bibliotech.maplogic;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
import java.util.ArrayList;
import java.util.List;

public class WifiScanner {

    public static List<LecturaWiFi> obtenerLecturasWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> resultados = wifiManager.getScanResults();
        List<LecturaWiFi> lecturasWifi = new ArrayList<>();

        for (ScanResult resultado : resultados) {
            String mac = resultado.BSSID; // Dirección MAC de la red WiFi
            int nivelSenal = resultado.level; // Nivel de señal de la red WiFi
            lecturasWifi.add(new LecturaWiFi(mac, nivelSenal));
        }

        return lecturasWifi;
    }
}
