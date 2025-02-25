package com.example.tastyfood.home;

import com.example.tastyfood.model.Meal;

public interface SingleMealViewer {
    void showSingleMeal(Meal meal);
    void showError(String msg);
}
