package com.example.tastyfood.model.database;

import android.content.Context;
import com.example.tastyfood.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public class MealLocalDataSource {
    private MealDao mealDao;
    private static MealLocalDataSource manger = null;

    private MealLocalDataSource(Context context){
        MealDatabase mealDatabase = MealDatabase.getInstance(context);
        mealDao = mealDatabase.getMealDao();
    }

    public static MealLocalDataSource getDatabaseManager(Context _context){
        if (manger == null){
            manger = new MealLocalDataSource(_context);
        }
        return manger;
    }

    //Meal
    public Completable insertMeal(Meal meal){
        return mealDao.insertMealDetails(meal);
    }
    public Completable deleteMeal(Meal meal){
        return mealDao.deleteMealDetails(meal);
    }

    public Maybe<Meal> getMealById(String idMeal){
        return mealDao.getMealById(idMeal);
    }
    public Maybe<List<Meal>> getStoredFavDetailedMeals(){
        return mealDao.getStoredFavDetailedMeals();
    }
    public Completable clearMealDetails(){
        return mealDao.clearMealDetails();
    }


    //FavMeal
    public Flowable<List<FavMeal>> getStoredFavMeals(){
        return mealDao.getAllFavMeals();
    }
    public Maybe<FavMeal> getFavMealById(String idMeal){
        return mealDao.getFavMealById(idMeal);
    }
    public Completable insertFavMeal(FavMeal favMeal){
        return mealDao.insertFavMeal(favMeal);
    }
    public Completable deleteFavMeal(FavMeal favMeal){
        return mealDao.deleteFavMeal(favMeal);
    }
    public Completable clearFavMeal(){
        return mealDao.clearFavMeals();
    }

    //CalendaredMeal
    public Maybe<CalenderedMeal> getCalenderedMealByDate(String date){
        return mealDao.getCalenderedMealByDate(date);
    }
    public Completable insertCalenderedMeal(CalenderedMeal calenderedMeal){
        return mealDao.insertCalenderedMeal(calenderedMeal);
    }

    public Completable deleteCalenderedMeal(CalenderedMeal calenderedMeal){
        return mealDao.deleteCalenderedMeal(calenderedMeal);
    }

    public Maybe<CalenderedMeal> getCalenderedMealById(String idMeal){
        return mealDao.getCalenderedMealById(idMeal);
    }
    public Completable clearCalendarMeals(){
        return mealDao.clearCalenderedMeals();
    }
    public Maybe<List<CalenderedMeal>> getAllCalenderedMeals(){
        return mealDao.getAllCalenderedMeals();
    }

    //calendaredMeal with meal
    public Flowable<List<Meal>> getMealsByDate(String date){
        return mealDao.getMealsByDate(date);
    }


}

