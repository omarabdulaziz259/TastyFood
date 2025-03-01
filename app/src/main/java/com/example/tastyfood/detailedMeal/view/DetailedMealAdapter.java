package com.example.tastyfood.detailedMeal.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tastyfood.R;
import com.example.tastyfood.model.Meal;
public class DetailedMealAdapter extends RecyclerView.Adapter<DetailedMealAdapter.ViewHolder>{
    private final Context context;
    private Meal meal;

    public DetailedMealAdapter(Context context, Meal meal){
        this.context = context;
        this.meal = meal;
    }
    public void setMeals(Meal meal) {
        this.meal = meal;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailedMealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.detailed_meal_ing_cell, parent, false);
        DetailedMealAdapter.ViewHolder vh = new DetailedMealAdapter.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull DetailedMealAdapter.ViewHolder holder, int position) {
        holder.txtCellIngName.setText(meal.getIngredients().get(position));
        holder.txtCellMeasure.setText(meal.getMeasures().get(position));
        Glide.with(context).load("https://www.themealdb.com/images/ingredients/" + meal.getIngredients().get(position) +".png")
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(holder.imgCellIngredient);
    }
    @Override
    public int getItemCount() {
        return meal.getIngredients().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgCellIngredient;
        public TextView txtCellIngName, txtCellMeasure;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCellIngredient = itemView.findViewById(R.id.imgCellIngredient);
            txtCellIngName = itemView.findViewById(R.id.txtCellIngName);
            txtCellMeasure = itemView.findViewById(R.id.txtCellMeasure);
        }
    }
}