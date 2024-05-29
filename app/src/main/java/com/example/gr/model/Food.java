package com.example.gr.model;

import java.io.Serializable;

public class Food implements Serializable {

    private int id;
    private String name;
    private int numberOfServings;
    private float servingSize;
    private float calories;
    private float carbs;
    private float protein;
    private float fat;
    public Food(String name, float servingSize, float calories){
        this.name = name;
        this.servingSize = servingSize;
        this.calories = calories;
    }
    public Food(int id, String name, int numberOfServings, float servingSize, float calories, float carbs, float protein, float fat) {
        this.id = id;
        this.name = name;
        this.numberOfServings = numberOfServings;
        this.servingSize = servingSize;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
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

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public float getServingSize() {
        return servingSize;
    }

    public void setServingSize(float servingSize) {
        this.servingSize = servingSize;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
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
}
