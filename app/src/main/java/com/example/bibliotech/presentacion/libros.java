package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotech.R;
import com.example.bibliotech.datos.Book;
import com.example.bibliotech.datos.bookAdapter;
import com.example.bibliotech.datos.firestore.BookFireStore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class libros extends Fragment {
    ImageView buttonlibro;
    View rootView;

    private RecyclerView recyclerView;

    private bookAdapter bookAdapter;

    private List<Book> listBook = new ArrayList<>();
    private BookFireStore db = new BookFireStore();

    private List<Uri> UriList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootView = inflater.inflate(R.layout.libros, container, false);
        buttonlibro = rootView.findViewById(R.id.libroa);
        buttonlibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define the activity you want to start when the button is clicked
                Intent intent = new Intent(getActivity(), desplegableActivity.class); // Replace NewActivity with the desired activity
                startActivity(intent);
            }
        });

        cargarDatos();

        recyclerView = rootView.findViewById(R.id.RecyclerViewLibros);
        /*bookAdapter = new bookAdapter(listBook,getContext(), UriList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(bookAdapter);*/
        final boolean[] isFinish = new boolean[1];
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Book bookToTravel = listBook.get(position);
                        Intent intent = new Intent(getActivity(), desplegableActivity.class);
                        intent.putExtra("num_pag", bookToTravel.getPagenumber());
                        intent.putExtra("author",bookToTravel.getAuthor());
                        intent.putExtra("editorial", bookToTravel.getEditorial());
                        intent.putExtra("genre",bookToTravel.getSection());
                        intent.putExtra("sinopsis",bookToTravel.getSinopsis());
                        intent.putExtra("ISBN", bookToTravel.getISBN());
                        startActivity(intent);
                        isFinish[0] = true;


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));




        return rootView;
    }



    private void cargarDatos() {
        listBook.clear();
        db.getBooksSet(new BookFireStore.BookFireStoreSetCallback() {
            @Override
            public void onBookSetLoaded(Set<Book> books) {
                listBook = new ArrayList<>(books);

                for (Book book : listBook) {
                    FirebaseStorage.getInstance()
                            .getReference().child("images/portada/" + book.getISBN() + ".jpg").getDownloadUrl().addOnSuccessListener(task2 -> {
                                UriList.add(task2);
                            }).addOnFailureListener(e -> {
                                Log.d("BookFireStoreImageDownload", e.getMessage());
                            });
                }
                confgigureRecyclerView(listBook,UriList);
            }


            @Override
            public void onBookSetError(String errorMessage) {
                Log.d("Libros", errorMessage);
            }
        });

    }

    private void confgigureRecyclerView (List<Book> list,List<Uri> uriList){
        bookAdapter = new bookAdapter(list,getContext(),uriList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(bookAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Book bookToTravel = listBook.get(position);
                        Intent intent = new Intent(getActivity(), desplegableActivity.class);
                        intent.putExtra("num_pag", bookToTravel.getPagenumber());
                        intent.putExtra("author",bookToTravel.getAuthor());
                        intent.putExtra("editorial", bookToTravel.getEditorial());
                        intent.putExtra("genre",bookToTravel.getGenre());
                        intent.putExtra("sinopsis",bookToTravel.getSinopsis());
                        startActivity(intent);




                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
    }

}