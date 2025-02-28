package com.example.tastyfood.home.presenter;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tastyfood.home.model.SingleMealViewer;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealListViewer;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.MealLocalDataSource;

import java.time.LocalDate;

public class HomePresenter {
    private MealRepository mealRepository;
    private SharedPreferences sharedPref;
    private String sharedPrefMealOfTheDayId;
    private String sharedPrefDayOfRecentMealOfTheDay;

    public HomePresenter(SharedPreferences sharedPref, String sharedPrefMealOfTheDayId,
                         String sharedPrefDayOfRecentMealOfTheDay, MealLocalDataSource mealLocalDataSource){

        mealRepository = MealRepository.getInstance(mealLocalDataSource);
        this.sharedPref = sharedPref;
        this.sharedPrefMealOfTheDayId = sharedPrefMealOfTheDayId;
        this.sharedPrefDayOfRecentMealOfTheDay = sharedPrefDayOfRecentMealOfTheDay;

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getMealOfTheDay(SingleMealViewer singleMealViewer){
        String savedMealOfTheDay = getSavedMealOfTheDayIfExists();
        if (!savedMealOfTheDay.isEmpty()){
            mealRepository.getMealById(singleMealViewer, savedMealOfTheDay);
            return;
        }
        mealRepository.getRandomMeal(singleMealViewer);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getSavedMealOfTheDayIfExists(){
        String mealOfTheDayId = sharedPref.getString(sharedPrefMealOfTheDayId, "0");
        String dayOfRecentMealOfTheDay = sharedPref.getString(sharedPrefDayOfRecentMealOfTheDay, "0");
        String today = Integer.toString(LocalDate.now().getDayOfYear());
        if (dayOfRecentMealOfTheDay.equals(today) && !mealOfTheDayId.isEmpty()){
            return mealOfTheDayId;
        }
        return "";
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveMealOfTheDay(Meal meal){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(sharedPrefMealOfTheDayId, meal.getIdMeal());
        editor.putString(sharedPrefDayOfRecentMealOfTheDay, Integer.toString(LocalDate.now().getDayOfYear()));
        editor.apply();
    }

    public void getTopPicksMeals(MealListViewer mealListViewer){
        mealRepository.getMealsByFirstLetter(mealListViewer, 'K');
    }
}
