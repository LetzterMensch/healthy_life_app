package com.example.gr.model.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gr.model.RecordedWorkout;

import java.util.List;

@Dao
public interface RecordedWorkoutDAO {
    @Insert
    void insertRecordedWorkout(RecordedWorkout recordedWorkout);

    @Query("SELECT * FROM recordedworkout LIMIT 10")
    List<RecordedWorkout> getAllRecordedWorkout();
    @Query("SELECT * FROM recordedworkout WHERE timeStamp >= :timestamp")
    List<RecordedWorkout> findRecordedWorkoutWithTimeStamp(long timestamp);
    @Query("SELECT * FROM recordedworkout WHERE createdAt LIKE :dateTime")
    List<RecordedWorkout> findRecordedWorkoutByDate(String dateTime);
    @Query("SELECT * FROM recordedworkout WHERE id=:id")
    RecordedWorkout getRecordedWorkoutById(int id);
    @Query("SELECT * FROM recordedworkout WHERE timeStamp=:timestamp")
    RecordedWorkout getRecordedWorkoutByTimeStamp(long timestamp);

    @Delete
    void deleteRecordedWorkout(RecordedWorkout recordedWorkout);

    @Update
    void updateRecordedWorkout(RecordedWorkout recordedWorkout);

    @Query("DELETE from recordedWorkout")
    void deleteAllRecordedWorkout();
}
