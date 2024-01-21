package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bibliotech.R;

public class NotisAjustesActivity extends AppCompatActivity {
    private ImageView imageView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notis_ajustes);
        imageView = findViewById(R.id.atras4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotisAjustesActivity.this, perfilActivity.class);
                startActivity(intent);
            }
        });
    }
}
