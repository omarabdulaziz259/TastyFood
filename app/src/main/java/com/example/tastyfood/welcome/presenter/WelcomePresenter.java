package com.example.tastyfood.welcome.presenter;

import android.util.Log;

import com.example.tastyfood.model.DBInserter;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.CalenderedMeal;
import com.example.tastyfood.model.database.FavMeal;
import com.example.tastyfood.util.FireStoreManager;
import com.example.tastyfood.welcome.model.WelcomeNavigator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class WelcomePresenter implements DBInserter {

    private MealRepository repository;

    public WelcomePresenter(MealRepository repository){
        this.repository = repository;
    }
    public void getRemoteData(){
        getCalendaredMealsData();
        getFavMealsData();
    }
    private void getCalendaredMealsData(){
        FireStoreManager.getAllCalendaredMeals(this);
    }
    private void getFavMealsData(){
        FireStoreManager.getAllFavMeals(this);
    }

    public static void navigateToNextScreen(WelcomeNavigator welcomeNavigator, Boolean isInternetConnected){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null && isInternetConnected){
            welcomeNavigator.navigateToHome();
        }
        else {
            welcomeNavigator.errorMsg("Please connect to the internet to access this feature");
        }
    }

    @Override
    public void insertCalendarMeals(List<CalenderedMeal> calenderedMeals) {
        for (CalenderedMeal calenderedMeal: calenderedMeals) {
            repository.insertCalenderedMeal(calenderedMeal).subscribeOn(Schedulers.io()).subscribe();
            repository.getMealById(calenderedMeal.getIdMeal())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            mealResponse -> {
                                repository.insertMeal(mealResponse.getMeals().get(0)).subscribeOn(Schedulers.io()).subscribe();
                                },
                            error -> error.printStackTrace()
                    );
        }
    }

    @Override
    public void insertFavMeals(List<FavMeal> favMeals) {
        for (FavMeal favMeal: favMeals) {
            repository.insertFavMeal(favMeal).subscribeOn(Schedulers.io()).subscribe();
            repository.getMealById(favMeal.getIdMeal())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            mealResponse -> {
                                repository.insertMeal(mealResponse.getMeals().get(0)).subscribeOn(Schedulers.io()).subscribe();
                            },
                            error -> error.printStackTrace()
                    );
        }
    }

    @Override
    public void onFailure(String msg) {
        Log.i("TAG", "onFailure: " + msg);
    }
}