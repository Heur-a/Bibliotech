package com.example.bibliotech.datos;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bibliotech.R;
import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.presentacion.perfilActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class bookAdapter extends RecyclerView.Adapter<bookAdapter.bookAdapterViewHolder> {
    private List<Book> bookArrayList;
    private Context context;

    private List<Uri> uriList;
    public bookAdapter(List<Book> items, Context context, List<Uri> uriList) {

        bookArrayList = items;
        this.context = context;
        this.uriList = uriList;
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
        FirebaseStorage.getInstance()
                .getReference().child("images/portada/" +currentBook.getISBN() + ".jpg").getDownloadUrl().addOnSuccessListener(task -> {
                    holder.portada.setImageResource(0);
                    Picasso.get()
                            .load(task)
                            .into(holder.portada);
                }).addOnFailureListener(e -> {
                    Log.d("profileImgDownload", e.getMessage());
                });

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
