package com.example.tastyfood.model;

import java.util.List;

public interface MealListViewer {
    void onSuccess(List<Meal> mealList);
    void onFailed();

}
