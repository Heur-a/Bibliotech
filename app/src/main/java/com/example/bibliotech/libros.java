package com.example.bibliotech;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class libros extends Fragment {
    ImageView buttonlibro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.libros, container, false);
        buttonlibro = rootView.findViewById(R.id.libroa);
        buttonlibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define the activity you want to start when the button is clicked
                Intent intent = new Intent(getActivity(),LibrosDescActivity.class); // Replace NewActivity with the desired activity
                startActivity(intent);
            }
        });

        return rootView;
    }

}