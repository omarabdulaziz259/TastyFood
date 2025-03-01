package com.example.tastyfood.mainActivity.model;

public interface MainActivityNavigator {
    void navigateToHomeScreen();
    void navigateToFavScreen();
    void navigateToCategorySearchScreen();
    void navigateToCalenderScreen();
    void navigateToUserProfileScreen();
//    void navigateToGlobalSearchScreen();
    void unauthorizedAccess(String errorTitle, String msg);
    Boolean getInternetStatus();
}