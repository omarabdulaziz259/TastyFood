package com.example.tastyfood.favourite;


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

    void getFavMeals(){
        repository.getStoredFavDetailedMeals().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> favouriteHandler.onSuccessFetchingDB(meals),
                        error -> favouriteHandler.onFailed()
                );
    }
}
