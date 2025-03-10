package com.example.tastyfood.model.network;

import com.example.tastyfood.model.Meal;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class MealDeserializer implements JsonDeserializer<Meal> {
    @Override
    public Meal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Meal meal = new Meal();
        if (jsonObject.has("idMeal")){
            meal.setIdMeal( jsonObject.get("idMeal").getAsString());
        }
        if (jsonObject.has("strMeal")){
            meal.setStrMeal(jsonObject.get("strMeal").getAsString());
        }
        if (jsonObject.has("strCategory")){
            meal.setStrCategory(jsonObject.get("strCategory").getAsString());
        }
        if (jsonObject.has("strArea")){
            meal.setStrArea(jsonObject.get("strArea").getAsString());
        }
        if (jsonObject.has("strInstructions")){
            meal.setStrInstructions(jsonObject.get("strInstructions").getAsString());
        }
        if (jsonObject.has("strMealThumb")){
            meal.setStrMealThumb(jsonObject.get("strMealThumb").getAsString());
        }
        if (jsonObject.has("strYoutube")){
            meal.setStrYoutube(jsonObject.get("strYoutube").getAsString());
        }
        if (jsonObject.has("strIngredient")){
            meal.setStrIngredient(jsonObject.get("strIngredient").getAsString());
        }
        for (int i = 1; i <= 20; i++) {
            String ingredientKey = "strIngredient" + i;
            String measureKey = "strMeasure" + i;

            if (jsonObject.has(ingredientKey) && !jsonObject.get(ingredientKey).isJsonNull()) {
                String ingredient = jsonObject.get(ingredientKey).getAsString();
                if (!ingredient.isEmpty()) {
                    meal.addIngredient(ingredient);
                }
            }
            if (jsonObject.has(measureKey) && !jsonObject.get(measureKey).isJsonNull()) {
                String measure = jsonObject.get(measureKey).getAsString();
                if (!measure.isEmpty()) {
                    meal.addMeasure(measure);
                }
            }
        }

        return meal;
        }
}