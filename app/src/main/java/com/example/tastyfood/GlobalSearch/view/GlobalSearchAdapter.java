package com.example.tastyfood.GlobalSearch.view;

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
import com.example.tastyfood.GlobalSearch.model.GlobalSearchHandler;
import com.example.tastyfood.R;
import com.example.tastyfood.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class GlobalSearchAdapter extends RecyclerView.Adapter<GlobalSearchAdapter.ViewHolder> {
    private final Context context;
    private List<Meal> mealList = new ArrayList<>();
    private GlobalSearchHandler globalSearchHandler;
    public GlobalSearchAdapter(Context context, List<Meal> mealList, GlobalSearchHandler globalSearchHandler){
        this.context = context;
        this.mealList = mealList;
        this.globalSearchHandler = globalSearchHandler;
    }
    public void setMealsList(List<Meal> mealList) {
        this.mealList = (mealList != null) ? mealList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.search_cell, parent, false);
        GlobalSearchAdapter.ViewHolder vh = new GlobalSearchAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.txtSearchCell.setText(meal.getStrMeal());
        holder.constraintLayoutSearchCell.setOnClickListener(v -> {
            globalSearchHandler.navigateToDetailedMeal(meal);
        });
        Glide.with(context).load(meal.getStrMealThumb())
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(holder.imgSearchCell);
    }

    @Override
    public int getItemCount() {
        return (mealList != null) ? mealList.size() : 0;
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
