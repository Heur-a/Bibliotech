package com.example.bibliotech;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class desplegableActivity extends AppCompatActivity{
    ExpandableListViewAdapter listViewAdapter;
    ExpandableListView expandableListView;
    List<String> chapterList;
    HashMap<String, List<String>> topicList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantlibrospage);
        expandableListView = findViewById(R.id.eListView);
        showList();
        listViewAdapter = new ExpandableListViewAdapter(this, chapterList, topicList);
        expandableListView.setAdapter(listViewAdapter);

    }

    private void showList(){

        chapterList = new ArrayList<String>();
        topicList = new HashMap<String, List<String>>();

        chapterList.add("Número de páginas");
        chapterList.add("Autora");
        chapterList.add("Editorial");
        chapterList.add("Género");
        chapterList.add("Sinopsis");

        List<String> topic1 = new ArrayList<>();
        topic1.add("365 páginas");

        List<String> topic2 = new ArrayList<>();
        topic2.add("Elísabet Benavent");

        List<String> topic3 = new ArrayList<>();
        topic3.add("DEBOLSILLO");

        List<String> topic4 = new ArrayList<>();
        topic4.add("Romance");


        List<String> topic5 = new ArrayList<>();
        topic5.add("Margot y David provienen de mundos diferentes. Ella es heredera de un imperio " +
                "hotelero. Él debe desempeñar tres trabajos para llegar a fin de mes. Pero cuando sus " +
                "caminos se unen, se dan cuenta de que solo entre ellos pueden ayudarse a recuperar el amor" +
                " de sus vidas.");

        topicList.put(chapterList.get(0), topic1);
        topicList.put(chapterList.get(1), topic2);
        topicList.put(chapterList.get(2), topic3);
        topicList.put(chapterList.get(3), topic4);
        topicList.put(chapterList.get(4), topic5);


    }

}
