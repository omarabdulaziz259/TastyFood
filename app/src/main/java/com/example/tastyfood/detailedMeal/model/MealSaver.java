package com.example.tastyfood.detailedMeal.model;

public interface MealSaver {
    void calendaredMealSuccess();
    void insertingFavMealSuccess();
    void deleteFavMealSuccess();
    void reverseFavBtn(Boolean doReverse);
    void onFailed(String msg);
}