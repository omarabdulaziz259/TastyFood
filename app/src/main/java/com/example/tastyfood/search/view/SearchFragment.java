package com.example.tastyfood.search.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.example.tastyfood.R;
import com.example.tastyfood.home.model.SingleMealViewer;
import com.example.tastyfood.home.view.HomeFragmentDirections;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.MealLocalDataSource;
import com.example.tastyfood.search.model.SearchNavigator;
import com.example.tastyfood.search.model.SearchViewer;
import com.example.tastyfood.search.presenter.SearchPresenter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment implements SearchViewer, SearchNavigator, SingleMealViewer {

    private EditText txtSearch;
    private ChipGroup chipGroupSearch;
    private RecyclerView recyclerViewSearch;
    private SearchPresenter searchPresenter;
    private SearchAdapter searchAdapter;
    private NavController navController;
    private List<Meal> allMeals = new ArrayList<>();

    public SearchFragment() {}

    public static SearchFragment newInstance() {return new SearchFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        txtSearch = view.findViewById(R.id.txtSearch);
        chipGroupSearch = view.findViewById(R.id.chipGroupSearch);
        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch);
        recyclerViewSearch.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(),2 );
        searchAdapter = new SearchAdapter(requireContext(), new ArrayList<>(), this);
        recyclerViewSearch.setLayoutManager(layoutManager);
        recyclerViewSearch.setAdapter(searchAdapter);

        searchPresenter = new SearchPresenter(
                MealRepository.getInstance(MealLocalDataSource.getDatabaseManager(requireContext()))
                , this);
        chipGroupSearch.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipSearchCountry){
                searchPresenter.getCountries();
            } else if (checkedId == R.id.chipSearchCategory){
                searchPresenter.getCategories();
            } else if (checkedId == R.id.chipSearchIngredient) {
                searchPresenter.getIngredients();
            }
        });
        setTxtSearchObserver();
    }

    @SuppressLint("CheckResult")
    private void setTxtSearchObserver(){
        Observable<String> obs = Observable.create(emitter -> {
            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s != null)
                    emitter.onNext(s.toString());
                    Log.i("TAG", "onTextChanged: " + s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        });

        obs.debounce(400, TimeUnit.MILLISECONDS)
                .map(searchString ->  searchString.toLowerCase())
                .flatMap(searchString -> Observable.fromIterable(allMeals)
                        .filter(meal -> {
                            switch (searchAdapter.getType()){
                                case SearchAdapter.CATEGORIES:
                                    return meal.getStrCategory().toLowerCase().contains(searchString);
                                case SearchAdapter.COUNTRIES:
                                    return meal.getStrArea().toLowerCase().contains(searchString);
                                case SearchAdapter.INGREDIENTS:
                                    return meal.getStrIngredient().toLowerCase().contains(searchString);
                                case SearchAdapter.MEALS:
                                    return meal.getStrMeal().toLowerCase().contains(searchString);
                            }
                            return false;
                        })
                        .toList()
                        .toObservable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        filteredMeals -> {
                            Log.i("TAG", "setTxtSearchObserver: " + filteredMeals.toString());
                            searchAdapter.setMealsList(filteredMeals);
                        },
                        error -> error.printStackTrace()
                );
    }
    @Override
    public void showCountries(List<Meal> meals) {
        allMeals.clear();
        allMeals.addAll(meals);
        searchAdapter.setMealsList(meals, SearchAdapter.COUNTRIES);
    }

    @Override
    public void showCategories(List<Meal> meals) {
        allMeals.clear();
        allMeals.addAll(meals);
        searchAdapter.setMealsList(meals, SearchAdapter.CATEGORIES);
    }

    @Override
    public void showIngredients(List<Meal> meals) {
        allMeals.clear();
        allMeals.addAll(meals);
        searchAdapter.setMealsList(meals, SearchAdapter.INGREDIENTS);
    }

    @Override
    public void showFilteredMeals(List<Meal> meals) {
        allMeals.clear();
        allMeals.addAll(meals);
        searchAdapter.setMealsList(meals, SearchAdapter.MEALS);
    }

    @Override
    public void failed(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    private void clearSearchTxt(){
        txtSearch.clearFocus();
        txtSearch.setText("");
    }

    @Override
    public void getFilteredMealsByCountry(String country) {
        clearSearchTxt();
        searchPresenter.getMealsFilteredByCountry(country);
    }

    @Override
    public void getFilteredMealsByCategory(String category) {
        clearSearchTxt();
        searchPresenter.getMealsFilteredByCategory(category);
    }

    @Override
    public void getFilteredMealsByIngredients(String ingredient) {
        clearSearchTxt();
        searchPresenter.getMealsFilteredByIngredient(ingredient);
    }

    @Override
    public void navigateToDetailedMeal(Meal meal) {
        searchPresenter.getDetailedMealById(this, meal.getIdMeal());
    }

    @Override
    public void showSingleMeal(Meal meal) {
        Log.i("TAG", "navigateToDetailedMeal: " + meal);
        SearchFragmentDirections.ActionSearchFragmentToDetailedMealFragment action =
                SearchFragmentDirections.actionSearchFragmentToDetailedMealFragment(meal);
        navController.navigate(action);
    }

    @Override
    public void showError(String msg) {
        failed(msg);
    }
}