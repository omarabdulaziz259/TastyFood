package com.example.tastyfood.favourite;

import com.example.tastyfood.model.Meal;

import java.util.List;

public interface FavouriteHandler {
    void navigateToDetailedMeal(Meal meal);
    void onSuccessFetchingDB(List<Meal> mealList);
    void onFailed();
}
