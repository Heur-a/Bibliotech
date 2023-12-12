package com.example.bibliotech;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.List;

// Import statements

public class mapa extends Fragment {

    private static final int SOLICITUD_PERMISO_ACCESS_FINE_LOCATION = 0;
    private View view;
    List<LecturaWiFi> lecturas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mapa, container, false);
        solicitudpermiso();

        return view;
    }

    public void solicitudpermiso() {
        // Verifica si tienes el permiso CALL_PHONE
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            List<LecturaWiFi> lecturas = WifiScanner.obtenerLecturasWifi(view.getContext());
            Log.d("LecturaWIFI", lecturas.toString());
        } else {
            // Si no tienes el permiso, solicita permisos
            solicitarPermisoLlamada(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso acceso a ubicacion", SOLICITUD_PERMISO_ACCESS_FINE_LOCATION, view);
        }
    }

    public static void solicitarPermisoLlamada(final String permiso, String justificacion, final int requestCode, final View actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) actividad.getContext(), permiso)) {
            new AlertDialog.Builder(actividad.getContext())
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", (dialog, whichButton) -> ActivityCompat.requestPermissions((Activity) actividad.getContext(), new String[]{permiso}, requestCode)).show();
        } else {
            ActivityCompat.requestPermissions((Activity) actividad.getContext(), new String[]{permiso}, requestCode);
        }
    }
}
