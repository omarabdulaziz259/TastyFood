package com.example.tastyfood.sign_up.presenter;


import androidx.annotation.NonNull;

import com.example.tastyfood.sign_up.model.SignUpNavigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpPresenter {
    private final FirebaseAuth mAuth;
    SignUpNavigator SignUpNavigator;
    public SignUpPresenter(SignUpNavigator SignUpNavigator){
        mAuth = FirebaseAuth.getInstance();
        this.SignUpNavigator = SignUpNavigator;
    }

    public Boolean validateUserInputs(String email, String password, String passwordConfirm){
        if (email.isEmpty()){
            SignUpNavigator.onSignUpFailed("Please enter your email.");
            return false;
        }
        else if (passwordConfirm.isEmpty() || password.isEmpty()){
            SignUpNavigator.onSignUpFailed("Please fill in your password and password confirmation.");
            return false;
        }
        else if (password.length() < 8){
            SignUpNavigator.onSignUpFailed("Your password must be at least 8 characters long.");
            return false;
        }
        else if (!password.equals(passwordConfirm)){
            SignUpNavigator.onSignUpFailed("Your password must match the password confirmation.");
            return false;
        }
        return true;
    }
    public void signUp(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            SignUpNavigator.onSignUpSuccess();
                        } else {
                            SignUpNavigator.onSignUpFailed("Authentication failed");
                        }
                    }
                });
    }
}
