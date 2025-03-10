package com.example.tastyfood.model;

import android.annotation.SuppressLint;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

import com.example.tastyfood.home.model.SingleMealViewer;
import com.example.tastyfood.model.database.CalenderedMeal;
import com.example.tastyfood.model.database.FavMeal;
import com.example.tastyfood.model.database.MealLocalDataSource;
import com.example.tastyfood.model.network.MealApiService;
import com.example.tastyfood.model.network.MealRemoteDataSource;
import com.example.tastyfood.model.network.MealResponse;

import java.util.List;

public class MealRepository {
    private MealLocalDataSource mealLocalDataSource;
    private Retrofit retrofit;
    private static MealRepository repo= null;
    private MealApiService apiService;
    private SingleTransformer<MealResponse, MealResponse> customSchedulers;

    private MealRepository(MealLocalDataSource mealLocalDataSource){
        this.mealLocalDataSource = mealLocalDataSource;
        retrofit = MealRemoteDataSource.getRetrofit();
        apiService = retrofit.create(MealApiService.class);

        customSchedulers = upstream -> upstream.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread());

    }
    public static MealRepository getInstance(MealLocalDataSource mealLocalDataSource){
        if (repo == null){
            synchronized (MealRepository.class){
                if(repo == null){
                    repo = new MealRepository(mealLocalDataSource);
                }
            };
        }
        return repo;
    }
    // delete from here
    //Meal
    public Completable insertMeal(Meal meal){
        return mealLocalDataSource.insertMeal(meal);
    }
    public Completable deleteMeal(Meal meal){
        return mealLocalDataSource.deleteMeal(meal);
    }

    public Maybe<Meal> getMealByIdFromDB(String idMeal){
        return mealLocalDataSource.getMealById(idMeal);
    }

    public Maybe<List<Meal>> getStoredFavDetailedMeals(){
        return mealLocalDataSource.getStoredFavDetailedMeals();
    }

        //FavMeal
    public Flowable<List<FavMeal>> getStoredFavMeals(){
        return mealLocalDataSource.getStoredFavMeals();
    }
    public Completable insertFavMeal(FavMeal favMeal){
        return mealLocalDataSource.insertFavMeal(favMeal);
    }
    public Completable deleteFavMeal(FavMeal favMeal){
        return mealLocalDataSource.deleteFavMeal(favMeal);
    }
    public Maybe<FavMeal> getFavMealById(String idMeal){
        return mealLocalDataSource.getFavMealById(idMeal);
    }

    //CalendaredMeal
    public Completable insertCalenderedMeal(CalenderedMeal calenderedMeal){
        return mealLocalDataSource.insertCalenderedMeal(calenderedMeal);
    }

    public Maybe<CalenderedMeal> getCalenderedMealByDate(String date){
        return mealLocalDataSource.getCalenderedMealByDate(date);
    }

    public Completable deleteCalenderedMeal(CalenderedMeal calenderedMeal){
        return mealLocalDataSource.deleteCalenderedMeal(calenderedMeal);
    }
    public Maybe<CalenderedMeal> getCalenderedMealById(String idMeal){
        return mealLocalDataSource.getCalenderedMealById(idMeal);
    }

    public Completable clearAllSavedData(){
        return Completable.mergeArray(mealLocalDataSource.clearMealDetails(),
                mealLocalDataSource.clearCalendarMeals(),
                mealLocalDataSource.clearFavMeal());
    }
    public Flowable<List<Meal>> getMealsByDate(String date){
        return mealLocalDataSource.getMealsByDate(date);
    }
    public Maybe<List<CalenderedMeal>> getAllCalenderedMeals(){
        return mealLocalDataSource.getAllCalenderedMeals();
    }

    @SuppressLint("CheckResult")
    public void removeMealFromDB(Meal meal){
        getCalenderedMealById(meal.getIdMeal()).subscribeOn(Schedulers.io())
                .subscribe(
                        calenderedMeal -> {},
                        error -> {},
                        () -> {
                            getFavMealById(meal.getIdMeal()).subscribeOn(Schedulers.io())
                                    .subscribe(
                                            favMeal -> {},
                                            error -> {},
                                            () -> deleteMeal(meal).subscribeOn(Schedulers.io()).subscribe()
                                    );

                        }
                );
    }
    //Remote
    public void getMealsByFirstLetter(MealListViewer mealListViewer, char firstLetter){
        Single<MealResponse> response = apiService.getMealsByFirstLetter(firstLetter);
        response.compose(customSchedulers).subscribe(
                mealresponse -> mealListViewer.showMealsList(mealresponse.getMeals()),
                onError -> mealListViewer.onFailed(onError.getLocalizedMessage())
        );
    }
    public void getMealById(SingleMealViewer singleMealViewer, String mealID){
        Single<MealResponse> response = apiService.getMealById(mealID);
        response.compose(customSchedulers).subscribe(
                mealresponse -> singleMealViewer.showSingleMeal(mealresponse.getMeals().get(0)),
                onError -> singleMealViewer.showError(onError.getLocalizedMessage())
        );
    }
    public Single<MealResponse> getMealById(String mealID){
        return apiService.getMealById(mealID);
    }
    public void getRandomMeal(SingleMealViewer singleMealViewer){
        Single<MealResponse> response = apiService.getRandomMeal();
        response.compose(customSchedulers).subscribe(
                mealresponse -> singleMealViewer.showSingleMeal(mealresponse.getMeals().get(0)),
                onError -> singleMealViewer.showError(onError.getLocalizedMessage())
        );
    }

    public Single<MealResponse> getRemoteCategories() {
        return apiService.getListOfCategories();
    }
    public Single<MealResponse> getRemoteCountries() {
        return apiService.getListOfCountries();
    }
    public Single<MealResponse> getRemoteIngredients() {
        return apiService.getListOfIngredients();
    }
    public Single<MealResponse> getRemoteMealsFilteredByIngredient(String ingredient){
        return apiService.getMealsFilteredByIngredient(ingredient);
    }
    public Single<MealResponse> getRemoteMealsFilteredByArea(String strArea){
        return apiService.getMealsFilteredByCountry(strArea);
    }
    public Single<MealResponse> getRemoteMealsFilteredByCategory(String strCategory){
        return apiService.getMealsFilteredByCategory(strCategory);
    }
    public Single<MealResponse> getRemoteSearchedMeals(String searchText){
        return apiService.getSearchedMeals(searchText);
    }
}