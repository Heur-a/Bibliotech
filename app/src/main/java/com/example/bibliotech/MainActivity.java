package com.example.bibliotech;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ExpandableListViewAdapter listViewAdapter;
    ExpandableListView expandableListView;
    List<String> chapterList;
    HashMap<String, List<String>> topicList;



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
        expandableListView = findViewById(R.id.eListView);
        showList();
        listViewAdapter = new ExpandableListViewAdapter(this, chapterList,topicList);
        expandableListView.setAdapter(listViewAdapter);

        //Define header Views
        NavigationView Nav= findViewById(R.id.nav_view);
        View headerView = Nav.getHeaderView(0);
        ImageView imageHeader = headerView.findViewById(R.id.imageHeader);
        TextView headerText = headerView.findViewById(R.id.headerText);
        TextView idText = headerView.findViewById(R.id.headerId);

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
            UserCredentials credentials = FireBaseActions.getCredentials(Context);
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
        Intent intent = new Intent(this, AdminActivity.class);
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
    private void showList(){

        chapterList = new ArrayList<String>();
        topicList = new HashMap<String, List<String>>();

        chapterList.add("Chapter 1");
        chapterList.add("Chapter 2");
        chapterList.add("Chapter 3");
        chapterList.add("Chapter 4");
        chapterList.add("Chapter 5");

        List<String> topic1 = new ArrayList<>();
        topic1.add("Topic 1");
        topic1.add("Topic 2");
        topic1.add("Topic 3");

        List<String> topic2 = new ArrayList<>();
        topic2.add("Topic 1");
        topic2.add("Topic 2");
        topic2.add("Topic 3");

        List<String> topic3 = new ArrayList<>();
        topic3.add("Topic 1");
        topic3.add("Topic 2");
        topic3.add("Topic 3");

        List<String> topic4 = new ArrayList<>();
        topic4.add("Topic 1");
        topic4.add("Topic 2");
        topic4.add("Topic 3");

        List<String> topic5 = new ArrayList<>();
        topic5.add("Topic 1");
        topic5.add("Topic 2");
        topic5.add("Topic 3");

        topicList.put(chapterList.get(0), topic1);
        topicList.put(chapterList.get(1), topic2);
        topicList.put(chapterList.get(2), topic3);
        topicList.put(chapterList.get(3), topic4);
        topicList.put(chapterList.get(4), topic5);


    }
}

