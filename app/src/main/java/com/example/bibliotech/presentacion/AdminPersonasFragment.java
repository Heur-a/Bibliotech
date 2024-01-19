package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bibliotech.R;
import java.util.ArrayList;
import java.util.List;

public class AdminPersonasFragment extends Fragment {

    private List<Person> personList = new ArrayList<>();
    private PersonAdapter personAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_personas, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        Button btnAdd = rootView.findViewById(R.id.btnAdd);

        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        personAdapter = new PersonAdapter(personList);
        recyclerView.setAdapter(personAdapter);

        // Configuración del botón para añadir personas
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdminAnyadirActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void addPerson() {
        // Genera un nombre aleatorio para la nueva persona (puedes cambiar esto según tus necesidades)
        String randomName = "Persona " + (personList.size() + 1);

        // Añade la nueva persona a la lista
        personList.add(new Person(randomName));

        // Notifica al adaptador sobre el cambio en los datos
        personAdapter.notifyDataSetChanged();
    }
}
