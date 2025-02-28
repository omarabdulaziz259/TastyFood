package com.example.tastyfood.planned.view;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tastyfood.R;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.MealLocalDataSource;
import com.example.tastyfood.planned.model.PlannedHandler;
import com.example.tastyfood.planned.presenter.PlannedPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PlannedFragment extends Fragment implements PlannedHandler {

    private MaterialButton btnCalendarDate;
    private RecyclerView recyclerViewCalendar;
    private PlannedPresenter plannedPresenter;
    private PlannedAdapter plannedAdapter;
    private NavController navController;

    public PlannedFragment() {
        // Required empty public constructor
    }

    public static PlannedFragment newInstance() {
        return new PlannedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_planned, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        plannedPresenter = new PlannedPresenter(
                MealRepository.getInstance(MealLocalDataSource.getDatabaseManager(requireContext()))
                ,this);
        initializeUI(view);
        plannedPresenter.getMealsForDate(plannedPresenter.getTodaysDate());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeUI(@NonNull View view) {
        navController = Navigation.findNavController(view);
        btnCalendarDate = view.findViewById(R.id.btnCalendarDate);
        btnCalendarDate.setText(plannedPresenter.getTodaysDate());
        recyclerViewCalendar = view.findViewById(R.id.recyclerViewCalendar);
        recyclerViewCalendar.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCalendar.setLayoutManager(layoutManager);
        recyclerViewCalendar.setItemAnimator(new DefaultItemAnimator());
        plannedAdapter = new PlannedAdapter(getContext(), new ArrayList<>(), this);
        recyclerViewCalendar.setAdapter(plannedAdapter);
        setupCalendarDateBtnFunction();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupCalendarDateBtnFunction() {
        btnCalendarDate.setOnClickListener(v -> {
            long today = MaterialDatePicker.todayInUtcMilliseconds();
            long nextWeek = today + TimeUnit.DAYS.toMillis(6);
            CalendarConstraints.DateValidator dateValidator = new CalendarConstraints.DateValidator() {
                @Override
                public boolean isValid(long date) {
                    return date >= today && date <= nextWeek;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                }
            };

            CalendarConstraints constraints = new CalendarConstraints.Builder()
                    .setValidator(dateValidator)
                    .build();
            MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(constraints)
                    .build();
            materialDatePicker.addOnPositiveButtonClickListener((selection) ->{
                String date = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(new Date(selection));
                btnCalendarDate.setText(date);
                plannedPresenter.getMealsForDate(date);
            });
            materialDatePicker.show(getActivity().getSupportFragmentManager(), "TAG");
        });
    }

    @Override
    public void navigateToDetailedMeal(Meal meal) {
        PlannedFragmentDirections.ActionPlannedFragmentToDetailedMealFragment action =
                PlannedFragmentDirections.actionPlannedFragmentToDetailedMealFragment(meal);
        navController.navigate(action);
    }

    @Override
    public void deleteCalendaredMeal(Meal meal) {
        plannedPresenter.deleteMeal(meal);
    }

    @Override
    public void onSuccess(List<Meal> mealList) {
        plannedAdapter.setMealsList(mealList);
    }

    @Override
    public void onFailed(String msg) {
        plannedAdapter.setMealsList(new ArrayList<>());
        View rootView = getView();
        if (rootView != null) {
            Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT).show();
        } else {
            Log.e("PlannedFragment", "Snackbar could not be shown: No root view found.");
        }
    }
}