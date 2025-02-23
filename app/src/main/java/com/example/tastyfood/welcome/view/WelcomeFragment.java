package com.example.tastyfood.welcome.view;

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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;


public class WelcomeFragment extends Fragment {

    FirebaseAuth  mAuth;
    GoogleSignInClient googleSignInClient;

    Button btnSignInWithEmail, btnSignInWithGoogle, btnAsAGuest;
    TextView txtSignUp;
    public WelcomeFragment() {
        // Required empty public constructor
    }


    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignInWithEmail = view.findViewById(R.id.btnSignInWithEmail);
        btnAsAGuest = view.findViewById(R.id.btnGuest);
        btnSignInWithGoogle = view.findViewById(R.id.btnSignInWithGoogle);
        txtSignUp = view.findViewById(R.id.txtSignUp);
        NavController navController = Navigation.findNavController(view);
        btnSignInWithEmail.setOnClickListener(v -> navController.navigate(R.id.action_welcomeFragment_to_signInFragment));
        txtSignUp.setOnClickListener(v -> navController.navigate(R.id.action_welcomeFragment_to_signUpFragment));
    }
}