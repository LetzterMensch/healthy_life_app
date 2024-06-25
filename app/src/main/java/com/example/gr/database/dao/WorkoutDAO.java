package com.example.gr.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gr.model.Workout;

import java.util.List;

@Dao
public interface WorkoutDAO {
    @Insert
    void insertWorkout(Workout workout);

    @Query("SELECT * FROM workout LIMIT 10")
    List<Workout> getAllWorkout();
    @Query("SELECT * FROM workout WHERE diaryID = :diaryID")
    List<Workout> findWorkoutByDiaryId(int diaryID);
    @Query("SELECT * FROM workout WHERE id=:id")
    Workout getWorkoutById(int id);

    @Delete
    void deleteWorkout(Workout workout);

    @Update
    void updateWorkout(Workout workout);

    @Query("DELETE from workout")
    void deleteAllWorkout();
}
