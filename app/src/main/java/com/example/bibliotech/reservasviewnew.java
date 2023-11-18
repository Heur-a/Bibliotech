package com.example.bibliotech;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.Calendar;

public class reservasviewnew extends Fragment {
    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newreservasmain, container, false);
        Calendar c = Calendar.getInstance();
        tabLayout = rootView.findViewById(R.id.tabLayout);
        String dia = Integer.toString(c.get(Calendar.DATE));

        String mes = obtenerNombreMesAbreviado(c.get(Calendar.MONTH));

        String annio = Integer.toString(c.get(Calendar.YEAR));
        ArrayList<StaticRvModel> libros = new ArrayList<>();
        ArrayList<StaticRvModel> salas = new ArrayList<>();

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
