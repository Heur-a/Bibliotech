package com.example.bibliotech.presentacion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bibliotech.R;
import com.example.bibliotech.maplogic.LecturaWiFi;
import com.example.bibliotech.maplogic.WifiScanner;
import com.ortiz.touchview.TouchImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapActivity extends Activity {
    private Button izq, arr, abj, der, guar;
    private TextView textView;

    private int screenWidth, screenHeight;
    private int textViewWidth, textViewHeight;
    private float initialX, initialY;
    private float posX;
    private float posY;
    private TouchImageView imgmap;

    private StringBuilder coordenadasStringBuilder = new StringBuilder();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);

        izq = findViewById(R.id.btnizq);
        der = findViewById(R.id.btnder);
        arr = findViewById(R.id.btnarri);
        abj = findViewById(R.id.btnabaj);
        guar = findViewById(R.id.btnguar);
        textView = findViewById(R.id.puntero);
        imgmap = findViewById(R.id.imgmap);

        // Obtener dimensiones de la pantalla
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        // Obtener dimensiones del TextView
        textViewWidth = textView.getWidth();
        textViewHeight = textView.getHeight();

        // Iniciar en el centro de la pantalla
        posX = (screenWidth - textViewWidth) / 2;
        posY = (screenHeight - textViewHeight) / 2;
        updatePosition();

        // Agregar OnClickListener a cada botón
        izq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLeft();
            }
        });

        der.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveRight();
            }
        });

        arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveUp();
            }
        });

        abj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDown();
            }
        });

        guar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCoordenadasEnArchivo();
            }
        });

        // Agregar onTouchListener al TextView para moverlo con el dedo
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Guardar la posición inicial al tocar
                        initialX = event.getX();
                        initialY = event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // Calcular el cambio en las coordenadas
                        float deltaX = event.getX() - initialX;
                        float deltaY = event.getY() - initialY;

                        // Actualizar la posición del TextView
                        posX += deltaX;
                        posY += deltaY;

                        // Actualizar la posición inicial para el próximo movimiento
                        initialX = event.getX();
                        initialY = event.getY();

                        // Mantener el TextView dentro de los límites de la pantalla
                        posX = Math.max(0, Math.min(posX, screenWidth - textViewWidth));
                        posY = Math.max(0, Math.min(posY, screenHeight - textViewHeight));

                        updatePosition();
                        break;
                }
                return true;
            }
        });
    }

    // Métodos para mover el TextView en diferentes direcciones
    private void moveLeft() {
        posX -= 10;
        updatePosition();
    }

    private void moveRight() {
        posX += 10;
        updatePosition();
    }

    private void moveUp() {
        posY -= 10;
        updatePosition();
    }

    private void moveDown() {
        posY += 10;
        updatePosition();
    }

    // Método para actualizar la posición del TextView
    private void updatePosition() {
        textView.setX(posX);
        textView.setY(posY);
    }

    private void guardarCoordenadasEnArchivo() {
        String message = "Posición deseada en la imagen después del zoom: (" + posX + ", " + posY + ")";
        showMessage(message);

        // Almacena las coordenadas en el StringBuilder
        coordenadasStringBuilder.append(message);

        // Agrega información de lectura WiFi
        List<LecturaWiFi> lecturas = WifiScanner.obtenerLecturasWifi(this);
        if (!lecturas.isEmpty()) {
            coordenadasStringBuilder.append("\nLecturas WiFi:\n");
            for (LecturaWiFi lectura : lecturas) {
                coordenadasStringBuilder.append(lectura);


            }
        }

        coordenadasStringBuilder.append("\n\n");

        try {
            // Obtén el directorio público en el almacenamiento externo
            File directorioExterno = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            // Crea un objeto File para el archivo de texto en el directorio externo
            File archivoTxt = new File(directorioExterno, "coordenadas.txt");

            // Crea el flujo de salida para escribir en el archivo
            FileOutputStream fos = new FileOutputStream(archivoTxt, true); // El segundo parámetro "true" indica modo de apéndice

            // Escribe las coordenadas en el archivo
            fos.write(coordenadasStringBuilder.toString().getBytes());

            // Cierra el flujo de salida
            fos.close();

            Toast.makeText(MapActivity.this, "Coordenadas guardadas en " + archivoTxt.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MapActivity.this, "Error al guardar las coordenadas", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMessage(String message) {
        // Mostrar el mensaje con un Toast
        Toast.makeText(MapActivity.this, message, Toast.LENGTH_SHORT).show();
        List<LecturaWiFi> lecturas = WifiScanner.obtenerLecturasWifi(this);
        Log.d("LecturaWIFI", lecturas.toString());
        Log.d("possmap", message);
    }
}
