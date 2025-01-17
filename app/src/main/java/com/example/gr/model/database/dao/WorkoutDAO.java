package com.example.gr.model.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gr.model.Food;
import com.example.gr.model.Workout;

import java.util.List;

@Dao
public interface WorkoutDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWorkout(Workout workout);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Workout> workoutList);
    @Query("SELECT * FROM workout LIMIT 10")
    List<Workout> getAllWorkout();
    @Query("SELECT * FROM workout WHERE diaryID = :diaryID")
    List<Workout> findWorkoutByDiaryId(int diaryID);
    @Query("SELECT * FROM workout WHERE createdAt LIKE :dateTime")
    List<Workout> findWorkoutByDate(String dateTime);
    @Query("SELECT * FROM workout WHERE id=:id")
    Workout getWorkoutById(int id);

    @Delete
    void deleteWorkout(Workout workout);

    @Update
    void updateWorkout(Workout workout);

    @Query("DELETE from workout")
    void deleteAllWorkout();
}
