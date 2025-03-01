package com.example.tastyfood.welcome.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.example.tastyfood.mainActivity.view.MainActivity;
import com.example.tastyfood.R;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.MealLocalDataSource;
import com.example.tastyfood.util.InternetConnectivity;
import com.example.tastyfood.welcome.model.WelcomeNavigator;
import com.example.tastyfood.welcome.presenter.WelcomePresenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class WelcomeFragment extends Fragment implements WelcomeNavigator {

    private FirebaseAuth  mAuth;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private NavController navController;
    private Button btnSignInWithEmail, btnSignInWithGoogle, btnAsAGuest;
    private TextView txtSignUp;
    private WelcomePresenter welcomePresenter;
    public WelcomeFragment() {
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
        welcomePresenter = new WelcomePresenter(
                MealRepository.getInstance(MealLocalDataSource.getDatabaseManager(getContext())));
        navController = Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            Snackbar.make(requireView(), "Google Sign-In Failed", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btnSignInWithEmail = view.findViewById(R.id.btnSignInWithEmail);
        btnAsAGuest = view.findViewById(R.id.btnGuest);
        btnSignInWithGoogle = view.findViewById(R.id.btnSignInWithGoogle);
        txtSignUp = view.findViewById(R.id.txtSignUp);
        NavController navController = Navigation.findNavController(view);
        
        btnSignInWithEmail.setOnClickListener(v -> navController.navigate(R.id.action_welcomeFragment_to_signInFragment));
        txtSignUp.setOnClickListener(v -> navController.navigate(R.id.action_welcomeFragment_to_signUpFragment));

        btnSignInWithGoogle.setOnClickListener(v -> signIn());
        btnAsAGuest.setOnClickListener(v -> WelcomePresenter.navigateToNextScreen(this, InternetConnectivity.isInternetAvailable(getContext())));
        
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        welcomePresenter.getRemoteData();
                    } else {
                        updateUI(null);
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            navController.navigate(R.id.action_welcomeFragment_to_homeFragment);
        } else {
            Snackbar.make(requireView(), "Sign-In Failed", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setBottomNavVisibility(false);

    }

    @Override
    public void navigateToHome() {
        navController.navigate(R.id.action_welcomeFragment_to_homeFragment);
    }

    @Override
    public void errorMsg(String msg) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.error_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        Button btnErrorClose = view.findViewById(R.id.btnErrorClose);
        TextView txtErrorDesc = view.findViewById(R.id.txtErrorDesc);
        TextView txtErrorTitle = view.findViewById(R.id.txtErrorTitle);
        txtErrorTitle.setText("Error");
        txtErrorDesc.setText(msg);
        btnErrorClose.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }
}