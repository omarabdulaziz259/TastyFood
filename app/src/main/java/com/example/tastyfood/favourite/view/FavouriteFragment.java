package com.example.tastyfood.favourite.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tastyfood.R;
import com.example.tastyfood.favourite.model.FavouriteHandler;
import com.example.tastyfood.favourite.presenter.FavouritePresenter;
import com.example.tastyfood.mainActivity.view.MainActivity;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.MealLocalDataSource;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment implements FavouriteHandler {


    private FavouritePresenter favouritePresenter;
    private RecyclerView recyclerViewFavourite;
    private FavouriteAdapter favouriteAdapter;
    private NavController navController;
    public FavouriteFragment() {
        // Required empty public constructor
    }

    public static FavouriteFragment newInstance(String param1, String param2) {

        return new FavouriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).makeFavItemSelectedOnBottomNav();
        ((MainActivity) requireActivity()).setBottomNavVisibility(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        favouritePresenter = new FavouritePresenter(
                MealRepository.getInstance(MealLocalDataSource.getDatabaseManager(requireContext())),
                this);
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recyclerViewFavourite = view.findViewById(R.id.recycleViewFavourite);
        recyclerViewFavourite.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewFavourite.setLayoutManager(layoutManager);
        favouriteAdapter = new FavouriteAdapter(requireContext(), new ArrayList<>(), this);
        recyclerViewFavourite.setAdapter(favouriteAdapter);
        favouritePresenter.getFavMeals();
    }

    @Override
    public void navigateToDetailedMeal(Meal meal) {
        FavouriteFragmentDirections.ActionFavouriteFragmentToDetailedMealFragment action =
                FavouriteFragmentDirections.actionFavouriteFragmentToDetailedMealFragment(meal);
        navController.navigate(action);
    }

    @Override
    public void onSuccessFetchingDB(List<Meal> mealList) {
        favouriteAdapter.setMealsList(mealList);
    }

    @Override
    public void onFailed() {
        Snackbar.make(getView(), "Failed Fetching Data", Snackbar.LENGTH_SHORT).show();
    }
}