package com.example.tastyfood.sign_in.model;

public interface SignInNavigator {
    void onSignInSuccess();
    void onSignInFailed(String msg);
}
