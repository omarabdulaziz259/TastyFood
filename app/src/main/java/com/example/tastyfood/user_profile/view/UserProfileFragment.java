package com.example.tastyfood.user_profile.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tastyfood.mainActivity.presenter.MainActivityPresenter;
import com.example.tastyfood.mainActivity.view.MainActivity;
import com.example.tastyfood.R;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.MealLocalDataSource;
import com.example.tastyfood.user_profile.model.UserProfileHandler;
import com.example.tastyfood.user_profile.presenter.UserProfilePresenter;
import com.example.tastyfood.util.UserValidation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;


public class UserProfileFragment extends Fragment implements UserProfileHandler {


    private MaterialButton btnSignOutFromApp;
    private TextView txtEmailUserProfile;
    private NavController navController;
    private UserProfilePresenter userProfilePresenter;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfilePresenter = new UserProfilePresenter(
                MealRepository.getInstance(MealLocalDataSource.getDatabaseManager(getContext())),
                this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setBottomNavVisibility(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignOutFromApp = view.findViewById(R.id.btnSignOutFromApp);
        txtEmailUserProfile = view.findViewById(R.id.txtEmailUserProfile);
        navController = Navigation.findNavController(view);
        txtEmailUserProfile.setText(userProfilePresenter.getEmail());
        if (!UserValidation.validateUser()){
            btnSignOutFromApp.setText(getString(R.string.sign_in) + "\\" + getString(R.string.sign_up));
            btnSignOutFromApp.setIcon(getContext().getDrawable(R.drawable.login));
        }

        btnSignOutFromApp.setOnClickListener(v -> {
            if (UserValidation.validateUser()){
                userProfilePresenter.clearAllData();
            }else {
                navController.navigate(R.id.action_userProfileFragment_to_welcomeFragment);
            }
        });
    }

    @Override
    public void failed() {
        Snackbar.make(requireView(), "Failed to sign out please try again later", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void success() {
        navController.navigate(R.id.action_userProfileFragment_to_welcomeFragment);
    }
}