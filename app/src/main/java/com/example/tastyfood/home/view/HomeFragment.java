package com.example.tastyfood.home.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tastyfood.home.model.HomeNavigator;
import com.example.tastyfood.home.presenter.HomePresenter;
import com.example.tastyfood.home.model.SingleMealViewer;
import com.example.tastyfood.mainActivity.view.MainActivity;
import com.example.tastyfood.R;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealListViewer;
import com.example.tastyfood.model.database.MealLocalDataSource;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements SingleMealViewer, MealListViewer, HomeNavigator {
    private String SHARED_PREFERENCES_NAME;
    private String SHARED_PREFERENCES_MEAL_ID;
    private String SHARED_PREFERENCES_MEAL_DATE;
    private ImageView imgMealOfTheDay;
    private TextView txtNameMealOfTheDay;
    private RecyclerView recycleViewHome;
    private CardView cardMealOfTheDay;
    private HomeAdapter homeAdapter;
    private HomePresenter homePresenter;
    private Meal meal;
    private NavController navController;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return  new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SHARED_PREFERENCES_NAME = getString(R.string.mealoftheday);
        SHARED_PREFERENCES_MEAL_ID = getString(R.string.mealofthedayid);
        SHARED_PREFERENCES_MEAL_DATE = getString(R.string.dayofrecentmealoftheday);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).makeHomeItemSelectedOnBottomNav();
        ((MainActivity) requireActivity()).setBottomNavVisibility(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        homePresenter = setupHomePresenter();

        imgMealOfTheDay = view.findViewById(R.id.imgMealOfTheDay);
        txtNameMealOfTheDay = view.findViewById(R.id.txtNameMealOfTheDay);
        cardMealOfTheDay = view.findViewById(R.id.cardMealOfTheDay);

        homePresenter.getMealOfTheDay(this);

        recycleViewHome = view.findViewById(R.id.recycleViewHome);
        recycleViewHome.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recycleViewHome.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(requireContext(), new ArrayList<>(), this);
        recycleViewHome.setAdapter(homeAdapter);
        homePresenter.getTopPicksMeals(this);
        navController = Navigation.findNavController(view);

        cardMealOfTheDay.setOnClickListener(v -> {
            navigateToDetailedMeal(meal);
        });
    }

    private HomePresenter setupHomePresenter(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        return new HomePresenter(sharedPref, SHARED_PREFERENCES_MEAL_ID, SHARED_PREFERENCES_MEAL_DATE,
                MealLocalDataSource.getDatabaseManager(requireContext()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void showSingleMeal(Meal meal) {
        this.meal = meal;
        Glide.with(this).load(meal.getStrMealThumb())
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(imgMealOfTheDay);
        txtNameMealOfTheDay.setText(meal.getStrMeal());
        homePresenter.saveMealOfTheDay(meal);

    }

    @Override
    public void showError(String msg) {
        showErrorDialog(msg);
    }

    @Override
    public void showMealsList(List<Meal> mealList) {
        homeAdapter.setMealsList(mealList);
    }

    @Override
    public void onFailed(String msg) {
        showErrorDialog(msg);
    }
    private void showErrorDialog(String msg){
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.error_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        Button btnErrorClose = view.findViewById(R.id.btnErrorClose);
        TextView txtErrorDesc = view.findViewById(R.id.txtErrorDesc);
        txtErrorDesc.setText(msg);
        btnErrorClose.setOnClickListener(v -> {
            alertDialog.dismiss();
            Snackbar.make(requireView(), "Alert Dialog Closed", Snackbar.LENGTH_SHORT).show();
        });

        alertDialog.show();
    }

    @Override
    public void navigateToDetailedMeal(Meal meal) {
        HomeFragmentDirections.ActionHomeFragmentToDetailedMealFragment action =
                HomeFragmentDirections.actionHomeFragmentToDetailedMealFragment(meal);
        navController.navigate(action);
    }
}