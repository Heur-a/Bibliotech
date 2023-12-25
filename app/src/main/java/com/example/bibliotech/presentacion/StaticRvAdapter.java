package com.example.bibliotech.presentacion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotech.R;
import com.example.bibliotech.datos.firestore.StaticRvModel;

import java.util.ArrayList;

public class StaticRvAdapter extends RecyclerView.Adapter<StaticRvAdapter.StaticRVViewHolder> {
    private ArrayList<StaticRvModel> items;

    public StaticRvAdapter(ArrayList<StaticRvModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public StaticRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new StaticRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRVViewHolder holder, int position) {
        StaticRvModel currentItem = items.get(position);

        // Verifica la propiedad 'invisible' y ajusta el ancho en consecuencia
        if (currentItem.isInvisible()) {
            holder.itemView.getLayoutParams().width = 0; // Ancho cero para elementos invisibles
        } else {
            // Restaura el ancho original para elementos visibles
            holder.itemView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        // Establece los valores en los TextView
        holder.añoRes.setText(currentItem.getAño_res());
        holder.dayRes.setText(currentItem.getDia_res());
        holder.bookName.setText(currentItem.getBk_name());
        holder.dayDev.setText(currentItem.getDia_dev());
        holder.mesRes.setText(currentItem.getMes_res());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRVViewHolder extends RecyclerView.ViewHolder {
        TextView bookName, dayDev, mesRes, dayRes, añoRes;

        public StaticRVViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name);
            dayDev = itemView.findViewById(R.id.book_day_dev);
            mesRes = itemView.findViewById(R.id.book_mes_res);
            dayRes = itemView.findViewById(R.id.book_day_res);
            añoRes = itemView.findViewById(R.id.book_año_res);
        }
    }
}
