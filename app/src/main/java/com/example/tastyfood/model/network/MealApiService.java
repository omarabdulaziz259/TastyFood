package com.example.tastyfood.model.network;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {
    @GET("search.php")
    Single<MealResponse> getMealsByFirstLetter(@Query("f") char firstLetter);
    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") String mealId);
    @GET("random.php")
    Single<MealResponse> getRandomMeal();
    @GET("categories.php")
    Single<CategoryResponse> getCategories();
    @GET("filter.php")
    Single<MealResponse> getMealsFilteredByIngredient(@Query("i") String ingredient);
    @GET("filter.php")
    Single<MealResponse> getMealsFilteredByCategory(@Query("c") String strCategory);
    @GET("filter.php")
    Single<MealResponse> getMealsFilteredByCountry(@Query("a") String strArea);
    @GET("list.php?c=list")
    Single<MealResponse> getListOfCategories();
    @GET("list.php?a=list")
    Single<MealResponse> getListOfCountries();
    @GET("list.php?i=list")
    Single<MealResponse> getListOfIngredients();
}