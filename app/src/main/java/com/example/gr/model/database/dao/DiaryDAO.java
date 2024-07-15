package com.example.gr.model.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gr.model.Diary;
import com.example.gr.model.Food;

import java.util.Date;
import java.util.List;

@Dao
public interface DiaryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDiary(Diary diary);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Diary> diaryList);
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
