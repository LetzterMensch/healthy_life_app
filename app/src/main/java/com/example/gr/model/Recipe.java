package com.example.gr.model;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.gr.ControllerApplication;
import com.example.gr.model.database.LocalDatabase;

import java.io.Serializable;
import java.util.Calendar;

@Entity(tableName = "recipe")
public class Recipe implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @Ignore
    private Food food;
    private int foodID;
    private String foodUUID;
    private String name;
    private String image;
    private String description;
    private float carbs;
    private float protein;
    private float fat;
    private int calories;
    private String ingredients;
    private String url;
    private long timestamp;
    public Recipe(){

    }
    @Ignore
    public Recipe(String name, String image, String description, float carbs, float protein, float fat, int calories, String ingredients, String url) {
        this.timestamp = Calendar.getInstance().getTimeInMillis();
        this.food = new Food(name,calories,protein,fat,carbs,this.timestamp);
        this.name = name;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.calories = calories;
        this.image = image;
        this.description = description;
        this.ingredients = ingredients;
        this.url = url;
    }
    public long saveRecipe(){
        return LocalDatabase.getInstance(ControllerApplication.getContext()).recipeDAO().insertRecipe(this);
    }
    public int updateRecipe(){
        return LocalDatabase.getInstance(ControllerApplication.getContext()).recipeDAO().updateRecipe(this);
    }

    public String getFoodUUID() {
        return foodUUID;
    }

    public void setFoodUUID(String foodUUID) {
        this.foodUUID = foodUUID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getCarbs() {
        return carbs;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
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

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
