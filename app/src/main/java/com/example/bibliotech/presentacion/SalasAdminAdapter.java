package com.example.bibliotech.presentacion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotech.R;
import com.example.bibliotech.datos.Room;

import java.util.List;

public class SalasAdminAdapter extends RecyclerView.Adapter<SalasAdminAdapter.SalasAdminViewHolder> {

    private List<Room> roomList;

    public SalasAdminAdapter(List<Room> roomList) {
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public SalasAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sala, parent, false);
        return new SalasAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalasAdminViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.bind(room);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    // Método para actualizar la información de un elemento en una posición específica
    public void updateItemAtPosition(int position, Room room) {
        roomList.set(position, room);
        notifyItemChanged(position);
    }

    public static class SalasAdminViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNumeroPersonas;
        private TextView txtNumeroSala;
        private TextView txtAccesorios;

        public SalasAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumeroPersonas = itemView.findViewById(R.id.txtAccesorios);
            txtNumeroSala = itemView.findViewById(R.id.txtNumeroSala);
            txtAccesorios = itemView.findViewById(R.id.txtAccesorios);
        }

        public void bind(Room room) {
            txtNumeroPersonas.setText(room.getNombreSala());
            txtNumeroSala.setText(room.getNumberpeople());
            txtAccesorios.setText(room.getAccesories().toString());
        }
    }
}
