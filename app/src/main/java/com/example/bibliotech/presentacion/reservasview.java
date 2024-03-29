package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

import com.example.bibliotech.R;
import com.example.bibliotech.presentacion.reservalibrosActivity;
import com.example.bibliotech.presentacion.reservasalaactivity;

public class reservasview extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reservas, container, false);

        Button buttonSalasReservadas = rootView.findViewById(R.id.button9);
        Button buttonlibrosReservadas = rootView.findViewById(R.id.button8);
        buttonSalasReservadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define the activity you want to start when the button is clicked
                Intent intent = new Intent(getActivity(), reservasalaactivity.class); // Replace NewActivity with the desired activity
                startActivity(intent);
            }
        });

        buttonlibrosReservadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define the activity you want to start when the button is clicked
                Intent intent = new Intent(getActivity(), reservalibrosActivity.class); // Replace NewActivity with the desired activity
                startActivity(intent);
            }
        });

        return rootView;
    }
}
