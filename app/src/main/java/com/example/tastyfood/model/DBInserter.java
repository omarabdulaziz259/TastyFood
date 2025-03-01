package com.example.tastyfood.model;

import com.example.tastyfood.model.database.CalenderedMeal;
import com.example.tastyfood.model.database.FavMeal;

import java.util.List;

public interface DBInserter {
    void insertCalendarMeals(List<CalenderedMeal> calenderedMeals);
    void insertFavMeals(List<FavMeal> favMeals);
    void onFailure(String msg);

}
