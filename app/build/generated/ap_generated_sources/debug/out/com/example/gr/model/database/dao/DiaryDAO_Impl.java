package com.example.gr.model.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.gr.model.Diary;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DiaryDAO_Impl implements DiaryDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Diary> __insertionAdapterOfDiary;

  private final EntityDeletionOrUpdateAdapter<Diary> __deletionAdapterOfDiary;

  private final EntityDeletionOrUpdateAdapter<Diary> __updateAdapterOfDiary;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllDiary;

  public DiaryDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDiary = new EntityInsertionAdapter<Diary>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `diary` (`id`,`caloriesGoal`,`burntCalories`,`userId`,`remainingCalories`,`date`,`intakeProtein`,`intakeCarb`,`intakeFat`,`intakeCalories`,`totalSteps`,`carbGoal`,`proteinGoal`,`fatGoal`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Diary entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCaloriesGoal());
        statement.bindLong(3, entity.getBurntCalories());
        statement.bindLong(4, entity.getUserId());
        statement.bindLong(5, entity.getRemainingCalories());
        if (entity.getDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDate());
        }
        statement.bindLong(7, entity.getIntakeProtein());
        statement.bindLong(8, entity.getIntakeCarb());
        statement.bindLong(9, entity.getIntakeFat());
        statement.bindLong(10, entity.getIntakeCalories());
        statement.bindLong(11, entity.getTotalSteps());
        statement.bindLong(12, entity.getCarbGoal());
        statement.bindLong(13, entity.getProteinGoal());
        statement.bindLong(14, entity.getFatGoal());
      }
    };
    this.__deletionAdapterOfDiary = new EntityDeletionOrUpdateAdapter<Diary>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `diary` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Diary entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfDiary = new EntityDeletionOrUpdateAdapter<Diary>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `diary` SET `id` = ?,`caloriesGoal` = ?,`burntCalories` = ?,`userId` = ?,`remainingCalories` = ?,`date` = ?,`intakeProtein` = ?,`intakeCarb` = ?,`intakeFat` = ?,`intakeCalories` = ?,`totalSteps` = ?,`carbGoal` = ?,`proteinGoal` = ?,`fatGoal` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Diary entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCaloriesGoal());
        statement.bindLong(3, entity.getBurntCalories());
        statement.bindLong(4, entity.getUserId());
        statement.bindLong(5, entity.getRemainingCalories());
        if (entity.getDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDate());
        }
        statement.bindLong(7, entity.getIntakeProtein());
        statement.bindLong(8, entity.getIntakeCarb());
        statement.bindLong(9, entity.getIntakeFat());
        statement.bindLong(10, entity.getIntakeCalories());
        statement.bindLong(11, entity.getTotalSteps());
        statement.bindLong(12, entity.getCarbGoal());
        statement.bindLong(13, entity.getProteinGoal());
        statement.bindLong(14, entity.getFatGoal());
        statement.bindLong(15, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllDiary = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE from diary";
        return _query;
      }
    };
  }

  @Override
  public void insertDiary(final Diary diary) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDiary.insert(diary);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<Diary> diaryList) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDiary.insert(diaryList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteDiary(final Diary diary) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfDiary.handle(diary);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateDiary(final Diary diary) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfDiary.handle(diary);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllDiary() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllDiary.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllDiary.release(_stmt);
    }
  }

  @Override
  public List<Diary> getAllDiary() {
    final String _sql = "SELECT * FROM diary LIMIT 5";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfCaloriesGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesGoal");
      final int _cursorIndexOfBurntCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "burntCalories");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfRemainingCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "remainingCalories");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfIntakeProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeProtein");
      final int _cursorIndexOfIntakeCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeCarb");
      final int _cursorIndexOfIntakeFat = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeFat");
      final int _cursorIndexOfIntakeCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeCalories");
      final int _cursorIndexOfTotalSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSteps");
      final int _cursorIndexOfCarbGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "carbGoal");
      final int _cursorIndexOfProteinGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinGoal");
      final int _cursorIndexOfFatGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "fatGoal");
      final List<Diary> _result = new ArrayList<Diary>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Diary _item;
        _item = new Diary();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpCaloriesGoal;
        _tmpCaloriesGoal = _cursor.getInt(_cursorIndexOfCaloriesGoal);
        _item.setCaloriesGoal(_tmpCaloriesGoal);
        final int _tmpBurntCalories;
        _tmpBurntCalories = _cursor.getInt(_cursorIndexOfBurntCalories);
        _item.setBurntCalories(_tmpBurntCalories);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        final int _tmpRemainingCalories;
        _tmpRemainingCalories = _cursor.getInt(_cursorIndexOfRemainingCalories);
        _item.setRemainingCalories(_tmpRemainingCalories);
        final String _tmpDate;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmpDate = null;
        } else {
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
        }
        _item.setDate(_tmpDate);
        final int _tmpIntakeProtein;
        _tmpIntakeProtein = _cursor.getInt(_cursorIndexOfIntakeProtein);
        _item.setIntakeProtein(_tmpIntakeProtein);
        final int _tmpIntakeCarb;
        _tmpIntakeCarb = _cursor.getInt(_cursorIndexOfIntakeCarb);
        _item.setIntakeCarb(_tmpIntakeCarb);
        final int _tmpIntakeFat;
        _tmpIntakeFat = _cursor.getInt(_cursorIndexOfIntakeFat);
        _item.setIntakeFat(_tmpIntakeFat);
        final int _tmpIntakeCalories;
        _tmpIntakeCalories = _cursor.getInt(_cursorIndexOfIntakeCalories);
        _item.setIntakeCalories(_tmpIntakeCalories);
        final int _tmpTotalSteps;
        _tmpTotalSteps = _cursor.getInt(_cursorIndexOfTotalSteps);
        _item.setTotalSteps(_tmpTotalSteps);
        final int _tmpCarbGoal;
        _tmpCarbGoal = _cursor.getInt(_cursorIndexOfCarbGoal);
        _item.setCarbGoal(_tmpCarbGoal);
        final int _tmpProteinGoal;
        _tmpProteinGoal = _cursor.getInt(_cursorIndexOfProteinGoal);
        _item.setProteinGoal(_tmpProteinGoal);
        final int _tmpFatGoal;
        _tmpFatGoal = _cursor.getInt(_cursorIndexOfFatGoal);
        _item.setFatGoal(_tmpFatGoal);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Diary getDiaryById(final int id) {
    final String _sql = "SELECT * FROM diary WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfCaloriesGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesGoal");
      final int _cursorIndexOfBurntCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "burntCalories");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfRemainingCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "remainingCalories");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfIntakeProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeProtein");
      final int _cursorIndexOfIntakeCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeCarb");
      final int _cursorIndexOfIntakeFat = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeFat");
      final int _cursorIndexOfIntakeCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeCalories");
      final int _cursorIndexOfTotalSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSteps");
      final int _cursorIndexOfCarbGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "carbGoal");
      final int _cursorIndexOfProteinGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinGoal");
      final int _cursorIndexOfFatGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "fatGoal");
      final Diary _result;
      if (_cursor.moveToFirst()) {
        _result = new Diary();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpCaloriesGoal;
        _tmpCaloriesGoal = _cursor.getInt(_cursorIndexOfCaloriesGoal);
        _result.setCaloriesGoal(_tmpCaloriesGoal);
        final int _tmpBurntCalories;
        _tmpBurntCalories = _cursor.getInt(_cursorIndexOfBurntCalories);
        _result.setBurntCalories(_tmpBurntCalories);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpRemainingCalories;
        _tmpRemainingCalories = _cursor.getInt(_cursorIndexOfRemainingCalories);
        _result.setRemainingCalories(_tmpRemainingCalories);
        final String _tmpDate;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmpDate = null;
        } else {
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
        }
        _result.setDate(_tmpDate);
        final int _tmpIntakeProtein;
        _tmpIntakeProtein = _cursor.getInt(_cursorIndexOfIntakeProtein);
        _result.setIntakeProtein(_tmpIntakeProtein);
        final int _tmpIntakeCarb;
        _tmpIntakeCarb = _cursor.getInt(_cursorIndexOfIntakeCarb);
        _result.setIntakeCarb(_tmpIntakeCarb);
        final int _tmpIntakeFat;
        _tmpIntakeFat = _cursor.getInt(_cursorIndexOfIntakeFat);
        _result.setIntakeFat(_tmpIntakeFat);
        final int _tmpIntakeCalories;
        _tmpIntakeCalories = _cursor.getInt(_cursorIndexOfIntakeCalories);
        _result.setIntakeCalories(_tmpIntakeCalories);
        final int _tmpTotalSteps;
        _tmpTotalSteps = _cursor.getInt(_cursorIndexOfTotalSteps);
        _result.setTotalSteps(_tmpTotalSteps);
        final int _tmpCarbGoal;
        _tmpCarbGoal = _cursor.getInt(_cursorIndexOfCarbGoal);
        _result.setCarbGoal(_tmpCarbGoal);
        final int _tmpProteinGoal;
        _tmpProteinGoal = _cursor.getInt(_cursorIndexOfProteinGoal);
        _result.setProteinGoal(_tmpProteinGoal);
        final int _tmpFatGoal;
        _tmpFatGoal = _cursor.getInt(_cursorIndexOfFatGoal);
        _result.setFatGoal(_tmpFatGoal);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Diary getDiaryByDate(final String date) {
    final String _sql = "SELECT * FROM diary WHERE date LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfCaloriesGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesGoal");
      final int _cursorIndexOfBurntCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "burntCalories");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfRemainingCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "remainingCalories");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfIntakeProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeProtein");
      final int _cursorIndexOfIntakeCarb = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeCarb");
      final int _cursorIndexOfIntakeFat = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeFat");
      final int _cursorIndexOfIntakeCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "intakeCalories");
      final int _cursorIndexOfTotalSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSteps");
      final int _cursorIndexOfCarbGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "carbGoal");
      final int _cursorIndexOfProteinGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinGoal");
      final int _cursorIndexOfFatGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "fatGoal");
      final Diary _result;
      if (_cursor.moveToFirst()) {
        _result = new Diary();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpCaloriesGoal;
        _tmpCaloriesGoal = _cursor.getInt(_cursorIndexOfCaloriesGoal);
        _result.setCaloriesGoal(_tmpCaloriesGoal);
        final int _tmpBurntCalories;
        _tmpBurntCalories = _cursor.getInt(_cursorIndexOfBurntCalories);
        _result.setBurntCalories(_tmpBurntCalories);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
        final int _tmpRemainingCalories;
        _tmpRemainingCalories = _cursor.getInt(_cursorIndexOfRemainingCalories);
        _result.setRemainingCalories(_tmpRemainingCalories);
        final String _tmpDate;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmpDate = null;
        } else {
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
        }
        _result.setDate(_tmpDate);
        final int _tmpIntakeProtein;
        _tmpIntakeProtein = _cursor.getInt(_cursorIndexOfIntakeProtein);
        _result.setIntakeProtein(_tmpIntakeProtein);
        final int _tmpIntakeCarb;
        _tmpIntakeCarb = _cursor.getInt(_cursorIndexOfIntakeCarb);
        _result.setIntakeCarb(_tmpIntakeCarb);
        final int _tmpIntakeFat;
        _tmpIntakeFat = _cursor.getInt(_cursorIndexOfIntakeFat);
        _result.setIntakeFat(_tmpIntakeFat);
        final int _tmpIntakeCalories;
        _tmpIntakeCalories = _cursor.getInt(_cursorIndexOfIntakeCalories);
        _result.setIntakeCalories(_tmpIntakeCalories);
        final int _tmpTotalSteps;
        _tmpTotalSteps = _cursor.getInt(_cursorIndexOfTotalSteps);
        _result.setTotalSteps(_tmpTotalSteps);
        final int _tmpCarbGoal;
        _tmpCarbGoal = _cursor.getInt(_cursorIndexOfCarbGoal);
        _result.setCarbGoal(_tmpCarbGoal);
        final int _tmpProteinGoal;
        _tmpProteinGoal = _cursor.getInt(_cursorIndexOfProteinGoal);
        _result.setProteinGoal(_tmpProteinGoal);
        final int _tmpFatGoal;
        _tmpFatGoal = _cursor.getInt(_cursorIndexOfFatGoal);
        _result.setFatGoal(_tmpFatGoal);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
