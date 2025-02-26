package com.example.tastyfood.model.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FavMeal")
public class FavMeal {
    @PrimaryKey
    @NonNull
    private String idMeal;

    public FavMeal(@NonNull String idMeal) {
        this.idMeal = idMeal;
    }

    public String getIdMeal() {
        return idMeal;
    }
}