package com.example.tastyfood.welcome.presenter;

import com.example.tastyfood.welcome.model.WelcomeNavigator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomePresenter {

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
}