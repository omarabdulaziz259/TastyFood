package com.example.tastyfood.GlobalSearch.presenter;

import android.annotation.SuppressLint;

import com.example.tastyfood.GlobalSearch.model.GlobalSearchHandler;
import com.example.tastyfood.model.MealRepository;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GlobalSearchPresenter {
    private MealRepository repository;
    private GlobalSearchHandler globalSearchHandler;

    public GlobalSearchPresenter(MealRepository repository, GlobalSearchHandler globalSearchHandler) {
        this.repository = repository;
        this.globalSearchHandler = globalSearchHandler;
    }

    public void getMealsSearchedByName(String writtenText){
        repository.getRemoteSearchedMeals(writtenText).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> {
                            if (mealResponse != null && mealResponse.getMeals() != null) {
                                globalSearchHandler.showSearchedMeals(mealResponse.getMeals());
                            } else {
                                globalSearchHandler.showSearchedMeals(new ArrayList<>());
                                globalSearchHandler.failed("No meals found.");
                            }
                        },
                        error -> globalSearchHandler.failed(error.getLocalizedMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void setTxtSearchObserver(Observable<String> obs){
        obs.debounce(800, TimeUnit.MILLISECONDS)
                .map(searchString ->  searchString.toLowerCase())
                .observeOn(Schedulers.io())
                .subscribe(
                        filteredMeals -> getMealsSearchedByName(filteredMeals),
                        error -> error.printStackTrace()
                );
    }
}
