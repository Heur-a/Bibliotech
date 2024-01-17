package com.example.bibliotech.presentacion;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.example.bibliotech.R;
import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.datos.firestore.RoomFireStore;
import com.example.bibliotech.datos.reservaSala;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class salas extends Fragment implements View.OnClickListener {
    private final boolean[] DATE_SET = {false, false, false};
    Button btnDatePicker, btnTimePicker, btnTimePicker2, btnSearch;
    private int mYear, mMonth, mDay, mHour, mMinute, mHour2, mMinute2;
    private RoomFireStore ROOMDB;
    private reservaSala OPERATIONS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filtro_sala, container, false);

        //DEBUG:
        ROOMDB = new RoomFireStore();
        OPERATIONS = new reservaSala();

        // Obtén una referencia al contexto actual
        Context context = getContext();

        // Declara el Spinner y obtén el array de recursos
        Spinner mSpinner = view.findViewById(R.id.spinner_personas);
        Spinner mSpinner2 = view.findViewById(R.id.spinner_plant);

        // Inicializa y configura los elementos de DatePicker
        btnDatePicker = view.findViewById(R.id.btn_date);
        btnDatePicker.setOnClickListener(this);

        // Inicializa y configura los elementos de TimePicker
        btnTimePicker = view.findViewById(R.id.btn_time_desde);
        btnTimePicker2 = view.findViewById(R.id.btn_time_hasta);
        btnTimePicker.setOnClickListener(this);
        btnTimePicker2.setOnClickListener(this);


        //BtnSearch
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(task -> {
            boolean FLAG = true;
            for (boolean b : DATE_SET) {
                if (!b) {
                    FLAG = false;
                    break;
                }
            }
            if (FLAG) {
                anyadirReserva();
            }

        });

        Resources resources = getResources();

        String[] personasArray = resources.getStringArray(R.array.personas);
        String[] plantaArray = resources.getStringArray(R.array.planta);

        // Convierte el array en una lista
        List<String> personasList = Arrays.asList(personasArray);
        List<String> plantaList = Arrays.asList(plantaArray);

        // Crea un adaptador para el Spinner
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<>(context, R.layout.spinner_list, personasList);
        mArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        ArrayAdapter<String> mArrayAdapter2 = new ArrayAdapter<>(context, R.layout.spinner_list, plantaList);
        mArrayAdapter2.setDropDownViewResource(R.layout.spinner_list);

        // Establece el adaptador en el Spinner
        mSpinner.setAdapter(mArrayAdapter);
        mSpinner2.setAdapter(mArrayAdapter2);


        return view;


    }

    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
            DATE_SET[0] = true;
        }
        if (v == btnTimePicker) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            mHour = hourOfDay;
                            mMinute = minute;
                            btnTimePicker.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
            DATE_SET[1] = true;
        }
        if (v == btnTimePicker2) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour2 = c.get(Calendar.HOUR_OF_DAY);
            mMinute2 = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            mHour2 = hourOfDay;
                            mMinute2 = minute;
                            btnTimePicker2.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour2, mMinute2, false);
            timePickerDialog.show();
            DATE_SET[2] = true;
        }
    }


    private void anyadirReserva() {
        // Obtén una instància de Calendar
        Calendar calendarDesde = Calendar.getInstance();
        calendarDesde.set(mYear, mMonth, mDay, mHour, mMinute);

        Calendar calendarHasta = Calendar.getInstance();
        calendarHasta.set(mYear, mMonth, mDay, mHour2, mMinute2);

        // Crea l'objecte reservaSala amb les dates de Calendar
        reservaSala RESERVA = new reservaSala(
                calendarDesde.getTime(),
                calendarHasta.getTime(),
                FireBaseActions.getUserId(),
                "H-010");

        // Afegeix la reserva utilitzant l'objecte RoomFireStore
        ROOMDB.addReserva(RESERVA, "H-010");
    }



}
