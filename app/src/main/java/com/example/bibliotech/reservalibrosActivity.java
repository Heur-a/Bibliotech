package com.example.bibliotech;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotech.R;
import com.example.bibliotech.perfilActivity;

public class reservalibrosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paginasreservalib); // Set the layout for this activity
    }

    public void onClick(View view) {
        // Open the EditProfileActivity when the "editar" ImageView is clicked
        Intent intent = new Intent(reservalibrosActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
