package com.example.bibliotech;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

public class AdminActivity extends AppCompatActivity {
    private Map<Float, String> valueToNameMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estadisticaspage);

        // Inicializa el Map con los nombres correspondientes a los valores
        valueToNameMap = new HashMap<>();
        valueToNameMap.put(1f, "asd");
        valueToNameMap.put(2f, "asdasd");
        valueToNameMap.put(3f, "asdassadasdasddasd");
        valueToNameMap.put(4f, "asdasdasd");

        PieChart pieChart = findViewById(R.id.PieChart);

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
    }

    private String getSelectedText(float value) {
        return valueToNameMap.getOrDefault(value, "Nombre no encontrado");
    }
}
