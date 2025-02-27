package com.example.tastyfood.mainActivity.presenter;

import com.example.tastyfood.R;
import com.example.tastyfood.mainActivity.model.MainActivityNavigator;
import com.example.tastyfood.util.UserValidation;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityPresenter {

    private MainActivityNavigator mainActivityNavigator;

    public MainActivityPresenter(MainActivityNavigator mainActivityNavigator) {
        this.mainActivityNavigator = mainActivityNavigator;
    }

    public NavigationBarView.OnItemSelectedListener navlistener = item ->{
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            mainActivityNavigator.navigateToHomeScreen();
            return true;
        }else if (itemId == R.id.nav_profile){
            mainActivityNavigator.navigateToUserProfileScreen();
            return true;
        }
        if (UserValidation.validateUser()){
            if (itemId == R.id.nav_fav){
                mainActivityNavigator.navigateToFavScreen();
            } else if (itemId == R.id.nav_search){
                mainActivityNavigator.navigateToSearchScreen();
            } else if (itemId == R.id.nav_calender){
                mainActivityNavigator.navigateToCalenderScreen();
            }
        }else {
            mainActivityNavigator.unauthorizedAccess("To Access This Feature Please Login");
            return false;
        }
        return true;
    };


}
