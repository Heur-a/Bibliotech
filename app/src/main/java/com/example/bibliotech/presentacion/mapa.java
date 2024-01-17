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

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.bibliotech.maplogic.LecturaWiFi;
import com.example.bibliotech.maplogic.PosicionManager;
import com.example.bibliotech.R;
import com.example.bibliotech.maplogic.WifiScanner;

import java.util.List;

public class mapa extends Fragment {

    private static final int SOLICITUD_PERMISO_ACCESS_FINE_LOCATION = 0;
    private View view;
    public ImageView imageView;
    private PosicionManager posicionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mapa, container, false);
        imageView = view.findViewById(R.id.mapbtn);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            startActivity(intent);
        });

        posicionManager = new PosicionManager(requireContext());

        solicitudPermiso();
        iniciarActualizacionContinua();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        iniciarActualizacionContinua();
    }

    @Override
    public void onPause() {
        super.onPause();
        detenerActualizacionContinua();
    }

    private void iniciarActualizacionContinua() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            posicionManager.iniciarTriangulacionContinua();
        } else {
            solicitudPermiso();
        }
    }

    private void detenerActualizacionContinua() {
        posicionManager.detenerTriangulacionContinua();
    }

    public void solicitudPermiso() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            List<LecturaWiFi> lecturas = WifiScanner.obtenerLecturasWifi(view.getContext());
            Log.d("LecturaWIFI", lecturas.toString());
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso de acceso a la ubicación", SOLICITUD_PERMISO_ACCESS_FINE_LOCATION, view);
        }
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
}
