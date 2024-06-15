package com.example.gr.database;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;

import android.util.Log;

import com.example.gr.model.Food;
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
//            List<Food> foodList = gson.fromJson(reader, userListType);
            System.out.println(foodList);
            // Insert data into Room database
            db.clearAllTables();
            db.foodDAO().insertAll(foodList);
        } catch (Exception e) {
            Log.e("DataImporter", "Error importing data", e);
        }
    }
}
