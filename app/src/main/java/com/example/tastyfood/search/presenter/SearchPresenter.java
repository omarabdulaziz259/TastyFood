package com.example.tastyfood.search.presenter;

import android.annotation.SuppressLint;

import com.example.tastyfood.home.model.SingleMealViewer;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.search.model.SearchViewer;
import com.example.tastyfood.search.view.SearchAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter {

    private MealRepository repository;
    private SearchViewer searchViewer;
    private List<Meal> allMeals = new ArrayList<>();


    public SearchPresenter(MealRepository repository, SearchViewer searchViewer) {
        this.repository = repository;
        this.searchViewer = searchViewer;
    }

    public void getCountries(){
        repository.getRemoteCountries().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> searchViewer.showCountries(mealResponse.getMeals()),
                        error -> searchViewer.failed("Error getting countries")
                );
    }
    public void getCategories(){
        repository.getRemoteCategories().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> searchViewer.showCategories(mealResponse.getMeals()),
                        error -> searchViewer.failed("Error getting categories")
                );
    }
    public void getIngredients(){
        repository.getRemoteIngredients().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> searchViewer.showIngredients(mealResponse.getMeals()),
                        error -> searchViewer.failed("Error getting ingredients")
                );
    }
    public void getMealsFilteredByCategory(String category){
        repository.getRemoteMealsFilteredByCategory(category).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> searchViewer.showFilteredMeals(mealResponse.getMeals()),
                        error -> searchViewer.failed("Error fetching remote server")
                );
    }
    public void getMealsFilteredByCountry(String country){
        repository.getRemoteMealsFilteredByArea(country).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> searchViewer.showFilteredMeals(mealResponse.getMeals()),
                        error -> searchViewer.failed("Error fetching remote server")
                );
    }
    public void getMealsFilteredByIngredient(String ingredient){
        repository.getRemoteMealsFilteredByIngredient(ingredient).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> searchViewer.showFilteredMeals(mealResponse.getMeals()),
                        error -> searchViewer.failed("Error fetching remote server")
                );
    }
    public void getDetailedMealById(SingleMealViewer singleMealViewer, String mealId){
        repository.getMealById(singleMealViewer, mealId);
    }

    public void setAllMeals(List<Meal> meals) {
        allMeals.clear();
        allMeals.addAll(meals);
    }

    @SuppressLint("CheckResult")
    public void setTxtSearchObserver(Observable<String> obs, SearchAdapter searchAdapter){
        obs.debounce(400, TimeUnit.MILLISECONDS)
                .map(searchString ->  searchString.toLowerCase())
                .flatMap(searchString -> Observable.fromIterable(allMeals)
                        .filter(meal -> {
                            switch (searchAdapter.getType()){
                                case SearchAdapter.CATEGORIES:
                                    return meal.getStrCategory().toLowerCase().contains(searchString);
                                case SearchAdapter.COUNTRIES:
                                    return meal.getStrArea().toLowerCase().contains(searchString);
                                case SearchAdapter.INGREDIENTS:
                                    return meal.getStrIngredient().toLowerCase().contains(searchString);
                                case SearchAdapter.MEALS:
                                    return meal.getStrMeal().toLowerCase().contains(searchString);
                            }
                            return false;
                        })
                        .toList()
                        .toObservable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        filteredMeals -> {
                            searchAdapter.setMealsList(filteredMeals);
                        },
                        error -> error.printStackTrace()
                );
    }
}
