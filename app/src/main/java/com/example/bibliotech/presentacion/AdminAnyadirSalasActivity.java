package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotech.MainActivity;
import com.example.bibliotech.R;

public class AdminAnyadirSalasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anyadirsalas_admin);

        Button btnGuardar = findViewById(R.id.guardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminAnyadirSalasActivity.this, AdminSalasFragment.class);
                startActivity(intent);
            }
        });

    }

    /*
    private void guardarInformacion() {
        // Obtén los datos de los EditText
        String nombre = ((EditText) findViewById(R.id.textViewNameProfile)).getText().toString();
        String apellidos = ((EditText) findViewById(R.id.textViewSurnames)).getText().toString();
        String categoria = ((EditText) findViewById(R.id.textViewCategoryProfile)).getText().toString();
        String correo = ((EditText) findViewById(R.id.textViewEmailProfile)).getText().toString();

        // Realiza las acciones necesarias con la información obtenida
        // Por ejemplo, puedes añadir estos datos a una lista o realizar una operación de guardado en tu base de datos

        // Ejemplo: Añadir a una lista de personas
        // personList.add(new Person(nombre + " " + apellidos, categoria, correo));

        // Puedes también realizar otras acciones, como navegar a otra pantalla
        // o mostrar un mensaje de éxito
    }
    */

}
