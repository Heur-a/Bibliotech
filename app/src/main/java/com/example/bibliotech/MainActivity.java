package com.example.bibliotech;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.appcompat.widget.Toolbar;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    HashMap<String, List<String>> topicList;

    private static final int SOLICITUD_PERMISO_WRITE_CALL= 0;

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
        //Define header Views
        NavigationView Nav= findViewById(R.id.nav_view);
        View headerView = Nav.getHeaderView(0);
        ImageView imageHeader = headerView.findViewById(R.id.imageHeader);
        TextView headerText = headerView.findViewById(R.id.headerText);
        TextView idText = headerView.findViewById(R.id.headerId);
        //ponerDatosMockup();

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

        updateInfo(imageHeader,headerText,idText,this);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId(); // Get the selected item's ID

                if (id == R.id.btn_perfil) {
                    switchToProfilePage();
                } else if (id == R.id.btn_notis) {
                    //switchToNotificationsPage();
                } else if (id == R.id.btn_reservas) {
                    salas();
                } else if (id == R.id.btn_libros) {
                    librossss();
                } else if (id == R.id.btn_config) {
                    //switchToSettingsPage();
                } else if (id == R.id.btn_modo) {
                    // Implement the dark mode toggle logic here
                } else if (id == R.id.btn_reporterr) {
                    irtlf();
                } else if (id == R.id.btn_acercade) {
                    switchAcercade();
                } else if (id == R.id.btn_acreditacion) {
                    switchToAdminPage();
                } else if (id == R.id.btn_cerrarsesion) {
                    cerrarSesion(navigationView); // Call your existing logout method
                }

                // Close the navigation drawer after handling the item click
                drawableLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });




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
                    return new reservasviewnew();
                case 1:
                    return new libros();
                case 2:
                    return new home();
                case 3:
                    return new salas();
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

    private void switchToProfilePage() {
        // Implement the logic to navigate to the profile page here
        // For example, you can start a new activity or replace the current fragment.
        // Replace "YourProfileActivity.class" with your actual profile activity.
        Intent intent = new Intent(this, perfilActivity.class);
        startActivity(intent);
    }
    private void switchAcercade() {
        // Implement the logic to navigate to the profile page here
        // For example, you can start a new activity or replace the current fragment.
        // Replace "YourProfileActivity.class" with your actual profile activity.
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

    private void switchToAdminPage() {
        // Implement the logic to navigate to the profile page here
        // For example, you can start a new activity or replace the current fragment.
        // Replace "YourProfileActivity.class" with your actual profile activity.
        Intent intent = new Intent(this, AdminMainActivity.class);
        startActivity(intent);
    }

    public void salas() {
        // Open the EditProfileActivity when the "editar" ImageView is clicked
        Intent intent = new Intent(MainActivity.this, reservasalaactivity.class);
        startActivity(intent);
    }

    public void librossss() {
        // Open the EditProfileActivity when the "editar" ImageView is clicked
        Intent intent = new Intent(MainActivity.this, reservalibrosActivity.class);
        startActivity(intent);
    }

    /*public void ponerDatosMockup() {
        BookFireStore bookdb = new BookFireStore();
        Book book = new Book("12345","CACA","EDITIORAL CHACHI PIRULI","LO QUE EL VIENTO SE DEJÓ","55","CIENCIA","RESUMEN BUENO BUENO BUENO");
        bookdb.add(book);
        LocalDateTime specificDateTime = LocalDateTime.of(2023, 11, 24, 12, 30, 45);

        // Converteix LocalDateTime a Date
        Date date1 = Date.from(specificDateTime.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime specificDateTime2 = LocalDateTime.of(2023, 11, 30, 12, 30, 45);

        // Converteix LocalDateTime a Date
        Date date2 = Date.from(specificDateTime.atZone(ZoneId.systemDefault()).toInstant());

        reserva reservaLibro = new reserva(date1,date2,"31");
        reservaLibro.addToBook(book);
        UserFirestore userdb = new UserFirestore();
        User user = new User(FireBaseActions.user.getEmail(),null,FireBaseActions.user.getDisplayName(),FireBaseActions.user.getDisplayName(),FireBaseActions.user.getUid(),"user");
        userdb.add(user);
        reservaLibro reservaLibro1 = new reservaLibro(date1,date2,user.id,book.getISBN());
        reservaLibro1.anyadirAUser(user.id);

    }*/

    public void irtlf() {
        // Verifica si tienes el permiso CALL_PHONE
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // Si tienes el permiso, realiza la llamada
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:962849304"));
            startActivity(intent);
        } else {
            // Si no tienes el permiso, solicita permisos
            solicitarPermisoLlamada(Manifest.permission.CALL_PHONE, "Sin el permiso" +
                    " administrar llamadas no puedo borrar llamadas del registro.", SOLICITUD_PERMISO_WRITE_CALL, this);
        }
    }
    public static void solicitarPermisoLlamada(final String permiso, String justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso)) {
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", (dialog, whichButton) -> ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode)).show(); } else {
            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode);
        }
    }
}

