package com.example.gr.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

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
    private boolean isCustomized;
    private String subFoodIds;
    private long timestamp;
    @Ignore
    private List<Food> subFoodList;
    // recipe -> food
    @Ignore
    public Food(String name,int calories ,float protein, float fat, float carb, long timestamp){
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
        this.numberOfServings = 1.0f;
        this.servingSize = 1.0f;
        this.timestamp = timestamp;
        this.isCustomized = true;
    }
    // super-food -> food
    public Food(String name,int calories ,float protein, float fat, float carb, String subFoodIds){
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
        this.numberOfServings = 1.0f;
        this.servingSize = 1.0f;
        this.timestamp = Calendar.getInstance().getTimeInMillis();
        this.subFoodIds = subFoodIds;
        this.isCustomized = true;
    }
    @Ignore
    public Food(String name,float servingSize,int calories ,float protein, float fat, float carb){
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
        this.numberOfServings = 1.0f;
        this.servingSize = servingSize;
        this.timestamp = Calendar.getInstance().getTimeInMillis();
        this.isCustomized = true;
    }
    @Ignore
    public Food(){
        this.numberOfServings = 1.0f;
        this.servingSize = 100f;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isCustomized() {
        return isCustomized;
    }

    public void setCustomized(boolean customized) {
        isCustomized = customized;
    }

    public String getSubFoodIds() {
        return subFoodIds;
    }

    public void setSubFoodIds(String subFoodIds) {
        this.subFoodIds = subFoodIds;
    }

    public List<Food> getSubFoodList() {
        return subFoodList;
    }

    public void setSubFoodList(List<Food> subFoodList) {
        this.subFoodList = subFoodList;
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
