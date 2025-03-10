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

    //meal
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMealDetails(Meal meal);
    //Meal
    @Delete
    Completable deleteMealDetails(Meal meal);
    //Meal
    @Query("SELECT * FROM MealDetails WHERE idMeal = :idMeal")
    Maybe<Meal> getMealById(String idMeal);
    //Meal
    @Query("SELECT * FROM MealDetails INNER JOIN FavMeal ON MealDetails.idMeal = FavMeal.idMeal")
    Maybe<List<Meal>> getStoredFavDetailedMeals();
    //meal
    @Query("DELETE FROM MealDetails")
    Completable clearMealDetails();



    //favMeal
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertFavMeal(FavMeal favMeal);
    //FavMeal
    @Query("SELECT * FROM FavMeal WHERE idMeal = :idMeal")
    Maybe<FavMeal> getFavMealById(String idMeal);
    //FavMeal
    @Query("SELECT * FROM FavMeal")
    Flowable<List<FavMeal>> getAllFavMeals();
    //FavMeal
    @Delete
    Completable deleteFavMeal(FavMeal favMeal);
    //favMeal
    @Query("DELETE FROM FavMeal")
    Completable clearFavMeals();


    //CalendarMeal
    @Delete
    Completable deleteCalenderedMeal(CalenderedMeal calenderedMeal);
    //calendarMeal
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertCalenderedMeal(CalenderedMeal calenderedMeal);
    //CalendarMeal
    @Query("SELECT * FROM CalenderedMeal WHERE date = :date")
    Maybe<CalenderedMeal> getCalenderedMealByDate(String date);
    @Query("SELECT * FROM CalenderedMeal WHERE idMeal = :idMeal")
    Maybe<CalenderedMeal> getCalenderedMealById(String idMeal);
    //calendarMeal
    @Query("DELETE FROM CalenderedMeal")
    Completable clearCalenderedMeals();
    //calendarMeals
    @Query("SELECT * FROM CalenderedMeal")
    Maybe<List<CalenderedMeal>> getAllCalenderedMeals();

    @Query("SELECT * FROM MealDetails WHERE idMeal IN (SELECT idMeal FROM CalenderedMeal WHERE date = :date)")
    Flowable<List<Meal>> getMealsByDate(String date);

}