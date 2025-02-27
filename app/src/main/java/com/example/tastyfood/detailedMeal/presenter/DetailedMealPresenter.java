package com.example.tastyfood.detailedMeal.presenter;

import android.annotation.SuppressLint;

import com.example.tastyfood.detailedMeal.model.MealSaver;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.CalenderedMeal;
import com.example.tastyfood.model.database.FavMeal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;
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
        insertMealToDB(meal);
        CalenderedMeal calenderedMeal = new CalenderedMeal(meal.getIdMeal(), date);
        mealRepository.insertCalenderedMeal(calenderedMeal).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mealSaver.calendaredMealSuccess(),
                        error -> mealSaver.onFailed("There was an error occured please try again")
                );
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
        insertMealToDB(meal);
        mealRepository.insertFavMeal(new FavMeal(meal.getIdMeal())).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mealSaver.insertingFavMealSuccess(),
                        error -> mealSaver.onFailed("There was an error occurred please try again")
                );
    }
    @SuppressLint("CheckResult")
    public void deleteFavMeal(Meal meal){
        mealRepository.insertFavMeal(new FavMeal(meal.getIdMeal())).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mealSaver.deleteFavMealSuccess(),
                        error -> mealSaver.onFailed("There was an error occurred please try again")
                );
        removeMealFromDB(meal);
    }

    private void insertMealToDB(Meal meal){
        mealRepository.insertMeal(meal).subscribeOn(Schedulers.io())
                .subscribe();
    }
    private void removeMealFromDB(Meal meal){
        mealRepository.getCalenderedMealById(meal.getIdMeal()).subscribeOn(Schedulers.io())
                .subscribe(new MaybeObserver<CalenderedMeal>() {
                               @Override
                               public void onSubscribe(@NonNull Disposable d) {}
                               @Override
                               public void onSuccess(@NonNull CalenderedMeal calenderedMeal) {}
                               @Override
                               public void onError(@NonNull Throwable e) {}
                               @Override
                               public void onComplete() {
                                    mealRepository.getFavMealById(meal.getIdMeal()).subscribeOn(Schedulers.io())
                                            .subscribe(new MaybeObserver<FavMeal>() {
                                                @Override
                                                public void onSubscribe(@NonNull Disposable d) {}
                                                @Override
                                                public void onSuccess(@NonNull FavMeal favMeal) {}
                                                @Override
                                                public void onError(@NonNull Throwable e) {}
                                                @Override
                                                public void onComplete() {
                                                    mealRepository.deleteMeal(meal);
                                                }
                                            });
                               }
                           }

                );
    }
}
