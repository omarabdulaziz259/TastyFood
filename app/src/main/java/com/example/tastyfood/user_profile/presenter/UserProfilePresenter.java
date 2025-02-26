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
        if (user != null){
            return user.getEmail();
        }
        else {
            return "Please Login Here";
        }
    }

    public void signOut() {
        mAuth.signOut();
        //todo delete allData in database (calendered and fav) meals
    }


}
