/*
package com.example.bibliotech.presentacion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.R;
import com.example.bibliotech.datos.firestore.StaticRvModel;
import com.example.bibliotech.datos.reservaLibro;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class reservasviewnew extends Fragment {
    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newreservasmain, container, false);
        Calendar c = Calendar.getInstance();
        tabLayout = rootView.findViewById(R.id.tabLayout);
        //List<reservaLibro> resrvlib = reservaLibro.getReservasBook(FireBaseActions.user.getUid());
        String dia = Integer.toString(c.get(Calendar.DATE));
        //resrvlib.forEach(t -> {
          //  Log.d("HOLA", t.toString());
        //});

        List<reservaLibro> resrvlib = new ArrayList<>();
        reservaLibro.getReservasBook(FireBaseActions.user.getUid(), new reservaLibro.ReservasCallback() {
            @Override
            public void onReservasLoaded(List<reservaLibro> reservaList) {
                resrvlib.addAll(reservaList);
                // Aquí puedes realizar acciones con la lista de reservas cargadas
                for (reservaLibro reserva : resrvlib) {
                    Log.d("Datos de la DB", reserva.toString());
                }
            }

            @Override
            public void onReservasError(String errorMessage) {
                Log.d("reservaLibro getBooks", "Error: " + errorMessage);
            }
        });


        String mes = obtenerNombreMesAbreviado(c.get(Calendar.MONTH));

        String annio = Integer.toString(c.get(Calendar.YEAR));
        ArrayList<StaticRvModel> libros = new ArrayList<>();
        ArrayList<StaticRvModel> salas = new ArrayList<>();
        */
/*resrvlib.forEach(reservaLibro -> {
            LocalDate localDate = reservaLibro.getFechaIni().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            libros.add(new StaticRvModel(reservaLibro.getBookId().toString(), reservaLibro.getFechaFin().toString(), localDate.getMonth().toString(), String.valueOf(localDate.getDayOfMonth()),  String.valueOf(localDate.getDayOfYear()), false));
        });*//*

        libros.add(new StaticRvModel("lib A", "11/11/2025", mes, dia, annio, false));
        libros.add(new StaticRvModel("lib B", "12/11/2025", mes, dia, annio, false));
        libros.add(new StaticRvModel("lib C", "13/11/2025", mes, dia, annio, false));
        libros.add(new StaticRvModel("lib D", "14/11/2025", mes, dia, annio, false));

        salas.add(new StaticRvModel("sala 1", "11/11/2025", mes, dia, annio, false));
        salas.add(new StaticRvModel("sala 2", "12/11/2025", mes, dia, annio, false));
        salas.add(new StaticRvModel("sala 3", "13/11/2025", mes, dia, annio, false));
        salas.add(new StaticRvModel("sala 4", "14/11/2025", mes, dia, annio, false));

        agregarEspaciadores(libros);
        agregarEspaciadores(salas);

            recyclerView = rootView.findViewById(R.id.rv_book);
        staticRvAdapter = new StaticRvAdapter(libros);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(staticRvAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    cambiarContenidoConAnimacion(libros, false);
                } else {
                    cambiarContenidoConAnimacion(salas, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No necesitamos hacer nada aquí
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No necesitamos hacer nada aquí
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        StaticRvModel selectedItem = libros.get(position);

                        Intent intent = new Intent(getActivity(), libroresActivity.class);

                        intent.putExtra("nombreLibro", selectedItem.getBk_name());
                        intent.putExtra("fechaDevolucion", selectedItem.getDia_dev());
                        intent.putExtra("fechaReservadia", selectedItem.getDia_res());
                        intent.putExtra("fechaReservames", Integer.toString(obtenerNumeroMesDesdeNombre(selectedItem.getMes_res())));
                        intent.putExtra("fechaReservaano", selectedItem.getAño_res());

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Manejar clic largo si es necesario
                    }
                })
        );

        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ajustarEscalaYOpacidad();
            }
        });

        return rootView;
    }

    private void agregarEspaciadores(ArrayList<StaticRvModel> lista) {
        lista.add(0, new StaticRvModel("Espaciador Invisible", "01/01/1970", "ENE", "01", "1970", true));
        lista.add(new StaticRvModel("Espaciador Invisible", "01/01/1970", "ENE", "01", "1970", true));
    }

    private String obtenerNombreMesAbreviado(int numeroMes) {
        String[] nombresMesesAbreviados = {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};
        return nombresMesesAbreviados[numeroMes];
    }

    private int obtenerNumeroMesDesdeNombre(String nombreMes) {
        String[] nombresMesesAbreviados = {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};
        for (int i = 0; i < nombresMesesAbreviados.length; i++) {
            if (nombresMesesAbreviados[i].equals(nombreMes)) {
                return i + 1; // Devuelve la posición más uno
            }
        }
        // Si el nombre del mes no se encuentra, devolver -1 o un valor por defecto según tu lógica
        return -1;
    }


    private void cambiarContenidoConAnimacion(final ArrayList<StaticRvModel> nuevoContenido, final boolean deDerechaAIzquierda) {
        final float translationX = deDerechaAIzquierda ? recyclerView.getWidth() : -recyclerView.getWidth();
        recyclerView.animate()
                .translationX(translationX)
                .setDuration(150)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        staticRvAdapter = new StaticRvAdapter(nuevoContenido);
                        recyclerView.setAdapter(staticRvAdapter);
                        recyclerView.setTranslationX(-translationX);
                        recyclerView.animate()
                                .translationX(0)
                                .setDuration(150)
                                .setListener(null);
                    }
                });
    }

    private void ajustarEscalaYOpacidad() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();

        for (int i = 0; i < totalItemCount; i++) {
            View itemView = layoutManager.findViewByPosition(i);

            if (itemView != null) {
                float centerOffset = Math.abs(itemView.getTop() + itemView.getHeight() / 2 - recyclerView.getHeight() / 2);

                float scaleFactor = 1 - Math.min(1, (centerOffset / (recyclerView.getHeight() / 2)) * (centerOffset / (recyclerView.getHeight() / 2)));
                float alphaFactor = 1 - Math.min(1, (centerOffset / (recyclerView.getHeight() / 2)) * (centerOffset / (recyclerView.getHeight() / 2)));

                itemView.setScaleX(scaleFactor);
                itemView.setScaleY(scaleFactor);
                itemView.setAlpha(alphaFactor);
            }
        }
    }
}
*/

package com.example.bibliotech.presentacion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import com.example.bibliotech.datos.Book;
import com.example.bibliotech.datos.firestore.BookFireStore;
import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.R;
import com.example.bibliotech.datos.firestore.StaticRvModel;
import com.example.bibliotech.datos.reservaLibro;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class reservasviewnew extends Fragment {

    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newreservasmain, container, false);
        Calendar c = Calendar.getInstance();
        tabLayout = rootView.findViewById(R.id.tabLayout);

        List<reservaLibro> resrvlib = new ArrayList<>();
        reservaLibro.getReservasBook(FireBaseActions.user.getUid(), new reservaLibro.ReservasCallback() {
            @Override
            public void onReservasLoaded(List<reservaLibro> reservaList) {
                resrvlib.addAll(reservaList);
                // Actualizar el RecyclerView con la nueva lista de reservas
                actualizarRecyclerView(resrvlib);
            }

            @Override
            public void onReservasError(String errorMessage) {
                Log.d("reservaLibro getBooks", "Error: " + errorMessage);
            }
        });

        return rootView;
    }

    private void actualizarRecyclerView(List<reservaLibro> reservaList) {
        String mes = obtenerNombreMesAbreviado(Calendar.getInstance().get(Calendar.MONTH));
        String dia = Integer.toString(Calendar.getInstance().get(Calendar.DATE));
        String annio = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        ArrayList<StaticRvModel> libros = new ArrayList<>();

        // Agregar espaciador arriba
        agregarEspaciador(libros);

        for (reservaLibro reservaLibro : reservaList) {
            // Obtener nombre del libro utilizando BookFireStore
            obtenerLibro(reservaLibro.getBookId(), new BookFireStore.bookCallback() {
                @Override
                public void onBookLoaded(Book book) {
                    LocalDate localDate = reservaLibro.getFechaIni().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    libros.add(new StaticRvModel(book.getName(),book.getISBN(), formatearFecha(reservaLibro.getFechaFin()),
                            obtenerNombreMesAbreviado(localDate.getMonthValue() - 1), formatearNumero(localDate.getDayOfMonth()),
                            Integer.toString(localDate.getYear()), false));
                }

                @Override
                public void onBookError(Exception e) {
                    Log.d("BookFireStore", "Error fetching book: " + e.getMessage());
                }

                @Override
                public void onBookDetailsLoaded(Book book) {

                }
            });
        }

        // Agregar espaciador abajo
        agregarEspaciador(libros);

        recyclerView = getView().findViewById(R.id.rv_book);
        staticRvAdapter = new StaticRvAdapter(libros);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(staticRvAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    cambiarContenidoConAnimacion(libros, false);
                } else {
                    cambiarContenidoConAnimacion(libros, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No necesitamos hacer nada aquí
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No necesitamos hacer nada aquí
            }
        });

        // Resto del código para configurar el RecyclerView, los clics, etc.

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        StaticRvModel selectedItem = libros.get(position);

                        // Verificar que el ISBN no sea nulo o vacío
                        String isbn = selectedItem.getBookId();
                        if (isbn != null && !isbn.isEmpty()) {
                            // Usar BookFireStore para obtener los detalles del libro
                            obtenerLibro(isbn, new BookFireStore.bookCallback() {
                                @Override
                                public void onBookLoaded(Book book) {
                                    // Resto del código para crear el Intent con los detalles del libro
                                    Intent intent = new Intent(getActivity(), libroresActivity.class);

                                    // Extras adicionales del libro
                                    intent.putExtra("nombreLibro", selectedItem.getBk_name());
                                    intent.putExtra("fechaDevolucion", selectedItem.getDia_dev());
                                    intent.putExtra("fechaReservadia", selectedItem.getDia_res());
                                    intent.putExtra("fechaReservames", Integer.toString(obtenerNumeroMesDesdeNombre(selectedItem.getMes_res())));
                                    intent.putExtra("fechaReservaano", selectedItem.getAño_res());

                                    // Extras del libro obtenidos de la base de datos
                                    intent.putExtra("author", book.getAuthor());
                                    intent.putExtra("pageNumber", book.getPagenumber());
                                    intent.putExtra("editorial", book.getEditorial());
                                    intent.putExtra("section", book.getSection());
                                    intent.putExtra("sinopsis", book.getSinopsis());

                                    startActivity(intent);
                                }

                                @Override
                                public void onBookError(Exception e) {
                                    // Manejar el error si es necesario
                                    Toast.makeText(getContext(), "Error al obtener detalles del libro", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onBookDetailsLoaded(Book book) {

                                }
                            });
                        } else {
                            // Manejar el caso en el que ISBN es nulo o vacío
                            Toast.makeText(getContext(), "ISBN no válido", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Manejar clic largo si es necesario
                    }
                })
        );


        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ajustarEscalaYOpacidad();
            }
        });
    }

    private void cambiarContenidoConAnimacion(final ArrayList<StaticRvModel> nuevoContenido, final boolean deDerechaAIzquierda) {
        final float translationX = deDerechaAIzquierda ? recyclerView.getWidth() : -recyclerView.getWidth();
        recyclerView.animate()
                .translationX(translationX)
                .setDuration(150)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        staticRvAdapter = new StaticRvAdapter(nuevoContenido);
                        recyclerView.setAdapter(staticRvAdapter);
                        recyclerView.setTranslationX(-translationX);
                        recyclerView.animate()
                                .translationX(0)
                                .setDuration(150)
                                .setListener(null);
                    }
                });
    }

    private void ajustarEscalaYOpacidad() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();

        for (int i = 0; i < totalItemCount; i++) {
            View itemView = layoutManager.findViewByPosition(i);

            if (itemView != null) {
                float centerOffset = Math.abs(itemView.getTop() + itemView.getHeight() / 2 - recyclerView.getHeight() / 2);

                float scaleFactor = 1 - Math.min(1, (centerOffset / (recyclerView.getHeight() / 2)) * (centerOffset / (recyclerView.getHeight() / 2)));
                float alphaFactor = 1 - Math.min(1, (centerOffset / (recyclerView.getHeight() / 2)) * (centerOffset / (recyclerView.getHeight() / 2)));

                itemView.setScaleX(scaleFactor);
                itemView.setScaleY(scaleFactor);
                itemView.setAlpha(alphaFactor);
            }
        }
    }
    private String obtenerNombreMesAbreviado(int numeroMes) {
        String[] nombresMesesAbreviados = {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};
        return nombresMesesAbreviados[numeroMes];
    }


    private void agregarEspaciador(ArrayList<StaticRvModel> lista) {
        lista.add(new StaticRvModel("Espaciador Invisible","1234", "01/01/1970", "ENE", "01", "1970", true));
    }
    private String formatearFecha(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(fecha);
    }

    private String formatearNumero(int numero) {
        return String.format(Locale.getDefault(), "%02d", numero);
    }
    private int obtenerNumeroMesDesdeNombre(String nombreMes) {
        String[] nombresMesesAbreviados = {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};
        for (int i = 0; i < nombresMesesAbreviados.length; i++) {
            if (nombresMesesAbreviados[i].equals(nombreMes)) {
                return i + 1; // Devuelve la posición más uno
            }
        }
        // Si el nombre del mes no se encuentra, devolver -1 o un valor por defecto según tu lógica
        return -1;
    }
    private void obtenerLibro(String bookId, BookFireStore.bookCallback callback) {
        BookFireStore bookFireStore = new BookFireStore();
        bookFireStore.getBook(bookId, callback);
    }
}

