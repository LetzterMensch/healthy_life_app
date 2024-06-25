package com.example.gr.model;

import android.service.controls.Control;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.gr.ControllerApplication;
import com.example.gr.adapter.FoodLogAdapter;
import com.example.gr.database.Converters;
import com.example.gr.database.LocalDatabase;
import com.example.gr.utils.DateTimeUtils;

import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity(tableName = "diary")
@TypeConverters({Converters.class})
public class Diary implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int caloriesGoal;
    private int burntCalories;
    private int userId;
    private int remainingCalories;
    private String date;
    private int intakeProtein;
    private int intakeCarb;
    private int intakeFat;
    private int intakeCalories;
    private int totalSteps;
    private int carbGoal;
    private int proteinGoal;
    private int fatGoal;
    @Ignore
    private List<FoodLog> breakfastLogs;
    @Ignore
    private List<FoodLog> lunchLogs;
    @Ignore
    private List<FoodLog> dinnerLogs;
    @Ignore
    private List<FoodLog> snackLogs;
    @Ignore
    private List<Workout> workoutList;

    public Diary() {
        this.date = DateTimeUtils.simpleDateFormat(Calendar.getInstance().getTime());
        ActivityUser activityUser = new ActivityUser();
        caloriesGoal = activityUser.getCaloriesBurntGoal();
        carbGoal = activityUser.getActivityUserCarbGoal();
        proteinGoal = activityUser.getActivityUserProteinGoal();
        fatGoal = activityUser.getActivityUserFatGoal();
    }

    @Ignore
    public Diary(String date) {
        this.date = date;
        this.totalSteps = 0;
        ActivityUser activityUser = new ActivityUser();
        this.caloriesGoal = activityUser.getCaloriesBurntGoal();
        this.carbGoal = activityUser.getActivityUserCarbGoal();
        this.proteinGoal = activityUser.getActivityUserProteinGoal();
        this.fatGoal = activityUser.getActivityUserFatGoal();
    }

    public void updateDiaryAfterLogging() {
        recalculateRemainingCalories(this.intakeCalories, this.burntCalories);
        LocalDatabase.getInstance(ControllerApplication.getContext()).diaryDAO().updateDiary(this);
    }

    public void updateDiaryAfterRemove(FoodLog foodLog) {
        this.intakeCalories -= foodLog.getTotalCalories();
        this.intakeCarb -= foodLog.getTotalCarb();
        this.intakeProtein -= foodLog.getTotalProtein();
        this.intakeFat -= foodLog.getTotalFat();
        recalculateRemainingCalories(this.intakeCalories, this.burntCalories);
        LocalDatabase.getInstance(ControllerApplication.getContext()).diaryDAO().updateDiary(this);
    }
    private void getWorkoutList(){
        this.workoutList = LocalDatabase.getInstance(ControllerApplication.getContext()).workoutDAO().findWorkoutByDiaryId(this.id);
        if (this.workoutList == null) {
            this.workoutList = new ArrayList<>();
        }
    }
    public void logWorkout(Workout workout) {
        getWorkoutList();
        this.workoutList.add(workout);
        this.burntCalories += workout.getCaloriesBurnt();
        LocalDatabase.getInstance(ControllerApplication.getContext()).workoutDAO().insertWorkout(workout);
        updateDiaryAfterLogging();
    }

    public int getTotalWorkoutDuration() {
        getWorkoutList();
        int sum = 0;
        for (Workout workout : this.workoutList
        ) {
            System.out.println(workout.getDiaryID());
            sum += workout.getDuration();
        }
        return sum;
    }

    public void updateFoodLog(FoodLog foodLog) {
        LocalDatabase.getInstance(ControllerApplication.getContext()).foodLogDAO().updateFoodLog(foodLog);
        this.intakeCalories += foodLog.getTotalCalories();
        this.intakeCarb += foodLog.getTotalCarb();
        this.intakeFat += foodLog.getTotalFat();
        this.intakeProtein += foodLog.getTotalProtein();
        updateDiaryAfterLogging();
    }

    public void logFood(FoodLog foodLog) {
        LocalDatabase.getInstance(ControllerApplication.getContext()).foodLogDAO().insertFoodLog(foodLog);
        switch (foodLog.getMeal()) {
            case 0:
                breakfastLogs.add(foodLog);
                break;
            case 1:
                lunchLogs.add(foodLog);
                break;
            case 2:
                dinnerLogs.add(foodLog);
                break;
            case 3:
                snackLogs.add(foodLog);
                break;
        }
        this.intakeCalories += foodLog.getTotalCalories();
        this.intakeCarb += foodLog.getTotalCarb();
        this.intakeFat += foodLog.getTotalFat();
        this.intakeProtein += foodLog.getTotalProtein();
        updateDiaryAfterLogging();
    }

    protected void recalculateRemainingCalories(int intakeCalories, int burntCalories) {
        this.remainingCalories = this.caloriesGoal - intakeCalories + burntCalories;
    }

    public List<FoodLog> getBreakfastLogs() {
        this.breakfastLogs = LocalDatabase.getInstance(ControllerApplication.getContext()).foodLogDAO().getBreakfastFoodLogs(this.id);
        return this.breakfastLogs;
    }

    public List<FoodLog> getLunchLogs() {
        this.lunchLogs = LocalDatabase.getInstance(ControllerApplication.getContext()).foodLogDAO().getLunchFoodLogs(this.id);
        return this.lunchLogs;
    }

    public List<FoodLog> getDinnerLogs() {
        this.dinnerLogs = LocalDatabase.getInstance(ControllerApplication.getContext()).foodLogDAO().getDinnerFoodLogs(this.id);
        return this.dinnerLogs;
    }

    public List<FoodLog> getSnackLogs() {
        this.snackLogs = LocalDatabase.getInstance(ControllerApplication.getContext()).foodLogDAO().getSnackFoodLogs(this.id);
        return this.snackLogs;
    }

    public void setBreakfastLogs(List<FoodLog> breakfastLogs) {
        this.breakfastLogs = breakfastLogs;
    }


    public void setLunchLogs(List<FoodLog> lunchLogs) {
        this.lunchLogs = lunchLogs;
    }


    public void setDinnerLogs(List<FoodLog> dinnerLogs) {
        this.dinnerLogs = dinnerLogs;
    }


    public void setSnackLogs(List<FoodLog> snackLogs) {
        this.snackLogs = snackLogs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCaloriesGoal() {
        return caloriesGoal;
    }

    public void setCaloriesGoal(int caloriesGoal) {
        this.caloriesGoal = caloriesGoal;
    }

    public int getBurntCalories() {
        return burntCalories;
    }

    public void setBurntCalories(int burntCalories) {
        this.burntCalories = burntCalories;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRemainingCalories() {
        return remainingCalories;
    }

    public void setRemainingCalories(int remainingCalories) {
        this.remainingCalories = remainingCalories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIntakeProtein() {
        return intakeProtein;
    }

    public void setIntakeProtein(int intakeProtein) {
        this.intakeProtein = intakeProtein;
    }

    public int getIntakeCarb() {
        return intakeCarb;
    }

    public void setIntakeCarb(int intakeCarb) {
        this.intakeCarb = intakeCarb;
    }

    public int getIntakeFat() {
        return intakeFat;
    }

    public void setIntakeFat(int intakeFat) {
        this.intakeFat = intakeFat;
    }

    public int getIntakeCalories() {
        return intakeCalories;
    }

    public void setIntakeCalories(int intakeCalories) {
        this.intakeCalories = intakeCalories;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getCarbGoal() {
        return carbGoal;
    }

    public void setCarbGoal(int carbGoal) {
        this.carbGoal = carbGoal;
    }

    public int getProteinGoal() {
        return proteinGoal;
    }

    public void setProteinGoal(int proteinGoal) {
        this.proteinGoal = proteinGoal;
    }

    public int getFatGoal() {
        return fatGoal;
    }

    public void setFatGoal(int fatGoal) {
        this.fatGoal = fatGoal;
    }

}
