package com.example.bibliotech.maplogic;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PosicionManager {

    private Context context;
    private Handler handler;
    private boolean isTriangulacionActiva;
    private FirebaseFirestore db;

    public PosicionManager(Context context) {
        this.context = context;
        this.handler = new Handler(Looper.getMainLooper());
        this.isTriangulacionActiva = false;
        this.db = FirebaseFirestore.getInstance();
    }

    public void iniciarTriangulacionContinua() {
        isTriangulacionActiva = true;
        ejecutarTriangulacionContinua();
    }

    public void detenerTriangulacionContinua() {
        isTriangulacionActiva = false;
    }

    private void obtenerPosicionesConocidas(PosicionesConocidasCallback callback) {
        Task<QuerySnapshot> taskq = db.collection("mapp1").get();
        taskq.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                Log.d("PosicionManager", "a");
                if (querySnapshot != null) {
                    List<LecturaPos> posicionesConocidas = new ArrayList<>();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Double posX = document.getDouble("posX");
                        Double posY = document.getDouble("posY");
                        Log.d("PosicionManager", "Obteniendo posiciones conocidas: Documento -> " + document.getId() + ", posX: " + posX + ", posY: " + posY);
                        if (posX != null && posY != null) {
                            Log.d("PosicionManager", "Las coordenadas posX y posY no son nulas.");
                            posicionesConocidas.add(new LecturaPos(posX, posY, obtenerLecturasWifi(document.getReference().collection("lecturaWIFI"))));
                        }
                    }
                    callback.onPosicionesConocidasObtenidas(posicionesConocidas);
                } else {
                    Log.e("PosicionManager", "Error: el resultado de la consulta es nulo");
                }
            } else {
                Log.e("PosicionManager", "Error al obtener posiciones conocidas", task.getException());
            }
        });
    }


    private List<LecturaWiFi> obtenerLecturasWifi(CollectionReference lecturasRef) {
        List<LecturaWiFi> lecturasWifi = new ArrayList<>();
        lecturasRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String mac = document.getString("mac");
                    Integer nivelSenal = document.getLong("nivelSenal").intValue();
                    if (mac != null && nivelSenal != null) {
                        lecturasWifi.add(new LecturaWiFi(mac, nivelSenal));
                    }
                }
            } else {
                Log.e("PosicionManager", "Error obteniendo lecturas WiFi", task.getException());
            }
        });
        return lecturasWifi;
    }


    private void ejecutarTriangulacionContinua() {
        if (!isTriangulacionActiva) {
            return;
        }

        // Obtener las lecturas WiFi del usuario
        List<LecturaWiFi> lecturasUsuario = obtenerLecturasUsuario(context);
        Log.d("PosicionManager", "Lecturas WiFi del usuario: " + lecturasUsuario);

        // Obtener las posiciones conocidas desde Firestore
        obtenerPosicionesConocidas(new PosicionesConocidasCallback() {
            @Override
            public void onPosicionesConocidasObtenidas(List<LecturaPos> posicionesConocidas) {
                if (posicionesConocidas == null) {
                    Log.d("PosicionManager", String.valueOf(posicionesConocidas.size()));
                    return;
                }


                if (!posicionesConocidas.isEmpty()) {
                    LecturaPos posicionCalculada = TriangulacionPosicion.triangularPosicion(posicionesConocidas, lecturasUsuario);

                    // Actualizar la posición del usuario
                    if (posicionCalculada != null) {
                        Log.d("PosicionManager", "Posición del usuario: X = " + posicionCalculada.getX() + ", Y = " + posicionCalculada.getY());
                    } else {
                        Log.d("PosicionManager", "No se pudo calcular la posición del usuario.");
                    }
                } else {
                    Log.d("PosicionManager", "No hay posiciones conocidas para realizar la triangulación.");
                }

                // Programar la siguiente ejecución después de un intervalo (por ejemplo, cada 5 segundos)
                handler.postDelayed(() -> ejecutarTriangulacionContinua(), 5000); // Intervalo en milisegundos
            }
        });
    }



    /*private void obtenerPosicionesConocidas(PosicionesConocidasCallback callback) {
        CollectionReference posicionesRef = db.collection("mapp1");

        Task<List<LecturaPos>> posicionesTask = posicionesRef.get().continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            List<Task<LecturaPos>> tasks = new ArrayList<>();

            for (QueryDocumentSnapshot document : task.getResult()) {
                Double posX = document.getDouble("posX");
                Double posY = document.getDouble("posY");

                Log.d("PosicionManager", "Obteniendo posiciones conocidas: Documento -> " + document.getId() + ", posX: " + posX + ", posY: " + posY);

                if (posX != null && posY != null) {
                    CollectionReference lecturasRef = document.getReference().collection("lecturaWIFI");
                    Task<List<LecturaWiFi>> lecturasTask = obtenerLecturasWifi(lecturasRef);

                    Task<LecturaPos> posicionTask = lecturasTask.continueWith(lecturas -> {
                        List<LecturaWiFi> lecturasWifi = lecturas.getResult();
                        Log.d("PosicionManager", "Lecturas WiFi obtenidas para documento " + document.getId() + ": " + lecturasWifi);
                        return new LecturaPos(posX, posY, lecturasWifi);
                    });

                    tasks.add(posicionTask);
                }
            }

            return Tasks.whenAllSuccess(tasks);
        });

        posicionesTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<LecturaPos> posicionesConocidas = task.getResult();
                callback.onPosicionesConocidasObtenidas(posicionesConocidas);
            } else {
                Log.e("PosicionManager", "Error al obtener posiciones conocidas", task.getException());
            }
        });
    }*/



    /*private Task<List<LecturaWiFi>> obtenerLecturasWifi(CollectionReference lecturasRef) {
        return lecturasRef.get().continueWith(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            List<LecturaWiFi> lecturasWifi = new ArrayList<>();

            for (QueryDocumentSnapshot lecturaDoc : task.getResult()) {
                String mac = lecturaDoc.getString("mac");
                int nivelSenal = lecturaDoc.getLong("nivelSenal").intValue();
                lecturasWifi.add(new LecturaWiFi(mac, nivelSenal));
            }

            return lecturasWifi;
        });
    }*/

    // Método para obtener las lecturas WiFi del usuario
    private List<LecturaWiFi> obtenerLecturasUsuario(Context context) {
        return WifiScanner.obtenerLecturasWifi(context);
    }

    // Interfaz para el callback de posiciones conocidas
    interface PosicionesConocidasCallback {
        void onPosicionesConocidasObtenidas(List<LecturaPos> posicionesConocidas);
    }
}
