package com.example.tastyfood.detailedMeal.presenter;

import android.annotation.SuppressLint;

import com.example.tastyfood.detailedMeal.model.MealSaver;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.CalenderedMeal;
import com.example.tastyfood.model.database.FavMeal;
import com.example.tastyfood.util.FireStoreManager;
import com.example.tastyfood.util.UserValidation;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailedMealPresenter {
    private MealRepository mealRepository;
    private MealSaver mealSaver;

    public DetailedMealPresenter(MealRepository mealRepository, MealSaver mealSaver){
        this.mealSaver = mealSaver;
        this.mealRepository = mealRepository;
    }

    @SuppressLint("CheckResult")
    public void insertCalenderedMeal(Meal meal, String date){
        if (UserValidation.validateUser()){
        insertMealToDB(meal);
        CalenderedMeal calenderedMeal = new CalenderedMeal(meal.getIdMeal(), date);
        mealRepository.insertCalenderedMeal(calenderedMeal).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnComplete( () -> FireStoreManager.saveCalendarMeal(calenderedMeal))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mealSaver.calendaredMealSuccess(),
                        error -> mealSaver.onFailed("There was an error occurred please try again")
                );}
        else {
            mealSaver.onFailed("Please Sign up to enable this Feature");
        }
    }

    @SuppressLint("CheckResult")
    public void checkFavMealExists(Meal meal){
        mealRepository.getFavMealById(meal.getIdMeal()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        favMeal -> mealSaver.reverseFavBtn(true),
                        error -> mealSaver.onFailed("Error occurred while checking meal in DB"),
                        () -> mealSaver.reverseFavBtn(false)
                );
    }

    @SuppressLint("CheckResult")
    public void insertFavMeal(Meal meal){
        FavMeal favMeal = new FavMeal(meal.getIdMeal());
        if (UserValidation.validateUser()){
            insertMealToDB(meal);
            mealRepository.insertFavMeal(favMeal).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnComplete(() -> FireStoreManager.saveFavMeal(favMeal))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mealSaver.insertingFavMealSuccess(),
                        error -> mealSaver.onFailed("There was an error occurred please try again")
                );
        }
        else {
            mealSaver.onFailed("Please Sign up to enable this Feature");
        }
    }
    @SuppressLint("CheckResult")
    public void deleteFavMeal(Meal meal){
        FavMeal favMeal = new FavMeal(meal.getIdMeal());
        mealRepository.deleteFavMeal(favMeal).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnComplete(() -> FireStoreManager.deleteFavMeal(favMeal))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            mealSaver.deleteFavMealSuccess();
                            mealRepository.removeMealFromDB(meal);
                        },
                        error -> mealSaver.onFailed("There was an error occurred please try again")
                );
    }
    private void insertMealToDB(Meal meal){
        mealRepository.insertMeal(meal).subscribeOn(Schedulers.io())
                .subscribe();
    }
}