package com.example.tastyfood.sign_up.view;

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
import android.widget.EditText;

import com.example.tastyfood.R;
import com.example.tastyfood.sign_up.model.SignUpNavigator;
import com.example.tastyfood.sign_up.presenter.SignUpPresenter;
import com.google.android.material.snackbar.Snackbar;

public class SignUpFragment extends Fragment implements SignUpNavigator {

    private NavController navController;
    private EditText txtEditFullNameSignUp, txtEditEmailSignUp, txtEditPasswordSignUp, txtEditPasswordSignUpConfirm;
    private Button btnSignUpToApp;
    private SignUpPresenter signUpPresenter;
    public SignUpFragment() {
        // Required empty public constructor
    }


    public static SignUpFragment newInstance(String param1, String param2) {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpPresenter = new SignUpPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        txtEditFullNameSignUp = view.findViewById(R.id.txtEditFullNameSignUp);
        txtEditEmailSignUp = view.findViewById(R.id.txtEditEmailSignUp);
        txtEditPasswordSignUp = view.findViewById(R.id.txtEditPasswordSignUp);
        txtEditPasswordSignUpConfirm = view.findViewById(R.id.txtEditPasswordSignUpConfirm);
        btnSignUpToApp = view.findViewById(R.id.btnSignUpToApp);

        btnSignUpToApp.setOnClickListener(v -> {
            String email = String.valueOf(txtEditEmailSignUp.getText());
            String password = String.valueOf(txtEditPasswordSignUp.getText());
            String passwordConfirm = String.valueOf(txtEditPasswordSignUpConfirm.getText());

            Boolean isUserInputsValid = signUpPresenter.validateUserInputs(email, password, passwordConfirm);

            if (isUserInputsValid){
                signUpPresenter.signUp(email, password);
            }
        });
    }

    @Override
    public void onSignUpSuccess() {
        navController.navigate(R.id.action_signUpFragment_to_homeFragment);
    }

    @Override
    public void onSignUpFailed(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }
}