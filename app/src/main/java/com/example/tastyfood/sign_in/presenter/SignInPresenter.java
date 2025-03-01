package com.example.tastyfood.sign_in.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tastyfood.model.DBInserter;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.CalenderedMeal;
import com.example.tastyfood.model.database.FavMeal;
import com.example.tastyfood.sign_in.model.SignInNavigator;
import com.example.tastyfood.util.FireStoreManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignInPresenter implements DBInserter {
    private FirebaseAuth mAuth;
    private SignInNavigator signInNavigator;
    private MealRepository repository;
    public SignInPresenter(SignInNavigator signInNavigator, MealRepository repository){
        mAuth = FirebaseAuth.getInstance();
        this.signInNavigator = signInNavigator;
        this.repository = repository;

    }

    public Boolean validateUserInputs(String email, String password){
        if (email.isEmpty()){
            signInNavigator.onSignInFailed("Please Input Your E-mail");
            return false;
        }
        else if (password.isEmpty()){
            signInNavigator.onSignInFailed("Please Fill Your Password");
            return false;
        }
        return true;
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            signInNavigator.onSignInSuccess();
                            getRemoteData();
                        } else {
                            signInNavigator.onSignInFailed("Authentication Failed");
                        }
                    }
                });
    }
    private void getRemoteData(){
        getCalendaredMealsData();
        getFavMealsData();
    }
    private void getCalendaredMealsData(){
        FireStoreManager.getAllCalendaredMeals(this);
    }
    private void getFavMealsData(){
        FireStoreManager.getAllFavMeals(this);
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
