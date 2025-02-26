package com.example.tastyfood.model.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.tastyfood.model.Meal;

@Database(entities = {Meal.class, FavMeal.class, CalenderedMeal.class}, version = 1)
@TypeConverters(Converters.class) // Register the converter here
public abstract class MealDatabase extends RoomDatabase {
    private static volatile MealDatabase INSTANCE;

    public abstract MealDao getMealDao();
    public static MealDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MealDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                                    MealDatabase.class, "mealDatabase")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}