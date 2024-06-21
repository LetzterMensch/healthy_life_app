package com.example.gr.database.dao;

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
import com.example.gr.model.Workout;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class WorkoutDAO_Impl implements WorkoutDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Workout> __insertionAdapterOfWorkout;

  private final EntityDeletionOrUpdateAdapter<Workout> __deletionAdapterOfWorkout;

  private final EntityDeletionOrUpdateAdapter<Workout> __updateAdapterOfWorkout;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllWorkout;

  public WorkoutDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWorkout = new EntityInsertionAdapter<Workout>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `workout` (`id`,`exerciseId`,`duration`,`caloriesBurnt`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Workout entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getExerciseId());
        statement.bindLong(3, entity.getDuration());
        statement.bindLong(4, entity.getCaloriesBurnt());
        if (entity.getCreatedAt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCreatedAt());
        }
      }
    };
    this.__deletionAdapterOfWorkout = new EntityDeletionOrUpdateAdapter<Workout>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `workout` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Workout entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfWorkout = new EntityDeletionOrUpdateAdapter<Workout>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `workout` SET `id` = ?,`exerciseId` = ?,`duration` = ?,`caloriesBurnt` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Workout entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getExerciseId());
        statement.bindLong(3, entity.getDuration());
        statement.bindLong(4, entity.getCaloriesBurnt());
        if (entity.getCreatedAt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCreatedAt());
        }
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllWorkout = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE from workout";
        return _query;
      }
    };
  }

  @Override
  public void insertWorkout(final Workout workout) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfWorkout.insert(workout);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteWorkout(final Workout workout) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfWorkout.handle(workout);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateWorkout(final Workout workout) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfWorkout.handle(workout);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllWorkout() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllWorkout.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAllWorkout.release(_stmt);
    }
  }

  @Override
  public List<Workout> getAllWorkout() {
    final String _sql = "SELECT * FROM workout LIMIT 10";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfCaloriesBurnt = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesBurnt");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final List<Workout> _result = new ArrayList<Workout>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Workout _item;
        _item = new Workout();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpExerciseId;
        _tmpExerciseId = _cursor.getInt(_cursorIndexOfExerciseId);
        _item.setExerciseId(_tmpExerciseId);
        final int _tmpDuration;
        _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        _item.setDuration(_tmpDuration);
        final int _tmpCaloriesBurnt;
        _tmpCaloriesBurnt = _cursor.getInt(_cursorIndexOfCaloriesBurnt);
        _item.setCaloriesBurnt(_tmpCaloriesBurnt);
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        _item.setCreatedAt(_tmpCreatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Workout getWorkoutById(final int id) {
    final String _sql = "SELECT * FROM workout WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfCaloriesBurnt = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesBurnt");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final Workout _result;
      if (_cursor.moveToFirst()) {
        _result = new Workout();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpExerciseId;
        _tmpExerciseId = _cursor.getInt(_cursorIndexOfExerciseId);
        _result.setExerciseId(_tmpExerciseId);
        final int _tmpDuration;
        _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        _result.setDuration(_tmpDuration);
        final int _tmpCaloriesBurnt;
        _tmpCaloriesBurnt = _cursor.getInt(_cursorIndexOfCaloriesBurnt);
        _result.setCaloriesBurnt(_tmpCaloriesBurnt);
        final String _tmpCreatedAt;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmpCreatedAt = null;
        } else {
          _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        }
        _result.setCreatedAt(_tmpCreatedAt);
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