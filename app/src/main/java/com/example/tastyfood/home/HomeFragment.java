package com.example.tastyfood.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tastyfood.R;
import com.example.tastyfood.model.Meal;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements SingleMealViewer{
    private String SHARED_PREFERENCES_NAME;
    private String SHARED_PREFERENCES_MEAL_ID;
    private String SHARED_PREFERENCES_MEAL_DATE;
    private ImageView imgMealOfTheDay;
    private TextView txtNameMealOfTheDay;
    private RecyclerView recycleViewHome;
    private CardView cardMealOfTheDay;
    private HomeAdapter homeAdapter;
    private ConstraintLayout errorConstraintLayout;
    private Button btnErrorClose;
    private HomePresenter homePresenter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        homePresenter = setupHomePresenter();

        errorConstraintLayout = view.findViewById(R.id.errorConstraintLayout);
        btnErrorClose = view.findViewById(R.id.btnErrorClose);

        imgMealOfTheDay = view.findViewById(R.id.imgMealOfTheDay);
        cardMealOfTheDay = view.findViewById(R.id.cardMealOfTheDay);
        txtNameMealOfTheDay = view.findViewById(R.id.txtNameMealOfTheDay);

        recycleViewHome = view.findViewById(R.id.recycleViewHome);
        recycleViewHome.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recycleViewHome.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(requireContext(), new ArrayList<>());
        recycleViewHome.setAdapter(homeAdapter);
        homePresenter.getMealOfTheDay(this);
        //todo cardMealOfTheDay.setOnClickListener();

    }

    private HomePresenter setupHomePresenter(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        return new HomePresenter(sharedPref, SHARED_PREFERENCES_MEAL_ID, SHARED_PREFERENCES_MEAL_DATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void showSingleMeal(Meal meal) {
        Glide.with(this).load(meal.getStrMealThumb())
//                .apply(new RequestOptions().override(400, 400))
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(imgMealOfTheDay);
        txtNameMealOfTheDay.setText(meal.getStrMeal());
        homePresenter.saveMealOfTheDay(meal);

    }

    @Override
    public void showError(String msg) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.error_dialog, errorConstraintLayout);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btnErrorClose.setOnClickListener(
                v -> {
                    alertDialog.dismiss();
                    Snackbar.make(requireView(), "alert Dialog clossed", Snackbar.LENGTH_SHORT).show();
                }
        );

    }
}