package com.example.tastyfood.user_profile.presenter;

import android.util.Log;

import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.user_profile.model.UserProfileHandler;
import com.example.tastyfood.util.UserValidation;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserProfilePresenter {
    MealRepository repository;
    UserProfileHandler userProfileHandler;
    public UserProfilePresenter(MealRepository repository, UserProfileHandler userProfileHandler) {
        this.repository = repository;
        this.userProfileHandler = userProfileHandler;
    }

    public String getEmail(){
       return UserValidation.getUser() != null ?
               UserValidation.getUser().getEmail()
               : "Please Login Here";
    }

    public void clearAllData(){
        repository.clearAllSavedData().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            signOut();
                            userProfileHandler.success();
                        },
                        error -> {
                            userProfileHandler.failed();
                            Log.i("TAG", "clearAllData: " + error.getLocalizedMessage());
                        }

                );
    }

    public void signOut() {
        UserValidation.getFireBaseAuth().signOut();
    }


}
