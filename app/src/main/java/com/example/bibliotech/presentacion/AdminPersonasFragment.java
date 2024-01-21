package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bibliotech.R;
import com.example.bibliotech.datos.User;
import com.example.bibliotech.datos.firestore.UserFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdminPersonasFragment extends Fragment {

    private List<User> userList = new ArrayList<>();
    private PersonAdapter personAdapter;

    private UserFirestore USERDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_personas, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        Button btnAdd = rootView.findViewById(R.id.btnAdd);

        USERDB = new UserFirestore();

        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        USERDB.getUser(new UserFirestore.getUserSet() {
            @Override
            public void onUserLoaded(Set<User> userSet) {
                userList = new ArrayList<>(userSet);
                personAdapter = new PersonAdapter(userList);
                recyclerView.setAdapter(personAdapter);
            }

            @Override
            public void onUserFail(Exception e) {
                Log.d("MEOW MEOW", "onUserFail: " + e.getMessage());
            }
        });


        personAdapter = new PersonAdapter(userList);
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
}
