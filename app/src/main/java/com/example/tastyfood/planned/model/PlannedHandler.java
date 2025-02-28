package com.example.tastyfood.planned.model;

import com.example.tastyfood.model.Meal;

import java.util.List;

public interface PlannedHandler {
    void navigateToDetailedMeal(Meal meal);
    void deleteCalendaredMeal(Meal meal);
    void onSuccess(List<Meal> mealList);
    void onFailed(String msg);
}
