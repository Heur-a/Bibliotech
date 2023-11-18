package com.example.bibliotech;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class libroresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sala_page_reservabook);  // Reemplaza con el nombre correcto de tu layout

        // Obtén datos de la intención
        UserCredentials credentials = FireBaseActions.getCredentials(this);
        String username = credentials.username;

        String nombreLibro = getIntent().getStringExtra("nombreLibro");
        String fechaDevolucion = getIntent().getStringExtra("fechaDevolucion");
        String fechaReservadia = getIntent().getStringExtra("fechaReservadia");
        String fechaReservames = getIntent().getStringExtra("fechaReservames");
        String fechaReservaano = getIntent().getStringExtra("fechaReservaano");

// Muestra los datos en tus vistas
        TextView user = findViewById(R.id.username);
        TextView nombreLibroTextView = findViewById(R.id.bk_name);
        TextView fechaDevolucionTextView = findViewById(R.id.day_dev);
        TextView fechaReservaTextView = findViewById(R.id.day_res);

        user.setText(username);
        nombreLibroTextView.setText(nombreLibro);
        fechaDevolucionTextView.setText(fechaDevolucion);

        fechaReservaTextView.setText(fechaReservadia + "/" + fechaReservames + "/" + fechaReservaano);
    }

}
