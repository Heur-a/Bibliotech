package com.example.bibliotech.presentacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bibliotech.MainActivity;
import com.example.bibliotech.R;

public class home extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.menu_principal, container, false);

        TextView username = v.findViewById(R.id.textViewUserMenuPrincipal);
        TextView  id = v.findViewById(R.id.textViewIdMenuPrincipal);
        ImageView pfp = v.findViewById(R.id.imageViewMenuPrincipal);

        MainActivity.updateInfo(pfp,username,id,v.getContext());
        return v;
    }
}