package com.example.tastyfood.planned.presenter;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.CheckResult;
import androidx.annotation.RequiresApi;

import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.CalenderedMeal;
import com.example.tastyfood.planned.model.PlannedHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannedPresenter {
    private MealRepository repository;
    private PlannedHandler plannedHandler;
    private String date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PlannedPresenter(MealRepository repository, PlannedHandler plannedHandler){
        date = getTodaysDate();
        this.repository = repository;
        this.plannedHandler = plannedHandler;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTodaysDate(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = today.format(formatter);
        return formattedDate;
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
       repository.deleteCalenderedMeal(calenderedMeal).subscribeOn(Schedulers.io()).subscribe(
               () -> repository.removeMealFromDB(meal)
       );
   }
}
