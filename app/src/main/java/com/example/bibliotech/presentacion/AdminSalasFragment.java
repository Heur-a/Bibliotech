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

public class AdminSalasFragment extends Fragment {

    private List<salasAdmin> salasList = new ArrayList<>();
    private SalasAdminAdapter salasAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_salas, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewSalas);
        Button btnAddSala = rootView.findViewById(R.id.btnAddSala);

        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        salasAdapter = new SalasAdminAdapter(salasList);
        recyclerView.setAdapter(salasAdapter);

        // Configuración del botón para añadir salas
        btnAddSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdminAnyadirSalasActivity.class);
                startActivity(intent);
            }
        });

        // addSala();

        return rootView;
    }

    /* private void addSala() {
        // Genera datos de prueba para la nueva sala (puedes cambiar esto según tus necesidades)
        String randomNumeroPersonas = "50";
        String randomNumeroSala = "101";
        String randomAccesorios = "Proyector, Pizarra";

        // Añade la nueva sala a la lista
        salasList.add(new salasAdmin(randomNumeroPersonas, randomNumeroSala, randomAccesorios));

        // Notifica al adaptador sobre el cambio en los datos
        salasAdapter.notifyDataSetChanged();
    }
    */

}
