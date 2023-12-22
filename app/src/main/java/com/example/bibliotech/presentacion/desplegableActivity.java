package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.example.bibliotech.R;
import com.example.bibliotech.datos.Book;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class desplegableActivity extends AppCompatActivity {
    ExpandableListViewAdapter listViewAdapter;
    ExpandableListView expandableListView;
    List<String> chapterList;
    HashMap<String, List<String>> topicList;
    private Book book = new Book();
    private String num_pag, author, editorial, genre, sinopsis;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantlibrospage);
        expandableListView = findViewById(R.id.eListView);


        Intent intentRebut = getIntent();
        if (intentRebut != null) {
            if (intentRebut.hasExtra("num_pag") &&
                    intentRebut.hasExtra("author") &&
                    intentRebut.hasExtra("editorial") &&
                    intentRebut.hasExtra("genre") &&
                    intentRebut.hasExtra("sinopsis")) {

                num_pag = intentRebut.getStringExtra("num_pag");
                author = intentRebut.getStringExtra("author");
                editorial = intentRebut.getStringExtra("editorial");
                genre = intentRebut.getStringExtra("genre");
                sinopsis = intentRebut.getStringExtra("sinopsis");

                // Utilitza les variables rebudes per omplir les dades del llibre
                book.setPagenumber(num_pag);
                book.setAuthor(author);
                book.setEditorial(editorial);
                book.setGenre(genre);
                book.setSinopsis(sinopsis);
            }
        }

        showList();
        listViewAdapter = new ExpandableListViewAdapter(this, chapterList, topicList);
        expandableListView.setAdapter(listViewAdapter);
    }

    private void showList() {
        chapterList = new ArrayList<>();
        topicList = new HashMap<>();

        chapterList.add("Número de páginas");
        chapterList.add("Autora");
        chapterList.add("Editorial");
        chapterList.add("Género");
        chapterList.add("Sinopsis");

        List<String> topic1 = new ArrayList<>();
        topic1.add(num_pag + " páginas");

        List<String> topic2 = new ArrayList<>();
        topic2.add(author);

        List<String> topic3 = new ArrayList<>();
        topic3.add(editorial);

        List<String> topic4 = new ArrayList<>();
        topic4.add(genre);

        List<String> topic5 = new ArrayList<>();
        topic5.add(sinopsis);

        topicList.put(chapterList.get(0), topic1);
        topicList.put(chapterList.get(1), topic2);
        topicList.put(chapterList.get(2), topic3);
        topicList.put(chapterList.get(3), topic4);
        topicList.put(chapterList.get(4), topic5);
    }
}
