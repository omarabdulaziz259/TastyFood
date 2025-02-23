package com.example.tastyfood.sign_in.presenter;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tastyfood.R;
import com.example.tastyfood.sign_in.model.SignInNavigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInPresenter {
    private FirebaseAuth mAuth;
    private SignInNavigator signInNavigator;
    public SignInPresenter(SignInNavigator signInNavigator){
        mAuth = FirebaseAuth.getInstance();
        this.signInNavigator = signInNavigator;

    }

    public Boolean validateUserInputs(String email, String password){
        if (email.isEmpty()){
            signInNavigator.onSignInFailed("Please Input Your E-mail");
            return false;
        }
        else if (password.isEmpty()){
            signInNavigator.onSignInFailed("Please Fill Your Password");
            return false;
        }
        return true;
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            signInNavigator.onSignInSuccess();
                        } else {
                            signInNavigator.onSignInFailed("Authentication Failed");
                        }
                    }
                });
    }
}
