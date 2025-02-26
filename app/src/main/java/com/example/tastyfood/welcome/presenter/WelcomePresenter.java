package com.example.tastyfood.welcome.presenter;

import com.example.tastyfood.welcome.model.WelcomeNavigator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomePresenter {

    public static void navigateToNextScreen(WelcomeNavigator welcomeNavigator){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            welcomeNavigator.navigateToHome();
        }
    }
}