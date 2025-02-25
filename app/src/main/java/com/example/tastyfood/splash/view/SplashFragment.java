package com.example.tastyfood.splash.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tastyfood.R;
import com.example.tastyfood.splash.model.SplashNavigator;
import com.example.tastyfood.splash.presenter.SplashPresenter;


public class SplashFragment extends Fragment implements SplashNavigator {

    private NavController navController;
    private SplashPresenter splashPresenter;

    public SplashFragment() {}

    public static SplashFragment newInstance() {return new SplashFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashPresenter = new SplashPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        splashPresenter.navigateToNextScreen();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        splashPresenter.removeHandlerCallbacks();
    }

    @Override
    public void navigateToWelcome() {
        navController.navigate(R.id.action_splashFragment_to_welcomeFragment);
    }

    @Override
    public void navigateToHome() {
        navController.navigate(R.id.action_splashFragment_to_homeFragment);
    }
}