package com.example.tastyfood.favourite.presenter;


import com.example.tastyfood.favourite.model.FavouriteHandler;
import com.example.tastyfood.model.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouritePresenter {
    private MealRepository repository;
    private FavouriteHandler favouriteHandler;

    public FavouritePresenter(MealRepository repository, FavouriteHandler favouriteHandler) {
        this.repository = repository;
        this.favouriteHandler = favouriteHandler;
    }

    public void getFavMeals(){
        repository.getStoredFavDetailedMeals().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> favouriteHandler.onSuccessFetchingDB(meals),
                        error -> favouriteHandler.onFailed()
                );
    }
}