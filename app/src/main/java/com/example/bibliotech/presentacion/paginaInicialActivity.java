package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.MainActivity;
import com.example.bibliotech.R;

public class paginaInicialActivity extends AppCompatActivity {

    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.paginainical);
        Button logIn,SignIn;
        logIn = findViewById(R.id.buttonlogInInit);
        SignIn = findViewById(R.id.buttonSignInInit);
        setClickListener(logIn, CustomLoginActivity.class);
        setClickListener(SignIn, RegisterActivity.class);
        FireBaseActions.getInstance();
        FireBaseActions.verificaSiUsuarioValidado(this, MainActivity.class,this);

    }


    private void changeActivity(Class v ) {

        Intent i = new Intent(this, v);
        startActivity(i);
        finish();
    }

    private void setClickListener(Button b, Class v) {
        b.setOnClickListener(task -> changeActivity(v));
    }

}
