package com.example.tastyfood.sign_up.presenter;

import android.widget.Toast;

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
            SignUpNavigator.onSignUpFailed("Please Input Your E-mail");
            return false;
        }
        else if (passwordConfirm.isEmpty() || password.isEmpty()){
            SignUpNavigator.onSignUpFailed("Please Fill Your Password and Password Confirmation");
            return false;
        }
        else if (!password.equals(passwordConfirm)){
            SignUpNavigator.onSignUpFailed("Your Password Must Match Password Confirm\"");
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
