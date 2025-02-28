package com.example.tastyfood.detailedMeal.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tastyfood.R;
import com.example.tastyfood.detailedMeal.model.MealSaver;
import com.example.tastyfood.detailedMeal.presenter.DetailedMealPresenter;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.model.MealRepository;
import com.example.tastyfood.model.database.MealLocalDataSource;
import com.example.tastyfood.util.CountryCode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DetailedMealFragment extends Fragment implements MealSaver {
    private ImageView imgDetailedMeal, imgDetailedMealArea, imgDetailedMealCategory;
    private TextView txtDetailedMeal, txtDetailedMealArea, txtDetailedMealCategory, txtMealInstructions;
    private RecyclerView ingredientsRecyclerView;
    private WebView youtubeWebView;
    private MaterialButton btnCalender, btnFav;
    private DetailedMealPresenter detailedMealPresenter;
    private DetailedMealAdapter detailedMealAdapter;

    private Meal meal;
    public DetailedMealFragment() {
        // Required empty public constructor
    }

    public static DetailedMealFragment newInstance(String param1, String param2) {
        return new DetailedMealFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detailed_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailedMealPresenter = setupDetailedMealPresenter();
        meal = DetailedMealFragmentArgs.fromBundle(getArguments()).getMeal();
        detailedMealPresenter.checkFavMealExists(meal);
        initializeUiComponents(view);
    }
    private void initializeUiComponents(View view){
        imgDetailedMeal = view.findViewById(R.id.imgDetailedMeal);
        Glide.with(this).load(meal.getStrMealThumb())
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(imgDetailedMeal);

        imgDetailedMealArea = view.findViewById(R.id.imgDetailedMealArea);
        Glide.with(this).load("https://www.themealdb.com/images/icons/flags/big/64/" +
                        CountryCode.getCountryCode(meal.getStrArea()) + ".png")
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(imgDetailedMealArea);

        imgDetailedMealCategory= view.findViewById(R.id.imgDetailedMealCategory);
        Glide.with(this).load("https://www.themealdb.com/images/category/"
                        + meal.getStrCategory() + ".png")
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(imgDetailedMealCategory);

        txtDetailedMeal= view.findViewById(R.id.txtDetailedMeal);
        txtDetailedMeal.setText(meal.getStrMeal());

        txtDetailedMealArea= view.findViewById(R.id.txtDetailedMealArea);
        txtDetailedMealArea.setText(meal.getStrArea());

        txtDetailedMealCategory= view.findViewById(R.id.txtDetailedMealCategory);
        txtDetailedMealCategory.setText(meal.getStrCategory());

        txtMealInstructions= view.findViewById(R.id.txtMealInstructions);
        txtMealInstructions.setText(meal.getStrInstructions());

        initializeVideo(view);

        btnCalender= view.findViewById(R.id.btnCalender);
        setupCalendarBtnFunction();
        btnFav= view.findViewById(R.id.btnFav);

        setupIngredientsRecyclerView(view);
    }

    private void setupIngredientsRecyclerView(View view) {
        ingredientsRecyclerView= view.findViewById(R.id.ingredientsRecyclerView);
        ingredientsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        detailedMealAdapter = new DetailedMealAdapter(requireContext(), meal);
        ingredientsRecyclerView.setAdapter(detailedMealAdapter);
    }
private void setupCalendarBtnFunction() {
    btnCalender.setOnClickListener(v -> {
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
                .setSelection(today)
                .setCalendarConstraints(constraints)
                .build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            String date = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(new Date(selection));
            detailedMealPresenter.insertCalenderedMeal(meal, date);
        });

        materialDatePicker.show(getActivity().getSupportFragmentManager(), "TAG");
    });
}
    private void initializeVideo(View view) {
        youtubeWebView= view.findViewById(R.id.youtubeWebView);
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        youtubeWebView.setWebViewClient(new WebViewClient());
        youtubeWebView.setWebChromeClient(new WebChromeClient());
        String videoUrl = meal.getStrYoutube().replace("watch?v=", "embed/");
        youtubeWebView.loadUrl(videoUrl);
    }

    private DetailedMealPresenter setupDetailedMealPresenter(){
        return new DetailedMealPresenter(MealRepository.getInstance(
                MealLocalDataSource.getDatabaseManager(getContext())), this);
    }

    @Override
    public void calendaredMealSuccess() {
        Snackbar.make(getView(), getString(R.string.meal_added_to_your_calendar_successfully), Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void insertingFavMealSuccess() {
        Snackbar.make(getView(), getString(R.string.meal_added_to_your_favourite_list), Snackbar.LENGTH_SHORT).show();
        reverseFavBtn(true);
    }

    @Override
    public void deleteFavMealSuccess() {
        Snackbar.make(getView(), getString(R.string.meal_removed_from_your_favourite_list), Snackbar.LENGTH_SHORT).show();
        reverseFavBtn(false);
    }

    @Override
    public void reverseFavBtn(Boolean doReverse) {
        if (doReverse){
            btnFav.setIconResource(R.drawable.favorite_filled);
            btnFav.setOnClickListener(v -> {
                detailedMealPresenter.deleteFavMeal(meal);
            });
        }
        else {
            btnFav.setIconResource(R.drawable.favorite_outline);
            btnFav.setOnClickListener(v -> {
                detailedMealPresenter.insertFavMeal(meal);
            });
        }

    }

    @Override
    public void onFailed(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }
}