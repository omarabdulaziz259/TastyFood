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

import com.example.tastyfood.R;
import com.example.tastyfood.user_profile.presenter.UserProfilePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserProfileFragment extends Fragment {


    Button btnSignOutFromApp;
    TextView txtEmailUserProfile;
    NavController navController;
    UserProfilePresenter userProfilePresenter;

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
        userProfilePresenter = new UserProfilePresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignOutFromApp = view.findViewById(R.id.btnSignOutFromApp);
        txtEmailUserProfile = view.findViewById(R.id.txtEmailUserProfile);
        navController = Navigation.findNavController(view);
        txtEmailUserProfile.setText(userProfilePresenter.getEmail());

        btnSignOutFromApp.setOnClickListener(v -> {
            userProfilePresenter.signOut();
            navController.navigate(R.id.action_userProfileFragment_to_welcomeFragment);
        });
    }
}