package com.example.gr.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;

import android.util.Log;

import com.example.gr.ControllerApplication;
import com.example.gr.model.Exercise;
import com.example.gr.model.Food;
import com.example.gr.model.database.LocalDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class DataImporter {

    public static void importFoodFromJson(Context context, LocalDatabase db) {
        try {
            InputStream inputStream = context.getAssets().open(
                    "data3.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            Type foodListType = new TypeToken<List<Food>>(){}.getType();
            ArrayList<Food> foodList = new Gson().fromJson(reader, foodListType);
//            System.out.println(foodList.get(0).getNumberOfServings());
            // Insert data into Room database
            for (Food food: foodList
                 ) {
                String key = ControllerApplication.getApp().getFoodDatabaseReference().push().getKey();
                food.setUuid(key);
            }
            if (db.foodDAO().getAllFood().isEmpty()){
                db.foodDAO().insertAll(foodList);
            }
        } catch (Exception e) {
            Log.e("DataImporter", "Error importing data", e);
        }
    }
    public static void importExerciseFromJson(Context context, LocalDatabase db) {
        try {
            InputStream inputStream = context.getAssets().open(
                    "exercise.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            Type exerciseListType = new TypeToken<List<Exercise>>(){}.getType();
            ArrayList<Exercise> exerciseList = new Gson().fromJson(reader, exerciseListType);
            // Insert data into Room database
            if(db.exerciseDAO().getAllExercise().isEmpty()){
                db.exerciseDAO().insertAll(exerciseList);
            }
        } catch (Exception e) {
            Log.e("DataImporter", "Error importing data", e);
        }
    }
}
