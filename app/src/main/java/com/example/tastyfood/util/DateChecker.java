package com.example.tastyfood.util;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateChecker {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTodaysDate(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
        String formattedDate = today.format(formatter);
        return formattedDate;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isDateInPast(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
        try {
            LocalDate date = LocalDate.parse(dateString, formatter.withLocale(Locale.ENGLISH));
            LocalDate today = LocalDate.now();
            Log.i("TAG", "isDateInPast: " + today);
            return date.isBefore(today);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}