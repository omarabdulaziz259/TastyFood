package com.example.tastyfood.util;

import android.util.Log;

import com.example.tastyfood.model.database.CalenderedMeal;
import com.example.tastyfood.model.database.FavMeal;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
