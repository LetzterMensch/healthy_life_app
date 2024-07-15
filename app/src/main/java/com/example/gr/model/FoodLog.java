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
    private String foodUUID;
    private int diaryId;
    @Ignore
    public Food food; // FoodLog inside foodLogList does not have food -> null. Because of @Ignore
    public FoodLog(){}
    @Ignore
    public FoodLog(Food food, int meal, float numberOfServings, int diaryId){
        this.food = food;
        this.foodId = food.getId();
        this.foodUUID = food.getUuid();
        this.diaryId = diaryId;
        this.meal = meal;
        this.numberOfServings = numberOfServings;
        this.totalCalories =  Math.round(food.getCalories()*numberOfServings);
        if(food.getCarb() + food.getProtein() + food.getFat() < (food.getCalories() - 10)){
            double calDiff = (food.getFat() * 9 + (food.getCarb() + food.getProtein())*4) - food.getCalories() ;
            float correctionVal = (float) (calDiff/3);
            if(correctionVal > 0){
                this.totalFat = Math.round((food.getFat()*9 - correctionVal)*numberOfServings);
                this.totalCarb =  Math.round((food.getCarb()*4 - correctionVal)*numberOfServings);
                this.totalProtein =  Math.round((food.getProtein()*4 - correctionVal)*numberOfServings);
            }else{
                this.totalFat = food.getFat()*numberOfServings*9;
                this.totalCarb = food.getCarb()*numberOfServings*4;
                this.totalProtein = food.getProtein()*numberOfServings*4;
            }
        }else{
            this.totalFat = food.getFat()*numberOfServings;
            this.totalCarb = food.getCarb()*numberOfServings;
            this.totalProtein = food.getProtein()*numberOfServings;
        }
    }
    public void updateFoodLog(float numberOfServings, boolean isMeasuredByGram){
        this.numberOfServings = numberOfServings;
        this.totalCalories =  Math.round(food.getCalories()*numberOfServings);
        if(isMeasuredByGram){
            this.totalFat = food.getFat()*numberOfServings*9;
            this.totalCarb = food.getCarb()*numberOfServings*4;
            this.totalProtein = food.getProtein()*numberOfServings*4;
        }else{
            this.totalFat = food.getFat()*numberOfServings;
            this.totalCarb = food.getCarb()*numberOfServings;
            this.totalProtein = food.getProtein()*numberOfServings;
        }

    }

    public String getFoodUUID() {
        return foodUUID;
    }

    public void setFoodUUID(String foodUUID) {
        this.foodUUID = foodUUID;
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
