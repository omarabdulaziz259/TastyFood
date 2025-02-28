package com.example.tastyfood.search.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tastyfood.R;
import com.example.tastyfood.home.model.HomeNavigator;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.search.model.SearchNavigator;
import com.example.tastyfood.util.CountryCode;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    public static final int COUNTRIES = 0;
    public static final int CATEGORIES = 1;
    public static final int INGREDIENTS = 2;
    public static final int MEALS = 3;

    private final Context context;
    private List<Meal> mealList;
    private SearchNavigator searchNavigator;
    private int type;

    public SearchAdapter(Context context, List<Meal> mealList, SearchNavigator searchNavigator){
        this.context = context;
        this.mealList = mealList;
        this.searchNavigator = searchNavigator;
    }

    public int getType(){
        return type;
    }
    public List<Meal> getMealList() {
        return mealList;
    }

    public void setMealsList(List<Meal> mealList, int type) {
        this.type = type;
        setMealsList(mealList);
    }
    public void setMealsList(List<Meal> mealList) {
        this.mealList = mealList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.search_cell, parent, false);
        SearchAdapter.ViewHolder vh = new SearchAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        if (type == COUNTRIES){
            holder.txtSearchCell.setText(meal.getStrArea());
            holder.constraintLayoutSearchCell.setOnClickListener(v -> {
                searchNavigator.getFilteredMealsByCountry(meal.getStrArea());
            });
            Glide.with(context).load("https://www.themealdb.com/images/icons/flags/big/64/" +
                            CountryCode.getCountryCode(meal.getStrArea()) + ".png")
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.error)
                    .into(holder.imgSearchCell);

        } else if (type == CATEGORIES){
            holder.txtSearchCell.setText(meal.getStrCategory());
            holder.constraintLayoutSearchCell.setOnClickListener(v -> {
                searchNavigator.getFilteredMealsByCategory(meal.getStrCategory());
            });
            Glide.with(context)
                    .load("https://www.themealdb.com/images/category/" +meal.getStrCategory() + ".png")
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.error)
                    .into(holder.imgSearchCell);

        } else if(type == INGREDIENTS){
            holder.txtSearchCell.setText(meal.getStrIngredient());
            holder.constraintLayoutSearchCell.setOnClickListener(v -> {
                searchNavigator.getFilteredMealsByIngredients(meal.getStrIngredient());
            });
            Glide.with(context).load("https://www.themealdb.com/images/ingredients/" +meal.getStrIngredient() + ".png")
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.error)
                    .into(holder.imgSearchCell);
        } else if(type == MEALS){
            holder.txtSearchCell.setText(meal.getStrMeal());
            holder.constraintLayoutSearchCell.setOnClickListener(v -> {
                searchNavigator.navigateToDetailedMeal(meal);
            });
            Glide.with(context).load(meal.getStrMealThumb())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.error)
                    .into(holder.imgSearchCell);
        }
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout constraintLayoutSearchCell;
        public TextView txtSearchCell;
        public ImageView imgSearchCell;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutSearchCell = itemView.findViewById(R.id.constraintLayoutSearchCell);
            txtSearchCell = itemView.findViewById(R.id.txtSearchCell);
            imgSearchCell = itemView.findViewById(R.id.imgSearchCell);
        }
    }
}
