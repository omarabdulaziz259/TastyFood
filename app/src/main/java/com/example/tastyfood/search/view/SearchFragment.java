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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private FloatingActionButton fabSearch;

    private NavController navController;

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


        fabSearch = view.findViewById(R.id.fabSearch);
        fabSearch.setOnClickListener(v ->
                navController.navigate(R.id.action_searchFragment_to_globalSearchFragment)
        );

        recyclerViewSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fabSearch.hide();
                } else if (dy < 0) {
                    fabSearch.show();
                }
            }
        });
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
        Chip chipSearchCountry = view.findViewById(R.id.chipSearchCountry);
        chipSearchCountry.setChecked(true);

        setTxtSearchObserver();
    }

    private void setTxtSearchObserver() {
        Observable<String> obs = Observable.create(emitter -> {
            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s != null)
                        emitter.onNext(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        });
        searchPresenter.setTxtSearchObserver(obs, searchAdapter);
    }

    @Override
    public void showCountries(List<Meal> meals) {
        searchPresenter.setAllMeals(meals);
        clearSearchTxt();
        searchAdapter.setMealsList(meals, SearchAdapter.COUNTRIES);
    }

    @Override
    public void showCategories(List<Meal> meals) {
        searchPresenter.setAllMeals(meals);
        clearSearchTxt();
        searchAdapter.setMealsList(meals, SearchAdapter.CATEGORIES);
    }

    @Override
    public void showIngredients(List<Meal> meals) {
        searchPresenter.setAllMeals(meals);
        clearSearchTxt();
        searchAdapter.setMealsList(meals, SearchAdapter.INGREDIENTS);
    }

    @Override
    public void showFilteredMeals(List<Meal> meals) {
        searchPresenter.setAllMeals(meals);
        searchAdapter.setMealsList(meals, SearchAdapter.MEALS);
    }

    @Override
    public void failed(String msg) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show();
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
        SearchFragmentDirections.ActionSearchFragmentToDetailedMealFragment action =
                SearchFragmentDirections.actionSearchFragmentToDetailedMealFragment(meal);
        navController.navigate(action);
    }

    @Override
    public void showError(String msg) {
        failed(msg);
    }
}