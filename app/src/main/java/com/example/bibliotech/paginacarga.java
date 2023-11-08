package com.example.bibliotech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class paginacarga extends AppCompatActivity {

    ImageView fondoanimacion;
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paginacarga);

        fondoanimacion = findViewById(R.id.fondoanimacion);
        lottieAnimationView = findViewById(R.id.animacion);
        fondoanimacion.animate().translationX(-1600).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationX(1400).setDuration(1000).setStartDelay(4000);
    }
}