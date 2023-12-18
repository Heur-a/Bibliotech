package com.example.bibliotech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bibliotech.presentacion.paginaInicialActivity;

//a
public class paginacarga extends AppCompatActivity {

    ImageView fondoanimacion;
    Handler handler; //se encarga de hacer el retardo

    private final int TIEMPO = 5000; //tiempo en ms
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paginacarga);
        ///////////////////////////////////////////////////////////
        //            Se define la animacion a realizar          //
        ///////////////////////////////////////////////////////////

        fondoanimacion = findViewById(R.id.fondoanimacion);
        lottieAnimationView = findViewById(R.id.animacion);
        fondoanimacion.animate().translationX(-1600).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationX(1400).setDuration(1000).setStartDelay(4000);
        ///////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////
        handler = new Handler();
        handler.postDelayed(new Runnable() { // define el retardo con el que se hara la accion
            @Override
            public void run() { //inicia la accion
                cambiopag();
            }
        },TIEMPO);// le pone el tiempo con el que tarda en hacer
    }

    public void cambiopag(){
        Intent intent = new Intent(this, paginaInicialActivity.class);
        startActivity(intent);
        finish();//se asegura de finalizar la actividad
    }


}