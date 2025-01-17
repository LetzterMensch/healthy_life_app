package com.example.gr.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.gr.model.WeightLog;
import com.example.gr.model.database.dao.DiaryDAO;
import com.example.gr.model.database.dao.ExerciseDAO;
import com.example.gr.model.database.dao.FoodDAO;
import com.example.gr.model.database.dao.FoodLogDAO;
import com.example.gr.model.database.dao.RecipeDAO;
import com.example.gr.model.database.dao.RecordedWorkoutDAO;
import com.example.gr.model.database.dao.WeightLogDAO;
import com.example.gr.model.database.dao.WorkoutDAO;
import com.example.gr.model.Diary;
import com.example.gr.model.Exercise;
import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;
import com.example.gr.model.Recipe;
import com.example.gr.model.RecordedWorkout;
import com.example.gr.model.Workout;

@Database(entities = {Food.class, Diary.class, Workout.class, FoodLog.class, Recipe.class, Exercise.class, RecordedWorkout.class, WeightLog.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class LocalDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "dev.db";

    private static LocalDatabase instance;

    public static synchronized LocalDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract FoodDAO foodDAO();
    public abstract DiaryDAO diaryDAO();
    public abstract ExerciseDAO exerciseDAO();
    public abstract FoodLogDAO foodLogDAO();
    public abstract WorkoutDAO workoutDAO();
    public abstract RecipeDAO recipeDAO();
    public abstract RecordedWorkoutDAO recordedWorkoutDAO();
    public abstract WeightLogDAO weightLogDAO();
}
