package com.example.tastyfood.GlobalSearch.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.tastyfood.GlobalSearch.model.GlobalSearchHandler;
import com.example.tastyfood.GlobalSearch.presenter.GlobalSearchPresenter;
import com.example.tastyfood.R;
import com.example.tastyfood.home.model.SingleMealViewer;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.MealLocalDataSource;
import com.example.tastyfood.search.presenter.SearchPresenter;
import com.example.tastyfood.search.view.SearchAdapter;
import com.example.tastyfood.search.view.SearchFragmentDirections;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class GlobalSearchFragment extends Fragment implements GlobalSearchHandler {
    private EditText txtGlobalSearch;
    private RecyclerView recyclerViewGlobalSearch;
    private GlobalSearchPresenter globalSearchPresenter;
    private GlobalSearchAdapter globalSearchAdapter;
    private NavController navController;

    public GlobalSearchFragment() {}

    public static GlobalSearchFragment newInstance() {return new GlobalSearchFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_global_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        txtGlobalSearch = view.findViewById(R.id.txtGlobalSearch);
        recyclerViewGlobalSearch = view.findViewById(R.id.recyclerViewGlobalSearch);
        recyclerViewGlobalSearch.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(),2 );
        globalSearchAdapter = new GlobalSearchAdapter(requireContext(), new ArrayList<>(), this);
        recyclerViewGlobalSearch.setLayoutManager(layoutManager);
        recyclerViewGlobalSearch.setAdapter(globalSearchAdapter);


        globalSearchPresenter = new GlobalSearchPresenter(
                MealRepository.getInstance(MealLocalDataSource.getDatabaseManager(requireContext()))
                , this);

        setTxtSearchObserver();
    }
    private void setTxtSearchObserver() {
        Observable<String> obs = Observable.create(emitter -> {
            txtGlobalSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s != null)
                        emitter.onNext(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        });
        globalSearchPresenter.setTxtSearchObserver(obs);
    }

    @Override
    public void navigateToDetailedMeal(Meal meal) {
        GlobalSearchFragmentDirections.ActionGlobalSearchFragmentToDetailedMealFragment action =
                GlobalSearchFragmentDirections.actionGlobalSearchFragmentToDetailedMealFragment(meal);
        navController.navigate(action);
    }

    @Override
    public void showSearchedMeals(List<Meal> meals) {
        if (!meals.isEmpty())
            globalSearchAdapter.setMealsList(meals);
        else
            globalSearchAdapter.setMealsList(new ArrayList<>());
    }

    @Override
    public void failed(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }
}