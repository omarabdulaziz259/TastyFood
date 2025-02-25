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

}
