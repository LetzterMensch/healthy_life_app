package com.example.gr.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "food")
public class Food implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private float numberOfServings;
    private float servingSize;
    private int calories;
    private float carb;
    private float protein;
    private float fat;
    public Food(String name,int calories ,float protein, float fat, float carb){
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
        this.numberOfServings = 1.0f;
        this.servingSize = 100f;
    }
    @Ignore
    public Food(){
        this.numberOfServings = 1.0f;
        this.servingSize = 100f;
    }
    @Ignore
    public Food(String name, float numberOfServings, int calories){
        this.name = name;
        this.servingSize = 100f;
        this.calories = calories;
        this.numberOfServings = numberOfServings;
        this.servingSize = 100f;
    }
    @Ignore
    public Food(int id, String name, float numberOfServings, float servingSize, int calories, float carb, float protein, float fat) {
        this.id = id;
        this.name = name;
        this.numberOfServings = numberOfServings;
        this.servingSize = servingSize;
        this.calories = calories;
        this.carb = carb;
        this.protein = protein;
        this.fat = fat;
    }

    public float getCarb() {
        return carb;
    }

    public void setCarb(float carb) {
        this.carb = carb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(float numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public float getServingSize() {
        return servingSize;
    }

    public void setServingSize(float servingSize) {
        this.servingSize = servingSize;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }
}
