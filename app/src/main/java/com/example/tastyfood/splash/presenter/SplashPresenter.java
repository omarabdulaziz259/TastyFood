package com.example.tastyfood.splash.presenter;

import android.os.Handler;

import com.example.tastyfood.splash.model.SplashNavigator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashPresenter {
    private FirebaseAuth mAuth;
    private SplashNavigator splashNavigator;
    private Runnable runnable;
    private Handler handler;
    private boolean isInternetConnected;

    public SplashPresenter(SplashNavigator splashNavigator, boolean isInternetConnected){
        this.splashNavigator = splashNavigator;
        mAuth = FirebaseAuth.getInstance();
        handler = new Handler();
        this.isInternetConnected = isInternetConnected;
    }

    public void navigateToNextScreen(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentUser != null){
                    if (isInternetConnected)
                        splashNavigator.navigateToHome();
                    else
                        splashNavigator.navigateToFav();
                }else {
                    splashNavigator.navigateToWelcome();
                }
            }
        };
        handler.postDelayed(runnable, 2500);
    }


    public void removeHandlerCallbacks(){
        handler.removeCallbacks(runnable);
    }
}
