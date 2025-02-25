package com.example.tastyfood.sign_in.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tastyfood.R;
import com.example.tastyfood.sign_in.model.SignInNavigator;
import com.example.tastyfood.sign_in.presenter.SignInPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment implements SignInNavigator {


    private NavController navController;

    EditText txtEditEmail, txtEditPassword;
    Button btnSignInToApp;
    SignInPresenter signInPresenter;
    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signInPresenter = new SignInPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        txtEditEmail = view.findViewById(R.id.txtEditEmail);
        txtEditPassword = view.findViewById(R.id.txtEditPassword);
        btnSignInToApp = view.findViewById(R.id.btnSignInToApp);

        btnSignInToApp.setOnClickListener(v -> {
            String email = String.valueOf(txtEditEmail.getText());
            String password = String.valueOf(txtEditPassword.getText());

            Boolean isUserInputsValid = signInPresenter.validateUserInputs(email, password);

            if (isUserInputsValid){
                signInPresenter.signIn(email, password);
            }


        });
    }

    @Override
    public void onSignInSuccess() {
        navController.navigate(R.id.action_signInFragment_to_homeFragment);
        //Todo after sign in successful - download from firebase the data of this user
    }

    @Override
    public void onSignInFailed(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }
}