package com.example.tastyfood.search.presenter;

import com.example.tastyfood.home.model.SingleMealViewer;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.search.model.SearchViewer;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter {

    private MealRepository repository;
    private SearchViewer searchViewer;

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
}
