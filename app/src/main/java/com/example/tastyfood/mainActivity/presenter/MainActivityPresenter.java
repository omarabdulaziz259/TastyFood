package com.example.tastyfood.mainActivity.presenter;

import com.example.tastyfood.R;
import com.example.tastyfood.mainActivity.model.MainActivityNavigator;
import com.example.tastyfood.util.UserValidation;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivityPresenter {

    private MainActivityNavigator mainActivityNavigator;

    public MainActivityPresenter(MainActivityNavigator mainActivityNavigator) {
        this.mainActivityNavigator = mainActivityNavigator;
    }

    public NavigationBarView.OnItemSelectedListener navlistener = item -> {
        int itemId = item.getItemId();

        if (!mainActivityNavigator.getInternetStatus() && requiresInternet(itemId)) {
            mainActivityNavigator.unauthorizedAccess("internet Connectivity error","Please Connect To The Internet To Access This Page");
            return false;
        }

        if (itemId == R.id.nav_home) {
            mainActivityNavigator.navigateToHomeScreen();
        } else if (itemId == R.id.nav_profile) {
            mainActivityNavigator.navigateToUserProfileScreen();
        } else if (itemId == R.id.nav_search) {
            mainActivityNavigator.navigateToCategorySearchScreen();
        } /*else if (itemId == R.id.nav_global_search){
            mainActivityNavigator.navigateToGlobalSearchScreen();
        }*/else if (itemId == R.id.nav_fav || itemId == R.id.nav_calender) {
            if (UserValidation.validateUser()) {
                if (itemId == R.id.nav_fav) {
                    mainActivityNavigator.navigateToFavScreen();
                } else {
                    mainActivityNavigator.navigateToCalenderScreen();
                }
            } else {
                mainActivityNavigator.unauthorizedAccess("Unauthorized Access","To Access This Feature Please Login from profile screen");
                return false;
            }
        }
        return true;
    };

    private boolean requiresInternet(int itemId) {
        return itemId == R.id.nav_home || itemId == R.id.nav_search /*|| itemId == R.id.nav_global_search*/;
    }
}