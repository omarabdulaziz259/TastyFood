package com.example.tastyfood.sign_up.view;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment";
    private FirebaseAuth mAuth;
    NavController navController;
    EditText txtEditFullNameSignUp, txtEditEmailSignUp, txtEditPasswordSignUp, txtEditPasswordSignUpConfirm;
    Button btnSignUpToApp;
    public SignUpFragment() {
        // Required empty public constructor
    }


    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

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

                if (email.isEmpty()){
                    Toast.makeText(getContext(), "Please Input Your E-mail", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwordConfirm.isEmpty() || password.isEmpty()){
                    Toast.makeText(getContext(), "Please Input Your Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(passwordConfirm)){
                    Toast.makeText(getContext(), "Your Password Must Match Password Confirm", Toast.LENGTH_SHORT).show();
                    return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                navController.navigate(R.id.action_signUpFragment_to_userProfileFragment);
                                //Todo after sign up successful
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                //Todo after sign up failed
                            }
                        }
                    });
        });
    }
}