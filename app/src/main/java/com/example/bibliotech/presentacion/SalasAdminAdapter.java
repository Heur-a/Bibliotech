package com.example.bibliotech.presentacion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotech.R;

import java.util.List;

public class SalasAdminAdapter extends RecyclerView.Adapter<SalasAdminAdapter.ViewHolder> {

    private List<salasAdmin> salasList;

    public SalasAdminAdapter(List<salasAdmin> salasList) {
        this.salasList = salasList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sala, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        salasAdmin sala = salasList.get(position);
        holder.txtNumeroPersonas.setText(sala.getNumeroPersonas());
        holder.txtNumeroSala.setText(sala.getNumeroSala());
        holder.txtAccesorios.setText(sala.getAccesorios());
    }

    @Override
    public int getItemCount() {
        return salasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNumeroPersonas;
        TextView txtNumeroSala;
        TextView txtAccesorios;

        public ViewHolder(View view) {
            super(view);
            txtNumeroPersonas = view.findViewById(R.id.txtNumeroPersonas);
            txtNumeroSala = view.findViewById(R.id.txtNumeroSala);
            txtAccesorios = view.findViewById(R.id.txtAccesorios);
        }
    }
}
