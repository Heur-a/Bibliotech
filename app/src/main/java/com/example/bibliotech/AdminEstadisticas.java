package com.example.bibliotech;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminEstadisticas extends Fragment {
    private Map<Float, String> valueToNameMap;
    int[] progressBarIds = {R.id.progressBar1, R.id.progressBar2, R.id.progressBar3, R.id.progressBar4};
    int[] porcIds = {R.id.porc1, R.id.porc2, R.id.porc3, R.id.porc4};

    int[] progressValues = new int[4];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.estadisticaspage, container, false);

        for (int i = 0; i < 4; i++) {
            ProgressBar progressBar = rootView.findViewById(progressBarIds[i]);
            TextView porcTextView = rootView.findViewById(porcIds[i]);

            progressValues[i] = randfun();

            progressBar.setProgress(progressValues[i]);
            porcTextView.setText(progressValues[i] + "%");
        }




        // Inicializa el Map con los nombres correspondientes a los valores
        valueToNameMap = new HashMap<>();
        valueToNameMap.put(1f, "libros");
        valueToNameMap.put(2f, "salas");
        valueToNameMap.put(3f, "personas");
        valueToNameMap.put(4f, "préstamos");

        PieChart pieChart = rootView.findViewById(R.id.PieChart);

        ArrayList<PieEntry> visitors = new ArrayList<>();
        visitors.add(new PieEntry(500, 1f));
        visitors.add(new PieEntry(100, 2f));
        visitors.add(new PieEntry(400, 3f));
        visitors.add(new PieEntry(600, 4f));

        PieDataSet pieDataSet = new PieDataSet(visitors, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setCenterText("Libros Más Resrvados");
        pieChart.setCenterTextColor(Color.BLUE);
        pieChart.animate();

        // Establece un listener para clics en las porciones del PieChart
        // Establece un listener para clics en las porciones del PieChart
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry selectedEntry = (PieEntry) e;

                // Obtiene el identificador personalizado asociado al Entry
                Float selectedIdentifier = (Float) selectedEntry.getData();

                // Muestra el valor numérico y un texto personalizado en el centro del PieChart
                String centerText = String.format("%.0f\n%s", selectedEntry.getValue(), getSelectedText(selectedIdentifier));
                pieChart.setCenterText(centerText);
                pieChart.invalidate();

                // Muestra un texto correspondiente al valor seleccionado en el TextView exterior
                String outsideText = getSelectedText(selectedIdentifier);
                pieChart.setCenterText(outsideText);
            }

            @Override
            public void onNothingSelected() {
                // Maneja cuando no se selecciona nada (opcional)
            }
        });
        return rootView;
    }

    private String getSelectedText(float value) {
        return valueToNameMap.getOrDefault(value, "Nombre no encontrado");
    }

    //funcion que genera valores randoms para poder probar el funcionamiento de las estadisitcas
    //CAMBIAR EN PODER!!!!!!!!!!!!!!!!!!!!!
    private int randfun(){
        return  (int)Math.floor(Math.random()*100);
    }

}
