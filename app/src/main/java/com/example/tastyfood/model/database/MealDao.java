package com.example.tastyfood.model.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tastyfood.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface MealDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMealDetails(Meal meal);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertFavMeal(FavMeal favMeal);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertCalenderedMeal(CalenderedMeal calenderedMeal);

    @Query("SELECT * FROM CalenderedMeal WHERE date = :date")
    Maybe<CalenderedMeal> getCalenderedMeal(int date);

    @Query("SELECT * FROM FavMeal")
    Flowable<List<FavMeal>> getAllFavMeals();

    @Delete
    Completable deleteFavMeal(FavMeal favMeal);

    @Delete
    Completable deleteCalenderedMeal(CalenderedMeal calenderedMeal);

    @Delete
    Completable deleteMealDetails(Meal meal);

    @Query("SELECT * FROM MealDetails WHERE idMeal = :idMeal")
    Maybe<Meal> getMealById(String idMeal);


}