package com.example.tastyfood.model;

import java.util.List;

public interface MealListViewer {
    void showMealsList(List<Meal> mealList);
    void onFailed(String msg);

}
