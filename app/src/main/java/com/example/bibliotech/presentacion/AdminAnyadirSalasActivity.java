package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotech.MainActivity;
import com.example.bibliotech.R;
import com.example.bibliotech.datos.Room;
import com.example.bibliotech.datos.firestore.RoomFireStore;

import java.util.Arrays;
import java.util.List;

public class AdminAnyadirSalasActivity extends AppCompatActivity {
    private List<Room> roomList;
    private RoomFireStore ROOMDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anyadirsalas_admin);
        ROOMDB = new RoomFireStore();

        Button btnGuardar = findViewById(R.id.guardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarInformacion();
                Intent intent = new Intent(AdminAnyadirSalasActivity.this, AdminSalasFragment.class);
                startActivity(intent);
            }
        });

    }



    private void guardarInformacion() {
        // Obtén los datos de los EditText
        String numeroPersonas = ((EditText) findViewById(R.id.textViewEmailProfile)).getText().toString();
        String numeroSala = ((EditText) findViewById(R.id.txtNumeroSala)).getText().toString();
        String accesorios = ((EditText) findViewById(R.id.txtAccesorios)).getText().toString();

        // Divide el string de accesorios por comas y crea una lista
        List<String> listaAccesorios = Arrays.asList(accesorios.split("\\s*,\\s*"));



        // Crea una nueva sala con la información
        Room nuevaRoom = new Room(numeroPersonas, numeroSala, listaAccesorios);



        // Agrega la nueva sala a la lista o base de datos según tus necesidades
        // ...

        ROOMDB.add(nuevaRoom);







    }

}
