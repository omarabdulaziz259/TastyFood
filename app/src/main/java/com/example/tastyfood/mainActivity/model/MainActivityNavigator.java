package com.example.tastyfood.mainActivity.model;

public interface MainActivityNavigator {
    void navigateToHomeScreen();
    void navigateToFavScreen();
    void navigateToSearchScreen();
    void navigateToCalenderScreen();
    void navigateToUserProfileScreen();
    void unauthorizedAccess(String msg);
}
