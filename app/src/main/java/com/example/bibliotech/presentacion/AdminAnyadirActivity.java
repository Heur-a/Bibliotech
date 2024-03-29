package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotech.R;
import com.example.bibliotech.datos.User;
import com.example.bibliotech.datos.firestore.UserFirestore;

public class AdminAnyadirActivity extends AppCompatActivity {
    private UserFirestore USERDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anyadirpersonas_admin);
        USERDB = new UserFirestore();

        Button btnGuardar = findViewById(R.id.guardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarInformacion();
                Intent intent = new Intent(AdminAnyadirActivity.this, AdminPersonasFragment.class);
                startActivity(intent);
            }
        });

    }


    private void guardarInformacion() {
        // Obtén los datos de los EditText
        String nombre = ((EditText) findViewById(R.id.textViewNameProfile)).getText().toString();
        String apellidos = ((EditText) findViewById(R.id.txtNumeroSala)).getText().toString();
        String categoria = ((EditText) findViewById(R.id.txtAccesorios)).getText().toString();
        String correo = ((EditText) findViewById(R.id.textViewEmailProfile)).getText().toString();

        //Crear usuario
        User user = new User(correo,null,nombre,apellidos,null,categoria);

        //Añadir a la base de datos
        USERDB.addAutoId(user);
    }


}
