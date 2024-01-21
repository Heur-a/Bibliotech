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
import com.example.bibliotech.datos.Room;
import com.example.bibliotech.datos.firestore.RoomFireStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdminSalasFragment extends Fragment {

    private List<Room> salasList = new ArrayList<>();
    private SalasAdminAdapter salasAdapter;
    private RoomFireStore ROOMDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_salas, container, false);


        ROOMDB = new RoomFireStore();
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewSalas);
        Button btnAddSala = rootView.findViewById(R.id.btnAddSala);

        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        salasAdapter = new SalasAdminAdapter(salasList);
        ROOMDB.getRoomsSet(new RoomFireStore.SetRoomCallback() {
            @Override
            public void onRoomsLoaded(Set<Room> roomList) {
                salasList = new ArrayList<>(roomList);
                salasAdapter = new SalasAdminAdapter(salasList);
                recyclerView.setAdapter(salasAdapter);
            }

            @Override
            public void onRoomsError(Exception e) {

            }
        });

        recyclerView.setAdapter(salasAdapter);

        // Configuración del botón para añadir salas
        btnAddSala.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AdminAnyadirSalasActivity.class);
            startActivity(intent);
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
