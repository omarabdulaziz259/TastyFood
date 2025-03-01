package com.example.tastyfood.GlobalSearch.model;

import com.example.tastyfood.model.Meal;

import java.util.List;

public interface GlobalSearchHandler {
    void navigateToDetailedMeal(Meal meal);
    void showSearchedMeals(List<Meal> meals);
    void failed(String msg);
}
