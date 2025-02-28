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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements MainActivityNavigator {
    private MainActivityPresenter mainActivityPresenter;
    private BottomNavigationView bottomNav;
    private NavController navController;


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
    }

    public void makeHomeItemSelectedOnBottomNav(){
        if (bottomNav != null && bottomNav.getSelectedItemId() != R.id.nav_home) {
            bottomNav.setSelectedItemId(R.id.nav_home);
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
    public void navigateToSearchScreen() {
    //todo
    }

    @Override
    public void navigateToCalenderScreen() {
        navController.navigate(R.id.action_global_plannedFragment);
    }

    @Override
    public void navigateToUserProfileScreen() {
        navController.navigate(R.id.action_global_userProfileFragment);
    }

    @Override
    public void unauthorizedAccess(String msg) {
        View view = LayoutInflater.from(this).inflate(R.layout.error_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        Button btnErrorClose = view.findViewById(R.id.btnErrorClose);
        TextView txtErrorDesc = view.findViewById(R.id.txtErrorDesc);
        TextView txtErrorTitle = view.findViewById(R.id.txtErrorTitle);
        txtErrorTitle.setText(msg);
        txtErrorDesc.setText("To Login -> Profile -> SignIn/SignUp");
        btnErrorClose.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();

    }
}