package com.example.bibliotech.presentacion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.bibliotech.R;
import com.example.bibliotech.datos.User;
import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.presentacion.AdminEstadisticas;
import com.example.bibliotech.presentacion.home;
import com.example.bibliotech.presentacion.libros;
import com.example.bibliotech.presentacion.mapa;
import com.example.bibliotech.presentacion.paginaInicialActivity;
import com.example.bibliotech.presentacion.reservasviewnew;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    HashMap<String, List<String>> topicList;



    private Toolbar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new AdminMainActivity.MiPagerAdapter(this)); //añade el adapter a la actividad
        DrawerLayout drawableLayout = findViewById(R.id.drawable_layout);


        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Configura los íconos y el texto de las pestañas en base a la posición
                    switch (position) {
                        case 0:
                            tab.setIcon(R.drawable.administrarusuariosseleccionado);
                            break;
                        case 1:
                            tab.setIcon(R.drawable.administrarlibrosseleccionado);
                            break;
                        case 2:
                            tab.setIcon(R.drawable.casaseleccionada);
                            break;
                        case 3:
                            tab.setIcon(R.drawable.verestadisticasseleccionado);
                            break;
                        case 4:
                            tab.setIcon(R.drawable.administrarespaciosseleccionado);
                            break;
                        // Agrega más casos para otras pestañas si es necesario
                    }
                }
        ).attach();

        // Configura el listener para cambiar los íconos cuando se selecciona o deselecciona una pestaña
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Cuando se selecciona una pestaña
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        tab.setIcon(R.drawable.administrarusuarios);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.administrarlibros);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.casaseleccionada);
                        break;
                    case 3:
                        tab.setIcon(R.drawable.verestadisticas);
                        break;
                    case 4:
                        tab.setIcon(R.drawable.adminisitrarespacios);
                        break;
                    // Agrega más casos para otras pestañas si es necesario
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Cuando se deselecciona una pestaña
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        tab.setIcon(R.drawable.administrarusuariosseleccionado);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.administrarlibrosseleccionado);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.casaseleccionada);
                        break;
                    case 3:
                        tab.setIcon(R.drawable.verestadisticasseleccionado);
                        break;
                    case 4:
                        tab.setIcon(R.drawable.administrarespaciosseleccionado);
                        break;
                    // Agrega más casos para otras pestañas si es necesario
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Acción a realizar si la pestaña seleccionada se vuelve a seleccionar
            }
        });
        viewPager.setCurrentItem(2);
    }

    public void setSupportActionBar(Toolbar supportActionBar) {
        this.supportActionBar = supportActionBar;
    }

    public class MiPagerAdapter extends FragmentStateAdapter {
        public MiPagerAdapter(FragmentActivity activity) {
            super(activity);
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        @Override
        @NonNull
        public Fragment createFragment(int position) { //dependiendo de la posicion del tab se mueve a un fragmento
            switch (position) {
                case 0:
                    return new AdminPersonasFragment();
                case 1:
                    return new AdminSalasFragment();
                case 2:
                    return new home();
                case 3:
                    return new AdminEstadisticas();
                case 4:
                    return new mapa();
            }
            return null;
        }
    }

    public void cerrarSesion(View view) {
        FireBaseActions.signOut();
        Intent i = new Intent(getApplicationContext(), paginaInicialActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    public static void updateInfo (ImageView pfp, TextView username, TextView id, Context Context) {
        if (FireBaseActions.getCurrentUser() != null) {
            User credentials = FireBaseActions.getUserAuth(Context);
            Glide.with(Context)
                    .load(credentials.photoUri)
                    .into(pfp);
            username.setText(credentials.username);
            id.setText(credentials.id);
        }
    }

}
