package com.example.bibliotech.presentacion;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.bibliotech.maplogic.LecturaWiFi;
import com.example.bibliotech.maplogic.PosicionManager;
import com.example.bibliotech.R;
import com.example.bibliotech.maplogic.TriangulacionService;
import com.example.bibliotech.maplogic.WifiScanner;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class mapa extends Fragment {

    private static final int SOLICITUD_PERMISO_ACCESS_FINE_LOCATION = 0;
    private View view;
   // public ImageView imageView;
    private PosicionManager posicionManager;

    private static final long INTERVALO_TRIANGULACION = 5000; // Intervalo en milisegundos (en este caso, 5 segundos)
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mapa, container, false);
        //imageView = view.findViewById(R.id.mapbtn);
       /* imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            startActivity(intent);
        });*/

        posicionManager = new PosicionManager(getContext());
        posicionManager.obtenerDatosParaTodasCoordenadas();
        solicitudPermiso();
        //iniciarServicioTriangulacion();
        // mostrarPosicionAproximada(); // Muestra la posición al abrir el fragment

        // Iniciar el temporizador para la triangulación continua
        // Iniciar el temporizador para la triangulación continua
        //iniciarTemporizadorTriangulacion();
        //iniciarActualizacionContinua(); // Inicia la actualización continua


        return view;
    }

    private void solicitudPermiso() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            List<LecturaWiFi> lecturasEnTiempoReal = WifiScanner.obtenerLecturasWifi(getContext(),  -70);
            Log.d("LecturaWIFI", lecturasEnTiempoReal.toString());
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso de acceso a la ubicación", SOLICITUD_PERMISO_ACCESS_FINE_LOCATION, view);
        }
    }

    /*private void iniciarServicioTriangulacion() {
        Intent servicioIntent = new Intent(getActivity(), TriangulacionService.class);
        getActivity().startService(servicioIntent);
    }*/

    private void mostrarMensaje(String mensaje) {
        new Handler(Looper.getMainLooper()).post(() -> {
            //Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
        });
    }

    public static void solicitarPermiso(final String permiso, String justificacion, final int requestCode, final View actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) actividad.getContext(), permiso)) {
            new AlertDialog.Builder(actividad.getContext())
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", (dialog, whichButton) -> ActivityCompat.requestPermissions((Activity) actividad.getContext(), new String[]{permiso}, requestCode)).show();
        } else {
            ActivityCompat.requestPermissions((Activity) actividad.getContext(), new String[]{permiso}, requestCode);
        }
    }

    @Override
    public void onDestroyView() {
        // Detener el temporizador cuando el fragmento se destruye
        //detenerTemporizadorTriangulacion();
        super.onDestroyView();
    }

  /*  private void iniciarTemporizadorTriangulacion() {
        // Crear un nuevo temporizador y programar la tarea de triangulación
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Triangular la posición continuamente
                triangularPosicionContinua();
            }
        }, 0, INTERVALO_TRIANGULACION);
    }

    private void detenerTemporizadorTriangulacion() {
        // Detener el temporizador si está en ejecución
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    private void iniciarActualizacionContinua() {
        // Comentado para evitar la actualización continua
        // posicionManager.actualizarLecturasWifiEnTiempoReal();
        // posicionManager.triangularPosicionActual();
    }*/

   /* private void triangularPosicionContinua() {
        // Obtener lecturas WiFi continuamente y triangular la posición
        List<LecturaWiFi> lecturasEnTiempoReal = WifiScanner.obtenerLecturasWifi(getContext(),  -70);

        if (lecturasEnTiempoReal != null && !lecturasEnTiempoReal.isEmpty()) {
            // Triangular la posición utilizando las lecturas WiFi
            mostrarPosicionActual(lecturasEnTiempoReal);
            Log.d("lecturas",lecturasEnTiempoReal.toString());
        } else {
            // No hay lecturas WiFi disponibles
            mostrarMensaje("No hay lecturas WiFi disponibles para la triangulación.");
        }
    }*/


    /*private void mostrarPosicionActual(List<LecturaWiFi> lecturas) {
        // Triangular la posición utilizando las lecturas WiFi
        // Esto mostrará la posición actual utilizando el método triangularPosicion de PosicionManager
        posicionManager.triangularPosicionEnTiempoReal(lecturas);
    }*/
}
