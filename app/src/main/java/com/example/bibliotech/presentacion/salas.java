package com.example.bibliotech.presentacion;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.bibliotech.R;
import com.example.bibliotech.datos.Room;
import com.example.bibliotech.datos.TimeOperations;
import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.datos.firestore.RoomFireStore;
import com.example.bibliotech.datos.reservaSala;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class salas extends Fragment implements View.OnClickListener {
    private final boolean[] DATE_SET = {false, false, false,false,false};
    Button btnDatePicker,  btnSearch;
    Spinner btnTimePicker, btnTimePicker2;
    private int mYear, mMonth, mDay, mHour, mMinute, mHour2, mMinute2;
    private RoomFireStore ROOMDB;
    private Set<android.icu.util.Calendar[]> calendarIniSet;
    private Map<Room, Set<android.icu.util.Calendar[]>> roomHours;
    private Map<Room, List<String>> roomsAviableHours;

    private Map<Room,List<String[]>> roomOccupiedHoursString;

    private final LocalTime horaINI = LocalTime.of(8,0);
    private final LocalTime horaFIN = LocalTime.of(22,0);

    private enum DATE_SETTING {
        FIRST_HOUR_SET(0),
        LAST_HOUR_SET(1),
        YEAR_MONTH_DATE_SET(2),
        PEOPLE_SET(3),
        FLOOR_SET(4);

        private final int value;

        DATE_SETTING(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filtro_sala, container, false);

        //DEBUG:
        ROOMDB = new RoomFireStore();
        reservaSala OPERATIONS = new reservaSala();

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
        btnTimePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Obtenir la hora i els minuts seleccionats
                String selectedTime = adapterView.getItemAtPosition(i).toString();
                String[] timeParts = selectedTime.split(":");

                // Emmagatzemar la hora i els minuts com a enters
                mHour = Integer.parseInt(timeParts[0]);
                mMinute = Integer.parseInt(timeParts[1]);

                TimeOperations.setSpinnerDataForAvailableHours(roomOccupiedHoursString,"H-010",mHour,mMinute,btnTimePicker2,30,horaFIN);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnTimePicker2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedTime = adapterView.getItemAtPosition(i).toString();
                String[] timeParts = selectedTime.split(":");
                Toast.makeText(context, "HOLA", Toast.LENGTH_SHORT).show();

                // Emmagatzemar la hora i els minuts com a enters
                mHour2 = Integer.parseInt(timeParts[0]);
                mMinute2 = Integer.parseInt(timeParts[1]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //BtnSearch
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(task -> {
           /* boolean FLAG = true;
            //MIRA SI SE HAN USADO TODOS LOS BOTONES
            for (boolean b : DATE_SET) {
                if (!b) {
                    FLAG = false;
                    break;
                }
            }*/
            /*if (FLAG) {*/
                anyadirReserva();
//            }

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


        //DEBUG
        ROOMDB.getReservaRooms(new RoomFireStore.RoomReserveMap() {
            @Override
            public void onRoomReserveMapLoaded(Map<Room, List<reservaSala>> roomsRsereva) {
                Log.d("RESERVA_MAP",roomsRsereva.toString());
            }

            @Override
            public void onRoomsReserveError(Exception e) {
                Log.d("RESERVA_MAP",e.getMessage());
            }
        });

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
                        public void onDateSet(DatePicker view,
                                              int year,
                                              int monthOfYear,
                                              int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            DATE_SET[DATE_SETTING.YEAR_MONTH_DATE_SET.getValue()] = true;
                            ROOMDB.getReservaRooms(new RoomFireStore.RoomReserveMap() {
                                @Override
                                public void onRoomReserveMapLoaded(Map<Room, List<reservaSala>> roomsReserva) {
                                    roomHours = new HashMap<Room, Set<android.icu.util.Calendar[]>>();
                                    roomsReserva.forEach((room, reservaSalas) -> {
                                        calendarIniSet = new HashSet<android.icu.util.Calendar[]>();
                                        for (reservaSala reservaSala : reservaSalas) {
                                            android.icu.util.Calendar calIni = android.icu.util.Calendar.getInstance();
                                            android.icu.util.Calendar calEnd = android.icu.util.Calendar.getInstance();
                                            calIni.setTime(reservaSala.getFechaIni());
                                            calEnd.setTime(reservaSala.getFechaFin());

                                            // Obté els camps de l'any, mes i dia
                                            int yearCal = calIni.get(Calendar.YEAR);
                                            int monthCal = calIni.get(Calendar.MONTH);
                                            int dayCal = calIni.get(Calendar.DAY_OF_MONTH);

                                            // Afegix a l'HashSet si es el mateix dia
                                            if (yearCal == year && monthCal == monthOfYear && dayCal == dayOfMonth) {
                                                android.icu.util.Calendar[] calendarPair = {calIni, calEnd};
                                                calendarIniSet.add(calendarPair);
                                            }
                                        }
                                            roomHours.put(room, calendarIniSet);


                                    });
                                    Log.d("ROOMRESERVASALA", roomHours.toString());
                                    Log.d("XDDD", TimeOperations.generateAvailableTimes(roomHours,horaINI,horaFIN,30).toString());
                                    roomsAviableHours = TimeOperations.generateAvailableTimes(roomHours,horaINI,horaFIN,30);
                                    TimeOperations.setSpinnerDataForRoom(roomsAviableHours,"H-010",btnTimePicker);
                                    roomOccupiedHoursString = TimeOperations.convertToTimeString(roomHours);


                                }

                                @Override
                                public void onRoomsReserveError(Exception e) {
                                    Toast.makeText(getContext(), "Something happened with our database", Toast.LENGTH_SHORT).show();
                                    Log.d("GETROOMSRESERVA", "onRoomsReserveError: " + e.getMessage());
                                }
                            });
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
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
        ROOMDB.addReserva(RESERVA, "H-010", new RoomFireStore.onReservaAdded() {
            @Override
            public void onSuccesListener(reservaSala reservaSala) {
                RESERVA.anyadirAUser(FireBaseActions.getUserId());
            }

            @Override
            public void onFailureListener(Exception e) {
                //NOTHING
            }
        });


    }



}
