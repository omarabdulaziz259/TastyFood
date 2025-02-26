package com.example.tastyfood.detailedMeal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tastyfood.R;
import com.example.tastyfood.home.HomeAdapter;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.util.CountryCode;

import java.util.ArrayList;

public class DetailedMealFragment extends Fragment {
    private ImageView imgDetailedMeal, imgDetailedMealArea, imgDetailedMealCategory;
    private TextView txtDetailedMeal, txtDetailedMealArea, txtDetailedMealCategory, txtMealInstructions;
    private RecyclerView ingredientsRecyclerView;
    private WebView youtubeWebView;
    private Button btnCalender, btnFav;
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

        youtubeWebView= view.findViewById(R.id.youtubeWebView);
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        youtubeWebView.setWebViewClient(new WebViewClient());
        youtubeWebView.setWebChromeClient(new WebChromeClient());
        String videoUrl = meal.getStrYoutube().replace("watch?v=", "embed/");
        String html = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"" + videoUrl + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        youtubeWebView.loadData(html, "text/html", "utf-8");

        btnCalender= view.findViewById(R.id.btnCalender);
//        todo btnCalender.setOnClickListener();
        btnFav= view.findViewById(R.id.btnFav);
//        todo btnFav.setOnClickListener();
        ingredientsRecyclerView= view.findViewById(R.id.ingredientsRecyclerView);
        ingredientsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        detailedMealAdapter = new DetailedMealAdapter(requireContext(), meal);
        ingredientsRecyclerView.setAdapter(detailedMealAdapter);
        detailedMealAdapter.setMeals(meal);
        for (int i = 0; i < meal.getIngredients().size(); i++) {
            Log.i("TAG", "Ingredient " + i + " : " + meal.getIngredients().get(i));
        }
        for (int i = 0; i < meal.getMeasures().size(); i++) {
            Log.i("TAG", "measure "+ i +" : "+ meal.getMeasures().get(i));

        }
    }
    private DetailedMealPresenter setupDetailedMealPresenter(){
        return new DetailedMealPresenter();
    }
}