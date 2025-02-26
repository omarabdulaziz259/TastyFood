package com.example.tastyfood.model.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "CalenderedMeal", primaryKeys = {"idMeal", "date"})
public class CalenderedMeal {
    @NonNull
    private String idMeal;
    private int date;

    public CalenderedMeal(@NonNull String idMeal, int date) {
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}