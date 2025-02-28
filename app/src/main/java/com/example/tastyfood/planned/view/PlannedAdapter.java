package com.example.tastyfood.planned.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tastyfood.R;
import com.example.tastyfood.model.Meal;
import com.example.tastyfood.planned.model.PlannedHandler;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PlannedAdapter extends RecyclerView.Adapter<PlannedAdapter.ViewHolder>{
    private final Context context;
    private List<Meal> mealList;
    private PlannedHandler plannedHandler;

    public PlannedAdapter(Context context, List<Meal> mealList, PlannedHandler plannedHandler){
        this.context = context;
        this.mealList = mealList;
        this.plannedHandler = plannedHandler;
    }
    public void setMealsList(List<Meal> mealList) {
        this.mealList = mealList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlannedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.planned_meal_cell, parent, false);
        PlannedAdapter.ViewHolder vh = new PlannedAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlannedAdapter.ViewHolder holder, int position) {
        holder.txtCellNamePlanned.setText(mealList.get(position).getStrMeal());
        holder.ConstraintLayoutCellPlanned.setOnClickListener(v ->
            plannedHandler.navigateToDetailedMeal(mealList.get(position))
        );
        holder.btnDeletePlannedMeal.setOnClickListener( v -> deleteItemInPosition(position, v));

        Glide.with(context).load(mealList.get(position).getStrMealThumb())
                .placeholder(R.drawable.logo)
                .error(R.drawable.error)
                .into(holder.imgCellPlanned);
    }
    Boolean doDeletion;
    void deleteItemInPosition(int position, View view){
        doDeletion = true;
        Meal temp = new Meal(mealList.get(position));
        mealList.remove(mealList.get(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mealList.size());
        Snackbar.make(view, "Undo Deletion for : " + temp.getStrMeal(), Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> {
                    doDeletion = false;
                    mealList.add(position, temp);
                    notifyItemInserted(position);
                    notifyItemRangeChanged(position, mealList.size());
                }).setActionTextColor(context.getResources().getColor(R.color.black))
                .addCallback(new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (doDeletion){
                            plannedHandler.deleteCalendaredMeal(temp);
                        }
                    }
                }).show();
    }
    @Override
    public int getItemCount() {
        return mealList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout ConstraintLayoutCellPlanned;
        public TextView txtCellNamePlanned;
        public ImageView imgCellPlanned;
        public ImageButton btnDeletePlannedMeal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayoutCellPlanned = itemView.findViewById(R.id.ConstraintLayoutCellPlanned);
            txtCellNamePlanned = itemView.findViewById(R.id.txtCellNamePlanned);
            imgCellPlanned = itemView.findViewById(R.id.imgCellPlanned);
            btnDeletePlannedMeal = itemView.findViewById(R.id.btnDeletePlannedMeal);
        }
    }
}