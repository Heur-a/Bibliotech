package com.example.bibliotech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRVViewHolder holder, int position) {
        StaticRvModel currentItem = items.get(position);

        // Verifica la propiedad 'invisible' y ajusta el ancho en consecuencia
        if (currentItem.isInvisible()) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.width = 0;// Ancho cero para elementos invisibles
            holder.itemView.setLayoutParams(params);
        } else {
            // Restaura el ancho original para elementos visibles
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;  // Ancho original
            holder.itemView.setLayoutParams(params);
        }

        holder.año_res.setText(currentItem.getAño_res());
        holder.dia_res.setText(currentItem.getDia_res());
        holder.bk_name.setText(currentItem.getBk_name());
        holder.dia_dev.setText(currentItem.getDia_dev());
        holder.mes_res.setText(currentItem.getMes_res());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRVViewHolder extends RecyclerView.ViewHolder {
        TextView bk_name, dia_dev, mes_res, dia_res, año_res;

        public StaticRVViewHolder(@NonNull View itemView) {
            super(itemView);
            bk_name = itemView.findViewById(R.id.book_name);
            dia_dev = itemView.findViewById(R.id.book_day_dev);
            mes_res = itemView.findViewById(R.id.book_mes_res);
            dia_res = itemView.findViewById(R.id.book_day_res);
            año_res = itemView.findViewById(R.id.book_año_res);
        }
    }
}

