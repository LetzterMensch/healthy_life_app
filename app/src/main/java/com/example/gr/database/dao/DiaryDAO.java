package com.example.gr.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gr.model.Diary;

import java.util.Date;
import java.util.List;

@Dao
public interface DiaryDAO {
    @Insert
    void insertDiary(Diary diary);

    @Query("SELECT * FROM diary LIMIT 5")
    List<Diary> getAllDiary();

    @Query("SELECT * FROM diary WHERE id=:id")
    Diary getDiaryById(int id);
    @Query("SELECT * FROM diary WHERE date LIKE :date")
    Diary getDiaryByDate(String date);
    @Delete
    void deleteDiary(Diary diary);

    @Update
    void updateDiary(Diary diary);

    @Query("DELETE from diary")
    void deleteAllDiary();
}