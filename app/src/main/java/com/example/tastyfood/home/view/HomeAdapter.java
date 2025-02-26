package com.example.tastyfood.home.view;

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

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    private final Context context;
    private List<Meal> mealList;
    private HomeNavigator homeNavigator;

    public HomeAdapter(Context context, List<Meal> mealList, HomeNavigator homeNavigator){
        this.context = context;
        this.mealList = mealList;
        this.homeNavigator = homeNavigator;
    }
    public void setMealsList(List<Meal> mealList) {
        this.mealList = mealList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.meal_cell, parent, false);
        HomeAdapter.ViewHolder vh = new HomeAdapter.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCellName.setText(mealList.get(position).getStrMeal());
        holder.constraintLayoutCell.setOnClickListener(v -> {
            homeNavigator.navigateToDetailedMeal(mealList.get(position));
        });
        Glide.with(context).load(mealList.get(position).getStrMealThumb())
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(holder.imgCell);
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout constraintLayoutCell;
        public TextView txtCellName;
        public ImageView imgCell;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutCell = itemView.findViewById(R.id.ConstraintLayoutCell);
            txtCellName = itemView.findViewById(R.id.txtCellName);
            imgCell = itemView.findViewById(R.id.imgCell);
        }
    }
}