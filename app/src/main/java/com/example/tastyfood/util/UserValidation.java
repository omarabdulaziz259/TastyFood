package com.example.tastyfood.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserValidation {
    public static boolean validateUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            return false;
        }
        return true;
    }
    public static FirebaseUser getUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser();
    }
    public static FirebaseAuth getFireBaseAuth(){
        return FirebaseAuth.getInstance();
    }
}
