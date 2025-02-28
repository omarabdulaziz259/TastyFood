package com.example.tastyfood.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "MealDetails")
public class Meal implements Serializable {
    @PrimaryKey
    @NonNull
    private String idMeal;
    private String strMeal;
    private String strCategory;
    private String strArea;
    private String strInstructions;
    private String strMealThumb;
    private String strYoutube;
    private List<String> ingredients;
    private List<String> measures;
    @Ignore
    private String strIngredient;

    public Meal() {
        ingredients = new ArrayList<>();
        measures = new ArrayList<>();
    }

    public Meal(Meal meal){
        this.idMeal = meal.getIdMeal();
        this.strMeal = meal.getStrMeal();
        this.strCategory = meal.getStrCategory();
        this.strArea = meal.getStrArea();
        this.strInstructions = meal.getStrInstructions();
        this.strMealThumb = meal.getStrMealThumb();
        this.strYoutube = meal.getStrYoutube();
        this.ingredients = new ArrayList<>();
        this.ingredients.addAll(meal.getIngredients());
        this.measures = new ArrayList<>();
        this.measures.addAll(meal.getMeasures());
        this.strIngredient = meal.strIngredient;
    }

    public Meal(@NonNull String idMeal, String strMeal, String strCategory, String strArea,
                String strInstructions, String strMealThumb, String strYoutube,
                ArrayList<String> ingredients, ArrayList<String> measures, String strIngredient) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strInstructions = strInstructions;
        this.strMealThumb = strMealThumb;
        this.strYoutube = strYoutube;
        this.ingredients = ingredients;
        this.measures = measures;
        this.strIngredient = strIngredient;
    }

    public void addIngredient(String ingredient){
        ingredients.add(ingredient);
    }
    public void addMeasure(String measure){
        measures.add(measure);
    }
    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public String getStrYoutube() {
        return strYoutube;
    }

    public void setStrYoutube(String strYoutube) {
        this.strYoutube = strYoutube;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getMeasures() {
        return measures;
    }

    public void setMeasures(ArrayList<String> measures) {
        this.measures = measures;
    }

    @Ignore
    public String getStrIngredient() {
        return strIngredient;
    }
    @Ignore

    public void setStrIngredient(String strIngredient) {
        this.strIngredient = strIngredient;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "idMeal='" + idMeal + '\'' +
                ", strMeal='" + strMeal + '\'' +
                ", strCategory='" + strCategory + '\'' +
                ", strArea='" + strArea + '\'' +
                ", strInstructions='" + strInstructions + '\'' +
                ", strMealThumb='" + strMealThumb + '\'' +
                ", strYoutube='" + strYoutube + '\'' +
                ", ingredients=" + ingredients +
                ", measures=" + measures +
                '}';
    }
}