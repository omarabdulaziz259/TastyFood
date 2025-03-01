package com.example.tastyfood.mainActivity.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tastyfood.R;
import com.example.tastyfood.mainActivity.model.MainActivityNavigator;
import com.example.tastyfood.mainActivity.presenter.MainActivityPresenter;
import com.example.tastyfood.util.InternetConnectivity;
import com.example.tastyfood.util.UserValidation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainActivityNavigator {
    private MainActivityPresenter mainActivityPresenter;
    private BottomNavigationView bottomNav;
    private NavController navController;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityPresenter = new MainActivityPresenter(this);

        bottomNav = findViewById(R.id.bottomNavigation);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(mainActivityPresenter.navlistener);
        setBottomNavVisibility(false);
        setInternetWatcher();
    }

    private void setInternetWatcher() {
        InternetConnectivity.observeInternetConnectivity(getApplicationContext())
                .distinctUntilChanged()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isConnected -> {
                          internetConnectionLost("internet Connection Lost", "Please reconnect to the internet", isConnected);
                  },
                        error -> error.printStackTrace()
                );
    }

    public void makeHomeItemSelectedOnBottomNav(){
        if (bottomNav != null && bottomNav.getSelectedItemId() != R.id.nav_home) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }
    public void makeFavItemSelectedOnBottomNav(){
        if (bottomNav != null && bottomNav.getSelectedItemId() != R.id.nav_fav) {
            bottomNav.setSelectedItemId(R.id.nav_fav);
        }
    }
    public void setBottomNavVisibility(boolean isVisible) {
        bottomNav = findViewById(R.id.bottomNavigation);
        if (isVisible) {
            bottomNav.setVisibility(View.VISIBLE);
        } else {
            bottomNav.setVisibility(View.GONE);
        }
    }


    @Override
    public void navigateToHomeScreen() {
        navController.navigate(R.id.action_global_homeFragment);
    }

    @Override
    public void navigateToFavScreen() {
        navController.navigate(R.id.action_global_favouriteFragment);
    }

    @Override
    public void navigateToCategorySearchScreen() {
        navController.navigate(R.id.action_global_searchFragment);
    }

    @Override
    public void navigateToCalenderScreen() {
        navController.navigate(R.id.action_global_plannedFragment);
    }

//    @Override
//    public void navigateToGlobalSearchScreen(){
//        navController.navigate(R.id.action_global_globalSearchFragment);
//    }
    @Override
    public void navigateToUserProfileScreen() {
        navController.navigate(R.id.action_global_userProfileFragment);
    }

    @Override
    public Boolean getInternetStatus() {

        return InternetConnectivity.isInternetAvailable(getApplicationContext());
    }

    public void internetConnectionLost(String errorTitle, String msg, boolean shouldDismiss) {
        if (shouldDismiss){
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
            return;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.error_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        alertDialog = builder.create();

        Button btnErrorClose = view.findViewById(R.id.btnErrorClose);
        TextView txtErrorDesc = view.findViewById(R.id.txtErrorDesc);
        TextView txtErrorTitle = view.findViewById(R.id.txtErrorTitle);
        txtErrorTitle.setText(errorTitle);
        txtErrorDesc.setText(msg);
        btnErrorClose.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (UserValidation.validateUser()){
                navController.navigate(R.id.action_global_favouriteFragment);
            } else {
                navController.navigate(R.id.action_global_welcomeFragment);
            }
        });

        alertDialog.show();
    }
    @Override
    public void unauthorizedAccess(String errorTitle, String msg) {
        View view = LayoutInflater.from(this).inflate(R.layout.error_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        Button btnErrorClose = view.findViewById(R.id.btnErrorClose);
        TextView txtErrorDesc = view.findViewById(R.id.txtErrorDesc);
        TextView txtErrorTitle = view.findViewById(R.id.txtErrorTitle);
        txtErrorTitle.setText(errorTitle);
        txtErrorDesc.setText(msg);
        btnErrorClose.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();

    }
}