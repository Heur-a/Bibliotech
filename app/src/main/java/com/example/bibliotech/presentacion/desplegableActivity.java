package com.example.bibliotech.presentacion;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.example.bibliotech.R;


import androidx.appcompat.app.AppCompatActivity;

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
