package com.example.tastyfood.model.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "CalenderedMeal", primaryKeys = {"idMeal", "date"})
public class CalenderedMeal {
    @NonNull
    private String idMeal;
    @NonNull
    private String date;

    public CalenderedMeal(){

    }
    public CalenderedMeal(@NonNull String idMeal, String date) {
        this.idMeal = idMeal;
        this.date = date;
    }

    @NonNull
    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(@NonNull String idMeal) {
        this.idMeal = idMeal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}