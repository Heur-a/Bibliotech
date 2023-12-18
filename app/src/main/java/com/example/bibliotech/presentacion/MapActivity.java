package com.example.bibliotech.presentacion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
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

import java.util.List;

public class MapActivity extends Activity {
    private Button izq, arr, abj, der, guar;
    private TextView textView;

    private int screenWidth, screenHeight;
    private int textViewWidth, textViewHeight;
    private float initialX, initialY;
    private float posX;
    private float posY;
    private TouchImageView imgmap;

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
                // Obtener la posición del centro del TextView en relación con la imagen
                float[] viewCenter = new float[]{textView.getX() + textView.getWidth() / 2, textView.getY() + textView.getHeight() / 2};

                // Obtener la matriz de transformación actual de la imagen
                float[] matrixValues = new float[9];
                imgmap.getImageMatrix().getValues(matrixValues);

                // Obtener el factor de escala actual de la imagen
                float scale = matrixValues[Matrix.MSCALE_X];

                // Calcular la posición del punto deseado en relación con la imagen considerando el zoom y el desplazamiento
                float imageX = (viewCenter[0] - matrixValues[Matrix.MTRANS_X]) / scale;
                float imageY = (viewCenter[1] - matrixValues[Matrix.MTRANS_Y]) / scale;

                // Mostrar un mensaje con la posición deseada en la imagen después del zoom
                showMessage("Posición deseada en la imagen después del zoom: (" + imageX + ", " + imageY + ")");
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

    private void showMessage(String message) {
        // Mostrar el mensaje con un Toast
        Toast.makeText(MapActivity.this, message, Toast.LENGTH_SHORT).show();
        List<LecturaWiFi> lecturas = WifiScanner.obtenerLecturasWifi(this);
        Log.d("LecturaWIFI", lecturas.toString());
        Log.d("possmap", message);
    }
}