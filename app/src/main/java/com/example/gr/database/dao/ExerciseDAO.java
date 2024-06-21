package com.example.gr.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gr.model.Exercise;

import java.util.List;
@Dao
public interface ExerciseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Exercise> exerciseList);
    @Insert
    void insertExercise(Exercise exercise);

    @Query("SELECT * FROM exercise LIMIT 10")
    List<Exercise> getAllExercise();
    @Query("SELECT * FROM exercise WHERE name LIKE :searchSeq")
    List<Exercise> findExerciseByName(String searchSeq);
    @Query("SELECT * FROM exercise WHERE id=:id")
    Exercise getExerciseById(int id);

    @Delete
    void deleteExercise(Exercise exercise);

    @Update
    void updateExercise(Exercise exercise);

    @Query("DELETE from exercise")
    void deleteAllExercise();
}
