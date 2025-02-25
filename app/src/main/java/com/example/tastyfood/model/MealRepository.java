package com.example.tastyfood.model;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

import com.example.tastyfood.home.SingleMealViewer;
import com.example.tastyfood.model.database.MealLocalDataSource;
import com.example.tastyfood.model.network.CategoryResponse;
import com.example.tastyfood.model.network.MealApiService;
import com.example.tastyfood.model.network.MealRemoteDataSource;
import com.example.tastyfood.model.network.MealResponse;

public class MealRepository {
//    private MealLocalDataSource mealLocalDataSource;
    private Retrofit retrofit;
    private static MealRepository repo= null;
    private MealApiService apiService;
    private SingleTransformer<MealResponse, MealResponse> customSchedulers;

    private MealRepository(/*MealLocalDataSource mealLocalDataSource*/){
//        this.mealLocalDataSource = mealLocalDataSource;
        retrofit = MealRemoteDataSource.getRetrofit();
        apiService = retrofit.create(MealApiService.class);

        customSchedulers = upstream -> upstream.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread());

    }
    public static MealRepository getInstance(/*MealLocalDataSource mealLocalDataSource*/){
        if (repo == null){
            synchronized (MealRepository.class){
                if(repo == null){
                    repo = new MealRepository(/*mealLocalDataSource*/);
                }
            };
        }
        return repo;
    }
//    public Flowable<List<Meal>> getFavStoredMeals(){
//        return MealLocalDataSource.getFavStoredMeals();
//    }

//    public Completable insertFavMeal(Meal meal){
//        return MealLocalDataSource.insertFavMeal(meal);
//    }
//    public Completable removeFavMeal(Meal meal){
//        return MealLocalDataSource.removeFavMeal(meal);
//    }
    public void getMealsByFirstLetter(MealListViewer mealListViewer, char firstLetter){
        Single<MealResponse> call = apiService.getMealsByFirstLetter(firstLetter);
        call.compose(customSchedulers).subscribe(
                response -> mealListViewer.onSuccess(response.getMeals()),
                onError -> mealListViewer.onFailed()
        );
    }
    public void getMealById(SingleMealViewer singleMealViewer, String mealID){
        Single<MealResponse> call = apiService.getMealById(mealID);
        call.compose(customSchedulers).subscribe(
                response -> singleMealViewer.showSingleMeal(response.getMeals().get(0)),
                onError -> singleMealViewer.showError(onError.getLocalizedMessage())
        );
    }
    public void getRandomMeal(SingleMealViewer singleMealViewer){
        Single<MealResponse> call = apiService.getRandomMeal();
        call.compose(customSchedulers).subscribe(
                response -> singleMealViewer.showSingleMeal(response.getMeals().get(0)),
                onError -> singleMealViewer.showError(onError.getLocalizedMessage())
        );
    }

    public void getCategories(CategoryListViewer categoryListViewer){
        Single<CategoryResponse> call = apiService.getCategories();
        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                response -> categoryListViewer.onSuccess(response.getCategories()),
                onError -> categoryListViewer.onFailed()
        );
    }
}

