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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class reservasviewnew extends Fragment {

    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;
    private TabLayout tabLayout;
    private List<reservaLibro> resrvlib = new ArrayList<>();
    private ArrayList<StaticRvModel> libros = new ArrayList<>();

    private List<reservaSala> resrvsal = new ArrayList<>();
    private ArrayList<StaticRvModel> salas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newreservasmain, container, false);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        recyclerView = rootView.findViewById(R.id.rv_book);
        staticRvAdapter = new StaticRvAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(staticRvAdapter);
        cargarDatosLibros();
        cargarDatosSalas();
        cambiarContenidoConAnimacion(libros, false);
        return rootView;
    }

    private void configurarListeners() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    // Mostrar libros
                    cargarDatosLibros();
                } else {
                    // Mostrar salas
                    cargarDatosSalas();
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
        // Cargar datos al volver a la pantalla (puede que desees hacerlo aquí también)
        cargarDatosLibros();
        agregarEspaciador(salas, salas.size());
        agregarEspaciador(libros, libros.size());
    }

    private void cargarDatosLibros() {
        resrvlib.clear();
        reservaLibro.getReservasBook(FireBaseActions.user.getUid(), new reservaLibro.ReservasLibrosCallback() {
            @Override
            public void onReservasLoaded(List<reservaLibro> reservaList) {
                resrvlib.addAll(reservaList);
                actualizarRecyclerViewLibros(resrvlib);
                agregarEspaciador(libros, libros.size());
            }

            @Override
            public void onReservasError(String errorMessage) {
                Log.d("reservaLibro getBooks", "Error: " + errorMessage);
                // Puedes agregar lógica aquí para manejar el error, si es necesario
            }
        });
    }

    private void cargarDatosSalas() {
        resrvsal.clear();
        reservaSala.getReservaSala(FireBaseActions.user.getUid(), new reservaSala.ReservasSalasCallback() {
            @Override
            public void onReservasLoaded(List<reservaSala> reservaList) {
                resrvsal.addAll(reservaList);
                actualizarRecyclerViewSalas(resrvsal);
                agregarEspaciador(salas, salas.size());
            }

            @Override
            public void onReservasError(String errorMessage) {
                Log.d("reservaSala getRooms", "Error: " + errorMessage);
                // Puedes agregar lógica aquí para manejar el error, si es necesario
            }
        });
    }

    private void actualizarRecyclerViewLibros(List<reservaLibro> reservaList) {
        String mes = obtenerNombreMesAbreviado(Calendar.getInstance().get(Calendar.MONTH));
        String dia = Integer.toString(Calendar.getInstance().get(Calendar.DATE));
        String annio = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

        for (reservaLibro reservaLibro : reservaList) {
            // Obtener nombre del libro utilizando BookFireStore
            obtenerLibro(reservaLibro.getBookId(), new BookFireStore.bookCallback() {
                @Override
                public void onBookLoaded(Book book) {
                    LocalDate localDate = reservaLibro.getFechaIni().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    libros.add(new StaticRvModel(book.getName(), book.getISBN(), formatearFechaa(reservaLibro.getFechaFin()),
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
                    cambiarContenidoConAnimacion(salas, false);
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

                        String isbn = selectedItem.getBookId();
                        if (isbn != null && !isbn.isEmpty()) {
                            obtenerLibro(isbn, new BookFireStore.bookCallback() {
                                @Override
                                public void onBookLoaded(Book book) {
                                    Intent intent = new Intent(getActivity(), libroresActivity.class);

                                    intent.putExtra("nombreLibro", selectedItem.getBk_name());
                                    intent.putExtra("fechaDevolucion", selectedItem.getDia_dev());
                                    intent.putExtra("fechaReservadia", selectedItem.getDia_res());
                                    intent.putExtra("fechaReservames", Integer.toString(obtenerNumeroMesDesdeNombre(selectedItem.getMes_res())));
                                    intent.putExtra("fechaReservaano", selectedItem.getAño_res());

                                    intent.putExtra("author", book.getAuthor());
                                    intent.putExtra("pageNumber", book.getPagenumber());
                                    intent.putExtra("isbn", book.getISBN());

                                    startActivity(intent);
                                }

                                @Override
                                public void onBookError(Exception e) {
                                    Toast.makeText(getContext(), "Error al obtener detalles del libro", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onBookDetailsLoaded(Book book) {

                                }
                            });
                        } else {
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

    private void actualizarRecyclerViewSalas(List<reservaSala> reservaList) {
        for (reservaSala reservaSala : reservaList) {
            // Obtener nombre de la sala utilizando RoomFireStore
            obtenerSala(reservaSala.getRoomId(), new RoomFireStore.RoomCallback() {
                @Override
                public void onRoomLoaded(Room room) {
                    if (room != null) {
                        // Use the provided reservaSala instance
                        Date fechaIni = reservaSala.getFechaIni();
                        Date fechaFin = reservaSala.getFechaFin();

                        if (fechaIni != null && fechaFin != null) {
                            LocalDate localDateInicio = fechaIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            LocalDate localDateFin = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                            salas.add(new StaticRvModel(
                                    room.getNombreSala(),
                                    "Room ID: " + room.getNombreSala(),
                                    formatearFechab(localDateFin),
                                    obtenerNombreMesAbreviado(localDateFin.getMonthValue() - 1),
                                    formatearNumero(localDateFin.getDayOfMonth()),
                                    Integer.toString(localDateFin.getYear()),
                                    false
                            ));
                        } else {
                            // Manejar el caso en que fechaIni o fechaFin es null
                            Log.d("ReservaSala", "fechaIni or fechaFin is null");
                        }
                    } else {
                        // Manejar el caso en que room es null
                        Log.d("ReservaSala", "room es null");
                    }
                }

                @Override
                public void onRoomError(Exception e) {
                    Log.d("RoomFireStore", "Error fetching room: " + e.getMessage());
                }
            });
        }

        recyclerView = getView().findViewById(R.id.rv_book);
        staticRvAdapter = new StaticRvAdapter(salas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(staticRvAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        StaticRvModel selectedItem = salas.get(position);

                        // Find the corresponding reservaSala instance
                        reservaSala currentReservaSala = findReservaSalaById(selectedItem.getBookId(), reservaList);

                        if (currentReservaSala != null) {
                            Date fechaIni = currentReservaSala.getFechaIni();
                            Date fechaFin = currentReservaSala.getFechaFin();

                            if (fechaIni != null && fechaFin != null) {
                                LocalDate localDateInicio = fechaIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                LocalDate localDateFin = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                                obtenerSala(currentReservaSala.getRoomId(), new RoomFireStore.RoomCallback() {
                                    @Override
                                    public void onRoomLoaded(Room room) {
                                        if (room != null) {
                                            Intent intent = new Intent(getActivity(), reservasalaactivity.class);

                                            // Pass room data instead of book data
                                            intent.putExtra("nombreSalas", room.getNombreSala());
                                            intent.putExtra("fechaDevolucion", formatearFechab(localDateFin));
                                            intent.putExtra("fechaReservadia", formatearNumero(localDateInicio.getDayOfMonth()));
                                            intent.putExtra("fechaReservames", obtenerNombreMesAbreviado(localDateInicio.getMonthValue() - 1));
                                            intent.putExtra("fechaReservaano", Integer.toString(localDateInicio.getYear()));
                                            intent.putExtra("number", room.getNumberpeople());

                                            startActivity(intent);
                                        } else {
                                            // Handle the case where room is null
                                            Log.d("ReservaSala", "room es null");
                                        }
                                    }

                                    @Override
                                    public void onRoomError(Exception e) {
                                        Toast.makeText(getContext(), "Error al obtener detalles de la sala", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // Handle the case where fechaIni or fechaFin is null
                                Log.d("ReservaSala", "fechaIni or fechaFin is null");
                            }
                        } else {
                            // Handle the case where currentReservaSala is null
                            Log.d("ReservaSala", "currentReservaSala is null");
                        }
                    }


                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Manejar clic largo si es necesario
                    }
                })
        );
    }

    // Helper method to find reservaSala by ID
    private reservaSala findReservaSalaById(String roomId, List<reservaSala> reservaList) {
        for (reservaSala reservaSala : reservaList) {
            if (reservaSala.getRoomId().equals(roomId)) {
                return reservaSala;
            }
        }
        return null;
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


        private void agregarEspaciador(ArrayList<StaticRvModel> lista, int pos) {
            lista.add(pos,new StaticRvModel("Espaciador Invisible", "1234", "01/01/1970", "ENE", "01", "1970", true));
        }

     private String formatearFechaa(Date fecha) {
         if (fecha != null) {
             SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
             return sdf.format(fecha);
         } else {
             // Manejar el caso en que fecha sea nulo
             return "Fecha no disponible";
         }
     }

    private String formatearFechab(LocalDate localDate) {
        if (localDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
            return localDate.format(formatter);
        } else {
            // Handle the case where localDate is null
            return "Fecha no disponible";
        }
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
         private void obtenerSala(String salaid, RoomFireStore.RoomCallback callback) {
             RoomFireStore roomFireStore = new RoomFireStore();
             roomFireStore.getRoom(salaid, callback);
         }
    }
