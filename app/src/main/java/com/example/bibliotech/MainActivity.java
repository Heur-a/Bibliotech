package com.example.bibliotech;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {


    private Toolbar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_final);
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MiPagerAdapter(this));
        DrawerLayout drawableLayout = findViewById(R.id.drawable_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        ImageButton menuButton = findViewById(R.id.btn_menu_desplegable);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawableLayout.isDrawerOpen(GravityCompat.START)) {
                    drawableLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawableLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Configura los íconos y el texto de las pestañas en base a la posición
                    switch (position) {
                        case 0:
                            tab.setIcon(R.drawable.reservassinseleccionar);
                            break;
                        case 1:
                            tab.setIcon(R.drawable.librosinseleccionar);
                            break;
                        case 2:
                            tab.setIcon(R.drawable.casaseleccionada);
                            break;
                        case 3:
                            tab.setIcon(R.drawable.salasinseleccionar);
                            break;
                        case 4:
                            tab.setIcon(R.drawable.mapasinseleccionar);
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
                        tab.setIcon(R.drawable.reservasseleccionado);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.libroseleccionado);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.casaseleccionada);
                        break;
                    case 3:
                        tab.setIcon(R.drawable.salaseleccionada);
                        break;
                    case 4:
                        tab.setIcon(R.drawable.mapaseleccionado);
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
                        tab.setIcon(R.drawable.reservassinseleccionar);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.librosinseleccionar);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.casasinseleccionar);
                        break;
                    case 3:
                        tab.setIcon(R.drawable.salasinseleccionar);
                        break;
                    case 4:
                        tab.setIcon(R.drawable.mapasinseleccionar);
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
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new reservasview();
                case 1:
                    return new salas();
                case 2:
                    return new home();
                case 3:
                    return new libros();
                case 4:
                    return new mapa();
            }
            return null;
        }
    }

    public void cerrarSesion(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), CustomLoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}

