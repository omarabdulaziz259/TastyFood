package com.example.tastyfood.user_profile.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfilePresenter {
    FirebaseAuth mAuth;
    FirebaseUser user;

    public UserProfilePresenter() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public String getEmail(){
        return user.getEmail();
    }

    public void signOut() {
        mAuth.signOut();
        //todo delete allData in database (calendered and fav) meals
    }


}
