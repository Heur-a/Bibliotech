package com.example.bibliotech.datos;

import android.icu.util.Calendar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TimeOperations {

    // This method converts entry and exit time pairs for each room into a map of room to a list of formatted time strings
    public static Map<Room, List<String[]>> convertToTimeString(Map<Room, Set<Calendar[]>> hoursMap) {
        Map<Room, List<String[]>> roomsTimeStringMap = new HashMap<>();

        // Check if the input map is not null
        if (hoursMap != null) {
            // Iterate through each room and its set of entry and exit time pairs
            for (Map.Entry<Room, Set<Calendar[]>> entry : hoursMap.entrySet()) {
                Room room = entry.getKey();
                Set<Calendar[]> timePairs = entry.getValue();

                // Convert entry and exit time pairs to formatted time string list
                List<String[]> timeStringList = convertCalendarSetsToStringList(timePairs);

                // Add the result to the map only if there is at least one valid time pair
                    roomsTimeStringMap.put(room, timeStringList);

            }
        }

        return roomsTimeStringMap;
    }

    // This method converts entry and exit time pairs into a list of formatted time strings
    private static List<String[]> convertCalendarSetsToStringList(Set<Calendar[]> timePairs) {
        List<String[]> timeStringList = new ArrayList<>();

        // Format for displaying time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // Iterate through each pair of entry and exit time sets
        for (Calendar[] timePair : timePairs) {
            // Check if the time pair has both entry and exit times
            if (timePair != null && timePair.length == 2) {
                // Convert entry and exit time pairs to formatted time string array
                String[] timeStringArray = {
                        timeFormat.format(timePair[0].getTime()),
                        timeFormat.format(timePair[1].getTime())
                };
                // Add the time string array to the list
                timeStringList.add(timeStringArray);
            }
        }

        return timeStringList;
    }

    public static List<String> generateTimes(LocalTime startTime, LocalTime endTime, int minuteIncrement, List<String[]> excludedIntervals) {
        List<String> generatedTimes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Iterate over all times between the start and end with the specified minute increment
        while (startTime.isBefore(endTime) || startTime.equals(endTime)) {
            String currentFormattedTime = startTime.format(formatter);

            // Check if the current time is within any excluded interval
            if (!isWithinInterval(currentFormattedTime, excludedIntervals)) {
                generatedTimes.add(currentFormattedTime);
            }

            // Increment the current time with the specified minute increment
            startTime = startTime.plusMinutes(minuteIncrement);
        }

        return generatedTimes;
    }

    private static boolean isWithinInterval(String time, List<String[]> intervals) {
        LocalTime currentTime = LocalTime.parse(time);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        if(intervals.isEmpty() || intervals == null){
            return false;
        }

        for (String[] interval : intervals) {
            LocalTime intervalStartTime = LocalTime.parse(interval[0], formatter);
            LocalTime intervalEndTime = LocalTime.parse(interval[1], formatter);

            if ((currentTime.isAfter(intervalStartTime) || currentTime.equals(intervalStartTime))
                    && currentTime.isBefore(intervalEndTime))
            {
                return true; // It is within the interval
            }
        }

        return false; // It is not within any interval
    }

    public static Map<Room, List<String>> generateAvailableTimes(Map<Room, Set<Calendar[]>> hoursMap, LocalTime openingHour, LocalTime exitHour, int minuteInterval) {
        //Initialize return value
        Map<Room, List<String>> hoursAviableRooms = new HashMap<>();
        //Get occupied hours
        Map<Room, List<String[]>> hoursOccupiedRooms = convertToTimeString(hoursMap);
        if (!hoursOccupiedRooms.isEmpty()) {
                hoursOccupiedRooms.forEach((room, strings) -> {
                    List<String> hours = generateTimes(openingHour,exitHour,minuteInterval,strings);
                    hoursAviableRooms.put(room,hours);
                });
        }


        return hoursAviableRooms;
    }
    // This method sets the spinner data based on the selected room name
    public static void setSpinnerDataForRoom(Map<Room, List<String>> roomDataMap, String selectedRoomName, Spinner spinner) {
        // Check if the roomDataMap is not null
        if (roomDataMap != null) {
            // Iterate through each room in the map
            for (Map.Entry<Room, List<String>> entry : roomDataMap.entrySet()) {
                Room room = entry.getKey();

                // Check if the room name matches the selectedRoomName
                if (room.getNombreSala().equals(selectedRoomName)) {
                    // Get the list of strings for the selected room
                    List<String> roomData = entry.getValue();

                    // Check if the roomData is not null and not empty
                    if (roomData != null && !roomData.isEmpty()) {
                        // Create an ArrayAdapter using the roomData and set it to the spinner
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, roomData);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(spinnerAdapter);
                        return; // Exit the loop once the room is found
                    }
                }
            }
        }

        // If roomDataMap is null or the room is not found, set an empty adapter to clear the spinner
        spinner.setAdapter(new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, new ArrayList<>()));
    }

    public static void setSpinnerDataForAvailableHours(Map<Room, List<String[]>> occupiedHoursMap, String selectedRoomName, int startHour, int startMinute, Spinner spinner, int minuteInterval, LocalTime endTime) {
        // Get the occupied hours list for the selected room
        List<String[]> occupiedHoursList = getOccupiedHoursList(occupiedHoursMap, selectedRoomName);

        // Get the start time as LocalTime
        LocalTime startTime = LocalTime.of(startHour, startMinute);

        // Generate available hours until the very first occupied hour for the selected room or the specified end time
        List<String> availableHours = generateAvailableHours(startTime, occupiedHoursList, minuteInterval, endTime);

        // Create an ArrayAdapter using the availableHours and set it to the spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, availableHours);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    // Get the occupied hours list for the selected room
    private static List<String[]> getOccupiedHoursList(Map<Room, List<String[]>> occupiedHoursMap, String selectedRoomName) {
        for (Room room : occupiedHoursMap.keySet()) {
            if (room.getNombreSala().equals(selectedRoomName)) {
                return occupiedHoursMap.get(room);
            }
        }
        return new ArrayList<>();
    }

    // Generate available hours until the very first occupied hour or specified end time for the selected room
    private static List<String> generateAvailableHours(LocalTime startTime, List<String[]> occupiedHoursList, int minuteInterval, LocalTime endTime) {
        List<String> availableHours = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Check if the list of occupied hours is null or empty
        if (occupiedHoursList.isEmpty()) {
            // Iterate over all times between the start and the specified end time with the specified minute increment
            while (startTime.isBefore(endTime)) {
                String currentFormattedTime = startTime.format(formatter);

                // Add the current hour to the availableHours list
                availableHours.add(currentFormattedTime);

                // Increment the current time with the specified minute increment
                startTime = startTime.plusMinutes(minuteInterval);
            }
        } else {
            // Iterate over all times between the start and the very first occupied hour with the specified minute increment
            while (!occupiedHoursList.isEmpty() && startTime.isBefore(LocalTime.parse(occupiedHoursList.get(0)[0], formatter))) {
                String currentFormattedTime = startTime.format(formatter);

                // Add the current hour to the availableHours list
                availableHours.add(currentFormattedTime);

                // Increment the current time with the specified minute increment
                startTime = startTime.plusMinutes(minuteInterval);
            }
        }

        return availableHours;
    }










}
