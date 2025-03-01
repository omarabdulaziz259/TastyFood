package com.example.tastyfood.search.model;

import com.example.tastyfood.model.Meal;

import java.util.List;

public interface SearchViewer {
    void showCountries(List<Meal> meals);
    void showCategories(List<Meal> meals);
    void showIngredients(List<Meal> meals);
    void showFilteredMeals(List<Meal> meals);
    void failed(String msg);

}