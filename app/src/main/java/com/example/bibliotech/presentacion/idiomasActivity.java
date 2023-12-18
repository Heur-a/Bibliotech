package com.example.bibliotech.presentacion;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bibliotech.R;

public class idiomasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idiom_ajustes); // Asegúrate de usar el nombre correcto de tu archivo de layout

        // Inicializar el Spinner
        Spinner spinnerRegions = findViewById(R.id.spinner_regions);

        // Crear un ArrayAdapter usando el array de strings y un layout de spinner predeterminado
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.regions_array, android.R.layout.simple_spinner_item); // Asegúrate de usar el nombre correcto del array

        // Especificar el layout a usar cuando la lista de opciones aparece
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar el adaptador al spinner
        spinnerRegions.setAdapter(adapter);
    }
}
