package com.example.bibliotech.presentacion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.example.bibliotech.datos.Room;
import com.example.bibliotech.datos.firestore.BookFireStore;
import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.R;
import com.example.bibliotech.datos.firestore.RoomFireStore;
import com.example.bibliotech.datos.firestore.StaticRvModel;
import com.example.bibliotech.datos.reservaLibro;
import com.example.bibliotech.datos.reservaSala;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class reservasviewnew extends Fragment {

    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;
    private TabLayout tabLayout;

    private List<reservaLibro> reservaLibros = new ArrayList<>();
    private List<reservaSala> reservaSalas = new ArrayList<>();

    private ArrayList<StaticRvModel> libros = new ArrayList<>();
    private ArrayList<StaticRvModel> salas = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newreservasmain, container, false);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        recyclerView = rootView.findViewById(R.id.rv_book);
        staticRvAdapter = new StaticRvAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(staticRvAdapter);

        configurarListeners();
        cargarDatosLibros();  // Cargar datos de libros al iniciar la vista

        cargarDatosSalas();
        return rootView;
    }

    private void configurarListeners() {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        agregarEspaciador(salas, salas.size());
        agregarEspaciador(libros, libros.size());
    }

    private void cargarDatosLibros() {
        reservaLibro.getReservasBook(FireBaseActions.user.getUid(), new reservaLibro.ReservasLibrosCallback() {
            @Override
            public void onReservasLoaded(List<reservaLibro> reservaList) {
                reservaLibros.clear();
                reservaLibros.addAll(reservaList);
                actualizarRecyclerViewLibros();
                agregarEspaciador(libros, libros.size());
            }

            @Override
            public void onReservasError(String errorMessage) {
                Log.d("reservaLibro getBooks", "Error: " + errorMessage);
            }
        });
    }

    private void cargarDatosSalas() {
        reservaSala.getReservaSala(FireBaseActions.user.getUid(), new reservaSala.ReservasSalasCallback() {
            @Override
            public void onReservasLoaded(List<reservaSala> reservaList) {
                reservaSalas.clear();
                reservaSalas.addAll(reservaList);
                actualizarRecyclerViewSalas();
                agregarEspaciador(salas, salas.size());
            }

            @Override
            public void onReservasError(String errorMessage) {
                Log.d("reservaSala getRooms", "Error: " + errorMessage);
            }
        });
    }

    private void actualizarRecyclerViewLibros() {
        libros.clear();
        for (reservaLibro reservaLibro : reservaLibros) {
            obtenerLibro(reservaLibro.getBookId(), new BookFireStore.bookCallback() {
                @Override
                public void onBookLoaded(Book book) {
                    LocalDate localDate = reservaLibro.getFechaIni().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    libros.add(new StaticRvModel(book.getName(), book.getISBN(), formatearFecha(reservaLibro.getFechaFin()),
                            obtenerNombreMesAbreviado(localDate.getMonthValue() - 1), formatearNumero(localDate.getDayOfMonth()),
                            Integer.toString(localDate.getYear()), false));
                    actualizarAdapter(libros);  // Actualizar el adapter después de agregar datos
                }

                @Override
                public void onBookError(Exception e) {
                    Log.d("BookFireStore", "Error fetching book: " + e.getMessage());
                }

                @Override
                public void onBookDetailsLoaded(Book book) {
                    // Puedes realizar acciones específicas si es necesario
                }
            });
        }

        actualizarAdapter(libros);
    }

    private void actualizarRecyclerViewSalas() {
        salas.clear();
        for (reservaSala reservaSala : reservaSalas) {
            obtenerSala(reservaSala.getRoomId(), new RoomFireStore.RoomCallback() {
                @Override
                public void onRoomLoaded(Room room) {
                    LocalDate localDate = reservaSala.getFechaIni().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    salas.add(new StaticRvModel(
                            room.getNombreSala(),
                            reservaSala.getRoomId(),
                            formatearFecha(reservaSala.getFechaFin()),
                            obtenerNombreMesAbreviado(localDate.getMonthValue() - 1),
                            formatearNumero(localDate.getDayOfMonth()),
                            Integer.toString(localDate.getYear()),
                            false
                    ));
                }

                @Override
                public void onRoomError(Exception e) {
                    Log.d("RoomFireStore", "Error fetching room: " + e.getMessage());
                }
            });
        }

        actualizarAdapter(salas);
    }

    private void actualizarAdapter(ArrayList<StaticRvModel> data) {
        recyclerView.setAdapter(new StaticRvAdapter(data));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        StaticRvModel selectedItem = data.get(position);
                        if (tabLayout.getSelectedTabPosition() == 0) {
                            // Tratar la selección de libro
                            tratarSeleccionLibro(selectedItem);
                        } else if(tabLayout.getSelectedTabPosition() == 1) {
                            // Tratar la selección de sala
                            tratarSeleccionSala(selectedItem);
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

    private void tratarSeleccionLibro(StaticRvModel selectedItem) {
        String isbn = selectedItem.getBookId();
        if (isbn != null && !isbn.isEmpty()) {
            obtenerLibro(isbn, new BookFireStore.bookCallback() {
                @Override
                public void onBookLoaded(Book book) {
                    mostrarDetallesLibro(selectedItem, book);
                }

                @Override
                public void onBookError(Exception e) {
                    Toast.makeText(getContext(), "Error al obtener detalles del libro", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBookDetailsLoaded(Book book) {
                    // Puedes realizar acciones específicas si es necesario
                }
            });
        } else {
            Toast.makeText(getContext(), "ISBN no válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void tratarSeleccionSala(StaticRvModel selectedItem) {
        reservaSala currentReservaSala = findReservaSalaByNombreSala(selectedItem.getBookId(), reservaSalas);
        if (currentReservaSala != null) {
            obtenerSala(currentReservaSala.getRoomId(), new RoomFireStore.RoomCallback() {
                @Override
                public void onRoomLoaded(Room room) {
                    mostrarDetallesSala(selectedItem, currentReservaSala, room);
                }

                @Override
                public void onRoomError(Exception e) {
                    Toast.makeText(getContext(), "Error al obtener detalles de la sala", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void mostrarDetallesLibro(StaticRvModel selectedItem, Book book) {
        Intent intent = new Intent(getActivity(), libroresActivity.class);
        String autor = book.getAuthor();

        intent.putExtra("nombreLibro", selectedItem.getBk_name());
        intent.putExtra("fechaDevolucion", selectedItem.getDia_dev());
        intent.putExtra("fechaReservadia", selectedItem.getDia_res());
        intent.putExtra("fechaReservames", selectedItem.getMes_res());
        intent.putExtra("fechaReservaano", selectedItem.getAño_res());

        intent.putExtra("author", autor);
        intent.putExtra("pageNumber", book.getPagenumber());
        intent.putExtra("isbn", book.getISBN());

        startActivity(intent);
    }

    private void mostrarDetallesSala(StaticRvModel selectedItem, reservaSala currentReservaSala, Room room) {
        Intent intent = new Intent(getActivity(), reservasalaactivity.class);
        LocalDate localDate = currentReservaSala.getFechaIni().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        intent.putExtra("nombreSalas", room.getNombreSala());
        intent.putExtra("fechaDevolucion", selectedItem.getDia_dev());
        intent.putExtra("fechaReservadia", selectedItem.getDia_res());
        intent.putExtra("fechaReservames", selectedItem.getMes_res());
        intent.putExtra("fechaReservaano", Integer.toString(localDate.getYear()));
        intent.putExtra("number", room.getNumberpeople());

        startActivity(intent);
    }

    private void cambiarContenidoConAnimacion(final ArrayList<StaticRvModel> nuevoContenido, final boolean deDerechaAIzquierda) {
        final float translationX = deDerechaAIzquierda ? recyclerView.getWidth() : -recyclerView.getWidth();
        recyclerView.animate()
                .translationX(translationX)
                .setDuration(150)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        actualizarAdapter(nuevoContenido);
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

    private void agregarEspaciador(ArrayList<StaticRvModel> lista, int pos) {
        lista.add(pos, new StaticRvModel("Espaciador Invisible", "1234", "01/01/1970", "ENE", "01", "1970", true));
    }

    private String formatearFecha(Date fecha) {
        if (fecha != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.format(fecha);
        } else {
            return "Fecha no disponible";
        }
    }

    private String formatearNumero(int numero) {
        return String.format(Locale.getDefault(), "%02d", numero);
    }

    private void obtenerLibro(String bookId, BookFireStore.bookCallback callback) {
        BookFireStore bookFireStore = new BookFireStore();
        bookFireStore.getBook(bookId, callback);
    }

    private void obtenerSala(String salaId, RoomFireStore.RoomCallback callback) {
        RoomFireStore roomFireStore = new RoomFireStore();
        roomFireStore.getRoom(salaId, callback);
    }

    private reservaSala findReservaSalaByNombreSala(String nombreSala, List<reservaSala> reservaList) {
        for (reservaSala reserva : reservaList) {
            if (reserva.getRoomId().equals(nombreSala)) {
                return reserva;
            }
        }
        return null;
    }
}
