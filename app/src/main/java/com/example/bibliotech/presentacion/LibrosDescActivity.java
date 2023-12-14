package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotech.MainActivity;
import com.example.bibliotech.R;

public class LibrosDescActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantlibrospage); // Set the layout for this activity
    }

    public void onClick(View view) {
        // Open the EditProfileActivity when the "editar" ImageView is clicked
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
