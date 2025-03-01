package com.example.tastyfood.util;

import android.util.Log;

import com.example.tastyfood.model.DBInserter;
import com.example.tastyfood.model.database.CalenderedMeal;
import com.example.tastyfood.model.database.FavMeal;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FireStoreManager {
    public static void saveFavMeal(FavMeal favMeal) {
        String userUID = UserValidation.getUser().getUid();
        String documentName = favMeal.getIdMeal();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userUID != null) {
            db.collection("users")
                    .document(userUID)
                    .collection("favMeal")
                    .document(documentName)
                    .set(favMeal)
                    .addOnSuccessListener( (a) -> Log.i("TAG", "saveFavMealToFirestore: Success " + a))
                    .addOnFailureListener( (a) -> Log.i("TAG", "saveFavMealToFirestore: failed " + a.getLocalizedMessage()));
        }
    }
    public static void saveCalendarMeal(CalenderedMeal calenderedMeal) {
        String userUID = UserValidation.getUser().getUid();
        String documentName = calenderedMeal.getIdMeal() + "_" + calenderedMeal.getDate();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userUID != null) {
            db.collection("users")
                    .document(userUID)
                    .collection("CalendaredMeal")
                    .document(documentName)
                    .set(calenderedMeal)
                    .addOnSuccessListener( (a) -> Log.i("TAG", "saveCalendarMealToFirestore: Success " + a))
                    .addOnFailureListener( (a) -> Log.i("TAG", "saveCalendarMealToFirestore: failed " + a.getLocalizedMessage()));
        }
    }
    public static void deleteFavMeal(FavMeal favMeal){
        String userUID = UserValidation.getUser().getUid();
        String documentName = favMeal.getIdMeal();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userUID != null) {
            db.collection("users")
                    .document(userUID)
                    .collection("favMeal")
                    .document(documentName)
                    .delete()
                    .addOnSuccessListener( (a) -> Log.i("TAG", "deleteFavMealToFirestore: Success " + a))
                    .addOnFailureListener( (a) -> Log.i("TAG", "deleteFavMealToFirestore: failed " + a.getLocalizedMessage()));
        }
    }
    public static void deleteCalendarMeal(CalenderedMeal calenderedMeal){
        String userUID = UserValidation.getUser().getUid();
        String documentName = calenderedMeal.getIdMeal() + "_" + calenderedMeal.getDate();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userUID != null) {
            db.collection("users")
                    .document(userUID)
                    .collection("CalendaredMeal")
                    .document(documentName)
                    .delete()
                    .addOnSuccessListener( (a) -> Log.i("TAG", "deleteCalendarMealToFirestore: Success " + a))
                    .addOnFailureListener( (a) -> Log.i("TAG", "deleteCalendarMealToFirestore: failed " + a.getLocalizedMessage()));
        }
    }

    public static void getAllCalendaredMeals(DBInserter dbInserter) {
        String userUID = UserValidation.getUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userUID != null) {
            db.collection("users")
                    .document(userUID)
                    .collection("CalendaredMeal")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<CalenderedMeal> calenderedMeals = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            CalenderedMeal meal = document.toObject(CalenderedMeal.class);
                            calenderedMeals.add(meal);
                        }
                        dbInserter.insertCalendarMeals(calenderedMeals);
                    })
                    .addOnFailureListener(e -> dbInserter.onFailure(e.getLocalizedMessage()));
        }
    }
    public static void getAllFavMeals(DBInserter dbInserter) {
        String userUID = UserValidation.getUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userUID != null) {
            db.collection("users")
                    .document(userUID)
                    .collection("favMeal")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<FavMeal> favMeals = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            FavMeal meal = document.toObject(FavMeal.class);
                            favMeals.add(meal);
                        }
                        dbInserter.insertFavMeals(favMeals);
                    })
                    .addOnFailureListener(e -> dbInserter.onFailure(e.getLocalizedMessage()));
        }
    }
}
