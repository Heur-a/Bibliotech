package com.example.bibliotech.maplogic;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.security.Provider;

// TriangulacionService.java
public class TriangulacionService extends Service {

    private PosicionManager posicionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        posicionManager = new PosicionManager(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        realizarTriangulacionContinua();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        detenerTriangulacionContinua();
    }

    private void realizarTriangulacionContinua() {
        // Puedes ajustar la lógica según tus necesidades
        new Thread(() -> {
            while (true) {
                try {
                    // Realizar la triangulación
                    // (Asegúrate de manejar correctamente las coordenadas obtenidas)
                    //posicionManager.obtenerDatosParaTodasCoordenadas();

                    // Dormir durante un intervalo de tiempo antes de la próxima triangulación
                    Thread.sleep(30000);  // Por ejemplo, cada 30 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void detenerTriangulacionContinua() {
        // Puedes agregar lógica de detención si es necesario
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
