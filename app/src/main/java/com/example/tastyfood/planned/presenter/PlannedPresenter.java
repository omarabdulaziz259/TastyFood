package com.example.tastyfood.planned.presenter;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.CalenderedMeal;
import com.example.tastyfood.planned.model.PlannedHandler;
import com.example.tastyfood.util.DateChecker;
import com.example.tastyfood.util.FireStoreManager;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannedPresenter {
    private final MealRepository repository;
    private final PlannedHandler plannedHandler;
    private String date;
    private static boolean stopDeleteFunc = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PlannedPresenter(MealRepository repository, PlannedHandler plannedHandler){
        date = getTodaysDate();
        this.repository = repository;
        this.plannedHandler = plannedHandler;
        if (!stopDeleteFunc){
            deleteOldCalendarMeals();
            stopDeleteFunc = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTodaysDate(){
        return DateChecker.getTodaysDate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    public void getMealsForDate(String date){
        this.date = date;
        repository.getMealsByDate(date).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(
                       mealList -> {
                           if (mealList.isEmpty()){
                               String day = date.equals(getTodaysDate()) ? "Today" : date;
                                plannedHandler.onFailed("There isn't Any Meal For " + day);
                           }
                           else {
                               plannedHandler.onSuccess(mealList);
                           }
                       },
                       error ->  plannedHandler.onFailed("Failed to load meals: " + error.getMessage())
               );
   }
   @SuppressLint("CheckResult")
   public void deleteMeal(Meal meal){
       CalenderedMeal calenderedMeal = new CalenderedMeal(meal.getIdMeal(), date);
       repository.deleteCalenderedMeal(calenderedMeal)
               .subscribeOn(Schedulers.io())
               .observeOn(Schedulers.io())
               .subscribe(() -> {
                   repository.removeMealFromDB(meal);
                   FireStoreManager.deleteCalendarMeal(calenderedMeal);
               }
       );
   }

    @SuppressLint("CheckResult")
    private void deleteMealWithDate(Meal meal, String date){
       CalenderedMeal calenderedMeal = new CalenderedMeal(meal.getIdMeal(), date);
       repository.deleteCalenderedMeal(calenderedMeal).subscribeOn(Schedulers.io()).subscribe(
               () -> repository.removeMealFromDB(meal)
       );
   }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    private void deleteOldCalendarMeals(){
        DateChecker.isDateInPast(date);
        repository.getAllCalenderedMeals().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        calenderedMeals -> {
                            if (!calenderedMeals.isEmpty()){
                                for (CalenderedMeal meal: calenderedMeals){
                                    if (DateChecker.isDateInPast(meal.getDate())){
                                        repository.getMealByIdFromDB(meal.getIdMeal())
                                                .subscribeOn(Schedulers.io())
                                                .doOnSuccess( detailedMeal -> FireStoreManager.deleteCalendarMeal(new CalenderedMeal(detailedMeal.getIdMeal(), meal.getDate())))
                                                .observeOn(Schedulers.io())
                                                        .subscribe(
                                                                detailedMeal ->  deleteMealWithDate(detailedMeal, meal.getDate())
                                                        );
                                    }
                                }
                            }
                        },
                        error -> error.printStackTrace()
                );
   }
}
