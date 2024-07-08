package com.example.gr.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.gr.ControllerApplication;
import com.example.gr.model.database.Converters;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.utils.DateTimeUtils;

import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
    private List<WorkoutItem> workoutList;

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

    public void updateDiary() {
        LocalDatabase.getInstance(ControllerApplication.getContext()).diaryDAO().updateDiary(this);
    }
    public void updateDiaryAfterRemove(WorkoutItem workoutItem){
        if(workoutItem.getType() == 1){
            Workout workout = (Workout) workoutItem;
            if(this.burntCalories - workout.getCaloriesBurnt() >= 0){
                this.burntCalories -= workout.getCaloriesBurnt();
                this.workoutList.remove(workout);
            }else {
                this.burntCalories = 0;
                this.workoutList.clear();
            }
        }else{
            RecordedWorkout recordedWorkout = (RecordedWorkout) workoutItem;
            if(this.burntCalories - recordedWorkout.getCaloriesBurnt() >= 0){
                this.burntCalories -= recordedWorkout.getCaloriesBurnt();
                this.workoutList.remove(recordedWorkout);
            }else {
                this.burntCalories = 0;
                this.workoutList.clear();
            }
        }
        recalculateRemainingCalories(this.intakeCalories,this.burntCalories);
        LocalDatabase.getInstance(ControllerApplication.getContext()).diaryDAO().updateDiary(this);
    }
    public void updateDiaryAfterRemove(FoodLog foodLog) {
        this.intakeCalories -= foodLog.getTotalCalories();
        this.intakeCarb -= foodLog.getTotalCarb();
        this.intakeProtein -= foodLog.getTotalProtein();
        this.intakeFat -= foodLog.getTotalFat();
        this.intakeFat = Math.max(this.intakeFat, 0);
        this.intakeCarb = Math.max(this.intakeCarb, 0);
        this.intakeProtein = Math.max(this.intakeProtein, 0);
        this.intakeCalories = Math.max(this.intakeCalories, 0);
        recalculateRemainingCalories(this.intakeCalories, this.burntCalories);
        LocalDatabase.getInstance(ControllerApplication.getContext()).diaryDAO().updateDiary(this);
    }

    private void getWorkoutList() {
        if (this.workoutList == null) {
            this.workoutList = new ArrayList<>();
        }
        this.workoutList.clear();
        this.workoutList.addAll(LocalDatabase.getInstance(ControllerApplication.getContext()).workoutDAO().findWorkoutByDiaryId(this.id));
        this.workoutList.addAll(LocalDatabase.getInstance(ControllerApplication.getContext()).recordedWorkoutDAO().findWorkoutByDiaryId(this.id));

    }

    public void logWorkoutItem(WorkoutItem workoutItem) {
        getWorkoutList();
        if(workoutItem.getType() == 1){
            Workout workout = (Workout) workoutItem;
            this.burntCalories += workout.getCaloriesBurnt();
            workout.setDiaryID(this.id);
            LocalDatabase.getInstance(ControllerApplication.getContext()).workoutDAO().insertWorkout(workout);
        }else{
            RecordedWorkout recordedWorkout = (RecordedWorkout) workoutItem;
            this.burntCalories += recordedWorkout.getCaloriesBurnt();
            recordedWorkout.setDiaryID(this.id);
            LocalDatabase.getInstance(ControllerApplication.getContext()).recordedWorkoutDAO().insertRecordedWorkout(recordedWorkout);
        }
        this.workoutList.add(workoutItem);
        updateDiaryAfterLogging();
    }


    public long getTotalWorkoutDuration() {// return milliseconds
        getWorkoutList();
        int sum = 0;
        for (WorkoutItem workoutItem : this.workoutList
        ) {
            sum += workoutItem.getDurationInMillis();
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
        if((this.caloriesGoal - intakeCalories + burntCalories) < 0){
            this.remainingCalories = 0;
            return;
        }
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
        return this.intakeCalories;
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
