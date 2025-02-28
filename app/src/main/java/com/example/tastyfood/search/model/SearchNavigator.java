package com.example.tastyfood.search.model;

import com.example.tastyfood.model.Meal;

public interface SearchNavigator {
    void getFilteredMealsByCountry(String country);
    void getFilteredMealsByCategory(String category);
    void getFilteredMealsByIngredients(String ingredient);
    void navigateToDetailedMeal(Meal meal);
}
