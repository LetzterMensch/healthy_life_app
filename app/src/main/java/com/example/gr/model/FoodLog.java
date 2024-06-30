package com.example.gr.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "foodlog")
public class FoodLog implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private float totalCarb;
    private float totalProtein;
    private float totalFat;
    private int totalCalories;
    private int meal;
    private float numberOfServings;
    private int foodId;
    private int diaryId;
    @Ignore
    public Food food; // FoodLog inside foodLogList does not have food -> null. Because of @Ignore
    public FoodLog(){}
    @Ignore
    public FoodLog(Food food, int meal, float numberOfServings, int diaryId){
        this.food = food;
        this.foodId = food.getId();
        this.diaryId = diaryId;
        this.meal = meal;
        this.numberOfServings = numberOfServings;
        this.totalCalories =  Math.round(food.getCalories()*numberOfServings);
        this.totalFat = food.getFat()*numberOfServings*9;
        this.totalCarb = food.getCarb()*numberOfServings*4;
        this.totalProtein = food.getProtein()*numberOfServings*4;
    }
    public void updateFoodLog(float numberOfServings){
        this.numberOfServings = numberOfServings;
        this.totalCalories =  Math.round(food.getCalories()*numberOfServings);
        this.totalFat = food.getFat()*numberOfServings;
        this.totalCarb = food.getCarb()*numberOfServings;
        this.totalProtein = food.getProtein()*numberOfServings;
    }

    public int getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(int diaryId) {
        this.diaryId = diaryId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTotalCarb() {
        return totalCarb;
    }

    public void setTotalCarb(float totalCarb) {
        this.totalCarb = totalCarb;
    }

    public float getTotalProtein() {
        return totalProtein;
    }

    public void setTotalProtein(float totalProtein) {
        this.totalProtein = totalProtein;
    }

    public float getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(float totalFat) {
        this.totalFat = totalFat;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public int getMeal() {
        return meal;
    }

    public void setMeal(int meal) {
        this.meal = meal;
    }

    public float getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(float numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public Food getFood() {
        return this.food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
