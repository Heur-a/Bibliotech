package com.example.bibliotech.datos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.example.bibliotech.MainActivity;
import com.example.bibliotech.R;
import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.presentacion.perfilActivity;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class bookAdapter extends RecyclerView.Adapter<bookAdapter.bookAdapterViewHolder> {
    private List<Book> bookArrayList;
    private Context context;
    public bookAdapter(List<Book> items, Context context) {

        bookArrayList = items;
        this.context = context;
    }


    @NonNull
    @Override
    public bookAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card_viewa, parent, false);
        return new bookAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookAdapter.bookAdapterViewHolder holder, int position) {
                Book currentBook = bookArrayList.get(position);

                //TODO: Change this to actually put the ranking number
                holder.numero.setText(String.valueOf(position + 1));
        Glide.with(context)
                .load(currentBook.getImageUri())  // Utilitza la URL emmagatzemada a Firebase Storage
                .into(holder.portada);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;  // Ancho original
        holder.itemView.setLayoutParams(params);

    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    protected class bookAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView portada;
        TextView numero;
        public bookAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            portada = itemView.findViewById(R.id.featured_image);
            numero = itemView.findViewById(R.id.numero);

        }
    }
}
